package com.aries.template.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aries.template.R;
import com.aries.ui.widget.progress.UIProgressDialog;

public class FullScreenJTJKDialog extends UIProgressDialog{

    /** 导航栏的颜色 */
    int navigationBarColor = 0xffffff;
    /** 是否是 Light mode*/
    int cisLightNavigationBar = 1;
    /** 是否是 Light mode*/
    int isLightStatusBar = 1;
    /** 状态栏颜色 */
    private int statusBarBgColor = 0xffffff;


    public FullScreenJTJKDialog(Context context) {
        super(context);

        if (getWindow() == null) return;

        // 设置背景为空
        getWindow().setBackgroundDrawable(null);

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
            int navigationBarColor = 0xffffff;
            if(navigationBarColor!=0) getWindow().setNavigationBarColor(navigationBarColor);
            //尝试兼容部分手机上的状态栏空白问题
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }


        // 设置状态栏为 light 的模式
        setStatusBarLightMode();
        // 设置导航栏为 light 的模式
        setNavBarLightMode();

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        //处理VIVO手机8.0以上系统部分机型的状态栏问题和弹窗下移问题
        boolean isPortrait = getContext().getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT;
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
     * 金投健康风格
     */
    public static class JTJKBuilder extends WeChatBuilder {

        public JTJKBuilder(Context context) {
            super(context);
            setIndeterminateDrawable(R.drawable.dialog_loading_wei_bo)
                    .setBackgroundResource(R.color.colorLoadingBgWei)
                    .setMinWidth(dp2px(150))
                    .setMinHeight(dp2px(110))
                    .setTextColorResource(R.color.colorLoadingTextWeiBo);
        }

        @Override
        public int getGravity() {
            return Gravity.CENTER;
        }

        @Override
        public int getOrientation() {
            return LinearLayout.VERTICAL;
        }

        public UIProgressDialog create() {
            int margin = dp2px(12);
            View contentView = createContentView();
            mDialog = new FullScreenJTJKDialog(mContext);
            mDialog.setContentView(contentView);
            setDialog();
            mDialog.setGravity(Gravity.CENTER);
            mDialog.setMargin(margin, margin, margin, margin);
            mDialog.setCanceledOnTouchOutside(false);
            return (UIProgressDialog) mDialog;
        }

        private View createContentView() {
            mLLayoutRoot = new LinearLayout(mContext);
            mLLayoutRoot.setId(R.id.lLayout_rootProgressDialog);
            mLLayoutRoot.setOrientation(getOrientation());
            mLLayoutRoot.setGravity(getGravity());

            setRootView();
            mLLayoutRoot.addView(createProgressView());
            createText();
            return mLLayoutRoot;
        }

        private void createText() {
            if (TextUtils.isEmpty(mMessageStr)) {
                return;
            }
            mLLayoutRoot.setMinimumWidth(mMinWidth);
            mLLayoutRoot.setMinimumHeight(mMinHeight);

            mTvMessage = new TextView(mContext);
            mTvMessage.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mTvMessage.setId(R.id.tv_messageProgressDialog);
            mLLayoutRoot.addView(mTvMessage);

            setTextViewLine(mTvMessage);
            setTextAttribute(mTvMessage, mMessageStr, mTextColor, mTextSize, Gravity.LEFT, false);
            mTvMessage.setPadding(mTextPadding, mTextPadding, mTextPadding, mTextPadding);

        }
    }
}
