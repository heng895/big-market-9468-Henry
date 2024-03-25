package com.Henry.infrastructure.persistent.dao;

import com.Henry.infrastructure.persistent.po.Award;
import com.Henry.infrastructure.persistent.po.StrategyAward;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description 抽奖策略奖品明细配置 - 概率、规则
 * @Author Henry
 * @Date 2024/3/25
 */
@Mapper
public interface IStrategyAwardDAO {
    List<StrategyAward> queryStrategyAwardList();
}
