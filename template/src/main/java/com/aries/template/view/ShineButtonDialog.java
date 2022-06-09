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
import android.widget.Button;
import android.widget.TextView;

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
    }

    @Override
    protected MaterialDialog.Builder getDialogBuilder(Context context) {
        return new MaterialDialog.Builder(context)
                .customView(R.layout.dialog_custom, true);
    }

    @Override
    protected void initViews(Context context) {
        btn_inquiry = findViewById(R.id.btn_inquiry);
        btn_cancel = findViewById(R.id.btn_cancel);
        iv_close = findViewById(R.id.iv_close);
        tv_title_tip = findViewById(R.id.tv_title_tip);
        tv_content_tip = findViewById(R.id.tv_content_tip);


        //修复在对话框中的显示问题
//        shineButton.fitDialog(mDialog);
//        shineButton1.fitDialog(mDialog);
//        shineButton2.fitDialog(mDialog);
//        shineButton3.fitDialog(mDialog);
    }
}
