package com.cloudtravel.consumer.controller;

import com.cloudtravel.consumer.common.service.IBaseSpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
