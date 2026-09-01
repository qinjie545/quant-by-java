package com.marketboxer.startegy.compute.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 策略计算模块的注解标识
 * @author qinjie
 * Created At : 2022/10/16 23:45
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface StrategyComputor {

}
