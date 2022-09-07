package com.aries.template.xiaoyu.uvc;

import android.content.Context;

import com.serenegiant.usb.USBMonitor;

/**
 * USBMonitor 单例对象
 * 为了解决 USB 崩溃的问题，不再destroy，而是通过单例进行管理和使用
 */
public class USBMonitorProxy {
    private USBMonitor mUSBMonitor;

    //单例
    private static volatile USBMonitorProxy sInstance;
    private USBMonitorProxy() {
    }
    public static USBMonitorProxy with() {
        if (sInstance == null) {
            synchronized (USBMonitorProxy.class){
                if (sInstance==null)
                    sInstance = new USBMonitorProxy();
            }
        }
        return sInstance;
    }

    /**
     * 初始化 mUSBMonitor
     * @param context 上下文
     * @param listener USB 事件监听
     */
    public USBMonitorProxy init(final Context context, final USBMonitor.OnDeviceConnectListener listener){
        mUSBMonitor = new USBMonitor(context, listener);
        return this;
    }

    /**
     * 获取 USB Monitor
     */
    public USBMonitor get(){
        return mUSBMonitor;
    }

    // 暂时不释放
//    /**
//     * 释放
//     * 注意这里如果 destory 有可能造成崩溃
//     */
//    public void release(){
//        try{
//            if (mUSBMonitor != null) {
//                try {
//                    // 由于 hasPermission 有崩溃，所以必须要在 destroy 之前，先判断
//                    mUSBMonitor.hasPermission(null);
//                    mUSBMonitor.destroy();
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//                mUSBMonitor = null;
//            }
//        }catch (Exception e){e.printStackTrace();};
//    }
}
