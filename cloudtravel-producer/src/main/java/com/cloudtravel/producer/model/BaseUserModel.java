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
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(indexName = "user",indexStoreType = "test")
public class BaseUserModel implements Serializable {

    private static final long serialVersionUID = 8283483377785895360L;

    @Id
    @Field(type = FieldType.Long)
    private Long id;

    private String bizId;

    private String tenantId;

    private Integer userType;

    @Field(type = FieldType.Text)
    private String userName;

    @Field(type = FieldType.Text)
    private String idNumber;

    private Integer idNumType;

    private Date gmtCreate;

    private Date gmtUpdate;

    private String templateId;
}