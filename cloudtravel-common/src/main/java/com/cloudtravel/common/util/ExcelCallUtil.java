package com.cloudtravel.common.util;

import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExcelCallUtil extends AbstractThreadCall{


    private String param;

    public ExcelCallUtil() {
        super();
    }

    public ExcelCallUtil(String param) {
        super();
        this.param = param;
    }

    /**
     * test2
     * @return
     */
    @Override
    public String execute() {
        try {
            this.logPrint(param + "开始");
            Thread.sleep(1000);
            this.logPrint(param + "结束");
        }catch (Throwable e) {
            this.logPrint("ExcelExecute Error , message = {}" , true , e.getMessage());
        }
        return param;
    }

    public static void main(String[] args)throws Throwable {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10 , 20 , 1,
                TimeUnit.MINUTES , new LinkedBlockingQueue<>());
        String s = null;
        for (int i= 0; i < 10 ; i ++) {
            ExcelCallUtil excelCallUtil = new ExcelCallUtil("test" + i);
            if(i % 3 == 0) {
                Future<String> stringFuture = threadPoolExecutor.submit(excelCallUtil);
                s = stringFuture.get();
                excelCallUtil.logPrint(s+"return");
            }else {
                threadPoolExecutor.submit(excelCallUtil);
            }
        }
        for (int m = 0 ; m < 10 ; m ++){
            Thread.sleep(1000);
            if(s.equals("test"+m)) {
                System.out.println("主线程m = " + m);
            }
            System.out.println(s);
        }
    }
}
