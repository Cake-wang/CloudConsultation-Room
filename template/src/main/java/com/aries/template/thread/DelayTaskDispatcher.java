package com.aries.template.thread;

import android.os.Looper;
import android.os.MessageQueue;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 延迟任务处理器
 * 完全处理掉要延迟执行的任务后，结束
 * 每一个任务只执行一次
 * 添加 try catch 强化
 */
public class DelayTaskDispatcher {
    // linked 列队
    private Queue<Runnable> delayTask = new LinkedList<>();

    private MessageQueue.IdleHandler idleHandler = new MessageQueue.IdleHandler() {
        @Override
        public boolean queueIdle() {
            // do sth
            if (delayTask.size()>0){
                Runnable runnable=delayTask.poll();
                if (runnable != null){
                    try{
                        // 新线程执行接口
//                        new Thread(runnable).start();
                        runnable.run();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            // 完全处理掉要延迟执行的任务后，结束
            return !delayTask.isEmpty();
        }
    };

    /**
     * 添加任务
     */
    public DelayTaskDispatcher addTask(Runnable runnable){
        delayTask.add(runnable);
        return this;
    }

    /**
     * 启动列队
     */
    public void start(){
        Looper.myQueue().addIdleHandler(idleHandler);
    }


}
