package com.Henry.domain.strategy.service.rule.chain.factory;

import com.Henry.domain.strategy.model.entity.StrategyEntity;
import com.Henry.domain.strategy.repository.IStrategyRepository;
import com.Henry.domain.strategy.service.rule.chain.ILogicChain;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description 责任链工厂
 * @Author Henry
 * @Date 2024/4/6
 */
@Service
public class DefaultChainFactory {

    private final Map<String, ILogicChain> logicChainMap;
    private final IStrategyRepository strategyRepository;

    public DefaultChainFactory(Map<String, ILogicChain> logicChainMap, IStrategyRepository strategyRepository) {
        this.logicChainMap = logicChainMap;
        this.strategyRepository = strategyRepository;
    }

    /**
     * 通过策略ID，构建责任链(包括rule_models+default责任链)
     *
     * @param strategyId 策略ID
     * @return LogicChain
     */
    public ILogicChain openLogicChain(Long strategyId){
        StrategyEntity strategy = strategyRepository.queryStrategyEntityByStrategyId(strategyId);
        String[] ruleModels = strategy.ruleModels();
        if(ruleModels == null || ruleModels.length == 0){
            return logicChainMap.get("default");
        }
        ILogicChain logicChain = logicChainMap.get(ruleModels[0]);
        ILogicChain current = logicChain;
        for(int i = 1; i < ruleModels.length; i++){
            ILogicChain nextChain = logicChainMap.get(ruleModels[i]);
            current = current.appendNext(nextChain);
        }
        current.appendNext(logicChainMap.get("default"));
        return logicChain;
    }
}
