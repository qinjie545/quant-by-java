package com.marketboxer.data.access.mapper;

import com.marketboxer.core.model.StockProfile;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author qinjie  at 2022/9/17
 * e
 **/
@Repository
@Mapper
public interface StockProfileMapper {

    @Insert("<script>"
            +"insert into stock_basic_info (id,security_name,security_code,market_code,is_delisting,exch_type,listing_time,lot_size,sec_type )  VALUES "
            +"<foreach collection='list' item='item' index='index' separator=','> "
            + "(#{item.id},#{item.securityName},#{item.securityCode},#{item.marketCode},#{item.isDelisting}, #{item.exchType}, #{item.listingTime}, #{item.lotSize}, #{item.secType})"
            + "</foreach> "
            + "</script>")
    public int insertMany(@Param("list") List<StockProfile> stockProfiles);
}
