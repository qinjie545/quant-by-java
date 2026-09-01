package com.marketboxer.feature.make.test;

import com.marketboxer.data.access.mapper.StockFeatureMapper;
import com.marketboxer.data.access.repo.StockFeatureRepo;
import com.marketboxer.data.access.repo.StockKLineRepo;
import com.marketboxer.feature.make.FeatureGenService;
import com.marketboxer.feature.make.FeatureGenerator;
import com.marketboxer.feature.make.FeatureGeneratorFactory;
import com.marketboxer.feature.make.strategy.s001ma.Strategy001MAFeatureGen;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;


/**
 * @author qinjie
 */
@SpringBootTest(classes = {FeatureGenService.class, StockFeatureRepo.class,
        FeatureGeneratorFactory.class,Strategy001MAFeatureGen.class,
        FeatureGeneratorFactory.class, MySQLConfigurationTest.class, StockKLineRepo.class})
@MapperScan(basePackages = "com.marketboxer.data.access.mapper")
public class FeatureGenServiceTest {

    @Autowired
    FeatureGenService featureGenService;

    @Test
    public void test(){
        Assert.isTrue(featureGenService.startGenAllStrategyFeatures(), "gen features failed!");
        System.out.println("TestOK");
    }

}
