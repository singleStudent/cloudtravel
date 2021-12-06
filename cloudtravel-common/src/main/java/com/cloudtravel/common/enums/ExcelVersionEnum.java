package com.cloudtravel.common.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by Administrator on 2021/5/28 0028.
 */
public enum ExcelVersionEnum {
    /** 2007年以前的 */
    HSSF(1 , false , "xls") ,

    /** 2007年以上的 */
    XSSF(2 , true , "xlsx");

    /** code  */
    private Integer version;

    /** 版本 */
    private Boolean isXssf;

    /** 文件名后缀 */
    private String fileSuffix;

    ExcelVersionEnum(Integer version, Boolean isXssf, String fileSuffix) {
        this.version = version;
        this.isXssf = isXssf;
        this.fileSuffix = fileSuffix;
    }

    /**
     * 获取所有版本号
     * @return
     */
    public static List<Integer> getAllVersionCode() {
        List<Integer> list = new ArrayList<>();
        Arrays.stream(ExcelVersionEnum.values()).forEach(excelVersionEnum -> list.add(excelVersionEnum.version));
        return list;
    }

    /**
     * 判断是否存在该版本号
     * @param version
     * @return
     */
    public static Boolean checkVersion(Integer version) {
        Optional<ExcelVersionEnum> optional = Arrays.stream(ExcelVersionEnum.values()).
                filter(excelVersionEnum -> excelVersionEnum.version.equals(version)).findAny();
        return optional.isPresent() ? optional.get().isXssf : null;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getXssf() {
        return isXssf;
    }

    public void setXssf(Boolean xssf) {
        isXssf = xssf;
    }

    public String getFileSuffix() {
        return fileSuffix;
    }

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }
}
