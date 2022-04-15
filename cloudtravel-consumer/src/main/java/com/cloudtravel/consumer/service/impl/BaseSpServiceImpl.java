package com.cloudtravel.consumer.service.impl;

import com.cloudtravel.consumer.common.model.BaseSpModel;
import com.cloudtravel.consumer.common.service.IBaseSpService;
import com.cloudtravel.producer.model.BaseUserModel;
import com.cloudtravel.common.util.RandomHelper;
import com.cloudtravel.consumer.dao.IBaseSpDao;
import com.cloudtravel.producer.service.IBaseUserService;
import com.cloudtravel.shardingsphere.common.service.IShardBaseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseSpServiceImpl implements IBaseSpService {

    @Autowired
    IBaseUserService baseUserService;

    @Autowired
    IBaseSpDao baseSpDao;

    @Autowired
    IShardBaseUserService shardBaseUserService;

    private static Integer TEMPLATE_COUNT = 1;

    @Override
//    @GlobalTransactional(name = "sp-user-seata-group" , rollbackFor = Exception.class)
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
        String userSeqId = baseUserService.insertUser(userModel);

        Long shardId = shardBaseUserService.addUser(1L);
        return "userSeqId = " + userSeqId + " &&& spSeqId = " + spSeqId + " &&& shardingId = " + -1;
    }

    @Override
    public Long addSp(String templateId , BaseSpModel spModel) {

        baseSpDao.insertSelective(spModel);
//        throw new RuntimeException("test");
        return spModel.getSpId();
    }
}
