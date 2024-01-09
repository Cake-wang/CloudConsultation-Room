package com.aries.template.module.base;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.TextView;

import com.aries.library.fast.util.SizeUtil;
import com.aries.template.GlobalConfig;
import com.aries.template.MainActivity;
import com.aries.template.R;
import com.aries.template.module.main.HomeFragment;
import com.aries.template.module.mine.ResultFragment;
import com.aries.template.utils.ActivityUtils;
import com.aries.template.utils.DateUtils;
import com.aries.template.utils.DefenceUtil;
import com.aries.template.view.ShineButtonDialog;

import androidx.annotation.Nullable;
import me.yokeyword.fragmentation.SupportFragment;

/******
 * 按钮，行为的基础性事件
 * 必须在每一个页面中，声明对应的id，不然会报错。
 * - 首页事件
 * - 返回上一页事件
 * - 时间计时器事件
 * - 当日日期展示
 *
 * @author  ::: louis luo
 * Date ::: 2022/6/9 2:40 PM
 *
 */
public abstract class BaseEventFragment extends BaseTimerFragment{
    /**监听按钮 返回*/
    protected Button btnBack;
    /**监听按钮 首页*/
    protected Button btnMain;
    /** 倒计时时间器 */
    protected TextView tvShowTimer;
    /** 机器设备ID */
    protected TextView machineIDTv;
    /** 组织名称 */
    protected TextView hospitalNameTv;
    /** 时钟显示  */
    protected TextView textClock;
    /** 时钟显示  */
    protected TextView versionCode;
    /** 120  秒倒计时间 */
    protected int timeCount = 120;

    ShineButtonDialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        btnBack =getView().findViewById(R.id.btn_back);
        btnMain = getView().findViewById(R.id.btn_main);
        tvShowTimer = getView().findViewById(R.id.jtjk_fz_fragment_timer);
        machineIDTv = getView().findViewById(R.id.jtjk_machine_id);
        textClock = getView().findViewById(R.id.tv_clock);
        versionCode = getView().findViewById(R.id.tv_version);
        hospitalNameTv = getView().findViewById(R.id.jtjk_hospital_name);

        // set
        if (btnBack !=null)
            btnBack.setOnClickListener(v -> {
                if (DefenceUtil.checkReSubmit("btn_back")) {
                    onDismiss();
                    pop();
                }
            });
        if (btnMain !=null)
            btnMain.setOnClickListener(v ->{
                if (DefenceUtil.checkReSubmit("btn_main")) {

                    if(((MainActivity)getActivity()).getTopFragment()  instanceof ResultFragment  && ResultFragment.result.contains("paySuc")){







                                dialog = new ShineButtonDialog(this.mContext);
//                                dialog.tv_title_tip.setText("请根据凭条或取药码至药柜取药\n请您惠存取药码："+ResultFragment.takeCode);
                                dialog.tv_title_tip.setText("请根据凭条或取药码至药柜取药");
                                dialog.tv_title_tip.setTextSize(SizeUtil.dp2px(20));


//                                String[] param = {"#38ABA0",ResultFragment.takeCode,"#00000000","0"};



//                                dialog.tv_content_tip.setText("请您惠存取药码："+ActivityUtils.formatTextView(param));
                        String str = "请您惠存取药码："+ "<font color=\"#38ABA0\">"+ResultFragment.takeCode+"</font>";
                        dialog.tv_content_tip.setText(Html.fromHtml(str));
                        dialog.tv_content_tip.setTextSize(SizeUtil.dp2px(24));

                        dialog.btn_inquiry.setText("确定");

                                dialog.btn_inquiry.setOnClickListener(vv -> {
                                    // 防御代码
                                    if (!DefenceUtil.checkReSubmit("BaseEventFragment.showSimpleConfirmDialog"))
                                        return;

                                    // 关闭对话框
                                    dialog.dismiss();
                                    onDismiss();
                                    gotoMain();


                                });
                        dialog.btn_cancel.setText("取消");
                                dialog.btn_cancel.setOnClickListener(vv ->  {dialog.dismiss();});
                                dialog.iv_close.setOnClickListener(vv -> {dialog.dismiss();});
                                dialog.show();






                    }else {
                        onDismiss();
                        gotoMain();
                    }

                }
            });
        if (versionCode!=null && mContext!=null){
            PackageInfo info = null;
            try {
                info = getActivity().getPackageManager().getPackageInfo(mContext.getPackageName(),0);
                versionCode.setText("当前版本: "+info.versionName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        resetView();
        // 沉浸
        fullScreen();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        if (!hidden){
//            // 重新进入后，刷新数据
//            resetView();
//        }
    }

    /**
     * 进入Fragment的时候，刷新界面的数据
     * 来自全局
     * 来自时间计时器
     */
    private void resetView(){
        if (machineIDTv !=null);
            machineIDTv.setText("机器编号:"+GlobalConfig.machineId);

        if (hospitalNameTv !=null)
            hospitalNameTv.setText(GlobalConfig.hospitalName);

        timeCount = 120;
    }

    /**
     * 关闭时间计时器
     */
    @Override
    protected void timeStop() {
        super.timeStop();
        if (tvShowTimer != null)
            tvShowTimer.setText("");
    }

    /**
     * 计时器任务处理
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void timeProcess() {
        super.timeProcess();
        // 是否执行 倒计时 显示对象任务
        if (!dismissCountTimeTag){
            // 显示对象 计时器
            if ( tvShowTimer !=null){
                String[] param = {"#38ABA0",--timeCount+"","#333333","秒"};
                tvShowTimer.setText(ActivityUtils.formatTextView(param));

                if(((MainActivity)getActivity()).getTopFragment()  instanceof ResultFragment &&timeCount>90 && ResultFragment.result.contains("paySuc")){

                    int timeCountx = timeCount-90;
                    String[] paramx = {"#fff",timeCountx+"","#fff","秒"};
//                    String[] paramx = {"#fff",--timeCountx+"","#fff","秒可返回首页"};
//                    btnMain.setText(ActivityUtils.formatTextView(paramx));
                    btnMain.setText("首页("+ActivityUtils.formatTextView(paramx)+")");
                    btnMain.setEnabled(false);

                }else {
                    btnMain.setText("首页");
                    btnMain.setEnabled(true);

                }

            }

            // 显示对象 到时后跳转到主页
            if (timeCount==0){
                if(dialog!=null){
                    dialog.dismiss();
                }
                onDismiss();
                gotoMain();
            }
        }
        // 下方时钟对象
        if (textClock!=null)
            textClock.setText(DateUtils.getCurrentTime());
    }

    /**
     * 返回主页
     */
    public void gotoMain(){
        // 必须使用 singleTask 将所有Fragemnt的栈干掉
        start(HomeFragment.newInstance(), SupportFragment.SINGLETASK);
    }

    /**
     * fragment页面隐藏
     * 不论是按到返回还是回到首页
     * 只要按到这里两个按钮，就触发
     */
    public void onDismiss(){
    }

    public void fullScreen(){
        ActivityUtils.fullScreen(getActivity().getWindow(),false);
        ActivityUtils.lightOnScreen(getActivity().getWindow());
    }
}
