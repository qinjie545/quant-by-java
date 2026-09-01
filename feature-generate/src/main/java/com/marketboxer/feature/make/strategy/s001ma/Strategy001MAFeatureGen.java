package com.marketboxer.feature.make.strategy.s001ma;

import com.alibaba.fastjson.JSONObject;
import com.marketboxer.core.model.KLine;
import com.marketboxer.core.model.StockFeature;
import com.marketboxer.core.model.annotation.Strategy;
import com.marketboxer.data.access.repo.StockFeatureRepo;
import com.marketboxer.data.access.repo.StockKLineRepo;
import com.marketboxer.feature.make.FeatureGen;
import com.marketboxer.feature.make.FeatureGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qinjie
 */
@FeatureGen(strategyId = "Strategy001_MA", targetClass = Feature001MA.class)
@Component
@Strategy(value = "Strategy001_MA")
@Slf4j
public class Strategy001MAFeatureGen implements FeatureGenerator<Feature001MA> {

    @Autowired
    StockFeatureRepo stockFeatureRepo;

    @Autowired
    StockKLineRepo stockKLineRepo;

    @Override
    public List<StockFeature<Feature001MA>> generate() {

        String securityCode = "601788";
        int MAX_DAY_USED = 20;
        String strategyId = getClass().getAnnotation(Strategy.class).value();

        List<StockFeature<Feature001MA>> features = new ArrayList<>(10);

        /**
         * 1.取出来当前特征的最新的一条的日期
         * 2.根据这个日期进行增量更新计算特征
         * 3.返回有新增的变化
         */
        StockFeature<String> latestFeature001MA = stockFeatureRepo.findLatestFeatureItem(strategyId, securityCode);
        long dayUpdated = 0;
        if (latestFeature001MA != null) {
            dayUpdated = latestFeature001MA.getDayIntKey();
        }

        int dayMax = stockKLineRepo.findMaxDayInt(securityCode);

        List<Integer> dayIntNotGenList = stockKLineRepo.findDayIntFromStart2Latest(dayUpdated, dayMax);

        if (ObjectUtils.isEmpty(dayIntNotGenList)) {
            log.info("No GAP day between {} and {}", dayUpdated, dayMax);
            return null;
        }

        for (Integer dayIntNotGen : dayIntNotGenList) {
            if (dayIntNotGen == null) {
                continue;
            }

            List<KLine> kLines = stockKLineRepo.queryLatestNKLines(dayIntNotGen, MAX_DAY_USED);

            Double ma5 = null,ma10 = null,ma15 = null,ma20 = null;
            int index = 0;
            double sum = 0;
            for (KLine kLine : kLines) {
                sum += kLine.getClose();
                if (index == 4) {
                    ma5 = sum / 5;
                } else if (index == 9) {
                    ma10 = sum / 10 ;
                } else if (index == 14) {
                    ma15 = sum /15;
                } else if (index == 19) {
                    ma20 = sum /20;
                }
                index ++;
            }

            Feature001MA feature001MA = new Feature001MA();
            feature001MA.setMa5(ma5);
            feature001MA.setMa10(ma10);
            feature001MA.setMa15(ma15);
            feature001MA.setMa20(ma20);
            StockFeature<Feature001MA> stockFeature = new StockFeature<>();
            stockFeature.setCode(securityCode);
            stockFeature.setStrategyCode(strategyId);
            stockFeature.setCreateTime(System.currentTimeMillis());
            stockFeature.setDayIntKey(dayIntNotGen);
            stockFeature.setFeatureRawStore(JSONObject.toJSONString(feature001MA));
            features.add(stockFeature);
        }

        return features;
    }

}
