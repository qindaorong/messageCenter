package com.hariexpress.framework.task.components;

import com.xhxd.messagecenter.components.XmlChannelList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;


@Slf4j
public class XmlBuilder {

    final static String TASK_URL = "/sms-channel.xml";

    public static XmlChannelList loadTasks() throws Exception {
        XmlChannelList xmlTaskList = new XmlChannelList();

        JAXBContext jaxbContext = JAXBContext.newInstance(XmlChannelList.class);
        Unmarshaller um = jaxbContext.createUnmarshaller();
        ClassPathResource cpr = new ClassPathResource(TASK_URL);
        xmlTaskList = (XmlChannelList) um.unmarshal(cpr.getInputStream());
        return xmlTaskList;
    }

}
