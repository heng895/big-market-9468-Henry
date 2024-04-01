package com.Henry.domain.strategy.model.entity;

import com.Henry.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description 策略实体
 * @Author Henry
 * @Date 2024/3/28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyEntity {
    /**
     * 抽奖策略ID
     */
    private Long strategyId;
    /**
     * 抽奖策略描述
     */
    private String strategyDesc;
    /**
     * 抽奖规则类型 rule_weight,rule_blacklist
     */
    private String ruleModels;

    /**
     * 获取抽奖规则模型
     *
     * @return 抽奖规则模型集合
     */
    public String[] ruleModels() {
        if (StringUtils.isBlank(ruleModels)) return null;
        return ruleModels.split(Constants.SPLIT);
    }
    /**
     * 获取权重相等的抽奖规则
     *
     * @return 抽奖规则权重
     */
    public String getRuleWeight() {
        String[] ruleModels = this.ruleModels();
        for (String ruleModel : ruleModels) {
            if ("rule_weight".equals(ruleModel)) return ruleModel;
        }
        return null;
    }
}
