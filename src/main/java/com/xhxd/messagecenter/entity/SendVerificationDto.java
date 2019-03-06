package com.xhxd.messagecenter.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendVerificationDto extends BaseDto {

    /**
     * 短信通道 messageChannelEnums
     */
    private String messageChannel;

    /**
     * 消息接收人手机号码
     */
    private String mobileNumber;

    /**
     * 验证码消息
     */
    private String verificationCode;

}
