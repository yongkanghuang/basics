package com.basics.wechat.controller;

import com.basics.wechat.entity.WeChatResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description 微信模块
 * @Author hyk
 * @Date 2019/2/25 11:35
 **/
@RestController
@RequestMapping("/wx")
@Slf4j
public class WeChatController {

    /**
     * 小程序的appid
     */
    @Value(value = "${wx.appId}")
    public String appId;

    /**
     * 小程序secret
     */
    @Value(value = "${wx.appSecret}")
    public String appSecret;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/login")
    public Map<String,Object> login(@RequestParam String code,
                                    @RequestParam(required = false) String nickName,
                                    @RequestParam(required = false) String city,
                                    @RequestParam(required = false) String province,
                                    @RequestParam(required = false) String avatarUrl,
                                    @RequestParam(required = false) String gender){
        //https://www.cnblogs.com/c9999/p/6636415.html
        Map<String,Object> reMap = new HashMap<String,Object>();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //配置超时时间
        RequestConfig requestConfig = RequestConfig.custom().
                setConnectTimeout(1000).setConnectionRequestTimeout(1000)
                .setSocketTimeout(1000).setRedirectsEnabled(true).build();
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="
                +appId+"&secret="+appSecret+"&js_code="+code+"&=grant_type=authorization_code";
        HttpGet httpGet = new HttpGet(url);
        log.info(url);
        //设置超时时间
        httpGet.setConfig(requestConfig);
        //装配post请求参数
        List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
        //请求参数
//        list.add(new BasicNameValuePair("appid", getAppId()));
//        list.add(new BasicNameValuePair("secret", getAppSecret()));
//        list.add(new BasicNameValuePair("js_code", code));
//        list.add(new BasicNameValuePair("grant_type", "authorization_code"));
//        list.add(new BasicNameValuePair("nickName", nickName));
//        list.add(new BasicNameValuePair("city", city));
//        list.add(new BasicNameValuePair("province", province));
//        list.add(new BasicNameValuePair("avatarUrl", avatarUrl));
//        list.add(new BasicNameValuePair("gender", gender));

        try {
            //设置post求情参数
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,"UTF-8");
            HttpResponse httpResponse = httpClient.execute(httpGet);
            String strResult = "";
            if(httpResponse != null){
                System.out.println(httpResponse.getStatusLine().getStatusCode());
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    strResult = EntityUtils.toString(httpResponse.getEntity());
                } else if (httpResponse.getStatusLine().getStatusCode() == 400) {
                    strResult = "Error Response: " + httpResponse.getStatusLine().toString();
                } else if (httpResponse.getStatusLine().getStatusCode() == 500) {
                    strResult = "Error Response: " + httpResponse.getStatusLine().toString();
                } else {
                    strResult = "Error Response: " + httpResponse.getStatusLine().toString();
                }
            }else{

            }
            log.info(strResult);
            ObjectMapper objectMapper = new ObjectMapper();
            WeChatResult weChatResult = objectMapper.readValue(strResult, WeChatResult.class);
            //存入redis
            redisTemplate.opsForValue().set(weChatResult.getOpenid(),weChatResult.getSession_key(),weChatResult.getExpires_in(),TimeUnit.SECONDS);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (httpClient != null){
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return reMap;
    }

}
