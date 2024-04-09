package com.Henry.domain.strategy.service.rule.tree.factory.engine;

import com.Henry.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

/**
 * @Description 执行树组合接口
 * @Author Henry
 * @Date 2024/4/6
 */
public interface IDecisionTreeEngine {
    DefaultTreeFactory.StrategyAwardVO process(String userId, Long strategyId, Integer awardId);
}
