package com.basics.wechat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description //TODO
 * @Author hyk
 * @Date 2019/2/25 18:05
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeChatResult implements Serializable {

    private String session_key;

    private Long expires_in;

    private String openid;
}
