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
    public ResponseResult sendMessage(@RequestBody SendMessageDto sendMessageDto){

        try {
            smsService.sendMessage(sendMessageDto);
        }catch (BusinessException e){
            return ResponseResult.fail(e.getCode(), e.getMessage());
        }
        return  ResponseResult.fail(Constants.ExceptonMsg.SECURITY_CODE,Constants.ExceptonMsg.SECURITY_MSG);
    }

    @PostMapping(value="/sendVerificationCode")
    public ResponseResult sendVerificationCode(@RequestBody SendVerificationDto sendVerificationDto){

        try {
            smsService.sendVerificationCode(sendVerificationDto);
        }catch (BusinessException e){
            return ResponseResult.fail(e.getCode(), e.getMessage());
        }
        return  ResponseResult.fail(Constants.ExceptonMsg.SECURITY_CODE,Constants.ExceptonMsg.SECURITY_MSG);
    }

    @PostMapping(value="/checkVerificationCode")
    public ResponseResult checkVerificationCode(@RequestBody VerificationCodeDto verificationCodeDto){
        try {
            smsService.checkVerificationCode(verificationCodeDto);
        }catch (BusinessException e){
            return ResponseResult.fail(e.getCode() , e.getMessage());
        }
        return  ResponseResult.fail(Constants.ExceptonMsg.SECURITY_CODE,Constants.ExceptonMsg.SECURITY_MSG);
    }




    /*@GetMapping(value="/test" )
    public ResponseResult<String> test(){
        try {

            String str = Md5Utils.md5("xhjf_v1!#@messageServer");

            String jsonStr = "";
            Map<String,String> map = new HashMap<>(2);
            map.put("client_key","xhjf-service");
            map.put("client_secret", str);
            httpClientUtils.httpPost("http://127.0.0.1:8080/api/sendMessage",jsonStr,map);

            return ResponseResult.success("");
        }catch (BusinessException e){
            return ResponseResult.fail(e.getCode() , e.getMessage());
        }

    }*/


}
