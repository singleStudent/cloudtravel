package com.cloudtravel.db.service.impl;


import com.alibaba.fastjson.JSON;
import com.cloudtravel.db.dao.DbBaseUserDao;
import com.cloudtravel.db.dao.DbSpDao;
import com.cloudtravel.db.dao.DbTbDao;
import com.cloudtravel.db.model.DbBaseUserModel;
import com.cloudtravel.db.model.DbSpModel;
import com.cloudtravel.db.model.DbTbModel;
import com.cloudtravel.db.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TestServiceImpl implements ITestService {

    @Autowired
    DbBaseUserDao dbBaseUserDao;

    @Autowired
    DbSpDao dbSpDao;

    @Autowired
    DbTbDao dbTbDao;

    @Override
//    @Transactional(rollbackFor = Exception.class , propagation = Propagation.REQUIRED)
    public void test() {
        DbBaseUserModel dbBaseUserModel = new DbBaseUserModel();
        dbBaseUserModel.setBizId("1");
        dbBaseUserModel.setTenantId("1");
        dbBaseUserModel.setUserType(Short.valueOf("0"));
        dbBaseUserModel.setUserName("测试数据源切换-user");
        dbBaseUserModel.setIdNumber("测试数据源切换");
        dbBaseUserModel.setIdNumType(Short.valueOf("1"));
        dbBaseUserDao.insertSelective(dbBaseUserModel);
        System.out.println("新增user数据 , 返回Id = " + dbBaseUserModel.getId());

        DbSpModel dbSpModel = new DbSpModel();
        dbSpModel.setTenantId(1L);
        dbSpModel.setBizId(1L);
        dbSpModel.setSpName("测试数据源切换-sp");
        dbSpDao.insertSelective(dbSpModel);
        System.out.println("新增sp数据 , 返回spId = " + dbSpModel.getSpId());

        List<DbTbModel> dbTbModelList = dbTbDao.selectAll();
        System.out.println(JSON.toJSONString(dbTbModelList));
        throw new RuntimeException("test");
    }
}
