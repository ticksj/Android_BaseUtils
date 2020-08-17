package com.sj.android_base_utils.utils_log;

import android.util.Log;

/**
 * Created by SJ on 2020/8/17.
 */
public class LogUtils {
    private static final String TAG = "LogUtils";

    public static void e(String Tag,String msg){
        Log.e(Tag, msg );
    }
    public static void e(String msg){
        e(TAG,msg);
    }
}
