package com.atguigu.crowd.exception;

/**
 * 新增或更新管理员时如果检测到登录账号重复抛出该异常
 */
public class LoginAcctAlreadyInUseException extends RuntimeException{

    public LoginAcctAlreadyInUseException() {
        super();
    }

    public LoginAcctAlreadyInUseException(String message) {
        super(message);
    }
}
