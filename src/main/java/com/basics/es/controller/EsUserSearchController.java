package com.basics.es.controller;

import com.basics.es.entity.EsUser;
import com.basics.es.service.EsUserSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description //TODO
 * @Author hyk
 * @Date 2019/1/23 10:17
 **/
@Slf4j
@RestController
@RequestMapping("/esUser")
@EnableSwagger2
@Api(tags = "Es用户管理")
public class EsUserSearchController {

    @Autowired
    EsUserSearchService esUserSearchService;

    @GetMapping("/get/{id}")
    public EsUser getEsUserByid(@PathVariable String id){
        return esUserSearchService.getEsUserById(id);
    }

    /**
     *
     * @param esUser
     * @return
     */
    @PostMapping("/save")
    public EsUser saveUser(@RequestBody(required = false) EsUser esUser){
        List<EsUser> esUserList = new ArrayList<>();
        log.info("date："+esUser.getDate()+",date2："+esUser.getDateFormat());
        esUserList.add(esUser);
        esUserSearchService.saveEsUser(esUserList);
        return esUser;
    }

    @GetMapping("/delete/{id}")
    public Map<String,String> deleteEsUserByid(@PathVariable String id){
        esUserSearchService.deleteEsUserById(id);
        Map<String,String> map = new HashMap<>();
        map.put("code","200");
        return map;
    }

    @GetMapping("/list")
    public List<EsUser> searchEsUser(@RequestParam String userName,@RequestParam String phone,String orderField){
        EsUser esUser = new EsUser();
        esUser.setPhone(phone);
        esUser.setUserName(userName);
        List<EsUser> esUserList = esUserSearchService.getEsUserByQueryOrderByCreateBy(esUser,orderField);
        return esUserList;
    }

    @ApiOperation(value = "分页获取用户")
    @GetMapping("/list/page")
    public Page<EsUser> queryEsUserForList(@RequestParam(required = false) String userName,@RequestParam(required = false) String phone,
                                           @RequestParam(required = false) String startTime,@RequestParam(required = false) String endTime,
                                           @RequestParam(required = false) String orderField,@RequestParam int page,@RequestParam int size){
        EsUser esUser = new EsUser();
        esUser.setPhone(phone);
        esUser.setUserName(userName);
        Page<EsUser> userPage = esUserSearchService.getEsUserPageByCreateOn(esUser,startTime,endTime,page,size,orderField);
        return userPage;
    }

    @ApiOperation(value = "汇聚用户")
    @GetMapping("/agg/page")
    public Map<String,Object> getEsUserAggregationByCreateOn(@RequestParam(required = false) String userName, @RequestParam(required = false) String phone,String sex, @RequestParam(required = false) String orderField, @RequestParam int page,@RequestParam int size){
        EsUser esUser = new EsUser();
        esUser.setPhone(phone);
        esUser.setUserName(userName);
        esUser.setSex(sex);
        Map<String,Object> reMap = esUserSearchService.getEsUserAggregationByCreateOn(esUser,page,size,orderField);
        reMap.put("code","200");
        return reMap;
    }

}
