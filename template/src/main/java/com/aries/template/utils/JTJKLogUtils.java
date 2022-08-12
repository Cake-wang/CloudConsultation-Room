package com.aries.template.utils;

import android.util.Log;

import com.aries.library.fast.util.ToastUtil;

/**
 * 日志系统
 * 全应用日志工程
 */
public class JTJKLogUtils {

    public static final String TAG = "JTJK";
    public static boolean isToast = true;


    /**
     * 日志的信息
     */
    public static void message(String tips){
        if (isToast)
        ToastUtil.show(tips);
        Log.d(TAG,tips);
    }

    /**
     * 日志的信息
     */
    public static void message(String tips,String logtips){
        if (isToast)
        ToastUtil.show(tips);
        Log.d(TAG,logtips+"::::"+tips);
    }
}
