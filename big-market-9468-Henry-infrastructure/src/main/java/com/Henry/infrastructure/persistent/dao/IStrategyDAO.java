package com.Henry.infrastructure.persistent.dao;

import com.Henry.infrastructure.persistent.po.Award;
import com.Henry.infrastructure.persistent.po.Strategy;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description 抽奖策略
 * @Author Henry
 * @Date 2024/3/25
 */
@Mapper
public interface IStrategyDAO {
    List<Strategy> queryStrategyList();
}
