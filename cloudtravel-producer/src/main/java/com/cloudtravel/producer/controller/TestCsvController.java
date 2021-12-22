package com.cloudtravel.producer.controller;

import com.cloudtravel.common.util.CsvUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description :
 * @Author: Gosin
 * @Date: 2021/12/14 0014 16:28
 */
@Controller
@RequestMapping("csv")
public class TestCsvController {

    @RequestMapping(value = "outPut" , method = RequestMethod.GET)
    public void testCsvOutPut(HttpServletRequest request ,
                              HttpServletResponse response)throws Throwable {
        List exportData = new ArrayList<Map>();
        Map row1 = new LinkedHashMap<String, String>();
        row1.put("1", "11");
        row1.put("2", "12");
        row1.put("3", "13");
        row1.put("4", "14");
        exportData.add(row1);
        row1 = new LinkedHashMap<String, String>();
        row1.put("1", "21");
        row1.put("2", "22");
        row1.put("3", "23");
        row1.put("4", "24");
        exportData.add(row1);
        LinkedHashMap map = new LinkedHashMap();

        //设置列名
        map.put("1", "第一列名称");
        map.put("2", "第二列名称");
        map.put("3", "第三列名称");
        map.put("4", "第四列名称");
        //这个文件上传到路径，可以配置在数据库从数据库读取，这样方便一些！
        String path = "E:/";
        CsvUtils.exportDataFile(response , exportData ,map , path , "testCsv");
    }
}