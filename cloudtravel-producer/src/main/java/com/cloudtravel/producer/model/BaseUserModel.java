package com.cloudtravel.producer.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

/**
 * es的index必须为小写
 */
@Getter
@Setter
@AllArgsConstructor // lombok 包下 : 全参构造器
@NoArgsConstructor // lombok 包下 : 无参构造器
@Document(indexName = "user" , indexStoreType = "spUser" , refreshInterval = "10s") // indexName只允许小写
public class BaseUserModel implements Serializable {

    private static final long serialVersionUID = 8283483377785895360L;

    @Id
    @Field(type = FieldType.Long , index = true)
    private Long id;

    @Field(type = FieldType.Text , index = true)
    private String bizId;

    @Field(type = FieldType.Text , index = true)
    private String tenantId;

    @Field(type = FieldType.Text , index = true)
    private Integer userType;

    @Field(type = FieldType.Keyword , fielddata = true , analyzer = "ik_smart")
    private String userName;

    @Field(type = FieldType.Text)
    private String idNumber;

    @Field(type = FieldType.Text , index = true)
    private Integer idNumType;

    private Date gmtCreate;

    private Date gmtUpdate;

    @Field(type = FieldType.Text , index = true)
    private String templateId;
}