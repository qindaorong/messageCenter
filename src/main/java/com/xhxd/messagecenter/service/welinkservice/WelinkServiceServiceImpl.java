package com.xhxd.messagecenter.service.welinkservice;

import com.xhxd.messagecenter.common.enums.ChannelEnum;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class WelinkServiceServiceImpl implements SmsService {


    private com.xhxd.messagecenter.components.SmsManager smsManager;

    private com.xhxd.messagecenter.components.HttpClientUtils httpClientUtils;


    public WelinkServiceServiceImpl() {
        if(Objects.isNull(smsManager)){
            smsManager = SpringApplicationContext.getBean(SmsManager.class);
        }
        if(Objects.isNull(httpClientUtils)){
            httpClientUtils = SpringApplicationContext.getBean(HttpClientUtils.class);
        }
    }

    @Override
    public void sendVerificationCode(SendVerificationDto sendVerificationDto) {
        ChannelDto channelDto = smsManager.loadChannelDtoByChannelId(ChannelEnum.getByName(sendVerificationDto.getMessageChannel()));
        Map<String,String> headMap = new HashMap<>();
        Map<String,String> formMap = new HashMap<>();
        formMap.put("sname",channelDto.getUserName());
        formMap.put("spwd", channelDto.getPassword());
        formMap.put("scorpid","");
        formMap.put("sprdid",channelDto.getSprdId());
        formMap.put("sdst",sendVerificationDto.getMobileNumber());
        formMap.put("smsg",sendVerificationDto.getMessageContent());
        Response response = httpClientUtils.httpFormPostResponse(channelDto.getUrl(),headMap,formMap);
        String resultXml = "";
        Integer state = 0;
        if(response.isSuccessful()){
            try {
                resultXml = response.body().string();
                log.info("短信接口返回信息 ----> " + resultXml);
                Map<String, Object> resultMap = XmlUtil.xmlToMap(resultXml);
                state = Integer.valueOf((Integer) resultMap.get("State"));
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                response.close();
            }
        }
    }

    @Override
    public void checkVerificationCode(VerificationCodeDto verificationCodeDto) {

    }

    @Override
    public void sendMessage(SendMessageDto sendMessageDto) {
    }

}
