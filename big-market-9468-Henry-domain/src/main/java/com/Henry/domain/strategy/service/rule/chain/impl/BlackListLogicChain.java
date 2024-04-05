package com.Henry.domain.strategy.service.rule.chain.impl;

import com.Henry.domain.strategy.repository.IStrategyRepository;
import com.Henry.domain.strategy.service.rule.chain.AbstractLogicChain;
import com.Henry.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description 黑名单方法
 * @Author Henry
 * @Date 2024/4/5
 */
@Slf4j
@Component("rule_blacklist")
public class BlackListLogicChain extends AbstractLogicChain {

    @Resource
    protected IStrategyRepository strategyRepository;
    @Override
    public Integer logic(String userId, Long strategyId) {
        log.info("抽奖责任链-黑名单开始 userId: {} strategyId: {} ruleModel: {}", userId, strategyId, ruleModel());
        // 查询规则值配置
        String ruleValue = strategyRepository.queryStrategyRuleValue(strategyId, ruleModel());
        String[] splitRuleValue = ruleValue.split(Constants.COLON);
        Integer awardId = Integer.parseInt(splitRuleValue[0]);

        // 黑名单抽奖判断
        String[] userBlackIds = splitRuleValue[1].split(Constants.SPLIT);
        for (String userBlackId : userBlackIds) {
            if (userId.equals(userBlackId)) {
                log.info("抽奖责任链-黑名单接管 userId: {} strategyId: {} ruleModel: {} awardId: {}", userId, strategyId, ruleModel(), awardId);
                return awardId;
            }
        }

        // 过滤其他责任链
        log.info("抽奖责任链-黑名单放行 userId: {} strategyId: {} ruleModel: {}", userId, strategyId, ruleModel());
        return next().logic(userId, strategyId);
    }

    @Override
    protected String ruleModel() {
        return "rule_blacklist";
    }
}
