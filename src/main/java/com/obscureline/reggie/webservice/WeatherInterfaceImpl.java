package com.obscureline.reggie.webservice;

import javax.jws.WebService;

@WebService
public class WeatherInterfaceImpl implements WeatherInterface{
    @Override
    public String queryWeather(String cityName) {
        System.out.println("接收到客户端发送的城市名称:"+cityName);
        String result="晴,高温预警";
        return result;
    }
}