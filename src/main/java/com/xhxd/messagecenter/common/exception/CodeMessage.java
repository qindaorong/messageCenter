/**
 * @Description
 * @Author lindaoliang
 * @Create 2018/3/19
 * Copyright: Copyright (c) 2018
 * Company:北京思源政务通科技有限公司
 **/
package com.xhxd.messagecenter.common.exception;


import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.Map;

public class CodeMessage {
    private Integer code;
    private String message;

    public CodeMessage() {
    }

    public CodeMessage(Integer code, String message) {
        this.setCode(code);
        this.setMessage(message);
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage(String paramValue) {
        return StringUtils.replacePattern(this.message, "\\$\\{.*\\}", paramValue);
    }

    public String getMessage(Map<String, String> paramValues) {
        String msg = this.message;

        Map.Entry entry;
        for(Iterator var4 = paramValues.entrySet().iterator(); var4.hasNext(); msg = StringUtils.replacePattern(msg, "\\$\\{" + (String)entry.getKey() + "\\}", (String)entry.getValue())) {
            entry = (Map.Entry)var4.next();
        }

        return msg;
    }

    @Override
    public String toString() {
        return "code = " + this.code + "，message = " + this.message;
    }
}
