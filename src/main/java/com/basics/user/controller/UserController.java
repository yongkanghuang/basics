package com.basics.user.controller;

import com.basics.user.entity.User;
import com.basics.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @ApiOperation(value = "获取所有用户")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public List<User> getAlarmList(){
        log.info("test");
        log.debug("这是一个debug日志...");
        return userService.findUser();
    }

    @ApiOperation(value = "获取用户")
    @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "String")
    @RequestMapping(value = "/get" , method = RequestMethod.GET)
    public User getUser(@RequestParam String id){
        return userService.findUserById(id);
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
