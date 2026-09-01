package com.marketboxer.core.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 策略标记，标记当前组建负责的策略组别
 *
 * @author qinjie
 * Created At : 2022/10/16 23:27
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Strategy {

    /**
     * 当前这个组建所支持的策略编号
     * @return 策略编号
     */
    String value() default "";
}
