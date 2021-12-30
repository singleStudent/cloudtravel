package com.cloudtravel.producer.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.cloudtravel.common.producer.service.IBaseUserService;
import com.cloudtravel.common.producer.model.BaseUserModel;
import com.cloudtravel.producer.dao.IBaseUserDao;
import org.springframework.beans.factory.annotation.Autowired;

@Service(interfaceClass = IBaseUserService.class,version = "0.0.1")
public class BaseUserServiceImpl implements IBaseUserService {

    @Autowired
    IBaseUserDao IBaseUserModelMapper;

    @Override
    public String insertUser(BaseUserModel userModel) {
        int rows = IBaseUserModelMapper.insertSelective(userModel);
        return userModel.getId().toString();
    }
}
