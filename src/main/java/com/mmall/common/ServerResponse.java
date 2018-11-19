package com.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * 服务端响应对象
 *封装返回的Json
 * @param <T>
 */
//保证序列号Json的时候，对象（value）是null，key也会消失
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable {
    private int status;
    private String msg;
    private T data;

    private ServerResponse(int status){
        this.status = status;
    }

    private ServerResponse(int status,String msg){
        this.status = status;
        this.msg = msg;
    }

    private ServerResponse(int status,T data){
        this.status = status;
        this.data = data;
    }

    private ServerResponse(int status,String msg,T data){
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 返回json的状态码
     * 返回的json 不出现
     * @return
     */
    //不在序列化Json结果中
//    @JsonIgnore?
    public boolean isSuccess(){
        return this.status == ResponseCode.SECCESS.getCode();
    }

    public int getStatus() {
        return this.status;
    }

    public String getMsg(){
        return this.msg;
    }

    public T getData(){
        return this.data;
    }

    /**
     * 开放的构造函数
     * @param <T>
     * @return
     */
    public static  <T>ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(ResponseCode.SECCESS.getCode());
    }

    public static <T>ServerResponse<T> createSuccessByMeg(String msg){
        return new ServerResponse<T>(ResponseCode.SECCESS.getCode(),msg);
    }

    public static <T>ServerResponse<T> createBySuccess(T data){
        return new ServerResponse<T>(ResponseCode.SECCESS.getCode(),data);
    }

    public static <T>ServerResponse<T> createBySuccess(String msg, T data){
        return new ServerResponse<T>(ResponseCode.SECCESS.getCode(),msg,data);
    }

    public static <T>ServerResponse<T> createByError(){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }

    public static <T>ServerResponse<T> createByErrorMsg(String errorMsg){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),errorMsg);
    }

    public static <T>ServerResponse<T> createByErrorCodeMsg(int errorCode,String errotMsg){
        return new ServerResponse<T>(errorCode,errotMsg);
    }


}
