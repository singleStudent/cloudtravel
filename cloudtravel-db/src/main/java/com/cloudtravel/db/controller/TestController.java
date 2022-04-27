package com.cloudtravel.db.controller;

import com.cloudtravel.db.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("dbTest")
public class TestController {

    @Autowired
    ITestService testService;

    @GetMapping("index")
    public String test() {
        testService.test();
        return "1";
    }
}
