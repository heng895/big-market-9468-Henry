package com.Henry.domain.strategy.service.rule.chain;

import com.Henry.domain.strategy.service.rule.chain.factory.DefaultChainFactory;

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
    DefaultChainFactory.StrategyAwardVO logic(String userId, Long strategyId);
}
