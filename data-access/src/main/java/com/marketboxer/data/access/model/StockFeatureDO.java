package com.marketboxer.data.access.model;


import lombok.Data;

/**
 * @author qinjie
 */
@Data
public class StockFeatureDO {

    /**
     * 主键
     */
    long id;

    /**
     * 股票代码
     */
    String securityCode ;

    /**
     * 策略编号
     */
    String strategyId;

    /**
     * 策略版本
     */
    String strategyVersion;

    /**
     * 特征存储
     */
    String featureJsonStore;

    /**
     * 创建时间
     */
    long createTime;

    /**
     * 更新时间
     */
    long updateTime;

    /**
     * 日期int
     */
    long dayIntKey;



}
