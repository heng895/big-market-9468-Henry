package com.Henry.domain.strategy.service;

import com.Henry.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;

/**
 * @Description
 * @Author Henry
 * @Date 2024/4/16
 */
public interface IRaffleAward {
    List<StrategyAwardEntity> queryRaffleStrategyAwardList(Long strategyId);
}
