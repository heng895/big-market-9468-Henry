package com.Henry.domain.strategy.service.rule.chain;

/**
 * @Description 责任链接口
 * @Author Henry
 * @Date 2024/4/5
 */
public interface ILogicChain extends ILogicChainArmory{
    /**
     * 责任链接口
     * @param userId
     * @param strategyId
     * @return 奖品ID
     */
    Integer logic(String userId, Long strategyId);
}
