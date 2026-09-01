package com.marketboxer.job;

import com.marketboxer.trade.execute.TradeExecuteService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;



/**
 * @author qinjie
 * @date ${DATE}
 */
@Component
public class TradeExecuteJobs extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger("StrategyComputeJobs");

    @Autowired
    TradeExecuteService tradeExecuteService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("strategyComputeJobExecuting!");
        //遍历待执行交易行为表的内容，调用futu接口进行交易
        boolean submitSuccess = tradeExecuteService.submitAllPendingTrade();
        if (! submitSuccess ) {
            logger.error("SUBMIT trade to market ERROR!! return false!");
        }

    }

}
