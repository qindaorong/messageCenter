package com.xhxd.messagecenter.components;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;


@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class XmlUser {

    @XmlAttribute(name = "name")
    private String name;

    @XmlAttribute(name = "password")
    private String password;

}
