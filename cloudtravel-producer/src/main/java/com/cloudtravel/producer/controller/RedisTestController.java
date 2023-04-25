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

    private static volatile Integer TOTAL_SAILED = 0;


    @GetMapping("/redisTest/set")
    public void set() {
        redisUtils.set("SKU-C-COUNT" , 100);
        TOTAL_SAILED = 0;
        System.out.println("设置成功");
    }

    @GetMapping("/redisTest/get")
    public Object get() {
        return redisUtils.get("testKey");
    }


    @GetMapping("/redisTest/testLock")
    public String testLock() throws Exception{
        int count = (int)(Math.random() * 10 + 1);
        RLock lock = redissonClient.getLock("SKU-C");
        lock.tryLock(5 , 10 , TimeUnit.SECONDS);
        String msg = "";
        try {
            Integer skuACount = (Integer)redisUtils.get("SKU-C-COUNT");
            msg += "当前商品库存="+skuACount +",本次欲购买商品数量="+count;
            if(null == skuACount){
                redisUtils.set("SKU-C-COUNT" ,100-count);
                msg += "当前商品未库存未加入缓存,初始化库存数=" + (100-count);
                TOTAL_SAILED += count;
            }else if(skuACount == 0 ) {
                msg += "当前库存为0,待补货";
            } else if(skuACount >= count) {
                redisUtils.decr("SKU-C-COUNT" , count);
                msg += ".库存扣减成功!";
                TOTAL_SAILED += count;
            }else {
                msg += "当前商品库存不足" +count+ ",无法完成下单!";
            }
            msg += "截止目前,共售出商品数=" + TOTAL_SAILED;
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
