package com.marketboxer.core.model;

/**
 * 交易执行动作，买，卖等
 *
 * @author qinjie
 * Created At : 2022/10/17 0:40
 */
public enum TradeAction {

    /**
     * 买一些，提交申请时需要指定数量
     */
    ACTION_BUY_SOME(1000),
    /**
     * 卖一些，提交申请时需要指定数量
     */
    ACTION_SELL_SOME(9000),

    /**
     * 所有当前的剩余钱，全部用来买
     */
    ACTION_BUY_ALL(1001),
    /**
     * 所有当前的剩余股票，全部卖出
     */
    ACTION_SELL_ALL(9999)
    ;

    private int code;

    TradeAction(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
