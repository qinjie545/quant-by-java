package com.marketboxer.data.access.repo;

import com.marketboxer.core.model.AccountBalance;
import com.marketboxer.core.model.ExecuteStatus;
import com.marketboxer.core.model.TradeExecuteRequest;
import com.marketboxer.data.access.mapper.TradeAccountMapper;
import com.marketboxer.data.access.mapper.TradeExecuteMapper;
import com.marketboxer.data.access.model.TradeAccountDO;
import com.marketboxer.data.access.model.TradeExecuteDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 交易执行相关的Repo
 *
 * @author qinjie
 * Created At : 2022/10/24 23:14
 */
@Repository
@Slf4j
public class TradeExecuteRepo {

    @Autowired
    TradeExecuteMapper tradeExecuteMapper;

    @Autowired
    TradeAccountMapper tradeAccountMapper;

    public boolean saveNew(String flowNo, TradeExecuteRequest tradeExecuteRequest) {

        long nowTimestamp = System.currentTimeMillis();
        int statusCode = ExecuteStatus.STATUS_NEW.getCode();
        Long count = tradeExecuteMapper.insert(flowNo, tradeExecuteRequest, nowTimestamp, statusCode);
        if (count != null && count > 0) {
            return true;
        }
        return false;
    }

    public AccountBalance getAccountBalanceInfoOf(String strategyId) {

        if (ObjectUtils.isEmpty(strategyId)) {
            log.error("EMPTY strategyId for query AccountBalance");
            return null;
        }

        TradeAccountDO tradeAccountDO = tradeAccountMapper.getAccountBalanceInfoOf(strategyId);
        if (tradeAccountDO == null ) {
            log.error("未查询到指定的strategyId的账户相关信息!,strategy_id :{}", strategyId);
            return null;
        }

        AccountBalance accountBalance = new AccountBalance();
        accountBalance.setStrategyId(tradeAccountDO.getStrategyId());
        accountBalance.setSecAmount(tradeAccountDO.getSecHold());
        accountBalance.setMoneyAmountInCent(tradeAccountDO.getMoneyHold());
        return accountBalance;
    }

    public List<TradeExecuteDO> findAllPendingTradeExecute() {
        return tradeExecuteMapper.findTradeExecuteByStatus(ExecuteStatus.STATUS_NEW.getCode(), ExecuteStatus.STATUS_DOWN.getCode());

    }

    public boolean updateStatus(String flowNo, int code) {
        int count = tradeExecuteMapper.updateStatusByFlowNo(flowNo, code);
        if (count > 0) {
            return true;
        }
        return false;
    }

    public boolean updateExecuteOrderNo(String flowNo, long orderNo) {
        int count = tradeExecuteMapper.updateExecuteOrderNoByFlowNo(flowNo, orderNo);
        if (count > 0 ) {
            log.info("update execute order No Success!");
            return true;
        }
        log.info("update execute order No FAILED!!");
        return false;
    }

    public boolean updateAccountMoneyHold(String strategyId, long moneyChangesFromTrade) {
        int count = tradeAccountMapper.updateAccountMoneyHoldByStrategyId(strategyId, moneyChangesFromTrade);
        if (count > 0 ) {
            log.info("update account money hold Success! {} : {}", strategyId, moneyChangesFromTrade);
            return true;
        }
        log.info("update account money hold FAILED!!{} : {}", strategyId, moneyChangesFromTrade);
        return false;
    }

    public boolean updateAccountSecurityHold(String strategyId, long securityChangesFromTrade) {
        int count = tradeAccountMapper.updateAccountSecurityHoldByStrategyId(strategyId, securityChangesFromTrade);
        if (count > 0 ) {
            log.info("update account security hold Success! {} : {}", strategyId, securityChangesFromTrade);
            return true;
        }
        log.info("update account security hold FAILED!!{} : {}", strategyId, securityChangesFromTrade);
        return false;
    }
}
