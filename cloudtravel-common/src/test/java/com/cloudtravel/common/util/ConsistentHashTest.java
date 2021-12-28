package com.cloudtravel.common.util;

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

}