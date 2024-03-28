package com.Henry.infrastructure.persistent.repository;

import com.Henry.domain.strategy.model.entity.StrategyAwardEntity;
import com.Henry.domain.strategy.repository.IStrategyRepository;
import com.Henry.infrastructure.persistent.dao.IStrategyAwardDAO;
import com.Henry.infrastructure.persistent.po.StrategyAward;
import com.Henry.infrastructure.persistent.redis.IRedisService;
import com.Henry.types.common.Constants;
import org.redisson.api.RMap;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description 策略仓储实现
 * @Author Henry
 * @Date 2024/3/27
 */
@Repository
public class StrategyRepository implements IStrategyRepository {
    @Resource
    private IStrategyAwardDAO strategyAwardDAO;
    @Resource
    private IRedisService redisService;

    /**
     * 查询策略奖品列表
     * @param strategyId 策略ID
     * @return 策略奖品列表
     */
    @Override
    public List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId) {
        // 从缓存中获取
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_KEY + strategyId;
        List<StrategyAwardEntity> strategyAwardEntities = redisService.getValue(cacheKey);
        if (strategyAwardEntities != null && !strategyAwardEntities.isEmpty()) {
            return strategyAwardEntities;
        }
        // 从数据库中获取
        List<StrategyAward> strategyAwards = strategyAwardDAO.queryStrategyAwardListByStrategyId(strategyId);
        strategyAwardEntities = new ArrayList<>(strategyAwards.size());
        for (StrategyAward strategyAward : strategyAwards) {
            StrategyAwardEntity strategyAwardEntity = StrategyAwardEntity.builder()
                    .strategyId(strategyAward.getStrategyId())
                    .awardId(strategyAward.getAwardId())
                    .awardCount(strategyAward.getAwardCount())
                    .awardCountSurplus(strategyAward.getAwardCountSurplus())
                    .awardRate(strategyAward.getAwardRate())
                    .build();
            strategyAwardEntities.add(strategyAwardEntity);
        }
        // 存入redis
        redisService.setValue(cacheKey, strategyAwardEntities);
        return strategyAwardEntities;
    }

    /**
     * 存储概率表长度和概率查找表
     * @param strategyId 策略ID
     * @param rateRange  概率范围
     * @param shuffleStrategyAwardSearchMap 概率查找表
     */
    @Override
    public void storeStrategyAwardSearchMap(Long strategyId, Integer rateRange, Map<Integer, Integer> shuffleStrategyAwardSearchMap) {
        //1.存储概率表数组长度
        redisService.setValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + strategyId, rateRange);
        //2.存储概率查找表
        Map<Integer, Integer> cacheRateTable = redisService.getMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + strategyId);
        cacheRateTable.putAll(shuffleStrategyAwardSearchMap);
    }

    /**
     * 获取概率范围(概率表数组长度)
     * @param strategyId 策略ID
     * @return 概率范围
     */
    @Override
    public int getRateRange(Long strategyId) {
        return redisService.getValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + strategyId);
    }

    /**
     * 传入概率key，获取奖品ID
     * @param strategyId 策略ID
     * @param rateKey    概率key
     * @return 奖品ID
     */
    @Override
    public Integer getStrategyAwardAssemble(Long strategyId, int rateKey) {
        return redisService.getFromMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + strategyId, rateKey);
    }
}
