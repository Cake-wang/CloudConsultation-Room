package com.aries.template.thridapp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;

import com.aries.template.GlobalConfig;
import com.aries.template.xiaoyu.dapinsocket.DapinSocketProxy;

/**
 * 金投健康第三方管理器
 * 管理身体检测第三方跳转器
 */
public class JTJKThirdAppUtil {

    private static boolean isOpenedBodyTesting; // 是否启动过第三方跳转

    /**
     * 跳转到第三方
     * 案例
     * new ComponentName("com.garea.launcher", "com.garea.launcher.login.LauncherLogin");
     */
    public void gotoBodyTesting(Activity activity, String pck, String opCls, String userName, String idCard, String mobile){
        // 大屏的通信代理
        DapinSocketProxy.with()
                .clearListener()
                .initWithOld(activity,GlobalConfig.machineIp,DapinSocketProxy.FLAG_SCREENFLAG_BODYTESTING_OPEN)
                .startSocket();

        isOpenedBodyTesting = true;

        Intent intent = new Intent(Intent.ACTION_MAIN);
        ComponentName componentName = new ComponentName(pck, opCls);
        intent.setComponent(componentName);
        intent.putExtra("userName", userName);//这里Intent传值
        intent.putExtra("idCard", idCard);
        intent.putExtra("mobile", mobile);
        activity.startActivity(intent);
    }

    /**
     * 从第三方返回后触发
     */
    public void backFromBodyTesting(Activity activity){
        if (!isOpenedBodyTesting)
            return;

        isOpenedBodyTesting = false;

        DapinSocketProxy.with()
                .clearListener()
                .initWithOld(activity,GlobalConfig.machineIp,DapinSocketProxy.FLAG_SCREENFLAG_BODYTESTING_FINISH)
                .startSocket();
    }
}
