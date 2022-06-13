package com.aries.template.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/******
 * Activity的专用工具箱
 *
 * @author  ::: louis luo
 * Date ::: 2022/6/13 9:48 AM
 *
 */
public class ActivityUtils {
//    /**
//     * 沉浸式布局
//     * 必须写在setContent 之前
//     * 可以写在onWindowChange
//     * @param window 获取activity的window对象
//     * @param ifShowNavigate 是否显示底部导航栏
//     */
//    public static void fullScreen(Window window, boolean ifShowNavigate) {
//        // 启动沉浸式
//        window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
//        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.setStatusBarColor(Color.TRANSPARENT);
//        }
//        if (ifShowNavigate){
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }else {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                window.setNavigationBarColor(Color.TRANSPARENT);
//            }
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
//    }

    /**
     * 沉浸式布局
     * 必须写在setContent 之前
     * 可以写在onWindowChange
     * @param window 获取activity的window对象
     * @param ifShowNavigate 是否显示底部导航栏
     */
    public static void fullScreen(Window window, boolean ifShowNavigate){
        boolean isRequestFocus = true;//焦点
        if (window == null) return;
        // 将父类的边框设定强制为0，一般这个父类都是最底层的显示对象
        window.getDecorView().setPadding(0, 0, 0, 0);

        // FLAG_HARDWARE_ACCELERATED 表示这个window是否启动硬件加速,请求硬件加速但不能保证硬件加速生效。
        // 如果仅是用来启动硬件加速,可以在代码中控制,使用下面的代码给指定window启动硬件加速:
        window.setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        // 全屏的窗口下面 会忽略 SOFT_INPUT_ADJUST_RESIZE  ，不会进行调整
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // 设置全屏
        // SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN : Activity全屏显示，但是状态栏不会被覆盖掉，而是正常显示，只是Activity顶端布局会被覆盖住
        // SYSTEM_UI_FLAG_FULLSCREEN : Activity全屏显示，且状态栏被覆盖掉
        // SYSTEM_UI_FLAG_VISIBLE : 状态栏和Activity共存，Activity不全屏显示。也就是应用平常的显示画面
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        window.getDecorView().setSystemUiVisibility(option);

        // 去掉状态栏的阴影
        if(Build.VERSION.SDK_INT == 19){
            //解决4.4上状态栏闪烁的问题
            // 设置状态栏为透明并且为全屏模式。
            // 然后通过添加一个与 StatusBar 一样大小的 View，将 View 的 backgroud 设置为我们想要的颜色，从而实现沉浸式。
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }else if (Build.VERSION.SDK_INT == 20) {
            // 由于透明主题不能在4.4以前的版本里面使用，
            // 所以系统样式跟以前没有区别，也就是看不到任何变化，
            // 这是一个兼容模式，这个模式可以兼容 20
            setWindowFlag(window,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);
        }else if (Build.VERSION.SDK_INT >= 21) {
            // 尝试去掉状态栏
//            int navigationBarColor = 0xffffff;//显示导航栏颜色
            int navigationBarColor = Color.TRANSPARENT; //去掉导航栏
            setWindowFlag(window,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, false);
            window.setStatusBarColor(Color.TRANSPARENT);
            if(navigationBarColor!=0) window.setNavigationBarColor(navigationBarColor);
            //尝试兼容部分手机上的状态栏空白问题
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        // 隐藏导航栏目
        if (!ifShowNavigate)
            hideNavigationBar(window);


        // 在show之前必须要关闭焦点，这个代码是很有必要的不能删除
        if(!isRequestFocus){
            //不获取焦点
            int flag = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            window.setFlags(flag,flag);
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    /**
     * 隐藏导航栏
     * 通过直接设置 对底层的 ViewGroup获取他的navigationBarBackground对象来控制
     * 所以这个ID是特别的设置
     */
    public static void hideNavigationBar(Window window) {
        // 获得最底层对象
        final ViewGroup decorView = (ViewGroup) window.getDecorView();
        for (int i = 0, count = decorView.getChildCount(); i < count; i++) {
            final View child = decorView.getChildAt(i);
            final int id = child.getId();
            if (id != View.NO_ID) {
                try {
                    String resourceEntryName = child.getContext().getResources().getResourceEntryName(id);
                    // 靶向到 navigationBarBackground 并设置他的可见度
                    if ("navigationBarBackground".equals(resourceEntryName)) {
                        child.setVisibility(View.INVISIBLE);
                    }
                } catch (Exception ignore) {
                    return;
                }
            }
        }
        // 通过FLAG设置导航栏
        final int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | uiOptions);
    }

    /**
     * 常亮屏幕
     * @param window 获取activity的window对象
     */
    public static void lightOnScreen(Window window){
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * 设置 window的FLAG的通用方法
     * 工具方法
     * 直接向window的LayoutParams设置FLAG
     * @param on ture为或，false为and
     */
    public static void setWindowFlag(Window window, final int bits, boolean on) {
        WindowManager.LayoutParams winParams = window.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        window.setAttributes(winParams);
    }
}
