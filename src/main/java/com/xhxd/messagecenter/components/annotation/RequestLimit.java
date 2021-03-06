package com.xhxd.messagecenter.components.annotation;

import com.xhxd.messagecenter.common.enums.ChannelEnum;
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


    /**
     * 验证码单位时间发送次数
     * @return
     */
    int verificationLimit() default 1;
}
