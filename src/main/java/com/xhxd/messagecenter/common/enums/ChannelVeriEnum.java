package com.xhxd.messagecenter.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 是否远程验证枚举类
 */
@AllArgsConstructor
@Getter
public enum ChannelVeriEnum {

    LOCAL("local"),
    REMOTE("remote");

    private String name;
}
