package com.basics.user.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hyk
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String id;

    @ApiModelProperty(value = "用户名",example = "ZhangSan")
    private String userName;
}
