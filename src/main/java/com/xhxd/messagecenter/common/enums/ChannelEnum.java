package com.xhxd.messagecenter.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 聚合短信枚举
 */
@AllArgsConstructor
@Getter
public enum ChannelEnum {

    WELINK("1","51welink");


    /**
     * 获取枚举的key
     * @param key
     * @return
     */
    public static String getKey(String key) {
        ChannelEnum[] businessModeEnums = values();
        String str  = null;
        for (ChannelEnum businessModeEnum : businessModeEnums) {
            if (businessModeEnum.getCode().equals(key)) {
                str =  businessModeEnum.getCode();
            }
        }
        return str;
    }

    /**
     * 根据name 获取枚举的key
     * @param name
     * @return
     */
    public static String getByName(String name) {
        ChannelEnum[] businessModeEnums = values();
        String str  = null;
        for (ChannelEnum businessModeEnum : businessModeEnums) {
            if (businessModeEnum.getName().equals(name)) {
                str =  businessModeEnum.getCode();
            }
        }
        return str;
    }


    public static ChannelEnum getCodeEnumByKey(String key) {
        ChannelEnum[] businessModeEnums = values();
        for (ChannelEnum businessModeEnum : businessModeEnums) {
            if (businessModeEnum.getCode().equals(key)) {
                return businessModeEnum;
            }
        }
        return null;
    }

    private String code;

    private String name;
}
