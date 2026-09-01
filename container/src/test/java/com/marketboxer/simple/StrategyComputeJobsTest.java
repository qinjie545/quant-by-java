package com.marketboxer.simple;

import com.marketboxer.conf.mysql.MySQLConfiguration;
import com.marketboxer.data.access.mapper.TradeExecuteMapper;
import com.marketboxer.data.access.repo.StockFeatureRepo;
import com.marketboxer.data.access.repo.StockKLineRepo;
import com.marketboxer.data.access.repo.TradeExecuteRepo;
import com.marketboxer.startegy.compute.StrategyComputeService;
import com.marketboxer.startegy.compute.StrategyComputorFactory;
import com.marketboxer.startegy.compute.computor.Strategy001MACompute;
import com.marketboxer.trade.execute.FutuOperator;
import com.marketboxer.trade.execute.TradeExecuteService;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * //TODO Add more comment of this file
 *
 * @author qinjie
 * Created At : 2022/11/13 14:40
 */
@SpringBootTest(classes = {StrategyComputeService.class, Strategy001MACompute.class, StrategyComputorFactory.class, TradeExecuteService.class,
TradeExecuteRepo.class, TradeExecuteMapper.class, MySQLConfiguration.class, FutuOperator.class, StockFeatureRepo.class, StockKLineRepo.class})
@MapperScan("com.marketboxer.data.access.mapper")
public class StrategyComputeJobsTest {

    @Autowired
    StrategyComputeService strategyComputeService;

    @Test
    public void testStrategyCompute(){
        strategyComputeService.computeAllStrategy();
    }

}
