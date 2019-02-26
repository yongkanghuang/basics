package com.basics.rabbitmq.consumer;

import com.basics.rabbitmq.entity.Message;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @Description 消息消费者
 * @Author hyk
 * @Date 2019/2/17 22:31
 **/
@Component
@Slf4j
public class MessageReceiver {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "queue-m",durable = "true"),
            exchange = @Exchange(name = "exchange-m",durable = "true",type = "topic"),
            key="queue.*"
    ))
    @RabbitHandler
    public void messageReceiver(@Payload Message message, @Headers Map<String,Object> headers,Channel channel) throws IOException {
        log.info("收到消息"+message.getId());
        Long deliveryTag = (Long)headers.get(AmqpHeaders.DELIVERY_TAG);
        // todo 日志记录 插入数据库
        channel.basicAck(deliveryTag,false);
    }
}
