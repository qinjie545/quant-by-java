package com.marketboxer.simple;

import com.marketboxer.conf.mysql.MySQLConfiguration;
import com.marketboxer.service.HistoryKLineFetcher;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * //TODO Add more comment of this file
 *
 * @author qinjie
 * Created At : 2022/10/23 22:12
 */
@SpringBootTest(classes = {HistoryKLineFetcher.class, MySQLConfiguration.class})
@MapperScan("com.marketboxer.data.access.mapper")
public class MarketSynJobsTest {

    @Autowired
    private HistoryKLineFetcher historyKLineFetcher;

    @Test
    public void testMarketSync(){
        historyKLineFetcher.updateStoreHistoryKLineOfCodeToNow("601788");
    }

}
