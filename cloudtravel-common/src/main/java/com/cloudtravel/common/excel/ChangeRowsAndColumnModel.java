package com.cloudtravel.common.excel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeRowsAndColumnModel {

    /** 从第几行开始进行行列翻转,默认为0,即第一行 */
    private Integer startRowIndex = 0;

    /** 从第几列开始进行翻转,默认为0即第一列 */
    private Integer startColIndex = 0;

    /** 文件全路径 */
    private String fullFilePath;

    /** 输出路径 */
    private String outPutPath;



}
