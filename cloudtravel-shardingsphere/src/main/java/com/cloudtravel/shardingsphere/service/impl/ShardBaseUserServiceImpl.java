package com.cloudtravel.shardingsphere.service.impl;

import com.cloudtravel.shardingsphere.common.service.IShardBaseUserService;
import com.cloudtravel.shardingsphere.dao.TUserModelMapper;
import com.cloudtravel.shardingsphere.model.TUserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShardBaseUserServiceImpl implements IShardBaseUserService {

    @Autowired
    TUserModelMapper userModelMapper;


    @Override
    public Long addUser(Long tenantId) {
        TUserModel model = new TUserModel();
        model.setTenantId(tenantId.toString());
        model.setBizId("1");
        model.setUserType(1);
        model.setUserName("test1");
        model.setIdNumber("231");
        model.setIdNumType(0);
        userModelMapper.insertSelective(model);
        return model.getId();
    }
}
