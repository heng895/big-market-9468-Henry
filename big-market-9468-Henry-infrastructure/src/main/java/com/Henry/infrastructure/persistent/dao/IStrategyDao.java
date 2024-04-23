package com.Henry.infrastructure.persistent.dao;

import com.Henry.infrastructure.persistent.po.Strategy;
import com.Henry.infrastructure.persistent.po.StrategyAward;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description 抽奖策略
 * @Author Henry
 * @Date 2024/3/25
 */
@Mapper
public interface IStrategyDao {
    List<Strategy> queryStrategyList();

    Strategy queryStrategyByStrategyId(Long strategyId);
}
