package com.aries.template.xiaoyu.dapinsocket;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.StringRes;

import com.alibaba.fastjson.JSON;
import com.aries.library.fast.util.ToastUtil;
import com.aries.template.xiaoyu.EaseModeProxy;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * 大屏socket 代理类
 * @author louisluo
 */
public class DapinSocketProxy {
    // socket 线程
    private SocThread socketThread;
    // socket 心跳
    private HeartBeat heartBeat;
    // socket 地址
    private String address;
    // 是否可以发送
    private boolean sendEnable = false;
    //  activity
    private Activity activityObj;
    // 当前的启动 标志 SCREENFLAG_CONTROLSCREEN 或者 SCREENFLAG_CLOSESCREEN
    private String currentFlag;
    // 网络的IP地址
    private String ip ="";

    /** 开启 大屏视频流socket的 FLAG */
    public static final String FLAG_SCREENFLAG_CONTROLSCREEN = "ControlScreen_";
    /** 关闭 大屏视频流socket的 FLAG */
    public static final String FLAG_SCREENFLAG_CLOSESCREEN = "CloseScreen_";
    /** 开启身体检测 FLAG */
    public static final String FLAG_SCREENFLAG_BODYTESTING_OPEN = "bodytesting_open_";
    /** 关闭身体检测 FLAG */
    public static final String FLAG_SCREENFLAG_BODYTESTING_FINISH = "bodytesting_finish_";

    /**
     * 单例化
     */
    //单例
    private static volatile DapinSocketProxy sInstance;
    private DapinSocketProxy() {
    }
    public static DapinSocketProxy with() {
        if (sInstance == null) {
            synchronized (DapinSocketProxy.class){
                if (sInstance==null)
                    sInstance = new DapinSocketProxy();
            }
        }
        return sInstance;
    }


    /**
     * 启动socket
     * @param activity 可视化对象
     * @param address 大屏socket地址
     * @param flag 大屏FLAG
     */
    public DapinSocketProxy init(Activity activity, String address, String flag) {
        this.activityObj = activity;
        this.address = address;

        // 必须有，否则会崩溃，由于这个SP不存在
        AddressSettingSharedPreference.getAddrs(activity,AddressSettingSharedPreference.ADDRESS);
        // 监听
        OnContextChangedListener onContextChangedListener = new OnContextChangedListener() {
            @Override
            public void onSendData(byte[] sendData) {
                if (socketThread != null) {
                    socketThread.send(sendData);
                }
            }

            @Override
            public void onSendData(String sendData) {
                //给dlna设备发信息
            }

            @Override
            public void onReceiveData(byte[] receiveData) {
                Message msg = mhandler.obtainMessage();
                msg.obj = SocThread.getDataBean(receiveData);
                mhandler.sendMessage(msg);// 结果返回给UI处理
            }

            @Override
            public void onReceiveData(String receiveData) {
                //Log.e("dawei","receiveData="+receiveData);
                //接收dlna发送的JSON string
                Message msg = mhandler.obtainMessage();
                try {
                    msg.obj = JSON.parseObject(receiveData,DataBean.class);
                }catch (Exception e){
                    e.printStackTrace();
                }
                mhandler.sendMessage(msg);// 结果返回给UI处理
            }
        };
        SessionContext.getSessiontContext().addOnContextChangedListener(onContextChangedListener);

        // 设置ip
        ipSetting(activityObj);

        // 设置Flag
        currentFlag = flag;

        return this;
    }

