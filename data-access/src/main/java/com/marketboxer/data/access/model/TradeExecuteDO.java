package com.marketboxer.data.access.model;

import lombok.Data;

/**
 * 交易待执行数据实体
 *
 * @author qinjie
 * Created At : 2022/10/29 22:58
 */
@Data
public class TradeExecuteDO {

    /**
     * 流水号
     */
    String flowNo;

    /**
     * 策略编号
     */
    String strategyId;

    /**
     * 当前的执行状态
     */
    int executeStatus;

    /**
     * 计划的交易行为
     */
    int tradeAction;

    /**
     * 股票数量，买时，用买这么多的股票，卖时，是卖出这么多的股票
     */
    long secAmount;

    /**
     * 资金数量，卖的时候，是预计会得到这么多钱。 买的时候，是计划购买这么多钱的。
     */
    long moneyAmount;

    /**
     * 市场类型，编码
     */
    String marketType;

    /**
     * 证券代码
     */
    String secCode;

    /**
     * 创建时间戳
     */
    long createTime;

    /**
     * 价格
     */
    double price;

    /**
     * 证券市场上的订单编号
     */
    long orderNo;

}
