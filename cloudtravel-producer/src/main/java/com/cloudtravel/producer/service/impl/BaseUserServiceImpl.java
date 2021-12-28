package com.cloudtravel.producer.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.cloudtravel.common.producer.IBaseUserService;
import com.cloudtravel.common.producer.model.BaseUserModel;
import com.cloudtravel.producer.dao.IBaseUserModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class BaseUserServiceImpl implements IBaseUserService {

    @Autowired
    IBaseUserModelMapper IBaseUserModelMapper;

    @Override
    public String insertUser(BaseUserModel userModel) {
        int rows = IBaseUserModelMapper.insertSelective(userModel);
        return userModel.getId().toString();
    }
}
