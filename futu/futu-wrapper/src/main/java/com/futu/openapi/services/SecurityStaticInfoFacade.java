package com.futu.openapi.services;

import com.futu.openapi.pb.Common;
import com.futu.openapi.pb.QotCommon;
import com.futu.openapi.pb.QotGetStaticInfo;
import com.futu.openapi.services.base.FutuBase;

import java.util.ArrayList;
import java.util.List;

public class SecurityStaticInfoFacade extends FutuBase {

    /**
     * 获取当前市场的所有证券的基本信息
     * @param market
     * @return
     */
    public List<QotCommon.SecurityStaticInfo> fetchSecurityStaticInfoOfMarket(QotCommon.QotMarket market) {

        System.out.println("Run SecuritySnapshot");
        try {
            boolean ret = initConnectQotSync("127.0.0.1", (short)11111);
            if (ret) {
                System.out.println("qot connected");
            } else {
                System.out.println("fail to connect opend");
                return new ArrayList<>();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }


        int[] securityTypes = {
                /**
                 * 正股
                */
                QotCommon.SecurityType.SecurityType_Eqty_VALUE,
                /**
                 * 指数
                */
                QotCommon.SecurityType.SecurityType_Index_VALUE,
                /**
                 * 信托
                */
                QotCommon.SecurityType.SecurityType_Trust_VALUE,
                /**
                 * 涡轮
                */
                QotCommon.SecurityType.SecurityType_Warrant_VALUE,
                /**
                 * 债券
                */
                QotCommon.SecurityType.SecurityType_Bond_VALUE
        };

        ArrayList<QotCommon.SecurityStaticInfo> securityStaticInfos = new ArrayList<>();

        for (int securityType : securityTypes) {
            QotGetStaticInfo.C2S c2s = QotGetStaticInfo.C2S.newBuilder()
                    .setMarket(market.getNumber())
                    .setSecType(securityType)
                    .build();
            QotGetStaticInfo.Response rsp = null;
            try {
                rsp = getStaticInfoSync(c2s);
            } catch (InterruptedException e) {
                throw new RuntimeException(e.getMessage());
            }
            if (rsp.getRetType() != Common.RetType.RetType_Succeed_VALUE) {
                System.err.printf("getStaticInfoSync fail: %s\n", rsp.getRetMsg());
                throw new RuntimeException(rsp.getRetMsg());
            }
            for (QotCommon.SecurityStaticInfo info : rsp.getS2C().getStaticInfoListList()) {
                securityStaticInfos.add(info);
            }
        }

        if (securityStaticInfos.size() == 0) {
            System.err.printf("Error market:'%s' can not get stock info ", market);
            return new ArrayList<>();
        }
        System.out.println("SecuritySnapshot End");
        return securityStaticInfos;
    }
}
