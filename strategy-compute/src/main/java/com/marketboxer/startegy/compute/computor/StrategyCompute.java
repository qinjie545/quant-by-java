package com.marketboxer.startegy.compute.computor;

import com.marketboxer.core.model.annotation.Strategy;

/**
 * 策略计算接口
 * @author qinjie
 * Created At : 2022/10/16 23:23
 */
public interface StrategyCompute {

    /**
     * 开始计算
     * @return
     */
    boolean compute();

    default String getStrategyId(){
        Strategy strategy = this.getClass().getAnnotation(Strategy.class);
        if (strategy == null){
            return null;
        }
        return strategy.value();
    }


}
