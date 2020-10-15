package com.sj.android_base_utils.utils_ui;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SJ on 2020/10/9.
 */
public class ColorUtils {
    /**
     * 获取随机颜色
     * @return
     */
    public static MyColor getRandomColor(){
        MyColor[] colors = MyColor.values();
        int index = (int) (Math.random() * colors.length);
        if (index>colors.length-1) {
            return MyColor.WHITE;
        }
        return colors[index];
    }
    /**
     * 获取所有颜色 Hex值
     * @return
     */
    public static List<String> getColors() {
        List<String> colors = new ArrayList<>();
        for (MyColor color : MyColor.values()) {
            colors.add(color.getHex());
        }
        return colors;
    }
    /**
     * 获取指定大小随机颜色组 Hex值
     * @return
     */
    public static List<String> getColors(int length) {
        if (length> MyColor.values().length) {
            return getColors();
        }
        List<String> colors = new ArrayList<>();
        List<MyColor> myColors = new ArrayList<>();
        for (MyColor myColor : MyColor.values()) {
            myColors.add(myColor);
        }
        for (int i = 0; i < length; i++) {
            MyColor myColor = myColors.get((int) (Math.random() * myColors.size()));
            myColors.remove(myColor);
            colors.add(myColor.getHex());
        }
        return colors;
    }
}
