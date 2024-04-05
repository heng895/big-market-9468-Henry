package com.Henry.domain.strategy.service;

import com.Henry.domain.strategy.model.entity.RaffleAwardEntity;
import com.Henry.domain.strategy.model.entity.RaffleFactorEntity;
import com.Henry.domain.strategy.model.entity.RuleActionEntity;
import com.Henry.domain.strategy.model.entity.StrategyEntity;
import com.Henry.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.Henry.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import com.Henry.domain.strategy.repository.IStrategyRepository;
import com.Henry.domain.strategy.service.IRaffleStrategy;
import com.Henry.domain.strategy.service.armory.IStrategyDispatch;
import com.Henry.domain.strategy.service.rule.chain.ILogicChain;
import com.Henry.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.Henry.domain.strategy.service.rule.filter.ILogicFilter;
import com.Henry.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
import com.Henry.types.enums.ResponseCode;
import com.Henry.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @Description 抽奖策略抽象类
 * @Author Henry
 * @Date 2024/4/1
 */
@Slf4j
public abstract class AbstractRaffleStrategy implements IRaffleStrategy {

    protected IStrategyRepository strategyRepository;
    // 策略调度服务 -> 只负责抽奖处理，通过新增接口的方式，隔离职责，不需要使用方关心或者调用抽奖的初始化
    protected IStrategyDispatch strategyDispatch;
    private DefaultChainFactory defaultChainFactory;

    public AbstractRaffleStrategy(IStrategyRepository strategyRepository, IStrategyDispatch strategyDispatch, DefaultChainFactory defaultChainFactory) {
        this.strategyRepository = strategyRepository;
        this.strategyDispatch = strategyDispatch;
        this.defaultChainFactory = defaultChainFactory;
    }

    /**
     * 抽奖前过滤黑名单
     *
     * @param raffleFactorEntity  抽奖因子实体对象
     * @param logics 规则模型
     * @return 规则过滤结果
     */
    protected abstract RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckRaffleBeforeLogic(RaffleFactorEntity raffleFactorEntity, String... logics);
    /**
     * 抽奖中过滤解锁
     *
     * @param raffleFactorEntity 抽奖因子实体对象
     * @param logics            规则模型
     * @return 规则过滤结果
     */
    protected abstract RuleActionEntity<RuleActionEntity.RaffleCenterEntity> doCheckRaffleCenterLogic(RaffleFactorEntity raffleFactorEntity, String... logics);

    /**
     * 抽奖逻辑 责任链重构
     *
     * @param raffleFactorEntity 抽奖因子实体对象
     * @return 抽奖结果
     */
    @Override
    public RaffleAwardEntity doRaffle(RaffleFactorEntity raffleFactorEntity) {
        // 1. 参数校验
        String userId = raffleFactorEntity.getUserId();
        Long strategyId = raffleFactorEntity.getStrategyId();
        if (null == strategyId || userId == null) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        // 2. 打开责任链
        ILogicChain logicChain = defaultChainFactory.openLogicChain(strategyId);

        // 3. 获取奖品ID
        Integer awardId = logicChain.logic(userId, strategyId);

        // 5. 查询奖品规则「抽奖中（拿到奖品ID时，过滤规则）、抽奖后（扣减完奖品库存后过滤，抽奖中拦截和无库存则走兜底）」
        StrategyAwardRuleModelVO strategyAwardRuleModelVO = strategyRepository.queryStrategyAwardRuleModelVO(strategyId, awardId);

        // 6. 抽奖中 - 规则过滤
        RuleActionEntity<RuleActionEntity.RaffleCenterEntity> ruleActionCenterEntity = this.doCheckRaffleCenterLogic(RaffleFactorEntity.builder()
                .userId(userId)
                .strategyId(strategyId)
                .awardId(awardId)
                .build(), strategyAwardRuleModelVO.raffleCenterRuleModelList());
        // 7. 不满足解锁要求，给出兜底奖励
        if (RuleLogicCheckTypeVO.TAKE_OVER.getCode().equals(ruleActionCenterEntity.getCode())){
            log.info("【临时日志】中奖中规则拦截，通过抽奖后规则 rule_luck_award 走兜底奖励。");
            return RaffleAwardEntity.builder()
                    .awardDesc("中奖中规则拦截，通过抽奖后规则 rule_luck_award 走兜底奖励。")
                    .build();
        }
        // 8. 返回中奖奖品
        return RaffleAwardEntity.builder()
                .awardId(awardId)
                .build();
    }
}
