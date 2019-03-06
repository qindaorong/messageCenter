/**
 * @Description
 * @Author lindaoliang
 * @Create 2018/3/19
 * Copyright: Copyright (c) 2018
 * Company:北京思源政务通科技有限公司
 **/
package com.xhxd.messagecenter.common.exception;

import java.util.Map;

public class BusinessException extends RuntimeException {
        private static final long serialVersionUID = 1L;
        private Integer code;

        public BusinessException(Integer code, String message) {
            this(code, message, (Throwable)null);
        }

        public BusinessException(Integer code, String message, Throwable t) {
            super(message, t);
            this.code = code;
        }

        public BusinessException(Throwable t) {
            this(ExceptionCode.SERVICE_BUSY, t);
        }

        public BusinessException(CodeMessage codeMessage) {
            this(codeMessage.getCode(), codeMessage.getMessage(), (Throwable)null);
        }

        public BusinessException(CodeMessage codeMessage, Throwable t) {
            this(codeMessage.getCode(), codeMessage.getMessage(), t);
        }

        public BusinessException(CodeMessage codeMessage, String paramValue) {
            this(codeMessage.getCode(), codeMessage.getMessage(paramValue), (Throwable)null);
        }

        public BusinessException(CodeMessage codeMessage, Throwable t, String paramValue) {
            this(codeMessage.getCode(), codeMessage.getMessage(paramValue), t);
        }

        public BusinessException(CodeMessage codeMessage, Map paramValues) {
            this(codeMessage.getCode(), codeMessage.getMessage(paramValues), (Throwable)null);
        }

        public BusinessException(CodeMessage codeMessage, Throwable t, Map paramValues) {
            this(codeMessage.getCode(), codeMessage.getMessage(paramValues), t);
        }

        public Integer getCode() {
            return this.code;
        }
    }

