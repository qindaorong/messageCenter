/**
 * @Description
 * @Author lindaoliang
 * @Create 2018/3/19
 * Copyright: Copyright (c) 2018
 * Company:北京思源政务通科技有限公司
 **/
package com.xhxd.messagecenter.common.bean;

import com.xhxd.messagecenter.common.exception.CodeMessage;

public class CoreExceptionCodes {
    public static final CodeMessage SUCCESS = new CodeMessage(0, "成功");
    public static final CodeMessage FAIL = new CodeMessage(-1 , "失败");
    public static final CodeMessage UNKNOWN_ERROR = new CodeMessage(999999, "系统异常");
    public static final CodeMessage PARAM_IS_NULL = new CodeMessage(20001, "参数为空");
    public static final CodeMessage HTTP_CODE_WRONG = new CodeMessage(61000, "Http请求返回码错误");

    CoreExceptionCodes() {
    }
}
