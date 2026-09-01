package com.marketboxer.job;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

//    /**
//     * 每天固定时间进行市场状态同步
//     */
    private String marketSyncCron = "0 0 18 ? * 2,3,4,5,6"; // corn表达式

    /**
     * 每天固定时间进行特征计算
     */
    private String featureGenCron = "0 0 20 ? * 2,3,4,5,6"; // corn表达式

    /**
     * 每天固定时间进行策略计算
     */
    private String strategyComputeCron = "0 0 22 ? * 2,3,4,5,6"; // corn表达式

    /**
     * 市场交易定时器，每天固定时间进行挂单
     */
    private String tradeExecuteCron = "0 0 10 ? * 2,3,4,5,6";

    /**
     * 每天固定时间进行市场状态同步
     */
//    private String marketSyncCron = "0 *1 * ? * 1,2,3,4,5,6,7"; // corn表达式
//
//    /**
//     * 每天固定时间进行特征计算
//     */
//    private String featureGenCron = "0 *2 * ? * 1,2,3,4,5,6,7"; // corn表达式
//
//    /**
//     * 每天固定时间进行策略计算
//     */
//    private String strategyComputeCron = "0 *3 * ? * 1,2,3,4,5,6,7"; // corn表达式
//
//    /**
//     * 市场交易定时器，每天固定时间进行挂单
//     */
//    private String tradeExecuteCron = "0 *4 * ? * 1,2,3,4,5,6,7";

    @Bean
    public JobDetail marketSyncJob() {
        return JobBuilder.newJob(MarketSyncJobs.class).withIdentity("MarketSyncJobs").storeDurably().build();
    }

    @Bean
    public Trigger marketSyncTrigger() {
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(marketSyncCron);
        return TriggerBuilder.newTrigger().forJob(marketSyncJob())
                .withIdentity("MarketSyncJob").withSchedule(scheduleBuilder).build();
    }

    @Bean
    public JobDetail featureGenJob() {
        return JobBuilder.newJob(FeatureGenJobs.class).withIdentity("FeatureGenJobs").storeDurably().build();
    }

    @Bean
    public Trigger featureGenTrigger() {
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(featureGenCron);
        return TriggerBuilder.newTrigger().forJob(featureGenJob())
                .withIdentity("FeatureGenJob").withSchedule(scheduleBuilder).build();
    }

    @Bean
    public JobDetail strategyComputeJob() {
        return JobBuilder.newJob(StrategyComputeJobs.class).withIdentity("StrategyComputeJobs").storeDurably().build();
    }

    @Bean
    public Trigger strategyComputeTrigger() {
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(strategyComputeCron);
        return TriggerBuilder.newTrigger().forJob(strategyComputeJob())
                .withIdentity("strategyComputeJob").withSchedule(scheduleBuilder).build();
    }

    @Bean
    public JobDetail tradeExecuteJob() {
        return JobBuilder.newJob(TradeExecuteJobs.class).withIdentity("TradeExecuteJobs").storeDurably().build();
    }

    @Bean
    public Trigger tradeExecuteTrigger() {
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(tradeExecuteCron);
        return TriggerBuilder.newTrigger().forJob(tradeExecuteJob())
                .withIdentity("tradeExecuteJob").withSchedule(scheduleBuilder).build();
    }



}