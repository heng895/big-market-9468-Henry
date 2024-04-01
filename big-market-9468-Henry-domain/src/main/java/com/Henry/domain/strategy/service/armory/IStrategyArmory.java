package com.Henry.domain.strategy.service.armory;

/**
 * @Description 策略库，负责初始化策略计算
 * @Author Henry
 * @Date 2024/3/27
 */
public interface IStrategyArmory {
    /**
     * 初始化策略计算
     *
     * @param strategyId 策略id
     * @return
     */
    boolean assembleLotteryStrategy(Long strategyId);
}
