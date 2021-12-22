package com.cloudtravel.common.util;

import com.alibaba.fastjson.JSON;
import com.cloudtravel.common.model.UserInfoModel;
import org.junit.Test;

import java.util.concurrent.*;

public class ConsistentHashTest {

    @Test
    public void addRealNodes()throws Throwable {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10 , 100 ,
                0 , TimeUnit.MILLISECONDS , new LinkedBlockingQueue<>(100));
        for (int i = 0 ; i < 10 ; i ++) {
            ExcelCallUtil excelCallUtil = new ExcelCallUtil("testParam"+i);
            Future<String> future = threadPoolExecutor.submit(excelCallUtil);
            System.out.println();
            if(i % 10 == 0){
                System.out.println(future.get());
            }
        }
        System.out.println("Main end");
    }

    @Test
    public void test_collection()throws Throwable {
        UserInfoModel userInfoModel1 = new UserInfoModel();
        userInfoModel1.setAge(10);
        UserInfoModel userInfoModel2 = new UserInfoModel();
        userInfoModel2.setAge(20);
        UserInfoModel userInfoModel3 = new UserInfoModel();
        userInfoModel3.setAge(30);

        ConcurrentHashMap<String , UserInfoModel> map1 = new ConcurrentHashMap<>();
        map1.put("test1" , userInfoModel1);

        ConcurrentHashMap<String ,UserInfoModel> map2 = new ConcurrentHashMap<>();
        map2.put("test3" , userInfoModel3);
        map2.put("test2" , userInfoModel2);
        map1.putAll(map2);
        System.out.println(JSON.toJSONString(map1));

        userInfoModel2.setAge(1000);

        System.out.println(JSON.toJSONString(map1));
    }
}