package com.marketboxer.rest;

import com.marketboxer.service.HistoryKLineFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("/kline")
public class HistoryKLineController {

    @Autowired
    HistoryKLineFetcher historyKLineFetcher;

    @GetMapping("/kline")
    public String klineDefault(){
        return "usage: /kline/get/{code}/{start}/{end} ";
    }


    @GetMapping("/kline/get/{code}/{start}/{end}")
    public String getHistoryKLines(@PathVariable("code") String code, @PathVariable("start") String start, @PathVariable("end") String end){
        try{
            int storeCount = historyKLineFetcher.fetchStoreHistoryKLineOfCodeStartEnd(code , start, end);
            return String.format("Done insert or update %d lines", storeCount);
        }catch (Exception e) {
            return String.format(String.format("Exception : %s", e.getMessage()));
        }
    }

    @GetMapping("/updateKline")
    public String updateKline() {
        int updateCnt = historyKLineFetcher.updateStoreHistoryKLineOfCodeToNow("601788");
        return String.format("update %d klines", updateCnt);
    }



}
