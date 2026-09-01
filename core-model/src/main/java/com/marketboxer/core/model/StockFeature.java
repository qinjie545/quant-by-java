package com.marketboxer.core.model;

import lombok.Data;

/**
 * @author qinjie
 */
@Data
public class StockFeature<T> {

    /**
     * 主键
     */
    long id;

    /**
     * 股票代码
     */
    String code ;

    /**
     * 策略编号
     */
    String strategyCode;

    /**
     * 策略版本
     */
    String strategyVersion;

    /**
     * 特征存储
     */
    String featureRawStore;

    /**
     * Raw feature data
     */
    T featureData;

    /**
     * 当前的特征对应的日期int值
     */
    long dayIntKey;

    /**
     * 创建时间
     */
    long createTime;

    /**
     * 更新时间
     */
    long updateTime;

}
