package com.cloudtravel.consumer.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.cloudtravel.common.consumer.model.BaseSpModel;
import com.cloudtravel.common.consumer.service.IBaseSpService;
import com.cloudtravel.common.producer.model.BaseUserModel;
import com.cloudtravel.common.producer.service.IBaseUserService;
import com.cloudtravel.common.util.RandomHelper;
import com.cloudtravel.consumer.dao.IBaseSpDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

@Service
public class BaseSpServiceImpl implements IBaseSpService {

    @Autowired
    IBaseUserService userService;

    @Autowired
    IBaseSpDao baseSpDao;

    @Autowired
    RedisTemplate redisTemplate;

    private static Integer TEMPLATE_COUNT = 1;

    @Override
    public String testAddUserAndSp() {

        String templateId = RandomHelper.getRandomStr(10);

        BaseUserModel userModel = new BaseUserModel();
        userModel.setBizId("999999999");
        userModel.setUserName("test1111");
        userModel.setUserType(0);
        userModel.setIdNumber("111111111");
        userModel.setIdNumType(0);
        userModel.setTenantId("01");
        String userSeqId = userService.insertUser(userModel);
        BaseSpModel spModel = new BaseSpModel();
        spModel.setFounderAccountId(99999L);
        spModel.setSpName("云寓公寓");
        Long spSeqId = this.addSp(templateId , spModel);

        return "userSeqId = " + userSeqId + " &&& spSeqId = " + spSeqId;
    }

    @Override
    public Long addSp(String templateId , BaseSpModel spModel) {

        baseSpDao.insertSelective(spModel);
        return spModel.getSpId();
    }
}
