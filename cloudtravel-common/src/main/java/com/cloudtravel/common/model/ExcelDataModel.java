package com.cloudtravel.common.model;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

/**
 * Created by Administrator on 2021/5/28 0028.
 */
@Getter
@Setter
public class ExcelDataModel {

    /** sheet名 */
    private String sheetName;

    /** 单元格内容 */
    private List<List<ExcelCellDataModel>> values;

    /** 版本code -  */
    private Integer workBookVersion;

    /** 标题所占的行数.默认为0-为设置列宽自适应准备 */
    private Integer titleRows = 0;
}
