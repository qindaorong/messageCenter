package com.xhxd.messagecenter.service.welinkservice;

import com.xhxd.messagecenter.common.enums.ChannelEnum;
import com.xhxd.messagecenter.common.exception.BusinessException;
import com.xhxd.messagecenter.common.exception.CodeMessage;
import com.xhxd.messagecenter.common.exception.ExceptionCode;
import com.xhxd.messagecenter.common.util.XmlUtil;
import com.xhxd.messagecenter.components.HttpClientUtils;
import com.xhxd.messagecenter.components.SmsManager;
import com.xhxd.messagecenter.components.SpringApplicationContext;
import com.xhxd.messagecenter.entity.ChannelDto;
import com.xhxd.messagecenter.entity.SendMessageDto;
import com.xhxd.messagecenter.entity.SendVerificationDto;
import com.xhxd.messagecenter.entity.VerificationCodeDto;
import com.xhxd.messagecenter.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class WelinkServiceImpl implements SmsService {

    private static WelinkServiceImpl welinkService = null;

    private SmsManager smsManager;

    private HttpClientUtils httpClientUtils;

    private WelinkServiceImpl() {
        if(Objects.isNull(welinkService)){
            if(Objects.isNull(smsManager)){
                smsManager = SpringApplicationContext.getBean(SmsManager.class);
            }
            if(Objects.isNull(httpClientUtils)){
                httpClientUtils = SpringApplicationContext.getBean(HttpClientUtils.class);
            }
        }
    }

    @Override
    public  Boolean sendVerificationCode(SendVerificationDto sendVerificationDto) {
        ChannelDto channelDto = smsManager.loadChannelDtoByChannelId(ChannelEnum.getByName(sendVerificationDto.getMessageChannel()));
        Boolean flag = sendMessageParam(channelDto,sendVerificationDto.getMobileNumber(),sendVerificationDto.getMessageContent());
        if(flag){
            return  Boolean.TRUE;
        }else{
            return  Boolean.FALSE;
        }
    }

    @Override
    public void checkVerificationCode(VerificationCodeDto verificationCodeDto) {

    }

    @Override
    public void sendMessage(SendMessageDto sendMessageDto) {
        ChannelDto channelDto = smsManager.loadChannelDtoByChannelId(ChannelEnum.getByName(sendMessageDto.getMessageChannel()));
        sendMessageParam(channelDto,sendMessageDto.getMobileNumber(),sendMessageDto.getMessageContent());

    }

    public Boolean  sendMessageParam(ChannelDto channelDto,String mobileNumber,String messageContent){
        Map<String,String> headMap = new HashMap<>();

        Map<String,String> formMap = new HashMap<>(6);

        formMap.put("sname",channelDto.getUserName());
        formMap.put("spwd", channelDto.getPassword());
        formMap.put("scorpid","");
        formMap.put("sprdid",channelDto.getSprdId());
        formMap.put("sdst",mobileNumber);
        formMap.put("smsg",messageContent);
        Response response = httpClientUtils.httpFormPostResponse(channelDto.getUrl(),headMap,formMap);
        String resultXml = "";
        Integer state = 0;
        if(response.isSuccessful()){
            try {
                resultXml = response.body().string();
                log.info("短信接口返回信息 ----> " + resultXml);
                Map<String, Object> resultMap = XmlUtil.xmlToMap(resultXml);
                state = Integer.valueOf(String.valueOf(resultMap.get("State")));
                if(state != 0){
                    CodeMessage codeMessage = new CodeMessage();
                    codeMessage.setCode(6010);
                    codeMessage.setMessage(String.valueOf(resultMap.get("State")));
                    throw  new BusinessException(codeMessage);
                }else{
                    return Boolean.TRUE;
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw  new BusinessException(ExceptionCode.SMS_SERVICE_ERROR);
            }finally {
                response.close();
            }
        }
        return Boolean.FALSE;
    }

    public static WelinkServiceImpl getInstanceWelinkService(){
        if(Objects.isNull(welinkService)){
            welinkService =  new WelinkServiceImpl();
        }
        return welinkService;
    }
}
