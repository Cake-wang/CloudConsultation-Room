package com.aries.template.module.mine;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aries.library.fast.util.SPUtil;
import com.aries.template.R;
import com.aries.template.module.base.BaseEventFragment;
import com.aries.template.view.ShineButtonDialog;
import com.aries.ui.view.title.TitleBarView;
import com.xuexiang.xaop.annotation.SingleClick;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 科室展示页面
 * 用于显示一级部门，二级部门
 * @author louisluo
 * @Author: AriesHoo on 2018/7/13 17:09
 * @E-Mail: AriesHoo@126.com
 * @Function: 我的
 */
public class VideoConsultFragment extends BaseEventFragment {
    /**
     * 输入显示对象
     */
    @Override
    public int getContentLayout() {
        return R.layout.fragment_video;
    }

    /** 从外部传入的数据  */
    private  Object inputObj;
    /** 120  秒倒计时间 */
    private int timeCount = 120;

    @BindView(R.id.jtjk_fz_fragment_timer)
    TextView timerTV; //时间计时器显示对象
    @BindView(R.id.btn_stjc)
    Button btn_stjc;// 上一页按钮
    @BindView(R.id.btn_finish)
    Button btn_finish;// 下一页按钮


    /**
     * 跳转科室，需要带的数据
     */
    public static VideoConsultFragment newInstance(Object inputObj) {
        VideoConsultFragment fragment = new VideoConsultFragment();
        return fragment;
    }

    /**
     * 构造函数
     * @param savedInstanceState 输入进来的数据
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 启动计时器
        timeStart();
    }

    /**
     * 页面的动作，包括数据传输和界面改变
     */
    @Override
    public void initView(Bundle savedInstanceState) {



    }


    @Override
    @SingleClick
    @OnClick({R.id.btn_back, R.id.btn_main, R.id.btn_cancel, R.id.btn_inquiry})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            case R.id.btn_back:
//
//                break;
//            case R.id.btn_main:
//
//                break;
            case R.id.btn_stjc:

                Intent intent = new Intent(Intent.ACTION_MAIN);
                /**知道要跳转应用的包命与目标Activity*/
                ComponentName componentName = new ComponentName("com.garea.launcher", "com.garea.launcher.login.LauncherLogin");
                intent.setComponent(componentName);
                intent.putExtra("userName", SPUtil.get(mContext,"userName","")+"");//这里Intent传值
                intent.putExtra("idCard", SPUtil.get(mContext,"idCard","")+"");
                intent.putExtra("mobile", SPUtil.get(mContext,"mobile","")+"");
                startActivity(intent);

                break;
            case R.id.btn_finish:

                showSimpleConfirmDialog();

                break;
            default:
                break;
        }
    }


    private void showSimpleConfirmDialog() {
//        new MaterialDialog.Builder(getContext())
//                .content(R.string.tip_cancel_register)
//                .positiveText(R.string.lab_yes)
//                .negativeText(R.string.lab_no)
//                .onPositive((dialog, which) -> cancelregister(appKey,tid,consultId))
//                .show();


        ShineButtonDialog dialog = new ShineButtonDialog(this.mContext);


            dialog.tv_title_tip.setText("结束问诊");
            dialog.tv_content_tip.setText("是否结束问诊");


        dialog.btn_inquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();


//                    HomeFragment fragment = findFragment(HomeFragment.class);
//                Bundle newBundle = new Bundle();
//
//                fragment.putNewBundle(newBundle);
                    // 在栈内的HomeFragment以SingleTask模式启动（即在其之上的Fragment会出栈）
                start(OrderFragment.newInstance(null));



            }
        });
        dialog.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    /**
     * 计时器任务处理
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void timeProcess() {
        super.timeProcess();
        timerTV.setText(--timeCount+"秒");
        if (timeCount==0){
            gotoMain();
        }
    }

    /**
     * 设置title的信息
     */
    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setBgColor(Color.WHITE)
                .setTitleMainText(R.string.mine);
    }
}
