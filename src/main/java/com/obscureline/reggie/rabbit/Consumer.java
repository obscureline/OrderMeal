package com.obscureline.reggie.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Consumer {

    //    @RabbitHandler
    //    @RabbitListener(queuesToDeclare = @Queue("notice_queue"))
    //    public void process(String message) {
    //        log.info("村里猿公子收到通知：" + message);
    //    }

    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue("notice_product_queue"))
    public void process(ScProduct product) {
        System.out.println("收到秒杀产品信息为：" + product);
    }

    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue("notice_product_queue"))
    public void processs(ScProduct product) {
        System.out.println("收到秒杀产品信息2为：" + product);
    }
}