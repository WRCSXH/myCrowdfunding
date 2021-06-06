package com.atguigu.crowd.exception;

/**
 * 禁止访问异常
 */
public class AccessForbiddenException extends RuntimeException {
    public AccessForbiddenException() {
        super();
    }

    public AccessForbiddenException(String message) {
        super(message);
    }
}
