package com.xhxd.messagecenter.service.aggregatedsms;

import com.xhxd.messagecenter.common.enums.ChannelEnum;
import com.xhxd.messagecenter.common.exception.BusinessException;
import com.xhxd.messagecenter.common.exception.ExceptionCode;
import com.xhxd.messagecenter.entity.ChannelDto;
import com.xhxd.messagecenter.entity.SendMessageDto;
import com.xhxd.messagecenter.entity.SendVerificationDto;
import com.xhxd.messagecenter.entity.VerificationCodeDto;
import com.xhxd.messagecenter.service.RedisService;
import com.xhxd.messagecenter.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class SmsServiceImpl implements SmsService {

    @Autowired
    private com.xhxd.messagecenter.components.SmsManager smsManager;

    @Autowired
    private RedisService redisService;

    @Autowired
    private com.xhxd.messagecenter.components.HttpClientUtils httpClientUtils;


    @Override
    public void sendVerificationCode(SendVerificationDto sendVerificationDto) {
        if(null == sendVerificationDto){
            throw new BusinessException(ExceptionCode.MESSAGE_NOT_NULL);
        }
        String messageContent = sendVerificationDto.getMessageContent();
        String verificationCode = sendVerificationDto.getVerificationCode();
        if(!StringUtils.isEmpty(messageContent) && !StringUtils.isEmpty(verificationCode)){
            if(messageContent.indexOf("#code#") != -1){
                messageContent =  messageContent.replace("#code#",verificationCode);
                sendVerificationDto.setMessageContent(messageContent);
            }
        }
        // 判断是那个通道
        String code = ChannelEnum.getByName(sendVerificationDto.getMessageChannel());
        ChannelDto  channelDto = smsManager.loadChannelDtoByChannelId(code);
        if(null != channelDto){
            boolean flag = channelDto.getOpenSwitch();
            if(!flag){
                throw new BusinessException(ExceptionCode.CHANNEL_CLOSURE);
            }
            sendVerificationCode(channelDto,sendVerificationDto);
        }

    }

    @Override
    public void checkVerificationCode(VerificationCodeDto verificationCodeDto) {

    }

    @Override
    public void sendMessage(SendMessageDto sendMessageDto) {

    }

    /**
     * 发送短信验证码
     * @param channelDto
     * @param sendVerificationDto
     * @return
     */
    //public int sendVerificationCode(String url,String userName,String password,String sprdId,String phoneNumber,String content){
    public int sendVerificationCode( ChannelDto channelDto,SendVerificationDto sendVerificationDto){
        Map<String,String> headMap = new HashMap<>();
        String postData = "sname=" + channelDto.getUserName() + "&spwd=" + channelDto.getPassword() + "&scorpid=&sprdid="+channelDto.getSprdId()+"&sdst=" + sendVerificationDto.getMobileNumber() + "&smsg=" + sendVerificationDto.getMessageContent();
        httpClientUtils.httpPostRes(channelDto.getUrl(),postData,headMap);
        return 0;
    }
}
