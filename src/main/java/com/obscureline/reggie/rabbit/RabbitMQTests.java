package com.obscureline.reggie.rabbit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class RabbitMQTests {

    @Autowired
    Producer producer;

    @RequestMapping("/1")
    public void amqpTest() throws InterruptedException {
        // 生产者发送消息
        producer.produce();
        // 让子弹飞一会儿
        Thread.sleep(1000);
    }


}