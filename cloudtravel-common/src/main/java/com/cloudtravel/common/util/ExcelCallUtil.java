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
}
