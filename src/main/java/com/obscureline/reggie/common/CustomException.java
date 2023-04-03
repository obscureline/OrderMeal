package com.obscureline.reggie.common;

/**
 * 自定义业务异常
 */
public class CustomException extends RuntimeException{

    //父类（运行时异常）
    public CustomException(String message){
        super(message);
    }
}