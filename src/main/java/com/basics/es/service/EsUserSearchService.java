package com.basics.es.service;

import com.basics.es.entity.EsUser;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @Description ES 用户查询
 * @Author hyk
 * @Date 2019/1/23 9:09
 **/
public interface EsUserSearchService {

    /**
     * 查询es数据源的单用户
     * @param id
     * @return
     */
    EsUser getEsUserById(String id);

    /**
     * 根据id删除
     * @param id
     */
    void deleteEsUserById(String id);

    /**
     * 批量添加
     * @param esUserList
     */
    void saveEsUser(List<EsUser> esUserList);

    /**
     * 根据条件查询
     * @param esUser
     * @param orderField
     * @return
     */
    List<EsUser> getEsUserByQueryOrderByCreateBy(EsUser esUser,String orderField);

    /**
     * 根据条件分页查询
     * @param esUser
     * @param page
     * @param size
     * @param orderField
     * @return
     */
    Page<EsUser> getEsUserPageByCreateOn(EsUser esUser,String startTime,String endTime, Integer page, Integer size, String orderField);

    /**
     * 对条件进行汇聚
     * @param esUser
     * @param page
     * @param size
     * @param orderField
     * @return
     */
    Map<String,Object> getEsUserAggregationByCreateOn(EsUser esUser, Integer page, Integer size, String orderField);

}
