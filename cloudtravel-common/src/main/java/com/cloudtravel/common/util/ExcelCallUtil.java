package com.cloudtravel.common.util;

public class ExcelCallUtil extends AbstractThreadCall{


    private String param;

    public ExcelCallUtil() {
        super();
    }

    public ExcelCallUtil(String param) {
        super();
        this.param = param;
    }

    @Override
    public String execute() {
        try {
            System.out.println(param + "开始");
            System.out.println(param + "结束");
            System.out.println();
        }catch (Throwable e) {
            this.logPrint("ExcelExecute Error , message = {}" , true , e.getMessage());
        }
        return "param = " + param;
    }
}
