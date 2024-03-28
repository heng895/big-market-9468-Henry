package com.Henry.domain.strategy.repository;

import com.Henry.domain.strategy.model.entity.StrategyAwardEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Description 策略仓储接口
 * @Author Henry
 * @Date 2024/3/27
 */
public interface IStrategyRepository {
    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    void storeStrategyAwardSearchMap(Long strategyId, Integer rateRange, Map<Integer, Integer> shuffleStrategyAwardSearchMap);

    int getRateRange(Long strategyId);

    Integer getStrategyAwardAssemble(Long strategyId, int rateKey);
}
