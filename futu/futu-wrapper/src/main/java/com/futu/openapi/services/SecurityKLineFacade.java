package com.futu.openapi.services;

import com.futu.openapi.pb.*;
import com.futu.openapi.services.base.FutuBase;

import java.util.ArrayList;
import java.util.List;

public class SecurityKLineFacade extends FutuBase {
    public List<QotCommon.KLine> query(QotCommon.QotMarket market, String securityCode, String beginTime, String endTime) {

        System.out.println("Run SecuritySnapshot");
        try {
            boolean ret = initConnectQotSync("127.0.0.1", (short) 11111);
            if (ret) {
                System.out.println("qot connected");
            } else {
                System.out.println("fail to connect opend");
                return new ArrayList<>() ;
            }

            QotCommon.Security sec = QotCommon.Security.newBuilder().setCode(securityCode).setMarket(market.getNumber()).build();
            QotRequestHistoryKL.C2S c2s = QotRequestHistoryKL.C2S.newBuilder().setSecurity(sec).setKlType(QotCommon.KLType.KLType_Day.getNumber())
                    .setBeginTime(beginTime)
                    .setEndTime(endTime)
                    .setRehabType(QotCommon.RehabType.RehabType_Forward_VALUE)
                    .build();

            QotRequestHistoryKL.Response resp = getHistoryKL(c2s);

            if (resp.getRetType() != Common.RetType.RetType_Succeed_VALUE) {
                System.out.println("error when request history KL");
                throw new RuntimeException(resp.getRetMsg());
            }

            return resp.getS2C().getKlListList();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}