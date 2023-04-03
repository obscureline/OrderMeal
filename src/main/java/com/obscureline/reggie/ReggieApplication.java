package com.obscureline.reggie;

import com.obscureline.reggie.webservice.WeatherInterfaceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.xml.ws.Endpoint;

@Slf4j
@SpringBootApplication
@ServletComponentScan//扫描拦截器（filter）
@EnableTransactionManagement//事务管理器
@EnableCaching//开启spring-cache缓存
public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class,args);
        //WebService服务开启[服务端]
        Endpoint.publish("http://127.0.0.1:8808/weather", new WeatherInterfaceImpl());
        log.info("项目启动成功");
    }
}