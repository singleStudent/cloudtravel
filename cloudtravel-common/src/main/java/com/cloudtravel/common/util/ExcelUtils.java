package com.cloudtravel.common.util;


import com.cloudtravel.common.builder.ExcelCellDataBuilder;
import com.cloudtravel.common.model.ExcelCellDataModel;
import com.cloudtravel.common.model.ExcelDataModel;
import com.cloudtravel.common.model.ExcelInitException;
import com.cloudtravel.common.enums.ExcelVersionEnum;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * excel工具类--支持灵活合并各行&列
 */
public class ExcelUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtils.class);

    private static final String PARAM_ERROR = "PARAM ERROR: ";

    private static final Short DEFAULT_FONT_SIZE = 11;


    /**
     * 根据封装好的数据对象生成对应的workBook对象
     *
     * @param dataModel
     * @return
     */
    public static Workbook createAndWriteToBook(ExcelDataModel dataModel) {
        try {
            checkModel(dataModel);
            Workbook workbook = initWorkbook(dataModel.getWorkBookVersion());
            Sheet sheet = workbook.createSheet(dataModel.getSheetName());
            List<List<ExcelCellDataModel>> dataArray = dataModel.getValues();
            //初始化单元格样式
            CellStyle cellStyle = initCellStyle(workbook);
            //渲染数据
            writeToWorkbook(sheet, cellStyle, dataArray, dataModel.getWorkBookVersion());
            //检查合并
            rangeCell(workbook, sheet, dataArray);
            //列宽自适应
            initColWidth(sheet, dataArray, dataModel.getTitleRows());
            return workbook;
        } catch (Exception e) {
            throw new ExcelInitException(e.getMessage());
        }
    }

    /**
     * 数据写入
     *
     * @param sheet
     * @param cellStyle
     * @param dataMap
     */
    private static void writeToWorkbook(Sheet sheet,
                                        CellStyle cellStyle,
                                        List<List<ExcelCellDataModel>> dataMap,
                                        Integer excelVersion) {
        Integer colSize = getMaxColIndex(dataMap);
        Integer currentRowIndex = 0;
        for (List<ExcelCellDataModel> dataModels : dataMap) {
            Row row = sheet.createRow(currentRowIndex);
            for (int m = 0; m < colSize; m++) {
                //此处创建最大数量的单元格 . 以便后续执行合并处理
                Cell cell = row.createCell(m);
                cell.setCellStyle(cellStyle);
                cell.setCellType(CellType.STRING);
                ExcelCellDataModel dataModel = getCell(dataModels, m);
                if (null != dataModel) {
                    cell.setCellValue(StringUtils.isEmpty(dataModel.getData()) ? "" : dataModel.getData());
                    if (!StringUtils.isEmpty(dataModel.getRemark())) {
                        addRemarkToCell(cell, dataModel.getRemark(), excelVersion);
                    }
                } else {
                    cell.setCellValue("");
                }
            }
            currentRowIndex++;
        }
    }

    /**
     * 给单元格增加备注
     *
     * @param cell         单元格对象
     * @param value        备注信息
     * @param excelVersion 生成的excel版本
     */
    private static void addRemarkToCell(Cell cell, String value, Integer excelVersion) {
        Sheet sheet = cell.getSheet();
        cell.removeCellComment();
        ClientAnchor anchor = null;
        RichTextString richTextString = null;
        if (excelVersion.equals(ExcelVersionEnum.XSSF.getVersion())) {
            //isXssf == xlsx xssf
            anchor = new XSSFClientAnchor();
            richTextString = new XSSFRichTextString(value);
        } else {
            // xls ==> hssf
            anchor = new HSSFClientAnchor();
            richTextString = new HSSFRichTextString(value);
        }
        anchor.setDx1(0);
        anchor.setDx2(0);
        anchor.setDy1(0);
        anchor.setDy2(0);
        anchor.setCol1(cell.getColumnIndex());
        anchor.setRow1(cell.getRowIndex());
        anchor.setCol2(cell.getColumnIndex() + 2);
        anchor.setRow2(cell.getRowIndex() + 2);
        Drawing drawing = sheet.createDrawingPatriarch();
        Comment comment = drawing.createCellComment(anchor);
        comment.setString(richTextString);
        cell.setCellComment(comment);
    }

    /**
     * 取单元格对象
     *
     * @param list
     * @param m
     * @return
     */
    private static ExcelCellDataModel getCell(List<ExcelCellDataModel> list, Integer m) {
        Optional<ExcelCellDataModel> excelCellDataModel = list.stream().
                filter(model -> model.getColIndex().equals(m)).findFirst();
        if (excelCellDataModel.isPresent()) {
            return excelCellDataModel.get();
        }
        ExcelCellDataModel model = m < list.size() ? list.get(m) : null;
        return null != model && (model.getColIndex() == -1 || m.equals(model.getColIndex())) ? model : null;
    }

    /**
     * 初始化样式
     *
     * @param workbook
     * @return
     */
    private static CellStyle initCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        //设置边框  下-左-右-上
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        //填充颜色
        cellStyle.setFillBackgroundColor(IndexedColors.AQUA.index);
        cellStyle.setFillPattern(FillPatternType.NO_FILL);
        //设置字体:默认宋体
        Font font = workbook.createFont();
        font.setFontName("宋体");
        //字号:默认11
        font.setFontHeightInPoints(DEFAULT_FONT_SIZE);
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return cellStyle;
    }

    /**
     * 单元格合并
     *
     * @param workbook
     * @param sheet
     * @param data
     */
    private static void rangeCell(Workbook workbook, Sheet sheet, List<List<ExcelCellDataModel>> data) {
        Integer rowSize = data.size();
        Integer colSize = getMaxColIndex(data);
        Integer currentRowIndex = 0;
        for (List<ExcelCellDataModel> dataModels : data) {
            Integer currentColIndex = 0;
            for (ExcelCellDataModel dataModel : dataModels) {
                Integer rangeRowNum = dataModel.getRangeRowNum();
                Integer rangeColNum = dataModel.getRangeColNum();
                if (rangeRowNum > 1 || rangeColNum > 1) {
                    Integer rangeRowEndIndex = currentRowIndex + rangeRowNum - 1;
                    Integer rangeColEndIndex = currentColIndex + rangeColNum - 1;
                    Assert.state(rangeRowEndIndex < rowSize, "This Row range out of max rowIndex");
                    Assert.state(rangeColEndIndex < colSize, "This col range out of max colIndex");
                    CellRangeAddress cas = new CellRangeAddress(currentRowIndex, rangeRowEndIndex,
                            currentColIndex, rangeColEndIndex);
                    sheet.addMergedRegion(cas);
                    RegionUtil.setBorderTop(BorderStyle.THIN, cas, sheet);
                    RegionUtil.setBorderBottom(BorderStyle.THIN, cas, sheet);
                    RegionUtil.setBorderLeft(BorderStyle.THIN, cas, sheet);
                    RegionUtil.setBorderRight(BorderStyle.THIN, cas, sheet);
                }
                currentColIndex++;
            }
            currentRowIndex++;
        }
    }

    /**
     * 列宽自适应
     *
     * @param sheet     sheet
     * @param dataMap   数据集
     * @param titleRows 标题占用了几行 . 因为有些标题较长 , 后续单元格无法参照标题自适应,不然会出现过宽的情况
     *                  titleRows = 2 , 标识占用了两行 , 即从第三行也就是RowIndex = 2开始
     */
    private static void initColWidth(Sheet sheet, List<List<ExcelCellDataModel>> dataMap, Integer titleRows) {
        Integer startRowIndex = titleRows;
        Integer maxColSize = getMaxColIndex(dataMap);
        for (int cowIndex = 0; cowIndex < maxColSize; cowIndex++) {
            Cell cell = null;
            Integer width = 0;
            for (int m = startRowIndex; m < sheet.getPhysicalNumberOfRows(); m++) {
                cell = sheet.getRow(m).getCell(cowIndex);
                String data = StringUtils.isEmpty(cell.getStringCellValue()) ? "" : cell.getStringCellValue();
                width = Math.max(width, data.getBytes(StandardCharsets.UTF_8).length * 256);
            }
            width = Math.min(65280, width);
            sheet.setColumnWidth(cowIndex, width);
        }
    }

    /**
     * 获取最长需要渲染多少个单元格
     *
     * @param data
     * @return
     */
    private static Integer getMaxColIndex(List<List<ExcelCellDataModel>> data) {
        Integer maxSize = data.stream().
                mapToInt(List::size).max().orElse(0);
        Integer maxSize2 = 0;
        for (List<ExcelCellDataModel> cellDataModels : data) {
            Integer m = cellDataModels.stream().max(Comparator.comparing(ExcelCellDataModel::getColIndex)).get().getColIndex();
            maxSize2 = Math.max(maxSize2, m);
        }
        return Math.max(maxSize, maxSize2);
    }


    /**
     * 参数校验
     *
     * @param model
     * @return
     */
    private static Boolean checkModel(ExcelDataModel model) {
        Assert.notNull(model, PARAM_ERROR + "ExcelDataModel is null");
        //验证sheetName格式
        WorkbookUtil.validateSheetName(model.getSheetName());
        Assert.notEmpty(model.getValues(), PARAM_ERROR + "无导出数据");
        return true;
    }

    private static Workbook initWorkbook(Integer version) {
        Boolean isXssf = ExcelVersionEnum.checkVersion(version);
        Assert.notNull(isXssf, "当前版本不支持version=" + version);
        return isXssf ? new XSSFWorkbook() : new HSSFWorkbook();
    }


    /**
     * 将excel文件输出到某个地址
     *
     * @param workbook
     * @param filePath
     * @param fileName
     */
    public static void writeWorkBookToFile(Workbook workbook, String filePath, String fileName) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePath + "/" + fileName);
            workbook.write(out);
        } catch (IOException e) {
            LOGGER.equals("write error , message = " + e.getMessage());
        } finally {
            if (null != out) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    LOGGER.error("Flush || Close FileOutPutStream Error , message = " + e.getMessage());
                }
            }
        }
    }


    public static void main(String[] args) {
        List<List<ExcelCellDataModel>> dataArray = new ArrayList<>();
        //第一行 : 标题行
        dataArray.add(Arrays.asList(ExcelCellDataBuilder.build("2021年5月学习计划表", 1, 5)));
        dataArray.add(Arrays.asList(ExcelCellDataBuilder.build("日期").setRemark("2021/07/09"),
                ExcelCellDataBuilder.build("源码解析").setRemark("必填"),
                ExcelCellDataBuilder.build("算法学习").setRemark("必填"),
                ExcelCellDataBuilder.build("框架学习").setRemark("必填"),
                ExcelCellDataBuilder.build("总计时长")));

        dataArray.add(Arrays.asList(ExcelCellDataBuilder.build("2021/05/01"),
                ExcelCellDataBuilder.build("spring源码之循环依赖"),
                ExcelCellDataBuilder.build("算法学习之hashMap解析"),
                ExcelCellDataBuilder.build("zookeeper进阶学习"),
                ExcelCellDataBuilder.build("4小时")));
        ExcelDataModel model1 = new ExcelDataModel();
        model1.setSheetName("2021年5月学习计划表");
        model1.setTitleRows(1);
        model1.setValues(dataArray);
        model1.setWorkBookVersion(ExcelVersionEnum.HSSF.getVersion());
        ExcelUtils.writeWorkBookToFile(ExcelUtils.createAndWriteToBook(model1), "d:/testexcel", "test1.xls");

        List<List<ExcelCellDataModel>> dataArray2 = new ArrayList<>();
        //第一行 : 标题行
        dataArray2.add(Arrays.asList(ExcelCellDataBuilder.build("2021年会考成绩表", 1, 5)));

        dataArray2.add(Arrays.asList(ExcelCellDataBuilder.build("姓名"),
                ExcelCellDataBuilder.build("科目").setRemark("说明"),
                ExcelCellDataBuilder.build("第一学期"),
                ExcelCellDataBuilder.build("第二学期"),
                ExcelCellDataBuilder.build("平均")));

        dataArray2.add(Arrays.asList(ExcelCellDataBuilder.build("小明", 2, 1),
                ExcelCellDataBuilder.build("语文"),
                ExcelCellDataBuilder.build("100"),
                ExcelCellDataBuilder.build("80"),
                ExcelCellDataBuilder.build("90")));

        dataArray2.add(Arrays.asList(ExcelCellDataBuilder.build(),
                ExcelCellDataBuilder.build("数学"),
                ExcelCellDataBuilder.build("150"),
                ExcelCellDataBuilder.build("130"),
                ExcelCellDataBuilder.build("140")));

        dataArray2.add(Arrays.asList(ExcelCellDataBuilder.build("总计", 1, 2),
                ExcelCellDataBuilder.build("250", 2, 1, 1),
                ExcelCellDataBuilder.build("210", 3, 1, 1)));

        dataArray2.add(Arrays.asList(ExcelCellDataBuilder.build("点评"),
                ExcelCellDataBuilder.build("努力", 1, 4)));
        ExcelDataModel model2 = new ExcelDataModel();
        model2.setSheetName("2021年会考成绩表");
        model2.setTitleRows(1);
        model2.setValues(dataArray2);
        model2.setWorkBookVersion(ExcelVersionEnum.HSSF.getVersion());
        ExcelUtils.writeWorkBookToFile(ExcelUtils.createAndWriteToBook(model2), "d:/testexcel", "test2.xls");
    }
}
