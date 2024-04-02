package com.Henry.domain.strategy.service;

import com.Henry.domain.strategy.model.entity.RaffleAwardEntity;
import com.Henry.domain.strategy.model.entity.RaffleFactorEntity;

/**
 * @Description 抽奖策略接口
 * @Author Henry
 * @Date 2024/4/1
 */
public interface IRaffleStrategy {
    /**
     * 执行抽奖；用抽奖因子入参，执行抽奖计算，返回奖品信息
     *
     * @param raffleFactorEntity 抽奖因子实体对象，根据入参信息计算抽奖结果
     * @return 抽奖的奖品
     */
    RaffleAwardEntity doRaffle(RaffleFactorEntity raffleFactorEntity);
}
