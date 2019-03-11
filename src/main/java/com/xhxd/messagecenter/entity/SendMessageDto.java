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
@ApiModel(value = "发送短信Dto")
public class SendMessageDto extends BaseDto {

    @ApiModelProperty(value = "通道id (目前请传值:{51welink} 通道,更多通道请查看 ChannelEnum 枚举类)")
    /**
     * 短信通道 messageChannelEnums
     */
    private String messageChannel;

    /**
     * 消息接收人手机号码
     */
    @ApiModelProperty(value = "消息接收人手机号码")
    private String mobileNumber;

    /**
     * 消息主体
     */
    @ApiModelProperty(value = "要发送消息主体")
    private String messageContent;

}
