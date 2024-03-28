package com.Henry.domain.strategy.service.armory;

/**
 * @Description 策略库，负责初始化策略计算
 * @Author Henry
 * @Date 2024/3/27
 */
public interface IStrategyArmory {
    /**
     * 初始化策略计算
     * @param strategyId 策略id
     */
    void assembleLotteryStrategy(Long strategyId);

    /**
     * 获取随机奖品的id
     * @param strategyId 策略id
     * @return 奖品id
     */
    Integer getRandomAwardId(Long strategyId);
}
