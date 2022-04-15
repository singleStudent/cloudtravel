package com.cloudtravel.producer.service.impl;

import com.cloudtravel.producer.service.IBaseUserService;
import com.cloudtravel.producer.model.BaseUserModel;
import com.cloudtravel.producer.dao.IBaseUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseUserServiceImpl implements IBaseUserService {

    @Autowired
    IBaseUserDao IBaseUserModelMapper;

    @Override
//    @GlobalTransactional(name = "sp-user-seata-group" , rollbackFor = Exception.class)
    public String insertUser(BaseUserModel userModel) {
        IBaseUserModelMapper.insertSelective(userModel);
        return userModel.getId().toString();
    }
}
