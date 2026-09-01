package com.marketboxer.feature.make;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author qinjie
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FeatureGen {

    /**
     * strategyId
     * @return
     */
    String strategyId();

    Class<?> targetClass();

}
