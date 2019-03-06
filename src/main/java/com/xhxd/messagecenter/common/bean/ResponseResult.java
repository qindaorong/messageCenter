/**
 * @Description
 * @Author lindaoliang
 * @Create 2018/3/19
 * Copyright: Copyright (c) 2018
 * Company:北京思源政务通科技有限公司
 **/
package com.xhxd.messagecenter.common.bean;

import com.alibaba.fastjson.JSONObject;
import com.xhxd.messagecenter.common.exception.BusinessException;
import com.xhxd.messagecenter.common.exception.CodeMessage;

import java.io.Serializable;

public class ResponseResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private CodeMessage meta;
    private T data;

    public ResponseResult(T data) {
        this(CoreExceptionCodes.SUCCESS.getCode(), CoreExceptionCodes.SUCCESS.getMessage(), data);
    }

    public ResponseResult() {
        this.meta = new CodeMessage();
    }

    public void setMeta(CodeMessage meta) {
        this.meta = meta;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ResponseResult(Integer code, String message) {
        this(code, message, (T) null);
    }

    public ResponseResult(Integer code, String message, T data) {
        this.meta = new CodeMessage();
        this.meta.setCode(code);
        this.meta.setMessage(message);
        this.data = data;
    }

    public ResponseResult(BusinessException be) {
        this(be.getCode(), be.getMessage(), (T) null);
    }

    public ResponseResult(CodeMessage codeMessage) {
        this(codeMessage.getCode(), codeMessage.getMessage(), (T) JSONObject.parseObject("{}",JSONObject.class));
    }

    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult(data);
    }

    public static <T> ResponseResult<T> fail(Integer code, String message) {
        return new ResponseResult(code, message);
    }

    public T getData() {
        return this.data;
    }

    public CodeMessage getMeta() {
        return this.meta;
    }

    public boolean checkSuccess() {
        return null != this.meta && CoreExceptionCodes.SUCCESS.getCode().equals(this.meta.getCode());
    }
}
