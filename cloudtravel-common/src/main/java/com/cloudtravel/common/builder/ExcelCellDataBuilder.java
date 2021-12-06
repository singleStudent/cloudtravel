package com.cloudtravel.common.builder;


import com.cloudtravel.common.model.ExcelCellDataModel;

/**
 * Created by Administrator on 2021/5/28 0028.
 */
public class ExcelCellDataBuilder {

    public static ExcelCellDataModel build(){
        return new ExcelCellDataModel();
    }

    public static ExcelCellDataModel build(String cellValue){
        return new ExcelCellDataModel().setData(cellValue);
    }


    public static ExcelCellDataModel build(String cellValue , Integer rangeRowNum , Integer rangeColNum){
        return new ExcelCellDataModel().setData(cellValue).setRangeRowNum(rangeRowNum).setRangeColNum(rangeColNum);
    }

    public static ExcelCellDataModel build(String cellValue , Integer colIndex , Integer rangeRowNum , Integer rangeColNum){
        return new ExcelCellDataModel().setData(cellValue).setColIndex(colIndex).setRangeRowNum(rangeRowNum).setRangeColNum(rangeColNum);
    }
}
