package com.aries.template.module.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

/******
 * 拥有时间计时器的Fragment
 * 时间会自动按照1秒运行，只需要重构 timeProcess 这个方法即可
 * 时间会根据生命周期进行资源消减和重建。优化时间资源。
 *
 * @author  ::: louis luo
 * Date ::: 2022/6/9 10:20 AM
 *
 */
public abstract class BaseTimerFragment extends BaseFragmentationFragment{

    /** 获取当前数据的计时器，时分秒 */
    private Timer timer;

    /** 是否关闭倒计时显示对象 true 关闭*/
    protected boolean dismissCountTimeTag;

    /**
     * 启动计时器
     * 主要任务，每1000毫秒执行一次
     */
    protected void timeStart(){
        dismissCountTimeTag = false;
        // 如果有timer，表示时间计时器已经启动，不可再启动
        if (timer!=null)
            return;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (getActivity()!=null)
                getActivity().runOnUiThread(() -> timeProcess());
            }
        },0,1000);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timeStart();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden){
            if (timer!=null){
                timer.cancel();
                timer = null;
            }
        }else{
            if (!dismissCountTimeTag)
                timeStart();
        }
    }

    /**
     * 释放时间资源
     */
    protected void timeStop(){
        if (timer!=null){
            timer.cancel();
            timer = null;
        }
    }

    /**
     * 关闭倒计时显示
     * 不显示倒计时，不进行倒计时计算
     */
    protected void dismissCountTimeStop(){
        dismissCountTimeTag = true;
        timeStop();
    }


    /**
     * 每次执行后执行到的方法
     * 主要任务，可以重写
     */
    protected void timeProcess() {}

    @Override
    public void onDestroy() {
        super.onDestroy();
        timeStop();
    }
}
