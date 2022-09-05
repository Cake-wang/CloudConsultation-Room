/*
 * Copyright (C) 2020 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.aries.template.view;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aries.template.R;
import com.xuexiang.xui.widget.alpha.XUIAlphaImageView;
import com.xuexiang.xui.widget.dialog.materialdialog.CustomMaterialDialog;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.util.zip.Inflater;


/**
 * 全屏置顶对话框
 *
 * 可以
 *
 * @author louisluo
 * @since 2020-01-06 23:39
 */
public class SplashDialog{

    private RelativeLayout screenLayout;
    private FullScreenDialog splashDialog;
    private ImageView splashImageView;

    /**
     * 构造窗体
     *
     * @param context
     */
    public SplashDialog(Context context) {
        initViews(context);
    }

    /**
     * 初始化绘制界面
     */
    protected void initViews(Context context) {
        if (context==null)
            return;
        // 获取最底层的显示对象
        screenLayout = new RelativeLayout(context);
        RelativeLayout.LayoutParams screenParams = new RelativeLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        screenLayout.setPadding(46,116,46,116);
        screenLayout.setLayoutParams(screenParams);
        screenLayout.setVerticalGravity(RelativeLayout.CENTER_IN_PARENT);
        screenLayout.setHorizontalGravity(RelativeLayout.CENTER_IN_PARENT);
        screenLayout.setBackgroundColor(0x00000000);

        splashImageView = new ImageView(context);
        ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        splashImageView.setLayoutParams(layoutParams);
        splashImageView.setBackground(context.getDrawable(R.mipmap.dialog_home_pic));
        splashImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        splashDialog = new FullScreenDialog(context);
        // set all view into content
        screenLayout.addView(splashImageView);

        splashDialog.setContentView(screenLayout);
        // 点击外部不隐藏
        splashDialog.setCancelable(false);
    }

    /**
     * 获取最底层绘制对象
     */
    public View getBGLayout(){
        return screenLayout;
    }

    /**
     * 释放资源
     */
    public void onDismiss(){
        splashDialog.dismiss();
        splashDialog = null;
    }

    public void show(){
        splashDialog.show();
    }
}
