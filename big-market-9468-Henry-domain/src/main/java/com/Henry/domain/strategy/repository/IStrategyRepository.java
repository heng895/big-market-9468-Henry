package com.Henry.domain.strategy.repository;

import com.Henry.domain.strategy.model.entity.StrategyAwardEntity;
import com.Henry.domain.strategy.model.entity.StrategyEntity;
import com.Henry.domain.strategy.model.entity.StrategyRuleEntity;
import com.Henry.domain.strategy.model.valobj.RuleTreeVO;
import com.Henry.domain.strategy.model.valobj.StrategyAwardRuleModelVO;

import java.util.List;
import java.util.Map;

/**
 * @Description 策略仓储接口
 * @Author Henry
 * @Date 2024/3/27
 */
public interface IStrategyRepository {
    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    void storeStrategyAwardSearchMap(String key, Integer rateRange, Map<Integer, Integer> shuffleStrategyAwardSearchMap);

    int getRateRange(Long strategyId);

    StrategyEntity queryStrategyEntityByStrategyId(Long strategyId);

    StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleModel);

    int getRateRange(String key);

    Integer getStrategyAwardAssemble(String key, int rateKey);

    String queryStrategyRuleValue(Long strategyId, Integer awardId, String ruleModel);

    String queryStrategyRuleValue(Long strategyId, String ruleModel);

    StrategyAwardRuleModelVO queryStrategyAwardRuleModelVO(Long strategyId, Integer awardId);

    RuleTreeVO queryRuleTreeVOByTreeId(String treeLock);
}
