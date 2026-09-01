package com.marketboxer.core.model;

import com.marketboxer.core.model.constant.MarketType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  交易执行器请求
 *
 * @author qinjie
 * Created At : 2022/10/17 0:34
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeExecuteRequest {
    /**
     * 策略编号
     */
    String strategyId;

    /**
     *  {@link TradeAction}
     */
    int executeAction;

    /**
     * 证券数量
     */
    long secAmount;

    /**
     * 钱的数量，单位是分
     */
    long moneyAmount;

    /**
     * 市场类型，用来挂单时进行选择
     * {@link MarketType}
     */
    String marketType;

    /**
     * 证券代码
     */
    String secCode;

    /**
     * 昨日收盘价
     */
    double price;

}
