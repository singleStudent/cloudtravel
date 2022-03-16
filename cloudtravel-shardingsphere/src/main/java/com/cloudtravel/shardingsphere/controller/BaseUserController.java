package com.cloudtravel.shardingsphere.controller;

import com.cloudtravel.shardingsphere.dao.TUserModelMapper;
import com.cloudtravel.shardingsphere.model.TUserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("user")
@Controller
@ResponseBody
public class BaseUserController {

    @Autowired
    TUserModelMapper userModelMapper;

    @GetMapping("add")
    public Long addUser(@RequestParam("tenantId") Long tenantId) {
        TUserModel model = new TUserModel();
        model.setTenantId(tenantId.toString());
        model.setBizId("1");
        model.setUserType(1);
        model.setUserName("test1");
        model.setIdNumber("231");
        model.setIdNumType(0);
        userModelMapper.insertSelective(model);
        throw new RuntimeException("test");
    }


}
