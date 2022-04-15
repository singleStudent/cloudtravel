package com.cloudtravel.common.util;

import org.junit.Test;
import java.util.*;
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

    public static void main(String[] args) {
        Queue<Integer> arr = new LinkedBlockingQueue<>();
        arr.add(1);
        arr.add(2);
        arr.add(3);
        arr.add(4);
        System.out.println("poll()" + arr.poll());//返回并删除头
        System.out.println("peek()" + arr.peek());//只返回头
        System.out.println("offer()" + arr.offer(2));//尾部插入
        System.out.println("element()" + arr.element());//返回头,为空抛异常
        System.out.println(arr);
    }
}