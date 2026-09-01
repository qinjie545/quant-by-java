package com.marketboxer.startegy.compute;

import com.marketboxer.startegy.compute.annotation.StrategyComputor;
import com.marketboxer.startegy.compute.computor.StrategyCompute;
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
 * Time: 2022-10-16 23:20
 */
@Component
public class StrategyComputorFactory implements InitializingBean, ApplicationContextAware {

    private Map<String, StrategyCompute> computeMap;
    private ApplicationContext applicationContext;

    public StrategyCompute getComputorOf(String strategyId) {
        if (ObjectUtils.isEmpty(computeMap)) {
            return null;
        }
        return computeMap.get(strategyId);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (applicationContext != null) {
            if (computeMap == null) {
                computeMap = new HashMap<>(10);
            }
            Map<String, Object> strategyComputorBeans = applicationContext.getBeansWithAnnotation(StrategyComputor.class);
            for (Object strategyCompute : strategyComputorBeans.values()) {
                if (strategyCompute instanceof  StrategyCompute) {
                    String id = ((StrategyCompute) strategyCompute).getStrategyId();
                    computeMap.put(id, (StrategyCompute) strategyCompute);
                }
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
