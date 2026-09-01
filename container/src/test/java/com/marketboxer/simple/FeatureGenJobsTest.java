package com.marketboxer.simple;

import com.marketboxer.conf.mysql.MySQLConfiguration;
import com.marketboxer.data.access.mapper.StockFeatureMapper;
import com.marketboxer.data.access.repo.StockFeatureRepo;
import com.marketboxer.data.access.repo.StockKLineRepo;
import com.marketboxer.feature.make.FeatureGenService;
import com.marketboxer.feature.make.FeatureGeneratorFactory;
import com.marketboxer.feature.make.strategy.s001ma.Strategy001MAFeatureGen;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * //TODO Add more comment of this file
 *
 * @author qinjie
 * Created At : 2022/10/23 21:30
 */
@SpringBootTest(classes = {FeatureGenService.class, StockFeatureRepo.class,
        StockFeatureMapper.class, FeatureGeneratorFactory.class,
        MySQLConfiguration.class, Strategy001MAFeatureGen.class, StockKLineRepo.class})
@MapperScan(basePackages = "com.marketboxer.data.access.mapper")
public class FeatureGenJobsTest {

    @Autowired
    FeatureGenService featureGenService;

    @Test
    public void testStart() {
        featureGenService.startGenAllStrategyFeatures();
    }

}
