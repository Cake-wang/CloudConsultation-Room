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
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.aries.template.GlobalConfig;
import com.aries.template.R;
import com.xuexiang.xui.widget.alpha.XUIAlphaImageView;
import com.xuexiang.xui.widget.dialog.materialdialog.CustomMaterialDialog;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;


/**
 * @author xuexiang
 * @since 2020-01-06 23:39
 */
public class ShineButtonDialog extends CustomMaterialDialog {
   public Button btn_inquiry;
    public Button btn_cancel;
    public XUIAlphaImageView iv_close;
    public TextView tv_title_tip;
    public TextView tv_content_tip;


    /**
     * 构造窗体
     *
     * @param context
     */
    public ShineButtonDialog(Context context) {
        super(context);
        // 空白以外的地方点击不可以取消
        mDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected MaterialDialog.Builder getDialogBuilder(Context context) {


        if(GlobalConfig.thirdFactory.equals("3")||GlobalConfig.thirdFactory.equals("2")){
            return new MaterialDialog.Builder(context)
                    .customView(R.layout.dialog_custom_l, true);
        }else {
            return new MaterialDialog.Builder(context)
                    .customView(R.layout.dialog_custom, true);
        }

    }

    @Override
    protected void initViews(Context context) {
        btn_inquiry = findViewById(R.id.btn_inquiry);
        btn_cancel = findViewById(R.id.btn_cancel);
        iv_close = findViewById(R.id.iv_close);
        tv_title_tip = findViewById(R.id.tv_title_tip);
        tv_content_tip = findViewById(R.id.tv_content_tip);

        //设置 对话的宽高
        if(GlobalConfig.thirdFactory.equals("3")||GlobalConfig.thirdFactory.equals("2")){

            btn_inquiry.setTextSize(30);
            btn_cancel.setTextSize(30);

            this.getDialog().getWindow().setLayout(650,-2);

        }else {
            this.getDialog().getWindow().setLayout(-2,-2);
        }


        // 隐藏导航栏
        // 如果打开这个选项，会直接进入非沉浸式
        hideNavigationBar();

        // 在show之前必须要关闭焦点，这个代码是很有必要的不能删除
        //不获取焦点
        int flag = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        getDialog().getWindow().setFlags(flag,flag);


        //修复在对话框中的显示问题
//        shineButton.fitDialog(mDialog);
//        shineButton1.fitDialog(mDialog);
//        shineButton2.fitDialog(mDialog);
//        shineButton3.fitDialog(mDialog);
    }

    /**
     * 隐藏导航栏
     * 通过直接设置 对底层的 ViewGroup获取他的navigationBarBackground对象来控制
     * 所以这个ID是特别的设置
     */
    public void hideNavigationBar() {
        // 获得最底层对象
        final ViewGroup decorView = (ViewGroup) getDialog().getWindow().getDecorView();
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
            return getDialog().getContext().getResources().getResourceEntryName(id);
        } catch (Exception ignore) {
            return "";
        }
    }
}
