package com.basics.es.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description 专门给es的实体
 * 实体类需要添加@Document，动态索引名字
 * 列@Field 指定列类型、中文分词。。
 *
 * @Author hyk
 * @Date 2019/1/22 16:19
 **/
@Data
@Document(indexName = "user_#{ T(com.basics.utils.EsIndexChange).getSuffix() }",type = "content",createIndex = false)
public class EsUser implements Serializable {

    @Id
    private String id;

    /**
     *  text字段上fielddata默认是禁用 ,index = false
     */
    @Field(type = FieldType.Text,fielddata = true)
    private String userName;

    @Field(type = FieldType.Text,fielddata = true)
    private String phone;

    @Field(type = FieldType.Text,fielddata = true)
    private String address;

    @Field(type = FieldType.Text,fielddata = true)
    private String sex;

    @Field(type = FieldType.Integer)
    private int age;

    /**
     * yyyy-MM-dd'T'HH:mm:ss
     * dateFormat的时间
     * @JsonFormat 格式解析会自动加8个小时
     * @DateTimeFormat 就不会加8 @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
     */
    @Field(type = FieldType.Date,format = DateFormat.custom,pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;

    /**
     * 加多一个字段 不使用date
     * 就不用存在差8个小时的时区问题
     */
    @Field(type = FieldType.Text,fielddata = true)
    private String dateFormat;

}
