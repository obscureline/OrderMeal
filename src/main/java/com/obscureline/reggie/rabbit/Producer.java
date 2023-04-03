package com.obscureline.reggie.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Producer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //    public void produce() {
    //        String message = "疫情期间注意防护";
    //        log.info("乡长说：" + message);
    //        rabbitTemplate.convertAndSend("notice_queue", message);
    //    }

    public void produce() {
        // 创建一个商品
        ScProduct product = new ScProduct();
        product.setName("苹果 14，512G，银灰");
        product.setNumber(8);
        product.setId(2022);
        product.setProductImg("./iphone14.png");
        // 转换发送商品信息
        System.out.println("产品被" + product.getName() + "秒杀，发送通知");
        rabbitTemplate.convertAndSend("notice_product_queue", product);
    }
}