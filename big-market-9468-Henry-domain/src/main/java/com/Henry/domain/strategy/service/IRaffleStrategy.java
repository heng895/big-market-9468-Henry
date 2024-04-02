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
     * 抽奖逻辑 包含前中后三个环节
     *
     * @param raffleFactorEntity 抽奖因子实体对象，根据入参信息计算抽奖结果
     * @return 抽奖的奖品
     */
    RaffleAwardEntity doRaffle(RaffleFactorEntity raffleFactorEntity);
}
