package com.basics.rabbitmq.producer;

import com.basics.rabbitmq.entity.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description mq生产者
 * @Author hyk
 * @Date 2019/2/17 21:50
 **/
@Slf4j
@Component
public class MqSender  {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void messageSender(Message message){

    }

    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            log.info(" 回调id:" + correlationData);
            if (ack) {
                log.info("消息成功消费");
            } else {
                log.info("消息消费失败:" + cause);
            }
        }
    };

}
