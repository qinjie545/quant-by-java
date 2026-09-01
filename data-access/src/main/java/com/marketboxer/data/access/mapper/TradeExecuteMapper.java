package com.marketboxer.data.access.mapper;

import com.marketboxer.core.model.TradeExecuteRequest;
import com.marketboxer.data.access.model.TradeExecuteDO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 交易执行相关的Mapper
 *
 * @author qinjie
 * Created At : 2022/10/24 23:09
 */

@Mapper
@Repository
public interface TradeExecuteMapper {


    /**
     * 插入一条待执行的交易记录
     *
     * @param flowNo              流水编号
     * @param tradeExecuteRequest 交易细节
     * @param createTime          创建时间
     * @param statusCode
     * @return 成功行数
     */
    @Insert("INSERT INTO stock_trade_execute(flow_no, strategy_id, trade_action, sec_amount, money_amount, market_type, sec_code, create_time, execute_status, price) " +
            "values(#{flow_no}, #{trade_request.strategyId}, #{trade_request.executeAction}, #{trade_request.secAmount}," +
            " #{trade_request.moneyAmount}, #{trade_request.marketType}, #{trade_request.secCode}, #{create_time}, #{status_code}, #{trade_request.price})")
    Long insert(@Param("flow_no") String flowNo, @Param("trade_request") TradeExecuteRequest tradeExecuteRequest,
                @Param("create_time") long createTime, @Param("status_code") int statusCode);

    /**
     * 查询当前某一些状态的交易执行记录
     * @param statusArray 状态数组
     * @return
     */
    @Select("<script>" +
            "SELECT * FROM stock_trade_execute where execute_status in (" +
                "<foreach collection='status_arr' index='index' item='item'  separator=','> " +
                " #{item}" +
                "</foreach> "+
            ")</script>")
    List<TradeExecuteDO> findTradeExecuteByStatus(@Param("status_arr") Integer... statusArray);

    /**
     * 根据流水号来更新交易执行状态
     * @param flowNo 流水号
     * @param code 执行状态
     * @return 影响行数
     */
    @Update("UPDATE stock_trade_execute set execute_status = #{status_code} where flow_no = #{flow_no}")
    int updateStatusByFlowNo(@Param("flow_no") String flowNo, @Param("status_code") int code);

    /**
     * 根据流水号来更新交易订单编号
     * @param flowNo 流水号
     * @param orderNo 订单编号
     * @return 影响行数
     */
    @Update("UPDATE stock_trade_execute set order_no = #{order_no} where flow_no = #{flow_no}")
    int updateExecuteOrderNoByFlowNo(@Param("flow_no") String flowNo, @Param("order_no") long orderNo);
}
