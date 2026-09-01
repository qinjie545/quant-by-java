package com.marketboxer.core.model;

import lombok.Data;

/**
 * 股票基本信息
 * @author qinjie  at 2022/9/16
 * e
 **/
@Data
public class StockProfile {

    /**
     * 市场代码
     */
    String marketCode;
    /**
     * 证券Code
     */
    String securityCode;
    /**
     * 是否已经退市
     */
    int isDelisting ;
    /**
     * 证券名称
     */
    String securityName ;
    /**
     * id
     */
    long id;
    /**
     * 交易所类型
     */
    int exchType ;
    /**
     * 上市时间
     */
    String listingTime;
    /**
     * 最小交易量
     */
    int lotSize;
    /**
     * 证券类型
     */
    int secType;

}
