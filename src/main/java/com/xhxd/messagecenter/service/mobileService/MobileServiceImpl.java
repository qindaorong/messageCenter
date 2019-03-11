package com.xhxd.messagecenter.service.mobileService;

import com.xhxd.messagecenter.common.enums.ChannelEnum;
import com.xhxd.messagecenter.common.exception.BusinessException;
import com.xhxd.messagecenter.common.exception.CodeMessage;
import com.xhxd.messagecenter.common.exception.ExceptionCode;
import com.xhxd.messagecenter.common.util.Md5Utils;
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
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class MobileServiceImpl implements SmsService {

    private static MobileServiceImpl moblieService = null;

    private SmsManager smsManager;

    private HttpClientUtils httpClientUtils;

    private MobileServiceImpl() {
        if(Objects.isNull(moblieService)){
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
        Boolean flag = sendMessagParam(channelDto,sendVerificationDto.getMessageContent(),sendVerificationDto.getMobileNumber());
        if(flag){
            return Boolean.TRUE;
        }else {
            return  Boolean.FALSE;
        }
    }

    @Override
    public void checkVerificationCode(VerificationCodeDto verificationCodeDto) {

    }

    @Override
    public void sendMessage(SendMessageDto sendMessageDto) {
        ChannelDto channelDto = smsManager.loadChannelDtoByChannelId(ChannelEnum.getByName(sendMessageDto.getMessageChannel()));
        sendMessagParam(channelDto,sendMessageDto.getMessageContent(),sendMessageDto.getMobileNumber());
    }

    public Boolean sendMessagParam(ChannelDto channelDto,String messageContent,String mobileNumber){
        Map<String,String> headMap = new HashMap<>();

        Map<String,String> formMap = new HashMap<>(8);
        formMap.put("corpId",channelDto.getSprdId());
        formMap.put("loginName", channelDto.getUserName());
        formMap.put("pwd", Md5Utils.md5(channelDto.getPassword()));
        formMap.put("ext", StringUtils.EMPTY);
        formMap.put("content",messageContent);
        formMap.put("mobileList",mobileNumber);
        formMap.put("sendTime",StringUtils.EMPTY);
        formMap.put("userId",StringUtils.EMPTY);

        Response response = httpClientUtils.httpFormPostResponse(channelDto.getUrl(),headMap,formMap);
        String resultXml;
        String state;
        if(response.isSuccessful()){
            try {
                resultXml = response.body().string();
                log.info("短信接口返回信息 ----> " + resultXml);
                Map<String, Object> resultMap = XmlUtil.xmlToMap(resultXml);
                state = String.valueOf(resultMap.get("code"));
                if(!StringUtils.equals("0000",state)){
                    CodeMessage codeMessage = new CodeMessage();
                    codeMessage.setCode(6010);
                    codeMessage.setMessage(String.valueOf(resultMap.get("code")));
                    throw  new BusinessException(codeMessage);
                }else{
                    return  Boolean.TRUE;
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw  new BusinessException(ExceptionCode.SMS_SERVICE_ERROR);
            }finally {
                response.close();
            }
        }
        return  Boolean.FALSE;

    }


    public static MobileServiceImpl getInstanceMoblieService(){
        if(Objects.isNull(moblieService)){
            moblieService =  new MobileServiceImpl();
        }
        return moblieService;
    }
}
