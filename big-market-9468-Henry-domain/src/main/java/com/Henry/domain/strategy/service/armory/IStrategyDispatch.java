package com.Henry.domain.strategy.service.armory;

/**
 * @Description 策略抽奖调度
 * @Author Henry
 * @Date 2024/3/28
 */
public interface IStrategyDispatch {
    /**
     * 获取抽奖策略装配的随机结果
     *
     * @param strategyId 策略ID
     * @return 抽奖结果
     */
    Integer getRandomAwardId(Long strategyId);

    Integer getRandomAwardId(Long strategyId, String ruleWeightValue);
}
