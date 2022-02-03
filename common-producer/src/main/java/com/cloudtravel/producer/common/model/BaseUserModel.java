package com.cloudtravel.producer.common.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class BaseUserModel implements Serializable {


    private static final long serialVersionUID = 8283483377785895360L;

    private Long id;

    private String bizId;

    private String tenantId;

    private Integer userType;

    private String userName;

    private String idNumber;

    private Integer idNumType;

    private Date gmtCreate;

    private Date gmtUpdate;

    private String templateId;
}