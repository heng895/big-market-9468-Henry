package com.Henry.domain.strategy.service.rule.tree.impl;

import com.Henry.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.Henry.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.Henry.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @Description 次数锁节点
 * @Author Henry
 * @Date 2024/4/6
 */
@Slf4j
@Component("rule_lock")
public class RuleLockLogicTreeNode implements ILogicTreeNode {

    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId) {
        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.ALLOW)
                .build();
    }
}
