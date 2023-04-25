package com.cloudtravel.producer.controller;

import com.cloudtravel.common.redis.utils.RedisUtils;
import org.apache.commons.math3.util.MathUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * redis工具类测试
 * @author Administrator
 */
@RestController
public class RedisTestController {

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    RedissonClient redissonClient;

    @GetMapping("/redisTest/set")
    public void set() {
        redisUtils.set("testKey" , "隔离中......");
    }

    @GetMapping("/redisTest/get")
    public Object get() {
        return redisUtils.get("testKey");
    }

    @GetMapping("/redisTest/testLock/{count}")
    public String testLock(@PathVariable Integer count) {
        RLock lock = redissonClient.getLock("SKU-B");
        lock.tryLock();
        String msg = "";
        try {
            Thread.sleep(5000);
            Integer skuACount = (Integer)redisUtils.get("SKU-B-COUNT");
            msg += "当前商品库存="+skuACount +",本次欲购买商品数量="+count;
            if(null == skuACount){
                redisUtils.set("SKU-B-COUNT" ,100-count);
                msg += "当前商品未库存未加入缓存,初始化库存数=" + (100-count);
            }else if(skuACount > count) {
                redisUtils.decr("SKU-B-COUNT" , count);
                msg += ".库存扣减成功!";
            }else {
                msg += "当前商品库存不足" +count+ ",无法完成下单!";
            }
            System.out.println();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        System.out.println(msg);
        return msg;
    }
}
