package com.xhxd.messagecenter.web.controller;

import com.xhxd.messagecenter.common.Constants;
import com.xhxd.messagecenter.common.bean.ResponseResult;
import com.xhxd.messagecenter.common.exception.BusinessException;
import com.xhxd.messagecenter.common.exception.ExceptionCode;
import com.xhxd.messagecenter.common.util.Md5Utils;
import com.xhxd.messagecenter.components.HttpClientUtils;
import com.xhxd.messagecenter.components.RedisHandler;
import com.xhxd.messagecenter.components.annotation.RequestLimit;
import com.xhxd.messagecenter.entity.SendMessageDto;
import com.xhxd.messagecenter.entity.SendVerificationDto;
import com.xhxd.messagecenter.entity.VerificationCodeDto;
import com.xhxd.messagecenter.service.SmsService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
public class MessageCenterController {

    @Autowired
    private SmsService smsService;

    @PostMapping(value="/sendMessage")
    @ApiOperation(value="发送短信信息",httpMethod = "POST", notes = "SendMessageDto  请求实体 具体参数以文档说明为参考")
    public ResponseResult sendMessage(@RequestBody SendMessageDto sendMessageDto){
        try {
            smsService.sendMessage(sendMessageDto);
            return ResponseResult.success(null);
        }catch (BusinessException e){
            return ResponseResult.fail(e.getCode(), e.getMessage());
        }
    }

    @PostMapping(value="/sendVerificationCode")
    @ApiOperation(value="验证短信验证码",httpMethod = "POST", notes = "SendVerificationDto -- 请求实体 具体参数以文档说明为参考")
    public Object sendVerificationCode(@RequestBody SendVerificationDto sendVerificationDto){

        try {
            smsService.sendVerificationCode(sendVerificationDto);
            return ResponseResult.success(null);
        }catch (BusinessException e){
            return ResponseResult.fail(e.getCode(), e.getMessage());
        }
    }


    @PostMapping(value="/checkVerificationCode")
    @ApiOperation(value="发送短信验证码",httpMethod = "POST", notes = "VerificationCodeDto  请求实体 具体参数以文档说明为参考")
    public Object checkVerificationCode(@RequestBody VerificationCodeDto verificationCodeDto){
        try {
            smsService.checkVerificationCode(verificationCodeDto);
            return ResponseResult.success(null);
        }catch (BusinessException e){
            return ResponseResult.fail(e.getCode() , e.getMessage());
        }
    }


}
