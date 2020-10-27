package com.sj.android_base_utils.utils_number;

import java.text.DecimalFormat;

/**
 * Created by SJ on 2020/10/27.
 */
public class DoubleUtils {

    private static String maxLength = "0.00000";

    public static String getFormatDouble(double d){
        return getFormatDouble(d,0);
    }
    public static String getFormatDouble(double d,int decimalLength){
        //# 与 0 的区别：#：没有则为空 0：没有则补0
        //使用0.00不足位补0，#.##仅保留有效位
        String pattern = "#0.##";
        if (decimalLength!=0) {
            if (decimalLength<maxLength.length()-2){
                pattern= maxLength.substring(0,decimalLength+2);
            }else {
                pattern = maxLength;
            }
        }
        DecimalFormat formater = new DecimalFormat(pattern);
        System.out.println(formater.format(123456.7897456));
        return formater.format(d);
    }
}
