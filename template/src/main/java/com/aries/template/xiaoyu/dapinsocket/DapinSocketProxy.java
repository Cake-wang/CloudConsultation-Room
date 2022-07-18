package com.aries.template.xiaoyu.dapinsocket;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.StringRes;

import java.io.UnsupportedEncodingException;

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
    //  //弱引用 activity
    private Activity activity;
    // 当前的启动 标志 SCREENFLAG_CONTROLSCREEN 或者 SCREENFLAG_CLOSESCREEN
    private String currentFlag;
    // 网络的IP地址
    private String ip;

    /** 开启socket的 FLAG */
    public static final String SCREENFLAG_CONTROLSCREEN = "ControlScreen_";
    /** 关闭 FLAG */
    public static final String SCREENFLAG_CLOSESCREEN= "CloseScreen_";


    /**
     * @param activity 可视化对象
     * @param ip 大屏socket地址
     */
    public DapinSocketProxy(Activity activity, String ip) {
        this.activity = activity;
        address = AddressSettingSharedPreference.getAddrs(this.activity,AddressSettingSharedPreference.ADDRESS);

        this.ip = ip;
    }

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
//                tv_hint.setText("连接成功");
                //连接成功
                sendEnable = true;
                AddressSettingSharedPreference.setAddrs(activity,AddressSettingSharedPreference.ADDRESS,address);
                if(sendEnable) {
//                    DataBean bean = new DataBean();
//                    bean.setMessageType(MessageType.START);
                    if (socketThread != null) {
//                        socketThread.send(bean.getSendDataByte());
                        String s = currentFlag+ip;
//                        Toast.makeText(this,s,Toast.LENGTH_LONG);
//                        tv_hint.setText("发送成功"+s);
                        try {
                            socketThread.sendNew(s.getBytes("utf-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return;
            }
            if (msg.what == SocThread.CONNECT_FAIL){
                sendEnable = false;
//                tv_hint.setText("连接失败");
                //连接失败
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
                        Toast.makeText(activity, "发送用户信息至大屏失败,请重试！", Toast.LENGTH_SHORT)
                                .show();
//							toLauncher(-1, true);
                        break;
                    case 1:
                        Toast.makeText(activity, "已发送用户信息至大屏！", Toast.LENGTH_SHORT)
                                .show();
                        break;
                }
            }
        }



//		}
    };


    /**
     * 启动socket
     * SCREENFLAG_CONTROLSCREEN 或者 SCREENFLAG_CLOSESCREEN
     * @StringRes{SCREENFLAG_CONTROLSCREEN,SCREENFLAG_CLOSESCREEN}
     */
    private void startSocket(String flag){
        if (activity==null)
            return;

        currentFlag = flag;

        if (!TextUtils.isEmpty(address) && address.contains(":")){
            closeSocket();
//			if (heartBeat != null){
//				heartBeat.stopHeart();
//			}
            socketThread = new SocThread(mhandler, mhandlerSend, activity);
            try {
                String[] split = address.split(":");
                if (split.length != 2){
                    Toast.makeText(activity,"地址格式错误,请联系管理员",Toast.LENGTH_SHORT).show();
                    return;
                }
                socketThread.setIport(split[0], Integer.parseInt(split[1]));
                socketThread.executeConn();
                heartBeat = new HeartBeat(socketThread,mhandler);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            Toast.makeText(activity,"地址格式错误,请联系管理员",Toast.LENGTH_SHORT).show();
        }
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
        if (activity!=null)
            activity = null;
    }
}
