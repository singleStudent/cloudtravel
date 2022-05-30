package com.cloudtravel.common.ms;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 题目1：现在，有一行括号序列，请你检查这行括号是否配对。输入数据都是一个字符串S(S的长度小于10000，且S不是空串），
 * 数据保证S中只含有"[","]","(",")"四种字符。如果匹配，则输出YES，否则NO
 * @author lsl
 */
public class Question1 {

    private static final Logger LOGGER = LoggerFactory.getLogger(Question1.class);

    private static final List<String> MATCH_CODE_ARR = Arrays.asList("[","]","(",")");


    private static final int PARAM_LENGTH_MAX = 10000;

    /**
     * 括号匹配也就是"[".size==="]".size && "(".size === ")".size]
     * @param param
     * @return
     */
    public static String check1(String param) {
        boolean flag = true;
        if(StringUtils.isNotBlank(param) && param.length() < PARAM_LENGTH_MAX) {
            String temp = null;
            Map<String , Integer> map = new HashMap<>();
            for (int i = 0; i < param.length() ; i ++) {
                temp = String.valueOf(param.charAt(i));
                if(!MATCH_CODE_ARR.contains(temp)) {
                    LOGGER.error("Param = {} contains illegal char {}" , param , temp);
                    flag = false;
                    break;
                }else {
                    if(map.containsKey(temp)) {
                        map.put(temp , map.get(temp)+1);
                    }else {
                        map.put(temp , 1);
                    }
                }
            }
            if(map.containsKey(MATCH_CODE_ARR.get(0)) && map.containsKey(MATCH_CODE_ARR.get(1))) {
                flag = map.get(MATCH_CODE_ARR.get(0)).equals(map.get(MATCH_CODE_ARR.get(1)));
            }
            if(flag && map.containsKey(MATCH_CODE_ARR.get(2)) && map.containsKey(MATCH_CODE_ARR.get(3))) {
                flag = map.get(MATCH_CODE_ARR.get(2)).equals(map.get(MATCH_CODE_ARR.get(3)));
            }
        }else {
            LOGGER.error("Param = {} is illegal!" , param);
        }
        return flag ? "YES" : "NO";
    }

    public static void main(String[] args) {
        System.out.println(check1("[[]()]"));
    }
}
