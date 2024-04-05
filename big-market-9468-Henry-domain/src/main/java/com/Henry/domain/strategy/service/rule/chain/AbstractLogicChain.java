package com.Henry.domain.strategy.service.rule.chain;

/**
 * @Description
 * @Author Henry
 * @Date 2024/4/5
 */
public abstract class AbstractLogicChain implements ILogicChain{
    private ILogicChain next;

    @Override
    public ILogicChain next() {
        return next;
    }

    @Override
    public ILogicChain appendNext(ILogicChain next) {
        this.next = next;
        return next;
    }

    protected abstract String ruleModel();
}
