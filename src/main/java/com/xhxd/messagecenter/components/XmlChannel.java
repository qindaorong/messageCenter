package com.xhxd.messagecenter.components;

import lombok.Data;

import javax.xml.bind.annotation.*;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "channel")
@Data
public class XmlChannel {

    @XmlAttribute(name = "id")
    private String id;

    @XmlAttribute(name = "open-switch")
    private Boolean openSwitch;

    @XmlAttribute(name = "url")
    private String url;

    @XmlElement(name = "user")
    private XmlUser xmlUser;
}
