package com.Henry.domain.strategy.service.rule.tree;

import com.Henry.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

/**
 * @Description 规则树接口
 * @Author Henry
 * @Date 2024/4/6
 */
public interface ILogicTreeNode {
    /**
     * 逻辑处理
     *
     * @param userId     用户ID
     * @param strategyId 策略ID
     * @param awardId    奖品ID
     * @param ruleValue  规则值
     * @return DefaultTreeFactory.TreeActionEntity
     */
    DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId, String ruleValue);

}
