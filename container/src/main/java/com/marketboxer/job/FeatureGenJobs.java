package com.marketboxer.job;

import com.marketboxer.feature.make.FeatureGenService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class FeatureGenJobs extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger("FeatureGenJobs");

    @Autowired
    FeatureGenService featureGenService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        // TODO 通过已经给更新的市场基本信息，更新策略自带的Feature生成信息，主要是通过调用策略的特征生成逻辑进行更新
        // 需要检查策略版本，增量更新等。

        logger.info("featureGenJobExecuting!");

        featureGenService.startGenAllStrategyFeatures();

    }

}
