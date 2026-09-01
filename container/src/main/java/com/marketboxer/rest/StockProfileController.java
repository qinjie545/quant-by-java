package com.marketboxer.rest;

import com.marketboxer.service.StockProfileFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qinjie
 */
@RestController("/stock_profile")
public class StockProfileController {

    @Autowired
    StockProfileFetcher stockProfileFetcher;

    @GetMapping("/stock_profile/{market_code}")
    public String updateAllStocks(@PathVariable("market_code") String marketCode) {
        int count = stockProfileFetcher.fetchStockProfile(marketCode);
        return String.format("update total %d security basic info!", count);
    }

    @GetMapping("/stock_profile/help")
    public String helpMsg() {
        return "usage: /stock_profile/{market_code} " +
                "\n marketCode can be : CNSH_S/CNSH_F/CNSZ_S/CNSZ_F/HK_S/HK_F/US_S/US_F/SG_S/SG_F/JP_S";
    }

}
