package com.aries.template.xiaoyu.dapinsocket;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class HeartBeat {
    public static int BREAK_WHAT = 400;
    private SocThread socThread;
    private Handler breakHandler;
    private long lastResTime;
    private static long HEARTDURATION = 3000;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if ((System.currentTimeMillis() - lastResTime) > 6 * 1000){
                //断开
                Log.e("TAG","断开了，重连中...");
                breakHandler.sendEmptyMessage(BREAK_WHAT);
                refreshTime();
                if(socThread != null){
                    socThread.executeConn();
                }
            }
            DataBean bean = new DataBean();
            bean.setDataType(3);
            bean.setBody("HeartBeat");
            if(socThread != null){
                socThread.send(bean.getSendDataByte());
            }
        }
    };
    private Runnable r;

    public void refreshTime() {
        this.lastResTime = System.currentTimeMillis();
    }
    public void stopHeart(){
        if(handler != null){
            handler.removeMessages(0);
            handler.removeCallbacks(r);
            handler = null;
        }
    }
    public HeartBeat(SocThread socThread, Handler breakHandler) {
        this.socThread = socThread;
        this.breakHandler = breakHandler;
        refreshTime();
        r = new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
                handler.postDelayed(r,HEARTDURATION);
            }
        };
        handler.post(r);
    }

}
