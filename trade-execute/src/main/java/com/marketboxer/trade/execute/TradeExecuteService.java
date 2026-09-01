package com.marketboxer.trade.execute;

import com.marketboxer.core.model.ExecuteStatus;
import com.marketboxer.core.model.TradeAction;
import com.marketboxer.core.model.TradeExecuteRequest;
import com.marketboxer.data.access.model.TradeExecuteDO;
import com.marketboxer.data.access.repo.TradeExecuteRepo;
import com.marketboxer.core.model.AccountBalance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;


/**
 * 这个是交易执行器服务，用来提交交易申请
 * 执行交易，记录交易行为等
 *
 * @author qinjie
 * Created At : 2022/10/17 0:18
 */
@Component
@Slf4j
public class TradeExecuteService {

    @Autowired
    TradeExecuteRepo tradeExecuteRepo;

    @Autowired
    FutuOperator futuOperator;


    /**
     *  保存当前的交易执行请求，并在交易时间到时，进行交易执行
     *  增加一条新的交易执行申请
     * @return 返回流水编号
     */
    public String submitTradeExecuteRequest(long createDayInt, TradeExecuteRequest tradeExecuteRequest) {
        /**
         * 先把当前的提交请求保存到待执行队列，等到开始执行的操作被触发后，就逐个执行挂单
         */
        /**
         * 当前因为一天只产生一次计算，所以默认只有一个rowId,就是0
         */
        long rowId = System.currentTimeMillis();
        String FLOW_NO_PATTERN = "STRATEGY:%s-CREATE_DAY:%d-ROWID:%d";
        String flowNo = String.format(FLOW_NO_PATTERN, tradeExecuteRequest.getStrategyId(), createDayInt, rowId );
        boolean saveResult = tradeExecuteRepo.saveNew(flowNo, tradeExecuteRequest);
        if (saveResult) {
            return flowNo;
        }
        return null;
    }

    public ExecuteStatus queryTradeExecuteStatus(String flowNo) {
        //TODO P2
        //查询当前的交易执行状态
        return ExecuteStatus.STATUS_NEW;
    }

    /**
     * 开始提交所有的未完成的状态的交易到市场中
     */
    public boolean submitAllPendingTrade() {
        /* TODO P0
          1.查询出来所有的未执行成功，而且状态是ON的待执行交易
          2.逐个挂单到交易市场。
          3.监听市场交易回调，成功后，修改状态到DONE
         */
        List<TradeExecuteDO> tradeExecuteDOs = tradeExecuteRepo.findAllPendingTradeExecute();

        if (ObjectUtils.isEmpty(tradeExecuteDOs)) {
            log.info("没有待执行的未挂单任务");
        }

        for (TradeExecuteDO tradeExecuteDO : tradeExecuteDOs) {
            if (tradeExecuteDO == null) {
                continue;
            }
            //TODO执行操作
            long orderNo = futuOperator.placeOrder(tradeExecuteDO);

            log.info("place Order over , orderNo:{}", orderNo);

            if (orderNo > 0 ) {
                System.out.printf("orderPlaced: %d%n", orderNo );
                boolean updateStatusResult = tradeExecuteRepo.updateStatus(tradeExecuteDO.getFlowNo(), ExecuteStatus.STATUS_ON.getCode());
                boolean updateExecuteOrderNoResult = tradeExecuteRepo.updateExecuteOrderNo(tradeExecuteDO.getFlowNo(), orderNo);
                if (updateStatusResult && updateExecuteOrderNoResult) {
                    log.info("更新订单状态、更新订单编号成功！");
                }
                boolean updateMoneyChangesRes = tradeExecuteRepo.updateAccountMoneyHold( tradeExecuteDO.getStrategyId(), getMoneyChangesFromTrade(tradeExecuteDO));
                boolean updateSecChangesRes = tradeExecuteRepo.updateAccountSecurityHold( tradeExecuteDO.getStrategyId(),  getSecurityChangesFromTrade(tradeExecuteDO));
                log.info("update money and sec changes to DB : {}, {}", updateMoneyChangesRes, updateSecChangesRes);
            } else {
                return false;
            }
        }
        return true;
    }

    private long getSecurityChangesFromTrade(TradeExecuteDO tradeExecuteDO) {
        if (TradeAction.ACTION_BUY_ALL.getCode() == tradeExecuteDO.getTradeAction()
                || TradeAction.ACTION_BUY_SOME.getCode() == tradeExecuteDO.getTradeAction()) {
            return tradeExecuteDO.getSecAmount() ;
        } else if (TradeAction.ACTION_SELL_ALL.getCode() == tradeExecuteDO.getTradeAction()
                || TradeAction.ACTION_SELL_SOME.getCode() == tradeExecuteDO.getTradeAction()){
            return  tradeExecuteDO.getSecAmount() * -1;
        } else {
            //Unknown trade Action!
            return 0;
        }
    }

    private long getMoneyChangesFromTrade(TradeExecuteDO tradeExecuteDO) {
        if (TradeAction.ACTION_BUY_ALL.getCode() == tradeExecuteDO.getTradeAction()
                || TradeAction.ACTION_BUY_SOME.getCode() == tradeExecuteDO.getTradeAction()) {
            return tradeExecuteDO.getMoneyAmount() * -1;
        } else if (TradeAction.ACTION_SELL_ALL.getCode() == tradeExecuteDO.getTradeAction()
                || TradeAction.ACTION_SELL_SOME.getCode() == tradeExecuteDO.getTradeAction()){
            return  tradeExecuteDO.getMoneyAmount();
        } else {
            //Unknown trade Action!
            return 0;
        }
    }

    /**
     * 查询当前策略的策略账号情况
     * 会返回钱 + 股票仓位
     * @param strategyId 策略编号
     * @return
     */
    public AccountBalance getAccountBalanceInfoOf(String strategyId) {
        if ( ObjectUtils.isEmpty(strategyId) ) {
            log.error("空的strategyId!");
            return  null;
        }
        AccountBalance accountBalance = tradeExecuteRepo.getAccountBalanceInfoOf(strategyId);
        return accountBalance;
    }
}
