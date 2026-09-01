package com.marketboxer.data.access.mapper;

import com.marketboxer.core.model.KLine;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface HistoryKLineMapper {

  @Select("SELECT * FROM stock_history_kline WHERE code = #{code}")
  List<KLine> findKLinesByCode(@Param("code") String code);

  @Insert( "<script>" +
            "insert into stock_history_kline (code, high, low, open, close,time_key,pe_ratio ,turnover_rate,volume, turnover, change_rate, last_close , timestamp, create_time, day_int_key )  VALUES "
            + "<foreach collection='list' item='item' index='index' separator=','> " +
                "(#{item.code},#{item.high},#{item.low},#{item.open},#{item.close}, #{item.timeKey}, #{item.peRatio}," +
                     " #{item.turnoverRate}, #{item.volume}, #{item.turnover}, #{item.changeRate}, #{item.lastClose}, #{item.timestamp}, #{item.createTime}, #{item.dayIntKey})" +
              "</foreach> "
          + "</script>")
  Integer insertOrUpdateKLines(@Param("list") List<KLine> kLines );

  /**
   * 查询当前股票的最新一个更新的timestamp，基于这个更新后续的
   * @param securityCode 证券代码
   * @return 这个Code的最新的那个K线数据的时间戳
   */
  @Select("select max(timestamp) from  stock_history_kline where code = #{code}")
  Long findLatestUpdateTimestamp(@Param("code") String securityCode);


  /**
   * 查询当前股票的最近的那一天数据的天的值
   * 比如X股票，最新的K线更新到了20221010 那么 这个函数就返回20221010
   * @param stockCode 股票代码
   * @return 最新更新的那一天
   */
  @Select("SELECT day_int_key from stock_history_kline where code = #{code} order by day_int_key desc limit 1")
  Integer findMaxDayIntOf(String stockCode);

  /**
   * 取出来当前的day_start / day_max 之间的交易日天（去重）
   * @param dayStart 起始日
   * @param dayEnd 结束日
   * @return 去重后的交易日列表
   */
  @Select("SELECT distinct day_int_key from stock_history_kline where day_int_key > #{day_start} and day_int_key <= #{day_end} order by day_int_key desc")
  List<Integer> findDayIntFromStart2Latest(@Param("day_start") long dayStart, @Param("day_end") int dayEnd);

  /**
   * 取当前dayEnd(包含)之前latestN条K线数据
   * @param dayEndInclude dayEnd（包含）
   * @param latestN N条
   * @return K线数据
   */
  @Select("SELECT * from stock_history_kline where day_int_key <= #{dayEnd} order by day_int_key desc limit #{latestN}")
  List<KLine> queryLatestNKLines(@Param("dayEnd") long dayEndInclude, @Param("latestN") int latestN);

}