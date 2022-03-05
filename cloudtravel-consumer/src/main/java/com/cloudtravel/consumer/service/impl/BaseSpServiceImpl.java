package com.cloudtravel.consumer.service.impl;

import com.cloudtravel.consumer.common.model.BaseSpModel;
import com.cloudtravel.consumer.common.service.IBaseSpService;
import com.cloudtravel.producer.common.model.BaseUserModel;
import com.cloudtravel.producer.common.service.IBaseUserService;
import com.cloudtravel.common.util.RandomHelper;
import com.cloudtravel.consumer.dao.IBaseSpDao;
import com.cloudtravel.shardingsphere.common.service.ShardBaseUserService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseSpServiceImpl implements IBaseSpService {

    @Autowired
    IBaseUserService userService;

    @Autowired
    IBaseSpDao baseSpDao;

    @Autowired
    ShardBaseUserService shardBaseUserService;

    private static Integer TEMPLATE_COUNT = 1;

    @Override
    @GlobalTransactional(name = "sp-user-seata-group" , rollbackFor = Exception.class)
    public String testAddUserAndSp() {

        String templateId = RandomHelper.getRandomStr(10);

        BaseUserModel userModel = new BaseUserModel();
        userModel.setBizId("999999999");
        userModel.setUserName("test1111");
        userModel.setUserType(0);
        userModel.setIdNumber("111111111");
        userModel.setIdNumType(0);
        userModel.setTenantId("01");


        BaseSpModel spModel = new BaseSpModel();
        spModel.setFounderAccountId(99999L);
        spModel.setSpName("云寓公寓");
        Long spSeqId = this.addSp(templateId , spModel);
        String userSeqId = userService.insertUser(userModel);

        Long shardId = shardBaseUserService.addUser(1L);
//        return "userSeqId = " + userSeqId + " &&& spSeqId = " + spSeqId + " &&& shardingId = " + shardId;
        throw new RuntimeException("test");
    }

    @Override
    public Long addSp(String templateId , BaseSpModel spModel) {

        baseSpDao.insertSelective(spModel);
//        throw new RuntimeException("test");
        return spModel.getSpId();
    }
}
