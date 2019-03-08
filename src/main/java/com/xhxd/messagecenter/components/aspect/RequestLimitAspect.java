package com.xhxd.messagecenter.components.aspect;

import com.xhxd.messagecenter.common.exception.BusinessException;
import com.xhxd.messagecenter.components.RedisHandler;
import com.xhxd.messagecenter.components.annotation.RequestLimit;
import com.xhxd.messagecenter.entity.SendMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;
import java.util.concurrent.TimeUnit;

@Component
@Aspect
@Slf4j
public class RequestLimitAspect {

    @Autowired
    private RedisHandler redisHandler;


    @Before("execution(public * com.xhxd.messagecenter.web.controller.*.*(..)) && @annotation(limit)")
    public void requestLimit(JoinPoint joinPoint, RequestLimit limit) {

        String mobileNumber="";
        //获取参数对象
        Object[] args = joinPoint.getArgs();

        for(Object obj : args){
            if(obj instanceof SendMessageDto){
                mobileNumber = ((SendMessageDto)obj).getMobileNumber();
                break;
            }
        }

        String key = "req_mobileNumber_limit_".concat(mobileNumber);

        //加1后看看值
        long count = redisHandler.increment(key,1);

        //刚创建
        if (count == 1) {
            //设置1分钟过期
            redisHandler.expire(key,limit.time(),TimeUnit.DAYS);
        }
        if (count > limit.count()) {
            //log.info("用户IP[" + ip + "]访问地址[" + url + "]超过了限定的次数[" + limit.count() + "]");
            throw new BusinessException(10001,"超出访问次数限制");
        }
    }
}
