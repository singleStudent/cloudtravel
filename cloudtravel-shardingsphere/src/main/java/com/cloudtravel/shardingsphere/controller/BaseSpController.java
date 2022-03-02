package com.cloudtravel.shardingsphere.controller;

import com.cloudtravel.shardingsphere.dao.BSpModelMapper;
import com.cloudtravel.shardingsphere.model.BSpModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("sp")
@ResponseBody
public class BaseSpController {

    @Autowired
    BSpModelMapper spModelMapper;

    @Autowired
    BaseUserController userController;

    @GetMapping("addSp")
    @Transactional
    public String addSp(@RequestParam("tenantId") Long tenantId,
                      @RequestParam("bizId")Long bizId) {
        BSpModel model = new BSpModel();
        model.setBizId(bizId);
        model.setTenantId(tenantId);
        model.setSpName("测试分库分表");
        spModelMapper.insertSelective(model);
        return model.getSpId() +"===="+ userController.addUser(tenantId - 1);
    }
}
