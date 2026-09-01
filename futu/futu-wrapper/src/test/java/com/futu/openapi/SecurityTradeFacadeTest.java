package com.futu.openapi;

import com.futu.openapi.services.SecurityTradeFacade;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * @author qinjie
 * @date 2022/11/5
 */

public class SecurityTradeFacadeTest extends TestCase {

    @Test
    public void testBuySome(){

//        /**
//         * 港股
//         */
//        new SecurityTradeFacade().tradeBuyOrSell("06178", 200, 5, false, true );
        /**
         * A股
         */
        new SecurityTradeFacade().tradeBuyOrSell("601788", 200, 15, false, true, "ssshhhhs" );

    }

}
