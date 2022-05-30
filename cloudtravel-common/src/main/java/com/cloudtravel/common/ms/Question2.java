package com.cloudtravel.common.ms;

import java.util.Arrays;
import java.util.List;

/**
 * 题目2：
 * 设x, y, z, w 为平面上的4个点， 试写一段程序判断x y z w是否构成一个正方形。
 * @author lsl
 */
public class Question2 {

    /**
     * 点位的坐标类
     */
    public static class Point{

        /** 点在平面中x轴坐标的值 */
        private Double xVal;

        /** 点在平面中y轴坐标的值 */
        private Double yVal;

        public Point(Double xVal, Double yVal) {
            this.xVal = xVal;
            this.yVal = yVal;
        }

        public Point() {
        }

        public Double getxVal() {
            return xVal;
        }

        public void setxVal(Double xVal) {
            this.xVal = xVal;
        }

        public Double getyVal() {
            return yVal;
        }

        public void setyVal(Double yVal) {
            this.yVal = yVal;
        }
    }

    /**
     * 校验几个点位是否构成正方形
     * 一个点到任意点之间的距离和其他两个点的距离相同
     * 4个参数对应4个点位 . 0,1 == 2,3   0,2 == 1,3 , 0,3 == 1,2
     * x-y [xValDiff , yValDiff] 数组间同下标相同或者对称相同 z-w[xValDiff , yValDiff]
     * 1. x-y == z-w
     * 2. x-z == y-w
     * 3. x-w == y-z
     * @param params
     * @return
     */
    public static Boolean checkParam(List<Point> params) {
        Boolean check1 = checkArr(convertPointValDiff(params.get(0) , params.get(1)) , convertPointValDiff(params.get(2) , params.get(3)));
        if(check1) {
            Boolean check2 = checkArr(convertPointValDiff(params.get(0) , params.get(2)) , convertPointValDiff(params.get(1) , params.get(3)));
            if(check2) {
                return checkArr(convertPointValDiff(params.get(0) , params.get(3)) , convertPointValDiff(params.get(1) , params.get(2)));
            }
        }
        return false;
    }



    /**
     * 计算两个点位之间横纵坐标各自的距离差值[取绝对值]
     * @param point1
     * @param point2
     * @return
     */
    private static Double[] convertPointValDiff(Point point1 , Point point2) {
        Double [] pointValDiffArr = new Double[2];
        pointValDiffArr[0] = Math.abs(point1.getxVal() - point2.getxVal());
        pointValDiffArr[1] = Math.abs(point1.getyVal() - point2.getyVal());
        return pointValDiffArr;
    }

    /**
     * 对比两个差值数组是否相同
     * @param arr1     * @param arr2
     * @return
     */
    private static Boolean checkArr(Double[] arr1 , Double[] arr2) {
        return (arr1[0].equals(arr2[0]) && arr1[1].equals(arr2[1])) || (arr1[0].equals(arr2[1]) && arr1[1].equals(arr2[0]));
    }

    public static void main(String[] args) {
        List<Point> params = Arrays.asList(new Point(1d , 1d) , new Point(-1d , 0d) ,
                new Point(-2d , 2d) ,  new Point(0d , 3d));
        System.out.println(checkParam(params));
    }
}
