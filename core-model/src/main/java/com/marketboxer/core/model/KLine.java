package com.marketboxer.core.model;

import lombok.Data;

/**
 * @author qinjie
 */
@Data
public class KLine {

    /**
     * 证券代码
     */
    String code;

    /**
     * 最高
     */
    double high;
    /**
     * 最低
     */
    double low;

    /**
     * 开盘价
     */
    double open;
    /**
     * 收盘价
     */
    double close;

    /**
     * 时间格式：yyyy-MM-dd HH:mm:ss 港股和 A 股市场默认是北京时
     */
    String timeKey;

    /**
     * int格式的date存储
     */
    int dateIntKey;

    /**
     * 成交量
     */
    long volume;

    /**
     * pe_ratio
     */
    double peRatio;

    /**
     * 换手率
     */
    double turnoverRate;

    /**
     * 成交额
     */
    double turnover;

    /**
     * 涨跌幅-振幅
     */
    double changeRate;

    /**
     * 昨天收盘价
     */
    double lastClose;

    /**
     * 创建时间
     */
    long createTime;

    /**
     * 时间的整形表述，用来做排序型查询的
     */
    long timestamp;

    /**
     * 当前数据的int类型的day
     */
    long dayIntKey;

}
