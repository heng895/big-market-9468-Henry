package com.Henry.infrastructure.persistent.repository;

import com.Henry.domain.strategy.model.entity.StrategyAwardEntity;
import com.Henry.domain.strategy.model.entity.StrategyEntity;
import com.Henry.domain.strategy.model.entity.StrategyRuleEntity;
import com.Henry.domain.strategy.repository.IStrategyRepository;
import com.Henry.infrastructure.persistent.dao.IStrategyAwardDao;
import com.Henry.infrastructure.persistent.dao.IStrategyDao;
import com.Henry.infrastructure.persistent.dao.IStrategyRuleDao;
import com.Henry.infrastructure.persistent.po.Strategy;
import com.Henry.infrastructure.persistent.po.StrategyAward;
import com.Henry.infrastructure.persistent.po.StrategyRule;
import com.Henry.infrastructure.persistent.redis.IRedisService;
import com.Henry.types.common.Constants;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
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
    private IStrategyAwardDao strategyAwardDAO;
    @Resource
    private IRedisService redisService;
    @Resource
    private IStrategyDao strategyDao;
    @Resource
    private IStrategyRuleDao strategyRuleDao;

    /**
     * 查询策略奖品列表
     *
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
     *
     * @param key                           策略ID
     * @param rateRange                     概率范围
     * @param shuffleStrategyAwardSearchMap 概率查找表
     */
    @Override
    public void storeStrategyAwardSearchMap(String key, Integer rateRange, Map<Integer, Integer> shuffleStrategyAwardSearchMap) {
        //1.存储概率表数组长度
        redisService.setValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + key, rateRange);
        //2.存储概率查找表
        Map<Integer, Integer> cacheRateTable = redisService.getMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + key);
        cacheRateTable.putAll(shuffleStrategyAwardSearchMap);
    }

    /**
     * 获取概率范围(概率表数组长度)
     *
     * @param strategyId 策略ID
     * @return 概率范围
     */
    @Override
    public int getRateRange(Long strategyId) {
        return getRateRange(String.valueOf(strategyId));
    }
    @Override
    public int getRateRange(String key) {
        return redisService.getValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + key);
    }



    /**
     * 传入概率key，获取奖品ID
     *
     * @param key 策略ID
     * @param rateKey    概率key
     * @return 奖品ID
     */
    @Override
    public Integer getStrategyAwardAssemble(String key, int rateKey) {
        return redisService.getFromMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + key, rateKey);
    }

    /**
     * 根据策略ID查询策略信息
     *
     * @param strategyId 策略ID
     * @return 策略信息
     */
    @Override
    public StrategyEntity queryStrategyEntityByStrategyId(Long strategyId) {
        // 优先从缓存获取
        String cacheKey = Constants.RedisKey.STRATEGY_KEY + strategyId;
        StrategyEntity strategyEntity = redisService.getValue(cacheKey);
        if (null != strategyEntity) return strategyEntity;
        Strategy strategy = strategyDao.queryStrategyByStrategyId(strategyId);
        strategyEntity = StrategyEntity.builder()
                .strategyId(strategy.getStrategyId())
                .strategyDesc(strategy.getStrategyDesc())
                .ruleModels(strategy.getRuleModels())
                .build();
        redisService.setValue(cacheKey, strategyEntity);
        return strategyEntity;
    }

    @Override
    public StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleModel) {
        // TODO: 2024/3/27 从缓存中获取

        StrategyRule strategyRuleReq = new StrategyRule();
        strategyRuleReq.setStrategyId(strategyId);
        strategyRuleReq.setRuleModel(ruleModel);
        StrategyRule strategyRuleRes = strategyRuleDao.queryStrategyRule(strategyRuleReq);
        return StrategyRuleEntity.builder()
                .strategyId(strategyRuleRes.getStrategyId())
                .awardId(strategyRuleRes.getAwardId())
                .ruleType(strategyRuleRes.getRuleType())
                .ruleModel(strategyRuleRes.getRuleModel())
                .ruleValue(strategyRuleRes.getRuleValue())
                .ruleDesc(strategyRuleRes.getRuleDesc())
                .build();
    }


}
