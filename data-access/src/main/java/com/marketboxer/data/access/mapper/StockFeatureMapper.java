package com.marketboxer.data.access.mapper;

import com.marketboxer.core.model.StockFeature;
import com.marketboxer.data.access.model.StockFeatureDO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface StockFeatureMapper {

    @Insert("insert into stock_feature_store(security_code, strategy_id, strategy_version, feature_json_store, create_time," +
            " update_time, day_int_key) values(#{security_code}, #{strategy_id}, #{strategy_version}, #{feature_json_store}, #{create_time}, #{update_time}, #{day_int_key})")
    int insertNewFeature(@Param("security_code") String securityCode, @Param("strategy_id") String strategyId,
                            @Param("strategy_version") String strategyVer,@Param("feature_json_store") String featureJsonStore,
                         @Param("create_time") long createTime, @Param("update_time") long updateTime, @Param("day_int_key") long dayIntKey);


    @Select("select * from stock_feature_store where strategy_id=#{strategy_id} and security_code=#{stock_code} order by day_int_key desc limit 1")
    @ResultType(StockFeatureDO.class)
    StockFeatureDO findLatestFeatureItem(@Param("strategy_id") String strategyId, @Param("stock_code") String stockCode);

}
