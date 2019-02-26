package com.basics.user.controller;

import com.basics.rabbitmq.entity.Message;
import com.basics.user.entity.User;
import com.basics.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * 用户控制层
 * @author hyk
 */
@RestController
@Slf4j
@RequestMapping("/user")
@Api(tags = "用户管理")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @ApiOperation(value = "获取所有用户")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public List<User> getAlarmList(){
        log.info("test");
        log.debug("这是一个debug日志...");
        return userService.findUser();
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

    @ApiOperation(value = "获取用户")
    @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "String")
    @RequestMapping(value = "/get" , method = RequestMethod.GET)
    public User getUser(@RequestParam String id){
        User user = userService.findUserById(id);
        if(user != null){
            //充当生产者 ==》发送消息到rabbitMq
            Message message = new Message();
            message.setId(UUID.randomUUID().toString());
            message.setMessageId(System.currentTimeMillis()+"$"+UUID.randomUUID().toString());
            CorrelationData correlationData = new CorrelationData();
            correlationData.setId(message.getId());
            rabbitTemplate.setConfirmCallback(confirmCallback);
            rabbitTemplate.convertAndSend("exchange-m","queue.m",message,correlationData);
        }

        return user;
    }

    @ApiOperation(value = "添加用户")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public String saveUser(@ApiParam(value = "用户实体",required = true)@RequestBody User user){
        //    @ApiImplicitParam(name = "user",value = "用户实体",required = true,paramType = "body", dataType = "User")
//        User user = new User("5","kju");
        userService.saveUser(user);
        return "测试数据";
    }

    @ApiOperation(value = "更新用户", notes = "更新已存在用户")
    @PostMapping(value = "update")
    @ApiImplicitParam(name = "user",value = "更新用户实体",required = true,paramType = "body", dataType = "User")
    public String updateUser(@RequestBody User user){
        System.out.println(user);
        return "sucess ["+user.toString()+"]";
    }
}
