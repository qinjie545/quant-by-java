package com.marketboxer.job;

import com.marketboxer.service.HistoryKLineFetcher;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class MarketSyncJobs extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger("MarketSyncJobs");

    @Autowired
    private HistoryKLineFetcher historyKLineFetcher;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        // TODO 更新当前市场的基本信息

        // TODO 更新当前关注标的的今日市场表现信息
        // 更新601788这一只股票的K线信息到今日
        historyKLineFetcher.updateStoreHistoryKLineOfCodeToNow("601788");

        logger.info("marketSyncJobExecuting!");
    }

}
