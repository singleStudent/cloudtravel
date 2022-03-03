package com.cloudtravel.shardingsphere.service.impl;

import com.cloudtravel.shardingsphere.common.service.ShardBaseUserService;
import com.cloudtravel.shardingsphere.dao.TUserModelMapper;
import com.cloudtravel.shardingsphere.model.TUserModel;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShardBaseUserServiceImpl implements ShardBaseUserService {

    @Autowired
    TUserModelMapper userModelMapper;


    @Override
//    @GlobalTransactional(name = "sp-user-seata-group" , rollbackFor = Exception.class)
    @Transactional
    @ShardingTransactionType(TransactionType.BASE)
    public Long addUser(Long tenantId) {
        TUserModel model = new TUserModel();
        model.setTenantId(tenantId.toString());
        model.setBizId("1");
        model.setUserType(1);
        model.setUserName("test1");
        model.setIdNumber("231");
        model.setIdNumType(0);
        userModelMapper.insertSelective(model);
//        throw new RuntimeException("test");
        return model.getId();
    }
}
