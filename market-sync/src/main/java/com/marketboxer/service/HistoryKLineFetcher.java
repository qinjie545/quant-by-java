package com.marketboxer.service;

import com.futu.openapi.pb.QotCommon;
import com.futu.openapi.services.SecurityKLineFacade;
import com.marketboxer.core.model.KLine;
import com.marketboxer.data.access.mapper.HistoryKLineMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 历史K线抓取
 * @author qinjie  at 2022/9/16
 * e
 **/
@Component
public class HistoryKLineFetcher {

    private static Logger logger = LoggerFactory.getLogger("HistoryKLineFetcher");

    @Autowired
    HistoryKLineMapper historyKLineMapper;

    public int fetchStoreHistoryKLineOfCodeStartEnd(String securityCode, String beginTime, String endTime){

        SecurityKLineFacade securityKLineFacade = new SecurityKLineFacade();
        /**
         * 仅仅支持A股股票下载
         */
        List<QotCommon.KLine> kLines = securityKLineFacade.query(QotCommon.QotMarket.QotMarket_CNSH_Security, securityCode, beginTime, endTime );
        if (!CollectionUtils.isEmpty(kLines)) {
             return  store(securityCode, kLines);
        }
        return 0;

    }

    /**
     * TODO 需要更加完善
     * 这个接口的本意是期望把一个股票的K线数据持续更新，知道更新到最新。当前先简单写一版本
     * 更加完善的做法是，需要查询当前的上市状态，如果是上市，则要从当前时间开始往前回溯，直到到上市日期（这些数据在basic_profile里有）。
     * 全部更新K线数据。
     *
     * @param securityCode
     * @return
     */
    public int updateStoreHistoryKLineOfCodeToNow(String securityCode){

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String nowTimeStr = simpleDateFormat.format(calendar.getTime());

        String timeBegin = "";

        Long timestampUpdated = historyKLineMapper.findLatestUpdateTimestamp(securityCode);
        if (timestampUpdated != null && timestampUpdated > 0 ) {
            Date timeUpdated = new Date(timestampUpdated);
            Calendar maxCalendar = Calendar.getInstance();
            maxCalendar.setTime(timeUpdated);
            maxCalendar.add(Calendar.DAY_OF_YEAR, +1);
            if (maxCalendar.after(Calendar.getInstance())) {
                //最新的更新时间+一天后，比当前天还大。说明当前已经是最新了。直接跳出
                return 0;
            }
            timeBegin = simpleDateFormat.format(maxCalendar.getTime());
        } else {
            timeBegin = "2000-01-01";
        }
        return fetchStoreHistoryKLineOfCodeStartEnd(securityCode, timeBegin, nowTimeStr);
    }

    private int store(String securityCode, List<QotCommon.KLine> kLines) {

        List<KLine> storeKLines = new ArrayList<>(kLines.size());

        for (QotCommon.KLine kLine : kLines ) {
            if (kLine == null ) {
                continue;
            }
            KLine storeKline = new KLine();
            storeKline.setCode(securityCode);
            storeKline.setHigh(kLine.getHighPrice());
            storeKline.setLow(kLine.getLowPrice());
            storeKline.setOpen(kLine.getOpenPrice());
            storeKline.setClose(kLine.getClosePrice());
            storeKline.setTimeKey(kLine.getTime());
            storeKline.setVolume(kLine.getVolume());
            storeKline.setChangeRate(kLine.getChangeRate());
            storeKline.setLastClose(kLine.getLastClosePrice());
            storeKline.setPeRatio(kLine.getPe());
            storeKline.setTurnover(kLine.getTurnover());
            storeKline.setTurnoverRate(kLine.getTurnoverRate());
            storeKline.setTimestamp(Double.valueOf(kLine.getTimestamp()).longValue() * 1000);
            storeKline.setCreateTime(System.currentTimeMillis());
            storeKline.setDayIntKey(getDayIntKeyFromTime(kLine.getTime()));
            storeKLines.add(storeKline);

        }

        int count =  historyKLineMapper.insertOrUpdateKLines(storeKLines);

        logger.info("insert success lines {}", count );

        return count;

    }

    private long getDayIntKeyFromTime(String time) {
        if (ObjectUtils.isEmpty(time)) {
            return 0;
        }

        String[] splitArray = time.split(" ");
        if (splitArray.length < 2){
            return 0;
        }

        String dateString = splitArray[0];

        dateString = dateString.replace("-", "");

        long dateLong = 0L;
        try{
            dateLong = Long.parseLong(dateString);
        } catch (NumberFormatException e){
            return 0;
        }
        return dateLong;
    }

}
