package com.cloudtravel.consumer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 */
@RestController
public class TestController {

    @GetMapping("test/index")
    public String index() {
        return "test";
    }
}
