package com.basics.sys.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description 用户实体对象
 * @Author hyk
 * @Date 2019/2/22 13:45
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUser implements Serializable {

    private String id;

    private String userName;

    private String nickName;

    private String password;

    /**
     * 性别，1：男，2：女
     */
    private int gender;

    private String phone;

    private String email;

    private String city;

    private String province;

    /**
     * 微信openID
     */
    private String openId;

    /**
     * 微信 UnionID
     */
    private String unionId;

    /**
     * 是否可用
     */
    private int locked;

    /**
     *最后登录时间
     */
    private Date lastLogin;

    private String loginIp;

    /**
     * 头像url
     */
    private String avatarUrl;

    private String remark;

    private Date regTime;

    private Date updateTime;

}
