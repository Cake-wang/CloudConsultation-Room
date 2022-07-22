package com.aries.template.xiaoyu.xinlin;


import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.ainemo.util.JsonUtil;
import com.aries.template.xiaoyu.model.EndPoint;
import com.aries.template.xiaoyu.model.RegEndPoint;
import com.aries.template.xiaoyu.model.RegRequest;
import com.aries.template.xiaoyu.model.RtcStartInvokeRequest;

import java.lang.ref.WeakReference;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import tech.gusavila92.websocketclient.WebSocketClient;

/**
 * 信令
 * 消息发送填装器
 * 信令的发送任务
 * - 病人离开，向医生发送消息
 * - 病人入会成功，向医生发送消息
 */
public class XLMessage {

    // 用户userId getConfigurationToThirdForPatient
    private String xlPatientUserId = "627dd085cc2f202b1d2146f3";
    // 地址
    private String XL_URL = "wss://app-DEV.ngarihealth.com/";
    // 信令 Socket 对象, 这个socket 不需要设置心跳包
    private WebSocketClient webSocketClient;
    //弱应用 activity
    private WeakReference<Activity> activity;

    /**
     * 初始化配置文件
     */
    public XLMessage(String xlPatientUserId, String XL_URL, Activity activity) {
        this.xlPatientUserId = xlPatientUserId;
        this.XL_URL = XL_URL;
        this.activity = new WeakReference<>(activity);
    }


    /**
     * 信令登录
     * 创造socket 常链接
     * 获取房间信息，房间 account
     */
    public void start(XLEventListener listener) throws URISyntaxException {
        URI uri = new URI(XL_URL);
        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                Log.i("WebSocket", "Session is starting");

                //连接成功服务器后必须要注册
                //{"topic":"REG","endPoint":{"userId":"62933bcbcf8912669abf4b98","roleId":"patient","appVersion":"4.1.1","isInVideo":0,"appType":4}}
                RegEndPoint regEndPoint = new RegEndPoint();
                regEndPoint.setUserId(xlPatientUserId);
                regEndPoint.setRoleId("patient");
                regEndPoint.setAppVersion("4.1.1");
                regEndPoint.setIsInVideo(0);
                regEndPoint.setAppType(4);

                RegRequest regRequest = new RegRequest();
                regRequest.setTopic("REG");
                regRequest.setEndPoint(regEndPoint);
                String registerMsg = JsonUtil.toJson(regRequest);
                webSocketClient.send(registerMsg);
            }

            @Override
            public void onTextReceived(String s) {
                final String message = s;
                ToastWithLogin(message);
                activity.get().runOnUiThread(() -> {
                    // todo：这里改成用json来解析
                    if (message.contains("REG_SUCCESS")) {
                        if (listener!=null)
                            listener.started(message);
                    }else if (message.contains("TX_RTC_SHUTDOWN_RES")){
                        // 医生已经离开
                        // TX_RTC_SHUTDOWN_RES
                        ToastWithLogin("医生已经离开");
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
            }

            @Override
            public void onCloseReceived() {
                Log.i("WebSocket", "Closed ");
                System.out.println("onCloseReceived");
            }
        };
        webSocketClient.setConnectTimeout(10000);
        webSocketClient.setReadTimeout(60000);
        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();
    }

    /**
     * 向doctor发送信息
     * @return 将数据转称 json 字符串，发送给 socket
     */
    public String sendDoctorMsg(String xlPatientUserId,
                                String xlPatientName,
                                String doctorUserId,
                                long consultId,
                                String meetingRoomNumber){
        //{"topic":"TX_RTC_START_INVOKE","endPoint":{"patientUserId":"62933bcbcf8912669abf4b98","doctorUserId":"5f339ceb9cd0500a923af577","patientName":"胡江","remark":"未知","orderId":815463559,"roomId":"910007727377","thirdAppVideoConsult":"xyLink","requestMode":"4"}}
        RtcStartInvokeRequest request = new RtcStartInvokeRequest();
        request.setTopic("TX_RTC_START_INVOKE");
        EndPoint endPoint = new EndPoint();
        endPoint.setPatientUserId(xlPatientUserId);
        endPoint.setPatientName(xlPatientName);
        endPoint.setDoctorUserId(doctorUserId);
        endPoint.setOrderId(consultId);
        endPoint.setRemark("未知");
        endPoint.setRoomId(meetingRoomNumber);
        endPoint.setThirdAppVideoConsult("xyLink");
        endPoint.setRequestMode("4");

        request.setEndPoint(endPoint);
        String cmd = JsonUtil.toJson(request);
        return cmd;
    }

    /**
     * 病人离开会议室
     */
    public String sendPatientLeave(String doctorUserId, String patientUserId){

//      if (socket != null) {
//        let msgObj = {
//                topic: `TX_RTC_SHUTDOWN`,
//        endPoint: {
//            doctorUserId: doctor.loginId,
//                    patientUserId: user.userId,
//                    role: 'patient',
//                    thirdAppVideoConsult: 'xyLink',
//        },
//                };
//        socket.send(JSON.stringify(msgObj));
//    }
        Map<String, Object> endPoint = new HashMap<>();
        endPoint.put("doctorUserId",doctorUserId);
        endPoint.put("patientUserId",patientUserId);
        endPoint.put("role","patient");
        endPoint.put("thirdAppVideoConsult","xyLink");

        Map<String, Object> map = new HashMap<>();
        map.put("topic","TX_RTC_SHUTDOWN");
        map.put("endPoint",endPoint);

        String cmd = JsonUtil.toJson(endPoint);
        return cmd;
    }

    /**
     * 打印并提示用户
     */
    private void ToastWithLogin(String msg){
        if (activity!=null && activity.get()!=null){
            Log.d("EaseModeProxy",msg);
            activity.get().runOnUiThread(() -> Toast.makeText(activity.get(), msg, Toast.LENGTH_SHORT).show());
        }
    }


    /**
     * 对外监听
     */
    public interface XLEventListener {
        default void started(String message){};//医生进入的时候，将医生的 video Info 给予外部
    }


}
