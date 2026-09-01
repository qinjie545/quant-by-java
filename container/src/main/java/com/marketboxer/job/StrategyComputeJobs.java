package com.marketboxer.job;

import com.marketboxer.startegy.compute.StrategyComputeService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class StrategyComputeJobs extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger("StrategyComputeJobs");

    @Autowired
    StrategyComputeService strategyComputeService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        //遍历所有的可用状态的策略，进行计算。并生成机器交易行为。存储到机器交易行为准备表

        strategyComputeService.computeAllStrategy();

        logger.info("strategyComputeJobExecuting!");
    }

}
