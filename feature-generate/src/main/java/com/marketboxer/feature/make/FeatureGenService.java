package com.marketboxer.feature.make;

import com.marketboxer.core.model.StockFeature;
import com.marketboxer.data.access.repo.StockFeatureRepo;
import com.marketboxer.feature.make.strategy.s001ma.FeatureStrategyIdEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author qinjie
 */
@Component
@Slf4j
public class FeatureGenService {

    @Autowired
    StockFeatureRepo stockFeatureRepo;

    @Autowired
    FeatureGeneratorFactory featureGeneratorFactory;

    private static Set<String> strategySet = new HashSet<>(10);

    static {
        strategySet.add(FeatureStrategyIdEnum.STRATEGY_ID_001MA.getCode());
    }

    public boolean startGenAllStrategyFeatures() {
        for (String strategy : strategySet) {
            FeatureGenerator<?> featureGenerator = featureGeneratorFactory.getGeneratorByStrategyCode(strategy);
            if ( ObjectUtils.isEmpty(featureGenerator) ) {
                log.error("can't find generators for current strategy!");
                continue;
            }
            List<? extends StockFeature<?>> features = featureGenerator.generate();
            if ( ObjectUtils.isEmpty(features)) {
                log.info("FeatureGen empty!");
                continue;
            }
            int storeCnt = stockFeatureRepo.storeFeature((List<StockFeature<?>>) features);
            if (storeCnt != 0 && storeCnt == features.size()) {
                return true;
            } else {
                log.error("store failed!, store successCnt:{}", storeCnt);
            }
        }
        return false;
    }

    private void startGenFeatureForStrategy(String strategy) {
        //TODO P1
    }
}
