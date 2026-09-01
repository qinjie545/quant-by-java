package com.marketboxer.simple;

import com.marketboxer.trade.execute.TradeExecuteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * //TODO Add more comment of this file
 *
 * @author qinjie
 * Created At : 2022/11/13 14:41
 */
@SpringBootTest( classes = {TradeExecuteService.class})
public class TradeExecuteJobsTest {

    @Autowired
    TradeExecuteService tradeExecuteService;

    @Test
    public void testSubmitAllPendingTrade(){
        tradeExecuteService.submitAllPendingTrade();
    }


}
