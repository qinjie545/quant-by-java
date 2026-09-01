package com.marketboxer.feature.make;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qinjie
 */
@Component
public class FeatureGeneratorFactory implements InitializingBean, ApplicationContextAware {

    private static Map<String, FeatureGenerator<?>> featureGeneratorMap ;

    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (applicationContext != null) {
            if (featureGeneratorMap == null) {
                featureGeneratorMap = new HashMap<>(10);
            }
            Map<String, Object> featureGeneratorBeans = applicationContext.getBeansWithAnnotation(FeatureGen.class);
            for (Object featureGenerator : featureGeneratorBeans.values()) {
                if (featureGenerator instanceof  FeatureGenerator) {
                    String strategyCode = featureGenerator.getClass().getAnnotation(FeatureGen.class).strategyId();
                    Class<?> targetClazz = featureGenerator.getClass().getAnnotation(FeatureGen.class).targetClass();
                    featureGeneratorMap.put(strategyCode, (FeatureGenerator<?>) featureGenerator);
                }
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public FeatureGenerator<?> getGeneratorByStrategyCode(String strategyCode) {
        if (ObjectUtils.isEmpty(featureGeneratorMap)) {
            return null;
        }
        return featureGeneratorMap.get(strategyCode);
    }

}
