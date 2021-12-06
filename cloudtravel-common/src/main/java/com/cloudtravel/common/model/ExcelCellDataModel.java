package com.cloudtravel.common.model;

import lombok.Getter;

import java.io.Serializable;

/**
 * Created by Administrator on 2021/5/28 0028.
 */
@Getter
public class ExcelCellDataModel implements Serializable {

    private static final long serialVersionUID = -9220349697728153457L;

    /**
     * 单元格的value值
     */
    private String data;

    /**
     * 元素所在行的第几列.默认为-1 . 为避免如下情况而设置 :
     * 若一行7列 , 只有三个元素 , 而前四个为空值 , 此处可直接声明colIndex = 4;
     * 循环创建该行cell时 , 会先判断当前单元格下表是否与当前设置的下标相同, 相同才取value值设置进去
     * 若为-1 . 则默认该行元素为从0开始满7个.不做特殊处理
     */
    private Integer colIndex = -1;

    /**
     * 该单元格要合并的行数 .default = 1
     */
    private Integer rangeRowNum = 1;

    /**
     * 该单元格要合并的列数 default =1
     */
    private Integer rangeColNum = 1;

    /**
     * 是否为标题
     */
    private Boolean titleCell = false;

    /**
     * 列宽自适应时:从哪一行开始--为避免前几行作为其他标识
     */
    private Integer initColWidthStartRowIndex = 0;

    /**
     * 单元格批注
     */
    private String remark;

    public ExcelCellDataModel setData(String data) {
        this.data = data;
        return this;
    }

    public ExcelCellDataModel setColIndex(Integer colIndex) {
        this.colIndex = colIndex;
        return this;
    }

    public ExcelCellDataModel setRangeRowNum(Integer rangeRowNum) {
        this.rangeRowNum = rangeRowNum;
        return this;
    }

    public ExcelCellDataModel setRangeColNum(Integer rangeColNum) {
        this.rangeColNum = rangeColNum;
        return this;
    }

    public ExcelCellDataModel setTitleCell(Boolean titleCell) {
        this.titleCell = titleCell;
        return this;
    }


    public ExcelCellDataModel setInitColWidthStartRowIndex(Integer initColWidthStartRowIndex) {
        this.initColWidthStartRowIndex = initColWidthStartRowIndex;
        return this;
    }


    public ExcelCellDataModel setRemark(String remark) {
        this.remark = remark;
        return this;
    }
}
