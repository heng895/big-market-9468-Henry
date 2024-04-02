package com.Henry.domain.strategy.service.raffle;

import com.Henry.domain.strategy.model.entity.RaffleAwardEntity;
import com.Henry.domain.strategy.model.entity.RaffleFactorEntity;
import com.Henry.domain.strategy.model.entity.RuleActionEntity;
import com.Henry.domain.strategy.model.entity.StrategyEntity;
import com.Henry.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.Henry.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import com.Henry.domain.strategy.repository.IStrategyRepository;
import com.Henry.domain.strategy.service.IRaffleStrategy;
import com.Henry.domain.strategy.service.armory.IStrategyDispatch;
import com.Henry.domain.strategy.service.rule.factory.DefaultLogicFactory;
import com.Henry.types.enums.ResponseCode;
import com.Henry.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;

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

    public AbstractRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch) {
        this.strategyRepository = repository;
        this.strategyDispatch = strategyDispatch;
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
     * 抽奖逻辑 包含前中后三个环节
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

        //2. 策略查询
        StrategyEntity strategy = strategyRepository.queryStrategyEntityByStrategyId(strategyId);

        //3. 抽奖前 - 规则过滤
        RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleActionEntity = this.doCheckRaffleBeforeLogic(RaffleFactorEntity.builder().userId(userId).strategyId(strategyId).build(), strategy.ruleModels());

        if (RuleLogicCheckTypeVO.TAKE_OVER.getCode().equals(ruleActionEntity.getCode())) {
            if (DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode().equals(ruleActionEntity.getRuleModel())) {
                // 黑名单返回固定的奖品ID
                return RaffleAwardEntity.builder()
                        .awardId(ruleActionEntity.getData().getAwardId())
                        .build();
            } else if (DefaultLogicFactory.LogicModel.RULE_WIGHT.getCode().equals(ruleActionEntity.getRuleModel())) {
                // 权重根据返回的信息进行抽奖
                RuleActionEntity.RaffleBeforeEntity raffleBeforeEntity = ruleActionEntity.getData();
                String ruleWeightValueKey = raffleBeforeEntity.getRuleWeightValueKey();
                Integer awardId = strategyDispatch.getRandomAwardId(strategyId, ruleWeightValueKey);
                return RaffleAwardEntity.builder()
                        .awardId(awardId)
                        .build();
            }
        }

        // 4. 默认抽奖流程
        Integer awardId = strategyDispatch.getRandomAwardId(strategyId);

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
