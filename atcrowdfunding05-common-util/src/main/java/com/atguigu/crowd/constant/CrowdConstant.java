package com.atguigu.crowd.constant;
/**
 * 以常量管理属性名和消息
 */
public class CrowdConstant {
    public static final String ATTR_NAME_EXCEPTION = "exception";
    public static final String ATTR_NAME_MESSAGE = "message";
    public static final String ATTR_NAME_LOGIN_ADMIN = "admin";
    public static final String ATTR_NAME_LOGIN_MEMBER = "member";
    public static final String ATTR_NAME_PAGE_INFO = "pageInfo";
    public static final String MESSAGE_LOGIN_FAILED_ACCT_NOT_EXIST = "登录失败，登录账号不存在！";
    public static final String MESSAGE_LOGIN_FAILED_ACCT_PASSWORD_ERROR = "登录失败，登录密码错误！";
    public static final String MESSAGE_LOGIN_SUCCESS = "登录成功！";
    public static final String MESSAGE_STRING_INVALIDATE = "不是有效的字符串，字符串为null或空字符串！";
    public static final String MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE = "登录账号不唯一！";
    public static final String MESSAGE_ACCESS_FORBIDDEN = "账号未登录，请登录后再访问！";
    public static final String MESSAGE_LOGIN_ACCT_ALREADY_IN_USE = "账号不能重复！";
    public static final String MESSAGE_ACCESS_DENIED = "抱歉，您没有权限执行本操作！";
    public static final String REDIS_CODE_PREFIX = "REDIS_CODE_PREFIX_";

    public static final String MESSAGE_REGISTER_CODE_IS_NULL = "注册失败！验证码已过期或手机号码不正确！";
    public static final String MESSAGE_REGISTER_CODE_IS_INCORRECT = "注册失败！验证码输入错误！";
    public static final String MESSAGE_HEADER_PICTURE_EMPTY = "头图不允许为空！";
    public static final String MESSAGE_HEADER_PICTURE_UPLOAD_FAILED = "头图上传失败！";
    public static final String MESSAGE_DETAIL_PICTURE_EMPTY = "详情图不允许为空！";
    public static final String MESSAGE_DETAIL_PICTURE_UPLOAD_FAILED = "详情图上传失败！";
    public static final String ATTR_NAME_TEMPLATE_PROJECT = "templateProject";
    public static final String MESSAGE_TEMPLATE_PROJECT_MISSING = "临时文件templateProject丢失！";
    public static final String ATTR_NAME_PORTAL_DATA = "portalData";
    public static final String MESSAGE_PORTAL_DATA_MISSING = "数据portalData丢失！";
    public static final String ATTR_NAME_DETAIL_PROJECT_DATA = "detailProjectData";
    public static final String MESSAGE_DETAIL_PROJECT_DATA_MISSING = "数据detailProjectData丢失！";
    public static final String ATTR_NAME_RETURN_CONFIRM_INFO = "returnConfirmInfo";
    public static final String MESSAGE_RETURN_CONFIRM_INFO_MISSING = "数据returnConfirmInfo丢失！";
    public static final String ATTR_NAME_ORDER_CONFIRM_INFO = "orderConfirmInfo";
    public static final String MESSAGE_ORDER_CONFIRM_INFO_MISSING = "数据orderConfirmInfo丢失！";
    public static final String MESSAGE_SAVE_NEW_ADDRESS_FAILED = "新增地址保存失败！请重试！";
    public static final String ATTR_NAME_ORDER_VO = "orderVO";
}
