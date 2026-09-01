package com.marketboxer.data.access.repo;

import com.marketboxer.core.model.StockFeature;
import com.marketboxer.data.access.mapper.StockFeatureMapper;
import com.marketboxer.data.access.model.StockFeatureDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author qinjie
 */
@Component
@Slf4j
@Repository
public class StockFeatureRepo {

    @Autowired
    StockFeatureMapper stockFeatureMapper;

    /**
     * 存储当前特征列表
     * @param features
     * @return
     */
    public int storeFeature(List<StockFeature<?>> features) {
        if (ObjectUtils.isEmpty(features)) {
            return 0;
        }
        int successCnt = 0;
        for (StockFeature stockFeatureItem : features) {
            if (stockFeatureItem == null) {
                continue;
            }
            long currentTime = System.currentTimeMillis();
            int count = stockFeatureMapper.insertNewFeature(stockFeatureItem.getCode(), stockFeatureItem.getStrategyCode(),
                    "", stockFeatureItem.getFeatureRawStore() , currentTime, currentTime, stockFeatureItem.getDayIntKey() );
            if (count == 0) {
                log.error("insert failed ! of : {}， {}", stockFeatureItem.getStrategyCode(), stockFeatureItem.getCode());
            } else {
                successCnt ++;
            }
        }
        return successCnt;
    }

    /**
     * 查询最新的一条特征记录
     * @param strategyId 策略编号
     * @param securityCode 股票代码
     * @return
     */
    public StockFeature<String> findLatestFeatureItem(String strategyId, String securityCode) {

        StockFeatureDO stockFeature = stockFeatureMapper.findLatestFeatureItem(strategyId, securityCode);
        if (stockFeature == null) {
            log.error("CAN'T find latest Feature of {},{}", strategyId, securityCode);
            return null;
        }

        StockFeature<String> stockFeatureStr = new StockFeature<>();

        stockFeatureStr.setFeatureRawStore(stockFeature.getFeatureJsonStore());
        stockFeatureStr.setFeatureData(stockFeature.getFeatureJsonStore());
        stockFeatureStr.setCode(stockFeature.getSecurityCode());
        stockFeatureStr.setDayIntKey(stockFeature.getDayIntKey());
        stockFeatureStr.setCreateTime(stockFeature.getCreateTime());
        stockFeatureStr.setUpdateTime(stockFeature.getUpdateTime());
        stockFeatureStr.setStrategyCode(stockFeature.getStrategyId());
        stockFeatureStr.setStrategyVersion(stockFeature.getStrategyVersion());
        stockFeatureStr.setId(stockFeature.getId());

        return stockFeatureStr;
    }
}
