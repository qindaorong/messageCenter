package com.xhxd.messagecenter.components.aspect;

import com.xhxd.messagecenter.common.exception.BusinessException;
import com.xhxd.messagecenter.components.RedisHandler;
import com.xhxd.messagecenter.components.annotation.RequestLimit;
import com.xhxd.messagecenter.entity.SendMessageDto;
import com.xhxd.messagecenter.entity.SendVerificationDto;
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
            if(obj instanceof SendMessageDto ){
                mobileNumber = ((SendMessageDto)obj).getMobileNumber();
                checkDailyRequestCount(mobileNumber,limit.time(),limit.count());
                break;
            }
            if(obj instanceof SendVerificationDto){
                mobileNumber = ((SendVerificationDto)obj).getMobileNumber();
                ckeckVerificationCount(mobileNumber,limit.time(),limit.verificationLimit());
                break;
            }
        }

    }

    /**
     * 手机号码日发送量检查
     * @param mobileNumber
     * @param times
     * @param limit
     */
    private void  checkDailyRequestCount(String mobileNumber,int times,int limit){
        String key = "req_mobileNumber_limit_".concat(mobileNumber);

        //加1后看看值
        long count = redisHandler.increment(key,1);

        //刚创建
        if (count == 1) {
            //设置1天过期
            redisHandler.expire(key,times,TimeUnit.DAYS);
        }
        if (count > limit) {
            throw new BusinessException(10001,"超出访问次数限制");
        }
    }

    /**
     * 验证码单位时间发送量检查
     * @param mobileNumber
     * @param times
     * @param verificationLimit
     */
    private void  ckeckVerificationCount(String mobileNumber,int times,int verificationLimit){
        String key = "verification_mobileNumber_limit_".concat(mobileNumber);
        //加1后看看值
        long count = redisHandler.increment(key,1);

        //刚创建
        if (count == 1) {
            //设置1天过期
            redisHandler.expire(key,times,TimeUnit.MINUTES);
        }
        if (count > verificationLimit) {
            throw new BusinessException(10002,"验证码发送过于频繁，请1分钟后再次发送！");
        }
    }

}
