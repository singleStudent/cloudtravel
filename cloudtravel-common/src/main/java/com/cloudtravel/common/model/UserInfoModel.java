package com.cloudtravel.common.model;

import lombok.Getter;

import java.io.Serializable;

/**
 * @Description : 用户信息model
 * @Author: Gosin
 * @Date: 2021/12/14 0014 15:25
 */
@Getter
public class UserInfoModel implements Serializable {
    private static final long serialVersionUID = -922008716988113296L;

    private String bizId;

    private String userName;

    private String phoneNumber;

    private Integer age;

    private String address;

    public UserInfoModel setBizId(String bizId) {
        this.bizId = bizId;
        return this;
    }

    public UserInfoModel setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public UserInfoModel setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public UserInfoModel setAge(Integer age) {
        this.age = age;
        return this;
    }

    public UserInfoModel setAddress(String address) {
        this.address = address;
        return this;
    }
}
