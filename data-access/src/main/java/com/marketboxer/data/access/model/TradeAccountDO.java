package com.marketboxer.data.access.model;

import lombok.Data;

/**
 * 账户相关DO对象
 *
 * @author qinjie
 * Created At : 2022/10/29 22:36
 */

@Data
public class TradeAccountDO {

    /**
     * 主键
     */
    long id;
    /**
     * 策略编号
     */
    String strategyId;
    /**
     * 金钱持有，单位分
     */
    long moneyHold;
    /**
     * 股票证券持有，单位1个
     */
    long secHold;

}
