package com.aries.template.module.base;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.aries.template.GlobalConfig;
import com.aries.template.R;
import com.aries.template.module.main.HomeFragment;
import com.aries.template.utils.ActivityUtils;
import com.aries.template.utils.DateUtils;
import com.aries.template.utils.DefenceUtil;

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
                    onDismiss();
                    gotoMain();
                }
            });
        if (versionCode!=null){
            PackageInfo info = null;
            try {
                info = getActivity().getPackageManager().getPackageInfo(mContext.getPackageName(),0);
                versionCode.setText("当前版本: "+info.versionName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        resetView();
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
            }
            // 显示对象 到时后跳转到主页
            if (timeCount==0){
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
}
