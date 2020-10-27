package com.sj.android_base_utils.utils_system;

import android.app.Application;
import android.content.pm.ApplicationInfo;

/**
 * Created by SJ on 2020/10/27.
 */
public class AndroidSystemUtils {
    /**
     * 判断当前App版本TargetVersion
     */
    public static int getCurrentTargetVersion(Application app){
        return app.getApplicationInfo().targetSdkVersion;
    }
    /**
     * 判断当前设备版本
     */
    public static int getCurrentSDKVersion(){
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 判断当前应用是否是debug状态
     */
    public static boolean isApkInDebug(Application app) {
        try {
            ApplicationInfo info = app.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }




}
