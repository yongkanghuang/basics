package com.basics.snmp.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description //TODO
 * @Author hyk
 * @Date 2018/12/21 11:33
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SnmpInfo {

    private String id;

    @ApiModelProperty(value = "服务器的ip",example = "127.0.0.1")
    private String ip;

    @ApiModelProperty(value = "服务器的oid",example = "1.0.01.1")
    private String oId;

    @ApiModelProperty(value = "用户名",example = "ZhangSan")
    private String creater;
}
