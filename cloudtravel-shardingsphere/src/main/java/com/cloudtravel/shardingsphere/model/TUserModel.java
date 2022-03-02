package com.cloudtravel.shardingsphere.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TUserModel {
    private Long id;

    private String bizId;

    private String tenantId;

    private Integer userType;

    private String userName;

    private String idNumber;

    private Integer idNumType;

    private Date gmtCreate;

    private Date gmtUpdate;
}