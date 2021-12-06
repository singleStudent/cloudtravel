package com.cloudtravel.common.enums;


/**
 * Created by Administrator on 2021/7/18 0018.
 */
public enum  ResultStatusEnum {
    /**  ok */
    OK("00000" , "ok") ,

    NOT_FOUNT_BEAN("00001" , "Not fount bean of ");

    private String code;

    private String desc;

    ResultStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
