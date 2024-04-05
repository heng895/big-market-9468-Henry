package com.Henry.domain.strategy.service.rule.chain;

/**
 * @Description
 * @Author Henry
 * @Date 2024/4/5
 */
public interface ILogicChainArmory {
    /**
     * 获取下一个责任链
     * @return
     */
    ILogicChain next();

    /**
     * 填充责任链
     * @param next
     * @return
     */
    ILogicChain appendNext(ILogicChain next);
}
