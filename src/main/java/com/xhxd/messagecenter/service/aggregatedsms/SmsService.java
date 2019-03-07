package com.xhxd.messagecenter.service.aggregatedsms;

import com.xhxd.messagecenter.entity.SendMessageDto;
import com.xhxd.messagecenter.entity.SendVerificationDto;
import com.xhxd.messagecenter.entity.VerificationCodeDto;

public interface SmsService {

    /**
     * 发送短信验证码
     * @param sendVerificationDto
     */
    void  sendVerificationCode(SendVerificationDto sendVerificationDto);

    /**
     * 发送短信验证码
     * @param verificationCodeDto
     */
    void  checkVerificationCode(VerificationCodeDto verificationCodeDto);


    /**
     * 发送短信验证码
     * @param sendMessageDto
     */
    void  sendMessage(SendMessageDto sendMessageDto);


}
