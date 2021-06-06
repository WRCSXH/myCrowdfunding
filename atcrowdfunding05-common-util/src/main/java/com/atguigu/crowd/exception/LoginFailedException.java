package com.atguigu.crowd.exception;

/**
 * 登录失败异常
 */
public class LoginFailedException extends RuntimeException{

    public LoginFailedException() {
        super();
    }

    public LoginFailedException(String message) {
        super(message);
    }
}
