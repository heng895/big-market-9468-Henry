package com.Henry.domain.strategy.service.rule.tree;

import com.Henry.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

/**
 * @Description 规则树接口
 * @Author Henry
 * @Date 2024/4/6
 */
public interface ILogicTreeNode {
    DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId);

}
