package com.xhxd.messagecenter.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(value = "发送短信验证码Dto")
public class SendVerificationDto extends BaseDto {

    /**
     * 短信通道 messageChannelEnums
     */
    @ApiModelProperty(value = "通道id (目前请传值:{51welink} 通道,更多通道请查看 ChannelEnum 枚举类)")
    private String messageChannel;

    /**
     * 消息接收人手机号码
     */
    @ApiModelProperty(value = "消息接收人手机号码")
    private String mobileNumber;

    /**
     *  消息内容
     */
    @ApiModelProperty(value = "消息内容")
    private String messageContent;

    /**
     * 验证码消息
     */
    @ApiModelProperty(value = "验证码消息")
    private String verificationCode;

}
