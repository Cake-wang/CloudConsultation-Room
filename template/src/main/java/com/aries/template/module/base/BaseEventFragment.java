package com.aries.template.module.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.aries.template.R;
import com.aries.template.module.main.HomeFragment;
import com.aries.template.utils.ActivityUtils;
import com.aries.template.utils.DateUtils;

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
    /** 120  秒倒计时间 */
    protected int timeCount = 120;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        btnBack =this.getView().findViewById(R.id.btn_back);
        btnMain = this.getView().findViewById(R.id.btn_main);
        tvShowTimer = this.getView().findViewById(R.id.jtjk_fz_fragment_timer);
        if (btnBack !=null)
            btnBack.setOnClickListener(v -> pop());
        if (btnMain !=null)
            btnMain.setOnClickListener(v -> gotoMain());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden)
            timeCount = 120;
    }

    /**
     * 计时器任务处理
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void timeProcess() {
        super.timeProcess();
        /**显示对象 计时器*/
        if ( tvShowTimer !=null){
            String[] param = {"#38ABA0",--timeCount+"","#333333","秒"};
            tvShowTimer.setText(ActivityUtils.formatTextView(param));
        }
        if (timeCount==0){
            gotoMain();
        }
    }

    /**
     * 返回主页
     */
    public void gotoMain(){
        // 必须使用 singleTask 将所有Fragemnt的栈干掉
        start(HomeFragment.newInstance(), SupportFragment.SINGLETASK);
    }
}
