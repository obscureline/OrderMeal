package com.obscureline.reggie.common;

/**
 * 给予ThreadLocal封装工具类，用户保存和获取当前用户id
 */
public class BaseContext {

    /**
     * 基于ThreadLocal封装工具类，用户保存和获取当前登录用户id
     */
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 为数据库设置值
     * @param id
     */
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    /**
     * 获取值
     * @return
     */
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}