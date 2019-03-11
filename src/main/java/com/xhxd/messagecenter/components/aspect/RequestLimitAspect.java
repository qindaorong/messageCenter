package com.xhxd.messagecenter.components.aspect;

import com.xhxd.messagecenter.common.exception.BusinessException;
import com.xhxd.messagecenter.common.exception.ExceptionCode;
import com.xhxd.messagecenter.components.RedisHandler;
import com.xhxd.messagecenter.components.annotation.RequestLimit;
import com.xhxd.messagecenter.entity.SendMessageDto;
import com.xhxd.messagecenter.entity.SendVerificationDto;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Aspect
@Slf4j
public class RequestLimitAspect {

    @Autowired
    private RedisHandler redisHandler;


    @Before("execution(public * com.xhxd.messagecenter.service.*.*(..)) && @annotation(limit)")
    public void requestLimit(JoinPoint joinPoint, RequestLimit limit) {

        String mobileNumber="";
        //获取参数对象
        Object[] args = joinPoint.getArgs();

        for(Object obj : args){
            if(obj instanceof SendMessageDto ){
                SendMessageDto sendMessageDto= ((SendMessageDto)obj);
                mobileNumber = sendMessageDto.getMobileNumber();

                checkDailyRequestCount(sendMessageDto.getMessageChannel(),mobileNumber,limit.time(),limit.count());
                break;
            }
            if(obj instanceof SendVerificationDto){
                SendVerificationDto sendVerificationDto= ((SendVerificationDto)obj);
                mobileNumber = sendVerificationDto.getMobileNumber();

                ckeckVerificationCount(sendVerificationDto.getMessageChannel(),mobileNumber,limit.time(),limit.verificationLimit());
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
    private void  checkDailyRequestCount(String channel,String mobileNumber,int times,int limit){
        String key = channel.concat("_req_mobileNumber_limit_").concat(mobileNumber);

        //加1后看看值
        long count = redisHandler.increment(key,1);

        //刚创建
        if (count == 1) {
            //设置1天过期
            redisHandler.expire(key,times,TimeUnit.DAYS);
        }
        if (count > limit) {
            throw new BusinessException(ExceptionCode.DAILY_CODE_UPPER_LIMIT);
        }
    }

    /**
     * 验证码单位时间发送量检查
     * @param mobileNumber
     * @param times
     * @param verificationLimit
     */
    private void  ckeckVerificationCount(String channel,String mobileNumber,int times,int verificationLimit){
        String key = channel.concat("_verification_mobileNumber_limit_").concat(mobileNumber);
        //加1后看看值
        long count = redisHandler.increment(key,1);

        //刚创建
        if (count == 1) {
            //设置1天过期
            redisHandler.expire(key,times,TimeUnit.MINUTES);
        }
        if (count > verificationLimit) {
            throw new BusinessException(ExceptionCode.ONE_MIN_SERVICE_ERROR);
        }
    }

}
