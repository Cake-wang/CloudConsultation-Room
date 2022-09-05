package com.aries.template.view;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * 弹出全面屏幕矿体
 *
 * 这个dialog支持沉浸式，包括全面屏，水滴屏，挖孔屏。
 * 可以快速抬起，并遮盖原来的activity。
 *
 * @author louisluo
 */
public class FullScreenDialog extends Dialog {
    Context context;
    /** 是否浮动 不可以使用true，可能需要权限 */
    boolean enableShowWhenAppBackground = false;
    /** 是否保持屏幕打开 */
    boolean keepScreenOn = false;
    /** 是否给与导航栏 全屏幕手机 */
    boolean hasNavigationBar = false;
    /** 需要焦点控制 */
    boolean isRequestFocus = false;
    /** 是否有隐藏状态栏，true 隐藏, 可以将状态栏网上顶 */
    boolean hasStatusBar = true;
    /** 是否是 Light mode*/
    int isLightStatusBar = 1;
    /** 导航栏的颜色 */
    int navigationBarColor = 0xffffff;
    /** 是否是 Light mode*/
    int cisLightNavigationBar = 1;
    /** 状态栏颜色 */
    private int statusBarBgColor = 0xffffff;

    /**
     * 构造函数
     * 必须获取外部的context作为弹起的依据
     * 不是单例
     * 使用案例：
     *      FullScreenDialog md = new FullScreenDialog(cordova.getActivity(), android.R.style.Theme_NoTitleBar);
     * @param context 弹起的依据
     * @param theme 这个样式不能随便放，建议使用 Theme_NoTitleBar，有可能会导致沉浸失败
     */
    public FullScreenDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    /**
     * 构造函数
     * 必须获取外部的context作为弹起的依据
     * 不是单例
     * 默认 Theme_NoTitleBar 作为样式，这个样式不可改变
     * @param context 弹起的依据
     */
    public FullScreenDialog(Context context) {
        super(context, android.R.style.Theme_NoTitleBar);
        this.context = context;
    }

    /**
     * 构造结束后启动代码
     * 通过各种配置完成沉浸式样式配置
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getWindow() == null) return;
        // 是否为浮动页面
        // 如果版本不足26 则统一使用TYPE_SYSTEM_ALERT
        // 如果使用浮动页面 TYPE_APPLICATION_OVERLAY 会导致需要请求一个动态权限
        // 一般这个设置为 false
        if (enableShowWhenAppBackground) {
            if (Build.VERSION.SDK_INT >= 26) {
                getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
            } else {
                getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            }
        }

        // 是否保持屏幕点亮
        // 这个判定会让屏幕一直保持点亮状态
        if(keepScreenOn){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        // 设置背景为透明
//        getWindow().setBackgroundDrawable(null);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setDimAmount(0f);

        // 将父类的边框设定强制为0，一般这个父类都是最底层的显示对象
        getWindow().getDecorView().setPadding(0, 0, 0, 0);

        // FLAG_HARDWARE_ACCELERATED 表示这个window是否启动硬件加速,请求硬件加速但不能保证硬件加速生效。
        // 如果仅是用来启动硬件加速,可以在代码中控制,使用下面的代码给指定window启动硬件加速:
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        // 全屏的窗口下面 会忽略 SOFT_INPUT_ADJUST_RESIZE  ，不会进行调整
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // 设置全屏
        // SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN : Activity全屏显示，但是状态栏不会被覆盖掉，而是正常显示，只是Activity顶端布局会被覆盖住
        // SYSTEM_UI_FLAG_FULLSCREEN : Activity全屏显示，且状态栏被覆盖掉
        // SYSTEM_UI_FLAG_VISIBLE : 状态栏和Activity共存，Activity不全屏显示。也就是应用平常的显示画面
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        getWindow().getDecorView().setSystemUiVisibility(option);

        // 去掉状态栏的阴影
        if(Build.VERSION.SDK_INT == 19){
            //解决4.4上状态栏闪烁的问题
            // 设置状态栏为透明并且为全屏模式。
            // 然后通过添加一个与 StatusBar 一样大小的 View，将 View 的 backgroud 设置为我们想要的颜色，从而实现沉浸式。
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }else if (Build.VERSION.SDK_INT == 20) {
            // 由于透明主题不能在4.4以前的版本里面使用，
            // 所以系统样式跟以前没有区别，也就是看不到任何变化，
            // 这是一个兼容模式，这个模式可以兼容 20
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);
        }else if (Build.VERSION.SDK_INT >= 21) {
            // 尝试去掉状态栏
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            // 如果状态栏背景已经被调整颜色，则不需要再镂空
            int navigationBarColor = getNavigationBarColor();
            if(navigationBarColor!=0) getWindow().setNavigationBarColor(navigationBarColor);
            //尝试兼容部分手机上的状态栏空白问题
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        // 隐藏导航栏
        // 如果打开这个选项，会直接进入非沉浸式
        if (!hasNavigationBar) {
            hideNavigationBar();
        }

        // 在show之前必须要关闭焦点，这个代码是很有必要的不能删除
        if(!isRequestFocus){
            //不获取焦点
            int flag = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            getWindow().setFlags(flag,flag);
        }

        // 设置状态栏为 light 的模式
        setStatusBarLightMode();
        // 设置导航栏为 light 的模式
        setNavBarLightMode();

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        //处理VIVO手机8.0以上系统部分机型的状态栏问题和弹窗下移问题
        boolean isPortrait = getContext().getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT;
        // 专门处理 ViVO的问题
        if(isVIVORoom() && isPortrait){
            getWindow().setLayout(getAppWidth(getContext()), Math.max(getAppHeight(getContext()),
                    getScreenHeight(getContext())));
            getWindow().getDecorView().setTranslationY(-getStatusBarHeight());
        }
    }

    /**
     * 获取导航栏颜色
     * 工具方法
     */
    private int getNavigationBarColor(){
                return navigationBarColor;
    }

