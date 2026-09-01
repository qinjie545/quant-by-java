package com.marketboxer.trade.execute;

import com.futu.openapi.services.SecurityTradeFacade;
import com.marketboxer.data.access.model.TradeExecuteDO;
import org.springframework.stereotype.Component;

/**
 * 富途相关API的操作组件
 *
 * @author qinjie
 * Created At : 2022/11/8 0:19
 */
@Component
public class FutuOperator {



    public long placeOrder(TradeExecuteDO tradeExecuteDO){
        String secCode = tradeExecuteDO.getSecCode();
        long secAmount = tradeExecuteDO.getSecAmount();
        double price = tradeExecuteDO.getPrice();
        return new SecurityTradeFacade().tradeBuyOrSell(secCode, secAmount, price, false, true , tradeExecuteDO.getFlowNo());
    }


}
