package com.cloudtravel.shardingsphere.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.cloudtravel.shardingsphere.dao.TUserModelMapper;
import com.cloudtravel.shardingsphere.model.TUserModel;
import com.cloudtravel.shardingsphere.service.ShardBaseUserService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;

@Service(interfaceClass = ShardBaseUserService.class , owner = "cloudtravel" , version = "1.0")
public class ShardBaseUserServiceImpl implements ShardBaseUserService {

    @Autowired
    TUserModelMapper userModelMapper;

    @Override
    @GlobalTransactional(name = "sp-user-seata-group" , rollbackFor = Exception.class)
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
