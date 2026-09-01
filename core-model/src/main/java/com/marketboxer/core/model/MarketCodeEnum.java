package com.marketboxer.core.model;

import lombok.Getter;

/**
 * @author qinjie  at 2022/9/17
 * e
 **/
@Getter
public enum MarketCodeEnum {

    /**
     * 上证A股股票
     */
    CNSH_S("上证A股——股票", "CNSH_S"),
    /**
     * 上证A股股票
     */
    CNSH_F("上证A股——期货", "CNSH_F"),
    /**
     * HK股股票
     */
    HK_S("港股——股票", "HK_S"),
    /**
     * HK股期货
     */
    HK_F("港股——期货", "HK_F"),
    /**
     * HK股股票
     */
    US_S("美股——股票", "US_S"),
    /**
     * HK股期货
     */
    US_F("美股——期货", "US_F"),
    /**
     * HK股股票
     */
    SG_S("新加坡——股票", "SG_S"),
    /**
     * HK股期货
     */
    SG_F("新加坡——期货", "SG_F"),
    /**
     * HK股股票
     */
    JP_S("日本股——股票", "JP_S")
    ;

    private  String marketName;
    private String marketCode;

    MarketCodeEnum(String name, String code){
        this.marketName = name;
        this.marketCode = code;
    }

    public static MarketCodeEnum fromCode(String marketCode) {
        switch (marketCode){
            case "CNSH_S":
                return CNSH_S;
            default:
                //TODO P3  add more converts
                return CNSH_S;
        }
    }

    public String getMarketCode() {
        return marketCode;
    }

    public String getMarketName() {
        return marketName;
    }
}