    /**
     * 初始化
     * 如果socket 已经有相关的数据则不再配置
     * 如果没有，则启动新的
     *  SCREENFLAG_CONTROLSCREEN 或者 SCREENFLAG_CLOSESCREEN
     *      * @StringRes{SCREENFLAG_CONTROLSCREEN,SCREENFLAG_CLOSESCREEN}
     */
    public DapinSocketProxy initWithOld(Activity activity, String address, String flag){
        if (activity!=null)
            this.activityObj = activity;
        if (TextUtils.isEmpty(address))
            this.address = address;
        if (SessionContext.getSessiontContext().isHaveContextChangedListener()){
            OnContextChangedListener onContextChangedListener = new OnContextChangedListener() {
                @Override
                public void onSendData(byte[] sendData) {
                    if (socketThread != null) {
                        socketThread.send(sendData);
                    }
                }

                @Override
                public void onSendData(String sendData) {
                    //给dlna设备发信息
                }

                @Override
                public void onReceiveData(byte[] receiveData) {
                    Message msg = mhandler.obtainMessage();
                    msg.obj = SocThread.getDataBean(receiveData);
                    mhandler.sendMessage(msg);// 结果返回给UI处理
                }

                @Override
                public void onReceiveData(String receiveData) {
                    //Log.e("dawei","receiveData="+receiveData);
                    //接收dlna发送的JSON string
                    Message msg = mhandler.obtainMessage();
                    try {
                        msg.obj = JSON.parseObject(receiveData,DataBean.class);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    mhandler.sendMessage(msg);// 结果返回给UI处理
                }
            };
            SessionContext.getSessiontContext().addOnContextChangedListener(onContextChangedListener);
        }
        if (TextUtils.isEmpty(ip))
            ipSetting(activityObj);
        currentFlag = flag;
        return this;
    }

    /**
     * 强制释放其他接口
     */
    public DapinSocketProxy clearListener(){
        SessionContext.getSessiontContext().removeAll();
        return this;
    }

    // 出去的信息
    @SuppressLint("HandlerLeak")
    Handler mhandler= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(TextUtils.isEmpty(currentFlag))
                return;
            if (msg.what == HeartBeat.BREAK_WHAT){
                //断开了
                return;
            }
            if (msg.what == SocThread.CONNECT_SUCCESS){
                //连接成功
                sendEnable = true;
                AddressSettingSharedPreference.setAddrs(activityObj,AddressSettingSharedPreference.ADDRESS,address);
                if (!TextUtils.isEmpty(ip))
                    AddressSettingSharedPreference.setAddrs(activityObj,AddressSettingSharedPreference.IP,ip);
                else
                    ipSetting(activityObj);
                if(sendEnable) {
                    if (socketThread != null) {
                        // 链接后发送
                        // 发送成功
                        String s = currentFlag+ip;
                        try {
                            socketThread.sendNew(s.getBytes("utf-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        ToastUtil.show("发送成功："+s);
                        // 成功后释放资源
                        closeSocket();
                    }
                }
                return;
            }
            if (msg.what == SocThread.CONNECT_FAIL){
                sendEnable = false;
                // 连接失败
                // 链接失败后，不释放资源，让他继续向大屏请求
                return;
            }
            try {
                if (msg.obj != null) {
                    DataBean bean = (DataBean) msg.obj;
                    if (bean.getDataType() == 3){
                        //心跳
                        heartBeat.refreshTime();
                    }else if (bean.getDataType() == 1){
                        //根据需求进行操作
                    }
                }
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
    };


    // 返回的信息
    @SuppressLint("HandlerLeak")
    Handler mhandlerSend= new Handler() {
        @Override
        public void handleMessage(Message msg) {
//			if(examType == -1) {
            String message = (String) msg.obj;
            byte[] bytes = message.getBytes();
            byte[] dataType = new byte[4];
            System.arraycopy(bytes, 0, dataType, 0, 4);
            byte[] messageType = new byte[4];
            System.arraycopy(bytes, 4, messageType, 0, 4);
            byte[] length = new byte[4];
            System.arraycopy(bytes, 8, length, 0, 4);
            byte[] body = new byte[bytes.length - 12];
            System.arraycopy(bytes, 12, body, 0, body.length);
            DataBean bean = new DataBean();
            bean.setDataType(NumberUtil.byteArrayToInt(dataType));
            bean.setMessageType(NumberUtil.byteArrayToInt(messageType));
            bean.setLength(NumberUtil.byteArrayToInt(length));
            bean.setBody(new String(body));
            if (bean.getDataType() == 1 && bean.getMessageType() == MessageType.USERINFO) {
                switch (msg.what){
                    case 0:
                        Toast.makeText(activityObj, "发送用户信息至大屏失败,请重试！", Toast.LENGTH_SHORT)
                                .show();
//							toLauncher(-1, true);
                        break;
                    case 1:
                        Toast.makeText(activityObj, "已发送用户信息至大屏！", Toast.LENGTH_SHORT)
                                .show();
                        break;
                }
            }
        }



//		}
    };


    /**
     * 启动socket
     */
    public void startSocket(){
        if (activityObj==null)
            return;

        if (socketThread==null){
            if (!TextUtils.isEmpty(address) && address.contains(":")){
                closeSocket();
//			if (heartBeat != null){
//				heartBeat.stopHeart();
//			}
                socketThread = new SocThread(mhandler, mhandlerSend, activityObj);
                try {
                    String[] split = address.split(":");
                    if (split.length != 2){
                        Toast.makeText(activityObj,"地址格式错误,请联系管理员",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    socketThread.setIport(split[0], Integer.parseInt(split[1]));
                    socketThread.executeConn();
                    // 启动心跳
                    heartBeat = new HeartBeat(socketThread,mhandler);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(activityObj,"地址格式错误,请联系管理员",Toast.LENGTH_SHORT).show();
            }
        }

        if (socketThread.client.isClosed()){
            // 如果 socketThread 被释放了，则重新链接
            socketThread.executeConn();
        }else {
            // 如果 socketThread 没有被释放，则直接使用他来发送消息
            String s = currentFlag+ip;
            try {
                socketThread.sendNew(s.getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 启动socket 完成后释放这个 socket 的资源
     * todo 通过返回信息确认，然后启动释放资源
     */
    public void startSocketWithEnd(){
        startSocket();
    }

    /**
     * 关闭socket
     */
    private void closeSocket() {
        if (heartBeat != null){
            heartBeat.stopHeart();
        }
        if (socketThread != null){
            socketThread.close();
            socketThread = null;
        }
        if (activityObj!=null)
            activityObj = null;
    }

    /**
     * 释放所有资源
     */
    public void Destroy(){
        // 释放连接
        closeSocket();
        // 释放handler
        mhandlerSend.removeCallbacksAndMessages(null);
        mhandlerSend = null;
        mhandler.removeCallbacksAndMessages(null);
        mhandler = null;
        // 释放自己
        sInstance = null;
    }

    /**
     * 获取IP地址
     */
    public String getLocalIpAddress() {
        try {
            String ipv4;
            ArrayList<NetworkInterface> nilist = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface ni: nilist)
            {
                ArrayList<InetAddress> ialist = Collections.list(ni.getInetAddresses());
                for (InetAddress address: ialist){
                    if (!address.isLoopbackAddress() && isIPv4Address(ipv4=address.getHostAddress()))
                    {
                        return ipv4;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("localip", ex.toString());
        }
        return null;
    }

    /**
     * 设置ip地址
     */
    public void ipSetting(final Activity activity){
        if (activity!=null){
            ConnectivityManager conMann = (ConnectivityManager)
                    activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            @SuppressLint("MissingPermission") NetworkInfo mobileNetworkInfo = conMann.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            @SuppressLint("MissingPermission") NetworkInfo wifiNetworkInfo = conMann.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (mobileNetworkInfo.isConnected()) {
                ip = getLocalIpAddress();
                System.out.println("本地ip-----"+ip);
            }else if(wifiNetworkInfo.isConnected())
            {
                WifiManager wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                int ipAddress = wifiInfo.getIpAddress();
                ip = intToIp(ipAddress);
                System.out.println("wifi_ip地址为------"+ip);
            }else if (!TextUtils.isEmpty(ip)){
                ip = AddressSettingSharedPreference.getAddrs(activity, AddressSettingSharedPreference.IP);
            }
        }

        // 强制获取
        if (TextUtils.isEmpty(ip)){
            String backip = getLocalIpAddress();
            if (!TextUtils.isEmpty(backip)){
                ip = backip;
            }
        }
    }

    public static boolean isIPv4Address(String ipAddress)
    {
        if((! ipAddress.contains(":"))&& ipAddress.contains(".")){
            String[] arry=ipAddress.split("\\.");
            return arry.length==4;
        }
        return false;
    }

    /**
     * 获取wifi 地址
     */
    public static String intToIp(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }
}
