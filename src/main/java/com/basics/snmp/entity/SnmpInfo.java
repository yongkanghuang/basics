package com.basics.snmp.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description snmp实体
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

    @ApiModelProperty(value = "oid类型",example = "网络、内存、cpu、自定义")
    private String oIdType;

    @ApiModelProperty(value = "系统类型",example = "1:linux,2:windows")
    private Integer systemType;

    @ApiModelProperty(value = "snmp版本",example = "1:v1,2:v2,3:v3")
    private Integer snmpVersion;

    @ApiModelProperty(value = "用户名",example = "ZhangSan")
    private String creater;
}
