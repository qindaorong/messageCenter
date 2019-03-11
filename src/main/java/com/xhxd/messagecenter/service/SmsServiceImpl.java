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
import com.xhxd.messagecenter.service.welinkservice.MoblieServiceImpl;
import com.xhxd.messagecenter.service.welinkservice.WelinkServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        serviceMap.put(ChannelEnum.WELINK.getName(), WelinkServiceImpl.getInstanceWelinkService());
        serviceMap.put(ChannelEnum.MOBILE.getName(), MoblieServiceImpl.getInstanceMoblieService());
    }


    @Override
   // @RequestLimit
    public Boolean sendVerificationCode(SendVerificationDto sendVerificationDto) {

        if(Objects.isNull(sendVerificationDto)){
            throw new BusinessException(ExceptionCode.MESSAGE_NOT_NULL);
        }

        //check SMS channels
        this.checkChannel(sendVerificationDto.getMessageChannel());

        // check sms messageContent sign
        this.checkChannelSign(sendVerificationDto.getMessageChannel(),sendVerificationDto.getMessageContent());

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
        this.checkChannel(verificationCodeDto.getMessageChannel());
        ChannelDto channelDto = smsManager.loadChannelDtoByChannelId(verificationCodeDto.getMessageChannel());

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
            throw new BusinessException(ExceptionCode.CHANNEL_EXISTENT);
        }
        return Boolean.TRUE;
    }


    private Boolean  checkChannelSign(String messageChannel,String messageContent){
        Boolean flag = false;
        String code = ChannelEnum.getByName(messageChannel);
        ChannelDto  channelDto = smsManager.loadChannelDtoByChannelId(code);
        if(null != channelDto){
           String keyWord  = channelDto.getKeyWord();
            if(!StringUtils.isEmpty(keyWord)){
                if(keyWord.indexOf(",") != -1){
                    String[] keyWords = keyWord.split(",");
                    List<String>  wordList  = Arrays.asList(keyWords);
                    flag = this.ifInclude(wordList,keyWord);
                    if(!flag){
                        throw  new BusinessException(ExceptionCode.MESSAGE_NOT_NULL);
                    }
                }
            }
        }else {
            throw new BusinessException(ExceptionCode.CHANNEL_EXISTENT);
        }
        return Boolean.TRUE;
    }


    public  boolean ifInclude(List<String> list,String str){
        for(int i=0; i<list.size(); i++){
            if (list.get(i).indexOf(str) != -1) {
                return true;
            }
        }
        return false;
    }
}
