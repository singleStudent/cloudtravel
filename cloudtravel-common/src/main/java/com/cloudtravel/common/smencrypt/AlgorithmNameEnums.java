package com.cloudtravel.common.smencrypt;

/**
 * @description: 加密算法名枚举
 * @author: walker
 * @DATE: 2022/9/4
 */
public enum AlgorithmNameEnums {

    SM_4("SM4" , "SM4加密") ,

    SM_3("SM3" , "SM3加密");

    private String name;

    private String desc;

    AlgorithmNameEnums(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
