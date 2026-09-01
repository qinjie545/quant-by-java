package com.futu.openapi.services;

import com.futu.openapi.FTAPI_Conn;
import com.futu.openapi.pb.*;
import com.futu.openapi.services.base.Config;
import com.futu.openapi.services.base.FutuBase;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class SecurityTradeFacade extends FutuBase {

    public long tradeBuyOrSell(String secCode, long quantity, double price, boolean isRealTrade, boolean buy, String flowNo) {
        try {
            //连接faceD
            boolean ret = initConnectTrdSync(Config.opendIP, Config.opendPort);
            if (!ret) {
                System.err.println("Fail to connect opend");
                return -1;
            } else {
                System.out.println("trd connected");
            }

            //解锁交易
            TrdUnlockTrade.Response unlockTradeRsp = unlockTradeSync(Config.unlockTradePwdMd5, true);
            if (unlockTradeRsp.getRetType() != Common.RetType.RetType_Succeed_VALUE) {
                System.err.printf("unlockTradeSync err; retType=%s msg=%s\n", unlockTradeRsp.getRetType(), unlockTradeRsp.getRetMsg());
                return -1;
            } else {
                System.out.println("unlock succeed");
            }

            TrdCommon.TrdEnv trdEnv = TrdCommon.TrdEnv.TrdEnv_Simulate;
            if (isRealTrade) {
                trdEnv = TrdCommon.TrdEnv.TrdEnv_Real;
            }
            /**
             * 交易市场选择
             */
            TrdCommon.TrdMarket trdMarket = TrdCommon.TrdMarket.TrdMarket_CN;

            TrdCommon.TrdSecMarket trdSecMarket = TrdCommon.TrdSecMarket.TrdSecMarket_CN_SH;

//            /**
//             * 获取当前的现金情况
//             */
//            TrdGetFunds.Response getFundsRsp = getFundsSync(Config.trdAcc, trdMarket, trdEnv,
//                    false, TrdCommon.Currency.Currency_Unknown);
//            System.out.printf("getFundsSync: %s\n", getFundsRsp);
//
//            TrdGetAccList.Response getAccListRsp = getAccListSync();
//            System.out.printf("getAccList: %s\n", getAccListRsp);
//
//            //获取仓位？
//            {
//                TrdGetPositionList.Response getPositionListRsp = getPositionListSync(Config.trdAcc,
//                        trdMarket,
//                        trdEnv, null,
//                        -50.0, 50.0, false);
//                System.out.printf("getPositionList: %s\n", getPositionListRsp);
//            }
//
//            //获取订单列表?
//            {
//                TrdGetOrderList.Response getOrderListRsp = getOrderListSync(Config.trdAcc, trdMarket,
//                        trdEnv, false, null,
//                        Arrays.asList(TrdCommon.OrderStatus.OrderStatus_Submitted));
//                System.out.printf("getOrderList: %s\n", getOrderListRsp);
//            }

            //同步下单？
            {
                TrdCommon.TrdHeader header = TrdCommon.TrdHeader.newBuilder()
                        .setTrdEnv(trdEnv.getNumber())
                        .setAccID(Config.trdAcc)
                        .setTrdMarket(trdMarket.getNumber())
                        .build();
                TrdPlaceOrder.C2S c2s = TrdPlaceOrder.C2S.newBuilder()
                        .setPacketID(trd.nextPacketID())
                        .setHeader(header)
                        .setTrdSide(buy?TrdCommon.TrdSide.TrdSide_Buy_VALUE : TrdCommon.TrdSide.TrdSide_Sell_VALUE)
                        .setOrderType(TrdCommon.OrderType.OrderType_Normal_VALUE)
                        .setCode(secCode)
                        .setQty(quantity)
                        .setPrice(price)
                        .setAdjustPrice(false)
                        .setSecMarket(trdSecMarket.getNumber())
                        .setRemark(flowNo)
                        .build();
                TrdPlaceOrder.Response placeOrderRsp = placeOrderSync(c2s);
                log.info("placeOrder: {}", placeOrderRsp);
                return placeOrderRsp.getS2C().getOrderID();
            }



//            {
//                TrdCommon.TrdFilterConditions filterConditions = TrdCommon.TrdFilterConditions.newBuilder()
//                        .addCodeList(secCode)
//                        .build();
//                TrdGetOrderFillList.Response getOrderFillListRsp = getOrderFillListSync(Config.trdAcc,
//                        trdMarket,
//                        trdEnv, false, filterConditions);
//                System.out.printf("getOrderFillList: %s\n", getOrderFillListRsp);
//            }
        }
        catch (InterruptedException e) {
            System.err.println("Interrupted");
        }

        return -1;
    }

    @Override
    public void onPush_UpdateOrderBook(FTAPI_Conn client, QotUpdateOrderBook.Response rsp) {
        System.out.printf("onPush_UpdateOrderBook: ask1: %f, bid1: %f\n",
                rsp.getS2C().getOrderBookAskList(0).getPrice(),
                rsp.getS2C().getOrderBookBidList(0).getPrice());
    }

    @Override
    public void onPush_UpdateBasicQuote(FTAPI_Conn client, QotUpdateBasicQot.Response rsp) {
        System.out.printf("onPush_UpdateBasicQuote: code: %s, high: %f, open: %f, low: %f, cur: %f\n",
                rsp.getS2C().getBasicQotList(0).getSecurity().getCode(),
                rsp.getS2C().getBasicQotList(0).getHighPrice(),
                rsp.getS2C().getBasicQotList(0).getOpenPrice(),
                rsp.getS2C().getBasicQotList(0).getLowPrice(),
                rsp.getS2C().getBasicQotList(0).getCurPrice());
    }

    @Override
    public void onPush_UpdateKL(FTAPI_Conn client, QotUpdateKL.Response rsp) {
        System.out.printf("onPush_UpdateKL: code: %s, close: %f, volume: %d\n",
                rsp.getS2C().getSecurity().getCode(),
                rsp.getS2C().getKlList(0).getClosePrice(),
                rsp.getS2C().getKlList(0).getVolume());
    }

    @Override
    public void onPush_UpdateRT(FTAPI_Conn client, QotUpdateRT.Response rsp) {
        System.out.printf("onPush_UpdateRT: time: %s, price: %f\n",
                rsp.getS2C().getRtList(0).getTime(),
                rsp.getS2C().getRtList(0).getPrice());
    }

    @Override
    public void onPush_UpdateTicker(FTAPI_Conn client, QotUpdateTicker.Response rsp) {
        System.out.printf("onPush_UpdateTicker: time: %s, dir: %d, price: %f\n",
                rsp.getS2C().getTickerList(0).getTime(),
                rsp.getS2C().getTickerList(0).getDir(),
                rsp.getS2C().getTickerList(0).getPrice());
    }

    @Override
    public void onPush_UpdateBroker(FTAPI_Conn client, QotUpdateBroker.Response rsp) {
        System.out.printf("onPush_UpdateBroker: ask_broker1: %s, bid_broker1: %s\n",
                rsp.getS2C().getBrokerAskList(0).getName(),
                rsp.getS2C().getBrokerBidList(0).getName());
    }

    @Override
    public void onPush_UpdateOrder(FTAPI_Conn client, TrdUpdateOrder.Response rsp) {
        System.out.printf("onPush_UpdateOrder: %s\n", rsp);
    }

    @Override
    public void onPush_UpdateOrderFill(FTAPI_Conn client, TrdUpdateOrderFill.Response rsp) {
        System.out.printf("onPush_UpdateOrderFill: %s\n", rsp);
    }
}
