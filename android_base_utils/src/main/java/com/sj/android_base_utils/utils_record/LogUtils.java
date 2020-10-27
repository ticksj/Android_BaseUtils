package com.sj.android_base_utils.utils_record;

import android.util.Log;

/**
 * Created by SJ on 2020/8/17.
 */
public class LogUtils {
    private static final String TAG = "LogUtils";

    public static boolean isDebug = true;

    public static void e(String msg) {
        e(TAG, msg);
    }

    public static void v(String msg) {
       v(TAG, msg);
    }

    public static void d(String msg) {
        d(TAG, msg);
    }

    public static void i(String msg) {
        i(TAG, msg);
    }

    public static void w(String msg) {
        w(TAG, msg);
    }


    public static void e(String TAG, String msg) {
        if (isDebug) {
            Log.e(TAG, msg);
        }
    }

    public static void v(String TAG, String msg) {
        if (isDebug) {
            Log.v(TAG, msg);
        }
    }

    public static void d(String TAG, String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }

    public static void i(String TAG, String msg) {
        if (isDebug) {
            Log.i(TAG, msg);
        }
    }

    public static void w(String TAG, String msg) {
        if (isDebug) {
            Log.w(TAG, msg);
        }
    }
}
