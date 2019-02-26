package com.basics.rabbitmq.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description
 * @Author hyk
 * @Date 2019/2/17 21:53
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {

    private String id;

    private String messageId;

    private String meassage;

    private Date createTime;

    private Date updateTime;
}
