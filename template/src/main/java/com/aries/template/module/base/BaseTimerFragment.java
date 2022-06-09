package com.aries.template.module.base;

import java.util.Timer;
import java.util.TimerTask;

/******
 * 拥有时间计时器的Fragment
 *
 * @author  ::: louis luo
 * Date ::: 2022/6/9 10:20 AM
 *
 */
public abstract class BaseTimerFragment extends BaseFragmentationFragment{

    /** 获取当前数据的计时器，时分秒 */
    private Timer timer;

    /**
     * 启动计时器
     * 主要任务，每1000毫秒执行一次
     */
    protected void timeStart(){
        // 如果有timer，表示时间计时器已经启动，不可再启动
        if (timer!=null)
            return;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(() -> timeProcess());
            }
        },0,1000);
    }

    /**
     * 每次执行后执行到的方法
     * 主要任务，可以重写
     */
    protected void timeProcess() {}

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer!=null){
            timer.cancel();
            timer = null;
        }
    }
}
