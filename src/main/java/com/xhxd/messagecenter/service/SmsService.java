package com.xhxd.messagecenter.service;

import com.xhxd.messagecenter.entity.SendMessageDto;
import com.xhxd.messagecenter.entity.SendVerificationDto;
import com.xhxd.messagecenter.entity.VerificationCodeDto;

public interface SmsService {
    /**
     * 发送短信验证码
     * @param sendVerificationDto
     */
    Boolean  sendVerificationCode(SendVerificationDto sendVerificationDto);

    /**
     * 验证验证码信息
     * @param verificationCodeDto
     */
    void  checkVerificationCode(VerificationCodeDto verificationCodeDto);


    /**
     * 发送短信信息
     * @param sendMessageDto
     */
    void  sendMessage(SendMessageDto sendMessageDto);
}
