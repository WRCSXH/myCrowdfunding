package com.atguigu.crowd.util;
/**
 * 统一整个项目中ajax请求的返回结果的格式（未来也可用于分布式架构中各个模块间调用时返回统一格式的ajax请求结果）
 * @param <T>
 */
// 声明泛型
public class ResultEntity<T> {
    // 常量，表示请求处理成功
    public static final String SUCCESS = "SUCCESS";
    // 常量，表示请求处理失败
    public static final String FAILED = "FAILED";
    // 封装请求处理的结果是成功还是失败
    // 使用泛型
    private String result;
    // 表示请求处理失败时返回的错误消息
    private String message;
    // 表示要返回的数据
    private T data;

    /**
     * 请求处理成功且不需要返回数据时使用的工具方法，增删改
     * @return
     */
    public static ResultEntity successWithoutData(){
        return new ResultEntity(SUCCESS,null,null);
    }

    /**
     * 请求处理成功且需要返回数据时使用的工具方法，查询
     * @param data 返回的数据
     * @param <Type> 返回的数据的类型
     * @return
     */
    // 声明泛型，使用泛型
    public static <Type> ResultEntity<Type> successWithData(Type data){
        return new ResultEntity<>(SUCCESS,null,data);
    }

    /**
     * 请求处理失败时使用的工具方法
     * @param message 返回的错误消息
     * @return
     */
    public static ResultEntity failed(String message){
        return new ResultEntity(FAILED,message,null);
    }

    public ResultEntity() {
    }

    public ResultEntity(String result, String message, T data) {
        this.result = result;
        this.message = message;
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultEntity{" +
                "result='" + result + '\'' +
                ", message='" + message + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
