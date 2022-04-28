package com.cloudtravel.db.controller;

import com.cloudtravel.db.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("dbTest")
@ResponseBody
public class TestController {

    @Autowired
    ITestService testService;

    @GetMapping("index")
    public String test() {
        testService.test();
        return "1";
    }
}
