package com.Henry.infrastructure.persistent.dao;

import com.Henry.infrastructure.persistent.po.Award;
import com.Henry.infrastructure.persistent.po.StrategyRule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description 策略规则
 * @Author Henry
 * @Date 2024/3/25
 */
@Mapper
public interface IStrategyRuleDao {
    List<StrategyRule> queryStrategyRuleList();
}
