package com.aries.template.xiaoyu.xinlin;


import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.aries.library.fast.util.ToastUtil;

import java.net.URI;
import java.net.URISyntaxException;

import tech.gusavila92.websocketclient.WebSocketClient;

/**
 * 信令
 * 消息发送填装器
 * 信令的发送任务
 * - 病人离开，向医生发送消息
 * - 病人入会成功，向医生发送消息
 *
 *  性能优化
 *  关闭释放
 *  数据收集，反馈，监听
 *  单例管理 比较容易被释放
 *
 * 如果关闭过 socket 就重连，并登录
 * 这里是通信的架子，没有通信标准
 */
public class XLMessage {

    // 用户 userId getConfigurationToThirdForPatient
    private String xlPatientUserId = "627dd085cc2f202b1d2146f3";
    // 地址
    private String XL_URL = "wss://app-DEV.ngarihealth.com/";
    // 信令 Socket 对象, 这个 socket 不需要设置心跳包
    private WebSocketClient webSocketClient;
    // 弱应用 activity
    private Activity activity;
    // 信令的监听对象
    private XLEventListener listener;
    // 延迟结束，成功或者失败1次后，结束
    private boolean isDelayDestroy;

    /**
     * 初始化配置文件
     */
    public XLMessage init(String xlPatientUserId, String XL_URL, Activity activity) {
        this.xlPatientUserId = xlPatientUserId;
        this.XL_URL = XL_URL;
        this.activity = activity;
        return this;
    }

    //单例
    private static volatile XLMessage sInstance;
    private XLMessage() {
    }
    public static XLMessage with() {
        if (sInstance == null) {
            synchronized (XLMessage.class){
                if (sInstance==null)
                    sInstance = new XLMessage();
            }
        }
        return sInstance;
    }

//    /**
//     * 仅登录
//     */
//    public void login(XLEventListener listener){
//        send("", listener);
//    }

    /**
     * 信令登录
     * 创造socket 常链接
     * 获取房间信息，房间 account
     * 自动释放资源的情况
     * - 纯登录
     * - 医生已经离开
     *
     * @param msg 这个消息可以是已经组织好的消息，getDoctorMsg，getPatientLeaveMsg 都可以。
     *            如果这个值为空，则仅仅登录。
     * @param inputlistener 如果消息发送，则返回这个监听
     */
    public void send(String msg, XLEventListener inputlistener){
        try {
            final URI uri = new URI(XL_URL);
            listener = inputlistener;
            // 如果已经关闭了socket，则进行重建
            if (webSocketClient==null){
                webSocketClient = new WebSocketClient(uri) {
                    @Override
                    public void onOpen() {
                        // 握手成功
//                        Log.i("WebSocket", "Session is starting");
                        //连接成功服务器后必须要注册
                        webSocketClient.send(new XLSend().getLoginMsg(xlPatientUserId));
                    }

                    @Override
                    public void onTextReceived(String s) {
                        final String message = s;
                        ToastWithLogin(message);
                        activity.runOnUiThread(() -> {
                            //这里改成用json来解析?
                            if (message.contains("REG_SUCCESS")) {
                                // 登录成功
                                if (!msg.equals(new XLSend().getLoginMsg(xlPatientUserId))){
                                    // 如果不只是登录，则登录成功后，继续发送消息
                                    webSocketClient.send(msg);
                                }else{
                                    // 如果只是登录，则直接返回结果
                                    if (listener!=null)
                                        listener.sended(message);
                                    // 释放监听
                                    destroy();
                                }
//                                ToastWithLogin("xl登录成功");
                            }else{
                                // 如果返回的不是登录
                                if (message.contains("TX_RTC_SHUTDOWN_RES")) {
                                    // 医生已经离开
                                    ToastWithLogin("医生已经离开");
                                    if (listener != null)
                                        listener.sended(message);
                                }else if (message.contains("SUCCESS")){
                                    // 其他各种返回成功了
                                    if (listener!=null)
                                        listener.sended(message);
                                    if (isDelayDestroy)
                                        destroy();
                                } else{
                                    // 如果是其他不知道的返回
                                    if (listener!=null)
                                        listener.sended(message);
                                    if (isDelayDestroy)
                                        destroy();
                                }
                            }
                        });
                    }

                    @Override
                    public void onBinaryReceived(byte[] data) {
                    }

                    @Override
                    public void onPingReceived(byte[] data) {
                    }

                    @Override
                    public void onPongReceived(byte[] data) {
                    }

                    @Override
                    public void onException(Exception e) {
                        System.out.println(e.getMessage());
                        destroy();
                    }

                    @Override
                    public void onCloseReceived() {
                        // socket 关闭
                        // 关闭后，webSocketClient 会被置空
//                        Log.i("WebSocket", "Closed ");
                        System.out.println("onCloseReceived");
                        if (isDelayDestroy)
                            destroy();
                    }
                };

                // socket配置
                // socket 链接超时
                webSocketClient.setConnectTimeout(15000);
                // socket 获取数据超时
                webSocketClient.setReadTimeout(60000);
                // socket 重新链接延时
                webSocketClient.enableAutomaticReconnection(6000);
                // socket 开始链接
                webSocketClient.connect();
            }else {
                // 如果 webSocketClient 续存，则直接发消息出去
                // 如果消息是空的，则单纯的只是登录，不需要进行send
                if (!TextUtils.isEmpty(msg))
                    webSocketClient.send(msg);
            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印并提示用户
     */
    private void ToastWithLogin(String msg){
        try {
            if (activity!=null){
                Log.d("JTJK","XLMESSAGE::"+msg);
                activity.runOnUiThread(() -> ToastUtil.show(msg));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 对外监听
     */
    public interface XLEventListener {
        // 消息已经发送 比如 医生进入的时候，将医生的 video Info 给予外部
        void sended(String message);
    }

    /**
     * 延迟结束
     * 成功或者失败一次后，才会执行释放
     */
    public void delayDestroy(){
        isDelayDestroy = true;
    }

    /**
     * 释放所有资源
     */
    public void destroy(){
        if (webSocketClient!=null)
            webSocketClient = null;
        if (listener!=null)
            listener = null;
        if (activity!=null)
            activity=null;
        if (sInstance!=null)
            sInstance = null;
    }
}
