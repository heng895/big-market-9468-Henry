package com.Henry.domain.strategy.service.armory;

import com.Henry.domain.strategy.model.entity.StrategyAwardEntity;
import com.Henry.domain.strategy.repository.IStrategyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;

/**
 * @Description 策略库，负责初始化策略计算
 * @Author Henry
 * @Date 2024/3/27
 */
@Slf4j
@Service
public class StrategyArmory implements IStrategyArmory {

    @Resource
    private IStrategyRepository strategyRepository;

    /**
     * 装配策略
     * @param strategyId 策略ID
     */
    @Override
    public void assembleLotteryStrategy(Long strategyId) {
        // 1. 从redis或数据库中获取策略信息
        List<StrategyAwardEntity> strategyAwardEntities = strategyRepository.queryStrategyAwardList(strategyId);
        // 2. 获取最小概率值
        BigDecimal minAwardRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        // 3. 获取概率总和
        BigDecimal sumAwardRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // 4. 获取概率值范围
        BigDecimal rateRange = sumAwardRate.divide(minAwardRate, 0, RoundingMode.CEILING);
        // 5. 存储概率查找表
        List<Integer> strategyAwardSearchTable = new ArrayList<>(rateRange.intValue());
        for (StrategyAwardEntity strategyAward : strategyAwardEntities) {
            Integer awardId = strategyAward.getAwardId();
            BigDecimal awardRate = strategyAward.getAwardRate();
            //计算每个概率值在查找表的个数，循环填充
//            int awardCount = strategyAward.getAwardCount();
            int awardCount = rateRange.multiply(awardRate).setScale(0, RoundingMode.CEILING).intValue();
            for (int i = 0; i < awardCount; i++) {
                strategyAwardSearchTable.add(awardId);
            }
        }
        // 6. 乱序
        Collections.shuffle(strategyAwardSearchTable);

        // 7. 构造hash映射（下标 -> 奖品id）
        Map<Integer, Integer> shuffleStrategyAwardSearchMap = new HashMap<>();
        for (int i = 0; i < strategyAwardSearchTable.size(); i++) {
            shuffleStrategyAwardSearchMap.put(i, strategyAwardSearchTable.get(i));
        }
        // 8.存入redis
        strategyRepository.storeStrategyAwardSearchMap(strategyId, shuffleStrategyAwardSearchMap.size(), shuffleStrategyAwardSearchMap);
    }

    @Override
    public Integer getRandomAwardId(Long strategyId) {
        int rateRange = strategyRepository.getRateRange(strategyId);
        return strategyRepository.getStrategyAwardAssemble(strategyId, new SecureRandom().nextInt(rateRange));
    }
}
