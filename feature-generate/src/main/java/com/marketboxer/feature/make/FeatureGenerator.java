package com.marketboxer.feature.make;

import com.marketboxer.core.model.StockFeature;

import java.util.List;

/**
 * @author qinjie
 */
public interface FeatureGenerator<T> {

    /**
     * 产生特征
     * @return
     */
    List<StockFeature<T>> generate();

}
