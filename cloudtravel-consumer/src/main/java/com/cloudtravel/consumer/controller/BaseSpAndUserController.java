package com.cloudtravel.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cloudtravel.common.consumer.model.BaseSpModel;
import com.cloudtravel.common.consumer.service.IBaseSpService;
import com.cloudtravel.common.producer.model.BaseUserModel;
import com.cloudtravel.common.producer.service.IBaseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

/**
 * @author Administrator
 */
@RequestMapping("spAndUser")
@Controller
@ResponseBody
public class BaseSpAndUserController {

    @Autowired(required = false)
    IBaseSpService spService;

    @GetMapping("index")
    public String index() {
        return spService.testAddUserAndSp();
    }
}
