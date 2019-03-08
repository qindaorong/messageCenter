package com.xhxd.messagecenter.service;

import com.xhxd.messagecenter.common.enums.ChannelEnum;
import com.xhxd.messagecenter.common.exception.BusinessException;
import com.xhxd.messagecenter.common.exception.ExceptionCode;
import com.xhxd.messagecenter.components.SmsManager;
import com.xhxd.messagecenter.components.annotation.RequestLimit;
import com.xhxd.messagecenter.entity.ChannelDto;
import com.xhxd.messagecenter.entity.SendMessageDto;
import com.xhxd.messagecenter.entity.SendVerificationDto;
import com.xhxd.messagecenter.entity.VerificationCodeDto;
import com.xhxd.messagecenter.service.welinkservice.WelinkServiceServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

    private Map<String,SmsService> serviceMap = new HashMap<>();

    @Autowired
    private SmsManager smsManager;

    @PostConstruct
    public void init(){
        serviceMap.put(ChannelEnum.WELINK.getName(),new WelinkServiceServiceImpl());
    }


    @Override
    @RequestLimit
    public void sendVerificationCode(SendVerificationDto sendVerificationDto) {

        if(Objects.isNull(sendVerificationDto)){
            throw new BusinessException(ExceptionCode.MESSAGE_NOT_NULL);
        }

        //check SMS channels
        this.checkChannel(sendVerificationDto.getMessageChannel());

        //replace word of context
        String messageContent = sendVerificationDto.getMessageContent();
        String verificationCode = sendVerificationDto.getVerificationCode();
        if(!StringUtils.isEmpty(messageContent) && !StringUtils.isEmpty(verificationCode)){
            if(messageContent.indexOf("#code#") != -1){
                messageContent =  messageContent.replace("#code#",verificationCode);
                sendVerificationDto.setMessageContent(messageContent);
            }
        }
        //send message
        serviceMap.get(sendVerificationDto.getMessageChannel()).sendVerificationCode(sendVerificationDto);

        //save verificationCode and mobileNumber into redis

    }

    @Override
    public void checkVerificationCode(VerificationCodeDto verificationCodeDto) {
        if(Objects.isNull(verificationCodeDto)){
            throw new BusinessException(ExceptionCode.MESSAGE_NOT_NULL);
        }

        //check SMS channels
        this.checkChannel(verificationCodeDto.getMessageChannel());

        serviceMap.get(verificationCodeDto.getMessageChannel()).checkVerificationCode(verificationCodeDto);

        //delete verificationCode and mobileNumber from  redis
    }

    @Override
    @RequestLimit
    public void sendMessage(SendMessageDto sendMessageDto) {

        if(Objects.isNull(sendMessageDto)){
            throw new BusinessException(ExceptionCode.MESSAGE_NOT_NULL);
        }
        //check SMS channels
        this.checkChannel(sendMessageDto.getMessageChannel());

        serviceMap.get(sendMessageDto.getMessageChannel()).sendMessage(sendMessageDto);

    }


    private Boolean  checkChannel(String messageChannel) throws BusinessException{
        String code = ChannelEnum.getByName(messageChannel);
        ChannelDto  channelDto = smsManager.loadChannelDtoByChannelId(code);
        if(null != channelDto){
            boolean flag = channelDto.getOpenSwitch();
            if(!flag){
                throw new BusinessException(ExceptionCode.CHANNEL_CLOSURE);
            }
        }else {
            //TODO  通道不存在
        }
        return Boolean.TRUE;
    }
}
