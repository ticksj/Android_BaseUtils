package com.sj.android_base_utils.utils_media.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

/**
 * Created by SJ on 2018/5/7.
 */

public class ToastManager {
    private static Toast mToast;
    private static ToastManager toastManager;

    public ToastManager() {

    }

    /**
     * 双重锁定，使用同一个Toast实例
     */
    public static ToastManager getInstance(Context context) {
        if (toastManager == null) {
            synchronized (ToastManager.class) {
                if (toastManager == null) {
                    toastManager = new ToastManager();
                    mToast = Toast.makeText(context,"",Toast.LENGTH_SHORT);
                }
            }
        }
        return toastManager;
    }

    public static void show(String msg){
        mToast.setText(msg);
        mToast.show();
    }

    public static void makeText(Context context, int xml_id) {
        View customView = LayoutInflater.from(context).inflate(xml_id,null);
//        ImageView img = (ImageView) customView.findViewById(R.id.iv);
//        TextView tv = (TextView) customView.findViewById(R.id.tv);
//        设置ImageView的图片
//        img.setBackgroundResource(R.drawable.ab);
//        设置textView中的文字
//        tv.setText("我是带图片的自定义位置的toast");
        //设置toast的View,Duration,Gravity最后显示
        mToast.setView(customView);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER,0,0);
        mToast.show();
    }

}
