package com.marketboxer.data.access.mapper;

import com.marketboxer.data.access.model.TradeAccountDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * 交易对应的账户相关的信息
 *
 * @author qinjie
 * Created At : 2022/10/29 22:30
 */
@Mapper
@Repository
public interface TradeAccountMapper {

    @Select("SELECT * FROM stock_account where strategy_id = #{strategy_id}")
    TradeAccountDO getAccountBalanceInfoOf(@Param("strategy_id") String strategyId);

    @Update("UPDATE stock_account set money_hold = money_hold + #{money_changes} where strategy_id = #{strategy_id}")
    int updateAccountMoneyHoldByStrategyId(@Param("strategy_id") String strategyId, @Param("money_changes") long moneyChangesFromTrade);

    @Update("UPDATE stock_account set sec_hold = sec_hold + #{sec_changes} where strategy_id = #{strategy_id}")
    int updateAccountSecurityHoldByStrategyId(@Param("strategy_id")String strategyId, @Param("sec_changes") long securityChangesFromTrade);
}
