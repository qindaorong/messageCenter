package com.xhxd.messagecenter.components.annotation;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface RequestLimit {

    /**
     * 1天允许访问的次数
     */
    int count() default 100;

    /**
     * 时间段，多少时间段内运行访问count次
     */
    int time() default 1;
}
