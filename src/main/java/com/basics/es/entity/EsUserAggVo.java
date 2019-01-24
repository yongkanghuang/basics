package com.basics.es.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description //TODO
 * @Author hyk
 * @Date 2019/1/23 23:41
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EsUserAggVo implements Serializable {

    /**
     * 汇聚用户名 分组
     */
    private String userNameKey;

    /**
     * 汇聚用户数
     */
    private Long userNameCount;

    /**
     * 平均年龄
     */
    private Double agaAvg;

    /**
     * 男
     */
    private String manKey="男";

    /**
     * 男 人数
     */
    private Long manCount=0L;

    private String womanKey="女";

    private Long womanCount=0L;

}
