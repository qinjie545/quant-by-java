package com.futu.openapi.services.base;

import com.futu.openapi.pb.TrdCommon;

public class Config {
    // Trading identifiers and credentials must never be committed to source control.
    public static long userID = getLongEnv("FUTU_USER_ID", 0L);
    public static long trdAcc = getLongEnv("FUTU_TRADE_ACCOUNT_ID", 0L);
    public static String unlockTradePwdMd5 = System.getenv().getOrDefault("FUTU_UNLOCK_TRADE_PASSWORD_MD5", "");
    public static TrdCommon.SecurityFirm securityFirm = TrdCommon.SecurityFirm.SecurityFirm_FutuSecurities; //trdAcc所属券商
    public static String opendIP = System.getenv().getOrDefault("FUTU_OPEND_HOST", "127.0.0.1");
    public static short opendPort = (short) getLongEnv("FUTU_OPEND_PORT", 11111L);
    public static String rsaKeyFilePath = System.getenv().getOrDefault("FUTU_RSA_KEY_FILE", "");

    private static long getLongEnv(String name, long defaultValue) {
        String value = System.getenv(name);
        return value == null || value.trim().isEmpty() ? defaultValue : Long.parseLong(value);
    }
}