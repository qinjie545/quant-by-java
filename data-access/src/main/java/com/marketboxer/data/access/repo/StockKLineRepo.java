package com.marketboxer.data.access.repo;

import com.marketboxer.core.model.KLine;
import com.marketboxer.data.access.mapper.HistoryKLineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author qinjie
 * @date 2022/10/4
 */
@Component
@Repository
public class StockKLineRepo {

    @Autowired
    HistoryKLineMapper historyKLineMapper;

    /**
     * 获取从dayEnd开始的最近N天的K线数据
     * @param dayEndInclude 截止日
     * @param latestN N条
     * @return
     */
    public List<KLine> queryLatestNKLines(long dayEndInclude, int latestN) {
        List<KLine> lines = historyKLineMapper.queryLatestNKLines(dayEndInclude, latestN);
        return lines;
    }

    /**
     * 查询当前最K线中，某只股票的最大天int
     * @param stockCode 股票代码
     * @return
     */
    public int findMaxDayInt(String stockCode) {
        Integer dayInt = historyKLineMapper.findMaxDayIntOf(stockCode);
        if (dayInt == null) {
            return 0;
        }
        return dayInt;
    }

    /**
     * 发现当前日，到最新一日，中间的dayInt的间隔
     * @param dayUpdated
     * @param dayMax
     * @return
     */
    public List<Integer> findDayIntFromStart2Latest(long dayUpdated, int dayMax) {
        List<Integer> dayIntDistinctList = historyKLineMapper.findDayIntFromStart2Latest(dayUpdated, dayMax);
        return dayIntDistinctList;
    }
}
