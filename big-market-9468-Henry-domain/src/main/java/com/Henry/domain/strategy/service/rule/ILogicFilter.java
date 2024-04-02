package com.Henry.domain.strategy.service.rule;

import com.Henry.domain.strategy.model.entity.RuleActionEntity;
import com.Henry.domain.strategy.model.entity.RuleMatterEntity;

/**
 * @Description 抽奖规则过滤接口
 * @Author Henry
 * @Date 2024/4/1
 */
public interface ILogicFilter<T extends RuleActionEntity.RaffleEntity> {
    RuleActionEntity<T> filter(RuleMatterEntity matterEntity);
}
