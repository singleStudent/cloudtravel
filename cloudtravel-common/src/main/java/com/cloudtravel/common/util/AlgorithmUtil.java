package com.cloudtravel.common.util;


import org.apache.commons.jexl3.*;
import org.apache.commons.jexl3.internal.Engine;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.beans.Expression;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 计算算法工具
 */
public class AlgorithmUtil {

    public static void test() throws Exception {

        JexlEngine engine = new Engine();

        JexlContext context = new MapContext();
        String expStr = "array.size()";

        List<Object> arr = new ArrayList<>();
        arr.add("this is an array");
        arr.add(new Integer(2));

        context.set("array",arr);

        JexlExpression expression = engine.createExpression(expStr);
        //两种实现方式
        Object evaluate = expression.evaluate(context);
        System.out.println(evaluate);

        Callable<Object> callable = expression.callable(context);
        Object call = callable.call();
        System.out.println(call);
    }

    public static void jexlScript() {

        JexlEngine engine = new Engine();

        JexlContext context = new MapContext();

        String expStr = "if(a>b){c=a;}else{c=b;}";

        context.set("a", 1);
        context.set("b", 2);
        context.set("c", 0);

        JexlScript script = engine.createScript(expStr);
        Object execute = script.execute(context);
        String s = execute.toString();

        expStr = "while(a<b){a=a+b;}";
        JexlScript script1  = engine.createScript(expStr);
        Object execute1 = script1.execute(context);
        String s1 = execute1.toString();


    }

    /**
     * jdk的ScriptEngine引擎执行公式计算
     * @param jexExp 公式 . 如 (a+b-c)*d/f
     * @param reflectParamMap 参数集合
     * @param paramScope 参数范围
     * @return
     */
    public static String calWithScriptEngine(String jexExp ,
                                           Map<String , Object> reflectParamMap ,
                                           Integer paramScope) throws Throwable{
        ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("js");
        Bindings bindings = scriptEngine.createBindings();
        bindings.putAll(reflectParamMap);
        paramScope = paramScope != null ? paramScope : ScriptContext.ENGINE_SCOPE;
        scriptEngine.setBindings(bindings , paramScope);
        //出参默认为double类型的 . 入参类型对结果精度无影响
        Object result = scriptEngine.eval(jexExp);
        //保留两位小数
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(result);
    }

    /**
     * jexlEngine引擎执行公式计算
     * @param jexExp 公式 . 如 (a+b-c)*d/f
     * @param reflectParamMap 参数集合
     * @return 返回结果默认String
     * @throws Throwable
     */
    public static String calWithJexlEngine(String jexExp ,
                                           Map<String , Object> reflectParamMap) throws Throwable{
        JexlEngine engine = new Engine();
        JexlExpression expression = engine.createExpression(jexExp);
        JexlContext jc = new MapContext();

        reflectParamMap.keySet().forEach(k->{
            //jexl计算结果类型根据入参决定:
            //如果参数都是int/Integer类型 . 那么即便结果值应该是4.8 . 返回值也是4 .所以这里为保证计算精度 . 入参可全部以decimal类型处理下
            //若参数中存在小数,则结果精度相对准确 . 出参为double类型
            Object paramVal = reflectParamMap.get(k);
            String paramValStr = paramVal != null ? String.valueOf(paramVal) : "0";
            jc.set(k , new BigDecimal(paramValStr));
        });
        Object result =expression.evaluate(jc);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(result);
    }

    public static void main(String[] args)throws Throwable {
        String jexlExpression = "(a+b-c)*d/f";
        HashMap<String , Object> param = new HashMap<>();
        param.put("a" , 10);
        param.put("b" , 5);
        param.put("c" , 3);
        param.put("d" , 2);
        param.put("f" , 8);
        System.out.println("jexlEngine计算 =" + calWithJexlEngine(jexlExpression , param));

        System.out.println("jdkEngine计算  =" + calWithScriptEngine(jexlExpression , param , ScriptContext.ENGINE_SCOPE));
    }
}
