package com.xhxd.messagecenter.web.controller;

import com.xhxd.messagecenter.common.bean.ResponseResult;
import com.xhxd.messagecenter.common.exception.BusinessException;
import com.xhxd.messagecenter.entity.SendMessageDto;
import com.xhxd.messagecenter.entity.SendVerificationDto;
import com.xhxd.messagecenter.entity.VerificationCodeDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
public class MessageCenterController {



    @PostMapping(value="/sendMessage")
    public ResponseResult<String> sendMessage(@RequestBody SendMessageDto sendMessageDto){

        try {
            return ResponseResult.success("");
        }catch (BusinessException e){
            return ResponseResult.fail(e.getCode(), e.getMessage());
        }
    }

    @PostMapping(value="/sendVerificationCode")
    public ResponseResult<String> sendVerificationCode(@RequestBody SendVerificationDto sendVerificationDto){

        try {

            return ResponseResult.success("success");
        }catch (BusinessException e){
            return ResponseResult.fail(e.getCode(), e.getMessage());
        }
    }

    @PostMapping(value="/checkVerificationCode")
    public ResponseResult<String> checkVerificationCode(@RequestBody VerificationCodeDto verificationCodeDto){
        try {
            return ResponseResult.success("");
        }catch (BusinessException e){
            return ResponseResult.fail(e.getCode() , e.getMessage());
        }

    }


    @GetMapping(value="/test" )
    public ResponseResult<String> test(){
        try {
            return ResponseResult.success("");
        }catch (BusinessException e){
            return ResponseResult.fail(e.getCode() , e.getMessage());
        }

    }


}
