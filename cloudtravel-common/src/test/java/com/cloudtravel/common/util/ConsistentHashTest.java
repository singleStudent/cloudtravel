package com.cloudtravel.common.util;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.nio.charset.Charset;
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

    @Test
    public void testQueue() {
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

    /**
     * 基础的布隆过滤器使用
     */
    @Test
    public void test_BloomFilter() {
        Long start = System.currentTimeMillis();
        BloomFilter<CharSequence> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()) ,500000 , Double.valueOf("0.03"));
        for(int i = 0 ; i < 5000 ; i ++) {
            String uuidStr = UUID.randomUUID().toString();
            bloomFilter.put(uuidStr);
        }
        System.out.println("500W条数据缓存完毕,此次缓存500w条数据共耗时 = " + (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        int mightContains = 0;
        for(int i = 0 ; i < 5000 ; i ++) {
            String uuidStr = UUID.randomUUID().toString();
            if(bloomFilter.mightContain(uuidStr)) {
                mightContains ++;
            }
        }
        System.out.println("布隆过滤器检查完毕,500w条数据检索共耗时: " + (System.currentTimeMillis() - start) + ";检查可能存在的数据条数 = " + mightContains);
        System.out.println(String.valueOf(bloomFilter.expectedFpp()));
    }


    @Test
    public void test_redisBloom() {
        String msg = "";
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        RedissonClient redissonClient = Redisson.create(config);
        RBloomFilter<String> rBloomFilter = redissonClient.getBloomFilter("testBloom2");
        rBloomFilter.tryInit(5000000L , 0.01);
        Long start = System.currentTimeMillis();
        List<String> list = new ArrayList<>(500000);
        for(int i = 0 ; i < 500000; i ++) {
            String uuIdStr = UUID.randomUUID().toString();
            rBloomFilter.add(uuIdStr);
            list.add(uuIdStr);
        }
        msg += "redisson之布隆过滤器加载50w条数据完毕,总耗时:" + (System.currentTimeMillis() - start);
        start = System.currentTimeMillis();
        int mightContains = 0;
        for(int i = 0 ; i < 10000 ; i ++){
            String uuIdStr = UUID.randomUUID().toString();
            Boolean containFlag = rBloomFilter.contains(list.get(i));
            if(containFlag){
                mightContains ++;
            }
        }
        System.out.println(msg + "布隆过滤器检查完毕,1w条数据检索共耗时: " + (System.currentTimeMillis() - start) + ";检查可能存在的数据条数 = " + mightContains);

    }

}