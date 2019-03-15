package com.xhxd.messagecenter.components;


import com.xhxd.messagecenter.common.exception.BusinessException;
import com.xhxd.messagecenter.common.exception.ExceptionCode;
import com.xhxd.messagecenter.entity.ChannelDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class SmsManager {
    private static XmlChannelList xmlChannelList ;
    private Map<String,ChannelDto> channelDtoMap = new ConcurrentHashMap<>();

    @PostConstruct
    private void init() {
        log.info("[sms-channel.xml] load start......");
        loadChannelXml();
        log.info("[sms-channel.xml] load over .....");
    }


    private void loadChannelXml(){
        try {
            xmlChannelList = com.hariexpress.framework.task.components.XmlBuilder.loadTasks();
            if(!CollectionUtils.isEmpty(xmlChannelList.getSmsChannelList())){

                ChannelDto channelDto;
                for(XmlChannel xmlChannel :xmlChannelList.getSmsChannelList()){
                    channelDto = new ChannelDto();
                    channelDto.convert2ChannelDto(xmlChannel);
                    channelDtoMap.put(channelDto.getId(),channelDto);
                }
            }

            log.info("[channelDtoMap] size is {}",channelDtoMap.size());

        } catch (Exception e) {
            log.error("load ChannelList is failed , Exception Message is {}", e.getMessage());
        }
    }


    /**
     * loadChannelDtoByChannelId
     * @param id
     * @return
     * @throws BusinessException
     */
    public ChannelDto loadChannelDtoByChannelId(String id) throws BusinessException {
        if(!channelDtoMap.containsKey(id)){
            throw new BusinessException(ExceptionCode.CHANNEL_EXISTENT);
        }
        if(!channelDtoMap.get(id).getOpenSwitch()){
            throw new BusinessException(ExceptionCode.CHANNEL_CLOSURE);
        }

        return channelDtoMap.get(id);
    }

}
