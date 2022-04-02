package com.cloudtravel.producer.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.cloudtravel.producer.common.service.IBaseUserService;
import com.cloudtravel.producer.common.model.BaseUserModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description :
 * @Author: Gosin
 * @Date: 2021/12/14 0014 16:28
 */
@Controller
@RequestMapping("user")
@ResponseBody
public class BaseUserController {

    @Reference
    IBaseUserService baseUserService;

    @RequestMapping(value = "insert" , method = RequestMethod.POST)
    public String insert(@RequestBody BaseUserModel userModel) {
        return baseUserService.insertUser(userModel);
    }
}