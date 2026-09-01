package com.marketboxer.startegy.compute.computor;

import com.alibaba.fastjson.JSONObject;
import com.marketboxer.core.model.KLine;
import com.marketboxer.core.model.StockFeature;
import com.marketboxer.core.model.constant.MarketType;
import com.marketboxer.data.access.repo.StockFeatureRepo;
import com.marketboxer.data.access.repo.StockKLineRepo;
import com.marketboxer.feature.make.strategy.s001ma.Feature001MA;
import com.marketboxer.startegy.compute.annotation.StrategyComputor;
import com.marketboxer.core.model.annotation.Strategy;
import com.marketboxer.core.model.TradeAction;
import com.marketboxer.core.model.TradeExecuteRequest;
import com.marketboxer.trade.execute.TradeExecuteService;
import com.marketboxer.core.model.AccountBalance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * 策略计算组件
 * @author qinjie
 * Created At : 2022/10/16 23:30
 */
@Strategy("Strategy001_MA")
@StrategyComputor
@Slf4j
public class Strategy001MACompute implements StrategyCompute {

    @Autowired
    TradeExecuteService tradeExecuteService;

    @Autowired
    StockFeatureRepo stockFeatureRepo;

    @Autowired
    StockKLineRepo stockKLineRepo;

    @Override
    public boolean compute() {
        String strategyId = getStrategyId();
        if (ObjectUtils.isEmpty(strategyId)) {
            return false;
        }
        /**
         * 1.确定当前日期，当前是否是交易日，是否已经收盘
         * 2.查询特征计算结果
         * 3.通过特征计算结果进行策略计算
         * 4.输出策略计算结果
         */
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String timeString = simpleDateFormat.format(Calendar.getInstance().getTime());
        if (ObjectUtils.isEmpty(timeString)) {
            throw new RuntimeException("Date error! can't get a now Date");
        }
        long dayIntOfNow = Long.parseLong(timeString);
        long dayNewFeature = 0;
        StockFeature<String> stockFeature = stockFeatureRepo.findLatestFeatureItem(strategyId,"601788");

        if (stockFeature == null) {
            log.error("查询最新特征记录出错！");
        }

        if (stockFeature != null) {
            dayNewFeature = stockFeature.getDayIntKey();
        }
        if (dayIntOfNow != dayNewFeature) {
            //今日最新的特征还没有生成，或者有其他的错误（比如，今日不是交易日）
            //仅当今日是交易日，而且今日的特征已经最新生成，才会执行策略操作
            return false;
        }

        String featureString = stockFeature.getFeatureRawStore();
        Feature001MA feature001MA = JSONObject.parseObject(featureString, Feature001MA.class);

        double ma5 = feature001MA.getMa5();
        double ma10 = feature001MA.getMa10();
        double ma15 = feature001MA.getMa15();
        double ma20 = feature001MA.getMa20();

        log.info("ma5={},ma10={},ma15={},ma20={}",ma5, ma10, ma15, ma20);

        long dayIntKey = stockFeature.getDayIntKey();
        List<KLine> kLines = stockKLineRepo.queryLatestNKLines(dayIntKey,1);

        if (ObjectUtils.isEmpty(kLines)){
            log.error("klines can't find! of day : {}", dayIntKey);
            return  false;
        }

        KLine kLine = kLines.get(0);

        double lastClose = kLine.getClose();

        AccountBalance accountBalance = tradeExecuteService.getAccountBalanceInfoOf(strategyId);

        long secHold = accountBalance.getSecAmount();
        long moneyHoldInCent = accountBalance.getMoneyAmountInCent();

        if (ma5 > ma20) {
            if ( ((ma5 - ma20) / ma20 ) > 0.01 ) {
                log.info("金叉");
                log.info("全部买入时机到");
                log.info("ma5= {} , ma20 = {}", ma5, ma20);
                //Buy all with lastClose price
                //总共可以购买的数量，因为按照一手100计算，所有会不能全部买上
                int canBuyAmount = (int) (moneyHoldInCent /  ( lastClose * 100 ) );
                canBuyAmount = canBuyAmount - canBuyAmount % 100;

                if (canBuyAmount <= 0) {
                    //钱不够买
                    log.info("当前计划全仓，但是没有钱了");
                    return false;
                }

                double buyMoneyTotal = canBuyAmount * lastClose;

                TradeExecuteRequest tradeExecuteRequest = TradeExecuteRequest.builder()
                        .strategyId(strategyId)
                        .secCode("601788")
                        .marketType(MarketType.MARKET_SH_A)
                        .executeAction(TradeAction.ACTION_BUY_ALL.getCode())
                        .moneyAmount( (long)(buyMoneyTotal * 100) )
                        .price(lastClose)
                        .secAmount(canBuyAmount).build();
                String flowNo = tradeExecuteService.submitTradeExecuteRequest(stockFeature.getDayIntKey(), tradeExecuteRequest);
                log.info("submit trade execute request success! flowNo : {}", flowNo);
            } else {
                log.info("未触发金叉阈值");
            }
        } else {
            if (((ma20 - ma5) / ma5) > 0.01) {
                log.info("死叉");
                log.info("全部卖出时机到");
                log.info("ma5= {} , ma20 = {}", ma5, ma20);
                //Sell All with lastClose price
                if (secHold <= 0) {
                    log.info("当前计算结果，全部清仓，当前确实已经清仓，则不操作  ");
                    return false;
                }
                //只能卖出整数手，不能卖散股
                secHold = secHold - secHold % 100;
                TradeExecuteRequest tradeExecuteRequest = TradeExecuteRequest.builder()
                        .strategyId(strategyId)
                        .secCode("601788")
                        .marketType(MarketType.MARKET_SH_A)
                        .executeAction(TradeAction.ACTION_SELL_ALL.getCode())
                        .moneyAmount((long) (secHold * lastClose * 100))
                        .price(lastClose)
                        .secAmount(secHold).build();
                String flowNo = tradeExecuteService.submitTradeExecuteRequest(stockFeature.getDayIntKey(), tradeExecuteRequest);
                log.info("submit trade execute request success! flowNo : {}", flowNo);
            } else {
                log.info("未触发死叉阈值");
            }
        }

        /**
         * python strategy Code here
         */
        /**
         *
         * ma5 = row['ma5']
         *         ma10 = row['ma10']
         *         ma15 = row['ma15']
         *         ma20 = row['ma20']
         *         last_close = row['last_close']
         *         open_price = row['open']
         *         close_price = row['close']
         *         time_day = index
         *
         *         low_price = min(open_price, close_price)
         *         high_price = max(open_price, close_price)
         *
         *         if ma5 > ma20:
         *             if ((ma5 - ma20) / ma20) > 0.03 :
         *                 print("金叉")
         *                 print("全部买入时机到time_day:%s" % time_day)
         *                 print("ma5=%.4f , ma20 = %.4f" % (ma5,ma20))
         *                 if cash_amount > 0 and (cash_amount / last_close) > min_buy_unit:
         *                     if last_close >= low_price and last_close <= high_price:
         *                         print("last_close price is between open and close")
         *                         stock_can_buy = cash_amount / last_close
         *                         stock_bought = stock_can_buy -( stock_can_buy % min_buy_unit)
         *                         if stock_bought > 0:
         *                             stock_hold = stock_hold + stock_bought
         *                             cash_amount = cash_amount - stock_bought * last_close
         *                             print("stock_hold:%d, and cash amount:%.2f, and total_captical:%.2f" % (stock_hold, cash_amount, stock_hold*close_price+cash_amount))
         *                             stock_kline_601788.stock_bought[index] = stock_bought
         *                         else:
         *                             stock_kline_601788.stock_bought[index] = 0
         *                     elif last_close > high_price:
         * #                       昨日收盘价，高于等于今日最高价。则挂限价单不会成功。
         *                         print("jump too low, can't buy!!!!!!!!!")
         *                         buy_cnt_jump_too_low = buy_cnt_jump_too_low + 1
         *                     elif last_close < low_price:
         * #                       昨日收盘价，低于今日最低价，则挂单买入，也无法成功，价格太低。
         *                         print("jump too high, can't buy!!!!!!!!!")
         *                         buy_cnt_jump_too_high = buy_cnt_jump_too_high + 1
         *                     else:
         *                         print("WTF")
         *
         *         else:
         *             if ((ma20 - ma5) / ma5) > 0.03:
         *                 print("死叉")
         *                 print("全部卖出时机到")
         *                 print("全部买入时机到time_day:%s" % time_day)
         *                 print("ma5=%.4f , ma20 = %.4f" % (ma5,ma20))
         *                 if stock_hold > 0 and stock_hold > min_buy_unit :
         *                     if last_close >= low_price and last_close <= high_price:
         *                         stock_can_sell = stock_hold
         *                         stock_sold= stock_can_sell -( stock_can_sell % min_buy_unit)
         *                         stock_hold = stock_hold - stock_sold
         *                         cash_amount = cash_amount + stock_sold * last_close
         *                         print("stock_hold:%d, and cash amount:%.2f, and total_captical:%.2f" % (stock_hold, cash_amount, stock_hold*close_price+cash_amount))
         *                         stock_kline_601788.stock_bought[index] = -stock_hold
         *                     elif last_close < low_price:
         *                         print("jump too high, can't sell!!!!!!!!!")
         *                         sell_cnt_jump_too_high = sell_cnt_jump_too_high + 1
         *                     elif last_close > high_price:
         *                         print("jump too low, can't sell!!!!!!!!!")
         *                         sell_cnt_jump_too_low = sell_cnt_jump_too_low + 1
         *                     else:
         *                         print("WTF")
         */

        return true;
    }


}
