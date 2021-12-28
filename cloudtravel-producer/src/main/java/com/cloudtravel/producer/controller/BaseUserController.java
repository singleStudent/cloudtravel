package com.cloudtravel.producer.controller;


import com.cloudtravel.common.producer.IBaseUserService;
import com.cloudtravel.common.producer.model.BaseUserModel;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    IBaseUserService baseUserService;

    @RequestMapping(value = "insert" , method = RequestMethod.POST)
    public String insert(@RequestBody BaseUserModel userModel) {
        return baseUserService.insertUser(userModel);
    }
}