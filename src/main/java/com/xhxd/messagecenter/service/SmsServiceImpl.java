package com.xhxd.messagecenter.service;

import com.xhxd.messagecenter.common.enums.ChannelEnum;
import com.xhxd.messagecenter.common.enums.ChannelVeriEnum;
import com.xhxd.messagecenter.common.exception.BusinessException;
import com.xhxd.messagecenter.common.exception.ExceptionCode;
import com.xhxd.messagecenter.components.RedisHandler;
import com.xhxd.messagecenter.components.SmsManager;
import com.xhxd.messagecenter.components.annotation.RequestLimit;
import com.xhxd.messagecenter.entity.ChannelDto;
import com.xhxd.messagecenter.entity.SendMessageDto;
import com.xhxd.messagecenter.entity.SendVerificationDto;
import com.xhxd.messagecenter.entity.VerificationCodeDto;
import com.xhxd.messagecenter.service.mobileService.MobileServiceImpl;
import com.xhxd.messagecenter.service.welinkservice.WeLinkServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

    private Map<String,SmsService> serviceMap = new HashMap<>();

    @Autowired
    private RedisHandler redisHandler;


    @Autowired
    private SmsManager smsManager;

    @PostConstruct
    public void init(){
        serviceMap.put(ChannelEnum.WELINK.getName(), WeLinkServiceImpl.getInstance());
        serviceMap.put(ChannelEnum.MOBILE.getName(), MobileServiceImpl.getInstance());
    }


    @Override
    @RequestLimit
    public Boolean sendVerificationCode(SendVerificationDto sendVerificationDto) {

        if(Objects.isNull(sendVerificationDto)){
            throw new BusinessException(ExceptionCode.MESSAGE_NOT_NULL);
        }

        // check sms channels messageContent sign
        this.checkChannel(sendVerificationDto.getMessageChannel(),sendVerificationDto.getMessageContent());

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
        Boolean flag = serviceMap.get(sendVerificationDto.getMessageChannel()).sendVerificationCode(sendVerificationDto);
        if(flag){
            //save verificationCode and mobileNumber into redis
            redisHandler.setForTimeMIN(sendVerificationDto.getMobileNumber(),sendVerificationDto.getVerificationCode(),10);
        }else {
            throw new BusinessException(ExceptionCode.MESSAGE_SERVICE_ERROR);
        }
        return  Boolean.TRUE;
    }

    @Override
    public void checkVerificationCode(VerificationCodeDto verificationCodeDto) {
        if(Objects.isNull(verificationCodeDto)){
            throw new BusinessException(ExceptionCode.MESSAGE_NOT_NULL);
        }

        //check SMS channels
        String code = ChannelEnum.getByName(verificationCodeDto.getMessageChannel());
        ChannelDto channelDto = smsManager.loadChannelDtoByChannelId(code);

        // local check code
        if(ChannelVeriEnum.LOCAL.getName().equalsIgnoreCase(channelDto.getVerificationCodeFrom())){
            String  codeValue = redisHandler.get(verificationCodeDto.getMobileNumber());
            if(StringUtils.isEmpty(codeValue)){
                throw new BusinessException(ExceptionCode.CODE_EXPIRATION);
            }
            if(!codeValue.equalsIgnoreCase(verificationCodeDto.getVerificationCode())){
                throw new BusinessException(ExceptionCode.CODE_ERROR);
            }
            //delete verificationCode and mobileNumber from  redis
            redisHandler.remove(verificationCodeDto.getMobileNumber());
            log.info("[SmsServiceImpl][checkVerificationCode] check verificationCode success (code={},mobileNumber={}),",codeValue,verificationCodeDto.getMobileNumber());
        }else{
            serviceMap.get(verificationCodeDto.getMessageChannel()).checkVerificationCode(verificationCodeDto);
        }


    }

    @Override
    @RequestLimit
    public void sendMessage(SendMessageDto sendMessageDto) {

        if(Objects.isNull(sendMessageDto)){
            throw new BusinessException(ExceptionCode.MESSAGE_NOT_NULL);
        }

        // check sms channels messageContent sign
        String code = ChannelEnum.getByName(sendMessageDto.getMessageChannel());
        this.checkChannel(code,sendMessageDto.getMessageContent());

        serviceMap.get(sendMessageDto.getMessageChannel()).sendMessage(sendMessageDto);



    }

    private Boolean checkChannel(String messageChannel, String messageContent){
        String code = ChannelEnum.getByName(messageChannel);
        ChannelDto  channelDto = smsManager.loadChannelDtoByChannelId(code);
        if(null != channelDto){
            if(!CollectionUtils.isEmpty(channelDto.getKeyWordsList())){
                Boolean flag = this.ifInclude(channelDto.getKeyWordsList(),messageContent);
                if(!flag){
                    throw  new BusinessException(ExceptionCode.MESSAGE_NOT_NULL);
                }
            }
        }else {
            throw new BusinessException(ExceptionCode.CHANNEL_EXISTENT);
        }
        return Boolean.TRUE;
    }


    /**
     *  验证list 是否包含字符串
     * @param list
     * @param str
     * @return
     */
    public  boolean ifInclude(List<String> list,String str){
        for(int i=0; i<list.size(); i++){
            if(str.indexOf(list.get(i)) != -1){
                return true;
            }
        }
        return false;
    }
}
