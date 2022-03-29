package com.cloudtravel.producer.service.impl;

import com.cloudtravel.producer.common.service.IBaseUserService;
import com.cloudtravel.producer.common.model.BaseUserModel;
import com.cloudtravel.producer.dao.IBaseUserDao;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseUserServiceImpl implements IBaseUserService {

    @Autowired
    IBaseUserDao IBaseUserModelMapper;

    @Override
//    @GlobalTransactional(name = "sp-user-seata-group" , rollbackFor = Exception.class)
    public String insertUser(BaseUserModel userModel) {
        int rows = IBaseUserModelMapper.insertSelective(userModel);
//        throw new RuntimeException("test");
        return userModel.getId().toString();
    }
}
