package com.aries.template.thridapp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;

import com.aries.template.GlobalConfig;
import com.aries.template.xiaoyu.dapinsocket.DapinSocketProxy;

/**
 * 金投健康第三方管理器
 * 管理身体检测第三方跳转器
 *
 * 注意这里的输入条件，是后台数据配置之前
 */
public class JTJKThirdAppUtil {

    /**
     * 由于第一次进入系统的时候，需要打开，所以默认值设置为 true
     */
    private static boolean isOpenedBodyTesting=false; // 是否启动过第三方跳转

    /**
     * 跳转到第三方
     * 案例
     * new ComponentName("com.garea.launcher", "com.garea.launcher.login.LauncherLogin");
     */
    public void gotoBodyTesting(Activity activity, String pck, String opCls, String userName, String idCard, String mobile){
        // 大屏的通信代理
        DapinSocketProxy.with()
                .initWithOld(activity,GlobalConfig.machineIp,DapinSocketProxy.FLAG_SCREENFLAG_BODYTESTING_OPEN)
                .startSocket();

        isOpenedBodyTesting = true;

        Intent intent = new Intent(Intent.ACTION_MAIN);
        ComponentName componentName = new ComponentName(pck, opCls);
        intent.setComponent(componentName);
        intent.putExtra("userName", userName.trim());//这里Intent传值, 名称不能有空格
        intent.putExtra("idCard", idCard);
        intent.putExtra("mobile", mobile);
        activity.startActivity(intent);
    }

    /**
     * 从第三方返回后触发
     */
    public void backFromBodyTesting(final Activity activity){
        if (!isOpenedBodyTesting)
            return;

        isOpenedBodyTesting = false;

        DapinSocketProxy.with()
                .initWithOld(activity,GlobalConfig.machineIp,DapinSocketProxy.FLAG_SCREENFLAG_BODYTESTING_FINISH)
                .startSocket();
    }

    /**
     * 强制 启动第三方返回
     * @param activity
     */
    public void backFromBodyTestingForce(final Activity activity){
        isOpenedBodyTesting = false;

        DapinSocketProxy.with()
                .initWithOld(activity,GlobalConfig.machineIp,DapinSocketProxy.FLAG_SCREENFLAG_BODYTESTING_FINISH)
                .startSocket();
    }

    /**
     * 启动大屏画面
     * 打开app时，启动画面
     * 每次返回到首页时，启动画面
     */
    public void onScreen(final Activity activity){
        DapinSocketProxy.with()
                .initWithOld(activity,GlobalConfig.machineIp,DapinSocketProxy.FLAG_SCREENFLAG_BODYTESTING_FINISH)
                .startSocket();
    }
}
