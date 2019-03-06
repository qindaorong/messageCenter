package com.xhxd.messagecenter.common.exception;

/**
 * 应用异常编码
 *
 * @author
 * @since
 */
public class ExceptionCode {

    /**
     * 服务器异常
     */
    public static final CodeMessage SERVICE_BUSY = new CodeMessage(000000, "服务器繁忙，请稍后重试");

    /**
     * 系统异常
     */
    public static final CodeMessage LOW_VERSION = new CodeMessage(010000, "版本过低，当前功能不支持");

    /**
     * 接口调用异常
     */
    public static final CodeMessage INTERFACE_USE_FAILURE = new CodeMessage(100000, "接口调用失败");

    public static final CodeMessage METHOD_FAILURE = new CodeMessage(100001, "执行操作失败");

    /**
     * 基础校验--参数错误
     */
    public static final CodeMessage PARAM_IS_ILLEGAL = new CodeMessage(101001, "参数非法");
    /**
     * 基础校验--参数为空
     */
    public static final CodeMessage PARAM_IS_NULL = new CodeMessage(101002, "参数不能为空");
    /**
     * 手机号已绑定
     */
    public static final CodeMessage PHONE_USED = new CodeMessage(101003, "手机号已绑定");
    /**
     * 验证码已绑定
     */
    public static final CodeMessage EXPERIENCE_CODE_USED = new CodeMessage(101004, "您输入的体验码已过期");
    /**
     * 验证码为空
     */
    public static final CodeMessage EXPERIENCE_CODE_NULL = new CodeMessage(101005, "您输入的体验码不正确");
    /**
     * redis中没有查到礼物
     */
    public static final CodeMessage GIFT_NULL = new CodeMessage(101006, "没有礼物");
    /**
     * redis中没有查到礼物
     */
    public static final CodeMessage GIFT_GOT = new CodeMessage(101007, "礼物已领取");
    /**
     * redis中没有查到礼物
     */
    public static final CodeMessage PHONE_JOINED = new CodeMessage(101008, "手机号已加入白名单");

    /**
     * session, code 相关
     */
    public static final CodeMessage SESSION_IS_FAIL = new CodeMessage(200001, "获取会话失败");

    public static final CodeMessage TOKEN_EXPIRE = new CodeMessage(200002, "用户会话参数已过期");

    public static final CodeMessage TOON_CODE_EXPIRE = new CodeMessage(200003, "toonCode已过期");

    public static final CodeMessage LOGIN_TOO_MANY = new CodeMessage(200004, "登录过于频繁，请稍后尝试");

    public static final CodeMessage NO_PERSSION = new CodeMessage(200005, "sorry,您没有权限");
}