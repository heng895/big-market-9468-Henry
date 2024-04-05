package com.Henry.domain.strategy.service.rule.filter;

import com.Henry.domain.strategy.model.entity.RuleActionEntity;
import com.Henry.domain.strategy.model.entity.RuleMatterEntity;

/**
 * @Description 抽奖规则过滤接口
 * @Author Henry
 * @Date 2024/4/1
 */
public interface ILogicFilter<T extends RuleActionEntity.RaffleEntity> {
    /**
     * 规则过滤：黑名单、次数锁、积分锁
     *
     * @param matterEntity 规则事项实体对象
     * @return 规则过滤结果
     */
    RuleActionEntity<T> filter(RuleMatterEntity matterEntity);
}
