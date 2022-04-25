package com.cloudtravel.db.service.impl;


import com.cloudtravel.db.dao.DbBaseUserDao;
import com.cloudtravel.db.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements ITestService {

    @Autowired
    DbBaseUserDao dbBaseUserDao;


}
