package com.cloudtravel.consumer.controller;

import com.cloudtravel.common.redis.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * redis工具类测试
 * @author Administrator
 */
@RestController
public class RedisTestController {

    @Autowired
    RedisUtils redisUtils;

    @GetMapping("/redisTest/set")
    public void set() {
        redisUtils.set("testKey" , "隔离中......");
    }

    @GetMapping("/redisTest/get")
    public Object get() {
        return redisUtils.get("testKey");
    }


}