    /**
     * 检测是否是VIVO手机
     * 工具方法
     */
    public boolean isVIVORoom(){
        //vivo的Y开头的8.0和8.1系统特殊(y91 y85 y97)：dialog无法覆盖到状态栏，并且坐标系下移了一个状态栏的距离
        boolean isYModel = Build.MODEL.contains("Y")
                || Build.MODEL.contains("y")
                || Build.MODEL.contains("V1809A");
        String brand = Build.BRAND;
        boolean isVivo = brand.equals("vivo");
        return isVivo && (Build.VERSION.SDK_INT == 26 || Build.VERSION.SDK_INT == 27) && isYModel;
    }

    /**
     * 设置 window的FLAG的通用方法
     * 工具方法
     * 直接向window的LayoutParams设置FLAG
     */
    public void setWindowFlag(final int bits, boolean on) {
        WindowManager.LayoutParams winParams = getWindow().getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        getWindow().setAttributes(winParams);
    }

    /**
     * 设置状态栏为Light 模式
     * 如果隐藏状态栏，则直接使用沉浸是样式
     * 不过不隐藏，
     */
    private void setStatusBarLightMode() {
        // 隐藏状态栏
        if (!hasStatusBar) {
            final ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
            final int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
                    View.SYSTEM_UI_FLAG_FULLSCREEN;
            getWindow().getDecorView().setSystemUiVisibility(decorView.getSystemUiVisibility() | uiOptions);
            return;
        }
        // 不隐藏状态栏
        int light = isLightStatusBar;
        // 版本必须高于 23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && light!=0) {
            View decorView = getWindow().getDecorView();
            int vis = decorView.getSystemUiVisibility();
            if (light > 0 ) {
                vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            decorView.setSystemUiVisibility(vis);
            getWindow().setStatusBarColor(statusBarBgColor);
        }
    }

    /**
     * 隐藏导航栏
     * 通过直接设置 对底层的 ViewGroup获取他的navigationBarBackground对象来控制
     * 所以这个ID是特别的设置
     */
    public void hideNavigationBar() {
        // 获得最底层对象
        final ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
        for (int i = 0, count = decorView.getChildCount(); i < count; i++) {
            final View child = decorView.getChildAt(i);
            final int id = child.getId();
            if (id != View.NO_ID) {
                String resourceEntryName = getResNameById(id);
                // 靶向到 navigationBarBackground 并设置他的可见度
                if ("navigationBarBackground".equals(resourceEntryName)) {
                    child.setVisibility(View.INVISIBLE);
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
     * 获取资源名
     * 工具方法
     * 获取context范围内的资源名称
     */
    private  String getResNameById(int id) {
        try {
            return getContext().getResources().getResourceEntryName(id);
        } catch (Exception ignore) {
            return "";
        }
    }

    /**
     * 设置导航栏模式为Light
     */
    public void setNavBarLightMode() {
        int light = cisLightNavigationBar;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && light!=0) {
            View decorView = getWindow().getDecorView();
            int vis = decorView.getSystemUiVisibility();
            if (light > 0) {
                vis |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            } else {
                vis &= ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            }
            decorView.setSystemUiVisibility(vis);
        }
    }

    /**
     * 触发事件
     * 兼容方法
     * 专门处理 VIVO的机型
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if(isVIVORoom()){ //VIVO的部分机型需要做特殊处理
            event.setLocation(event.getX(), event.getY()+getStatusBarHeight());
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 获取当前状态栏的高度
     * 但是这个方法实际上无法返回一个正确的高度，尤其是水滴屏，全面屏，挖孔屏
     * 单位 px
     * @return 当前状态栏高度
     */
    public int getStatusBarHeight() {
        // 通过获取资源的ID的方式快速获取高度
        Resources resources = Resources.getSystem();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * 获取应用界面的宽度
     * @param context 当前应用 context
     * @return 宽度，单位px
     */
    public  int getAppWidth(Context context) {
        // getSystemService是Android很重要的一个API，它是Activity的一个方法，
        // 根据传入的NAME来取得对应的Object，然后转换成相应的服务对象。
        // WINDOW_SERVICE 管理打开的窗口程序 管理操作对象为WindowManager
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return -1;
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return point.x;
    }


    /**
     * 获取当前应用界面的高度
     * 应用界面可见高度，可能不包含导航和状态栏，看Rom实现
     *  @param context 当前应用 context
     *  @return 高度，单位px
     */
    public int getAppHeight(Context context) {
        // getSystemService是Android很重要的一个API，它是Activity的一个方法，
        // 根据传入的NAME来取得对应的Object，然后转换成相应的服务对象。
        // WINDOW_SERVICE 管理打开的窗口程序 管理操作对象为WindowManager
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return -1;
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return point.y;
    }

    /**
     * 当前屏幕的高度
     * @param context 当前应用 context
     * @return 高度，单位px
     */
    public int getScreenHeight(Context context) {
        // getSystemService是Android很重要的一个API，它是Activity的一个方法，
        // 根据传入的NAME来取得对应的Object，然后转换成相应的服务对象。
        // WINDOW_SERVICE 管理打开的窗口程序 管理操作对象为WindowManager
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return -1;
        Point point = new Point();
        wm.getDefaultDisplay().getRealSize(point);
        return point.y;
    }
}