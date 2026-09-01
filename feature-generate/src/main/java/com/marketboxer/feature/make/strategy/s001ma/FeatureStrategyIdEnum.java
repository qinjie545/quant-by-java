package com.marketboxer.feature.make.strategy.s001ma;

/**
 * @author qinjie
 */
public enum FeatureStrategyIdEnum {

    STRATEGY_ID_001MA("Strategy001_MA");

    private String code;

    FeatureStrategyIdEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
