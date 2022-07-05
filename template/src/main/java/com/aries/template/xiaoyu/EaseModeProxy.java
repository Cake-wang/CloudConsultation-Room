package com.aries.template.xiaoyu;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.ainemo.sdk.otf.ConnectNemoCallback;
import com.ainemo.sdk.otf.LayoutElement;
import com.ainemo.sdk.otf.LoginResponseData;
import com.ainemo.sdk.otf.MakeCallResponse;
import com.ainemo.sdk.otf.NemoSDK;
import com.ainemo.sdk.otf.NemoSDKInitCallBack;
import com.ainemo.sdk.otf.Orientation;
import com.ainemo.sdk.otf.ResolutionRatio;
import com.ainemo.sdk.otf.Roster;
import com.ainemo.sdk.otf.RosterWrapper;
import com.ainemo.sdk.otf.Settings;
import com.ainemo.sdk.otf.SimpleNemoSDkListener;
import com.ainemo.sdk.otf.VideoInfo;
import com.ainemo.util.JsonUtil;
import com.aries.template.xiaoyu.meeting.MeetingVideoCell;
import com.aries.template.xiaoyu.model.RegEndPoint;
import com.aries.template.xiaoyu.model.RegReponse;
import com.aries.template.xiaoyu.model.RegRequest;
import com.aries.template.xiaoyu.model.RtcStartInvokeEndPoint;
import com.aries.template.xiaoyu.model.RtcStartInvokeRequest;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.lang.ref.WeakReference;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tech.gusavila92.websocketclient.WebSocketClient;

/******
 * 环信 + 信令 + 小鱼
 * 点对点直播
 *
 * @author  ::: louis luo
 * Date ::: 2022/6/30 2:26 PM
 *
 * todo 提示医生患者挂断视频
 *
 */
public class EaseModeProxy {

    //弱应用
    private WeakReference<Activity> activity;
    // 非全屏时，MeetingVideoCell 的容器
    private ViewGroup contentLayout;
    // 用来显示医生界面的cell
    private MeetingVideoCell videoCell;
    // 是否静音
    private boolean muteMic=false;
    // 是否关闭画面
    private boolean muteVideo=false;
    /** 信令 Socket 对象*/
    private WebSocketClient webSocketClient;

    // 复诊单的配置
    private Integer consultId = 815423874; //复诊单id 复诊单拿
    private String nickname = "eric"; //复诊人姓名 复诊单拿
    private String doctorUserId = "627a861baa36e516a612dc80"; //医生userId 复诊单拿

    // 环信的配置
    private String easemobUserName = "dev_patient_5494620"; //环信用户id getConfigurationToThirdForPatient
    private String easemobPassword = "patient123"; //固定值，不用改 getConfigurationToThirdForPatient

    // 信令的配置
    private String xlPatientUserId = "627dd085cc2f202b1d2146f3"; //用户userId getConfigurationToThirdForPatient

    //------------小鱼的配置
    private static final String xyAppId = "5886885697deb9f4760b3a5e1ab912b9a3b7dfd3"; //小鱼appid 固定
    private String account = "8827"; //患者小鱼id，实际上是从信令获取到的
    //    private static final String meetingRoomNumber = "910007543093"; //会议室房间号,从接口获取到的
    private String meetingRoomNumber = "9038284649"; //会议室房间号,从接口获取到的 还没有
    //    private static final String meetingPassword = "383164"; //会议室密码，从接口获取到的
    private String meetingPassword = "348642"; //会议室密码，从接口获取到的 还没有


    //单例
    private static volatile EaseModeProxy sInstance;
    // 环信 callback
    private EMCallBack emcallback;

    private EaseModeProxy() {
    }
    public static EaseModeProxy with() {
        if (sInstance == null) {
            synchronized (EaseModeProxy.class){
                if (sInstance==null)
                    sInstance = new EaseModeProxy();
            }
        }
        return sInstance;
    }

    /**
     * 弱引用配置 activity
     */
    public void setActivity(Activity activity){
        this.activity = new WeakReference<>(activity);
    }

    /**
     * easeMode初始化 视频三方组件
     */
    public void easemobInit(Context context) {
        //参看：https://docs-im.easemob.com/im/android/sdk/basic
        EMOptions options = new EMOptions();
        // 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
        options.setAutoTransferMessageAttachments(false);
        // 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
        options.setAutoDownloadThumbnail(true);
        //初始化
        EMClient.getInstance().init(context, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
    }

    /**
     * 初始化参数
     * @param inputAc 初始化 activity对象，全局使用
     * @param layout 初始化 存储显示对象容器
     */
    public EaseModeProxy init(Activity inputAc, ViewGroup layout){
        setActivity(inputAc);
        contentLayout = layout;
        return this;
    }

    /**
     * 环信登录
     * 如果不初始化，会导致 MeetingVideoCell 崩溃
     * 医生端有消息返回后，才会调用onMessageReceived 否则视作医生无响应。
     * @param layout 存储显示对象的容器，防止初始化失败的崩溃
     * @param userName 环信的用户名
     * @param password 环信的密码
     * @param xlName 信令的用户ID
     */
    public void easemobStart(Activity inputAc, ViewGroup layout , String userName, String password,String xlName) {
        if (userName==null || password==null || xlName==null)
            return;

        // 设置成员变量
        easemobUserName = userName;
        easemobPassword = password;
        xlPatientUserId = xlName;

        // 启动验证请求
        setActivity(inputAc);
        contentLayout = layout;
        //注册监听消息的回调地址 参看：https://docs-im.easemob.com/im/android/basics/message#%E6%8E%A5%E6%94%B6%E6%B6%88%E6%81%AF
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                activity.get().runOnUiThread(() -> {
                    //接收消息
                    for (EMMessage message : messages) {
                        Log.d("main", "onMessageReceived " + message.toString());
                        Map<String, Object> ext = message.ext();
                        if (ext == null) {
                            continue;
                        }
                        Object msgTypeObj = ext.get("msgType");
                        if (msgTypeObj == null) {
                            continue;
                        }
                        String msgType = (String) msgTypeObj;
                        if (msgType == null || !msgType.equals("26")) {
                            continue;
                        }
//                        todo 请自己调用openAPI方法设置meetingRoomNumber， meetingPassword
                        //获取房间信息
                        createWebSocketClient();
                        //todo 提示患者，医生挂断视频
                    }
                });
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {
            }

            @Override
            public void onMessageDelivered(List<EMMessage> messages) {
            }

            @Override
            public void onMessageRecalled(List<EMMessage> messages) {
            }
        });

        //登陆 https://docs-im.easemob.com/im/android/sdk/basic
//        EMClient.getInstance().logout(true);
        emcallback = new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                ToastWithLogin("登录环信聊天服务器成功");
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(int code, String message) {
                ToastWithLogin("登录环信聊天服务器失败, message:" + message);
            }
        };
        EMClient.getInstance().login(userName, password,emcallback );
    }

    /**
     * 信令登录
     * 创造socket 常链接
     * 获取房间信息，房间 account
     */
    public void createWebSocketClient() {
        URI uri;
        try {
            // Connect to local host
            uri = new URI("ws://172.21.1.95:9090/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

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
                        onRegSuccess(message);
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
     *  登录成功
     */
    private void onRegSuccess(String regResponseMsg) {
        //{"topic":"REG_SUCCESS","payload":70893}
        //注册成功后，获取于用户在小鱼的第三方账号
        RegReponse regReponse = JsonUtil.toObject(regResponseMsg, RegReponse.class);
        Integer payLoad = regReponse.getPayload();
        account = String.valueOf(payLoad);
        xyInit();
    }

    /**
     * 启动小鱼登录
     */
    public void xyInit() {
        //如果没有，则继续初始化
        Settings settings = new Settings(xyAppId);
        settings.setPrivateCloudAddress("cloud.xylink.com");

        int cameraId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing != Camera.CameraInfo.CAMERA_FACING_FRONT){
                cameraId = i;
                break;
            }
        }
        settings.setDefaultCameraId(cameraId);

        // 初始化 NEmoSDK
        NemoSDK.getInstance().init(activity.get(), settings, new NemoSDKInitCallBack() {
            @Override
            public void nemoSdkInitSuccess() {
                ToastWithLogin("初始化成功，开始登陆");
                xyThirdPartyLogin(account, nickname);
            }

            @Override
            public void nemoSdkInitFail(String s, String s1) {
                ToastWithLogin(s + " " + s1);
            }
        });

        // 初始化界面
        // 设置手机系统方向
        NemoSDK.getInstance().setOrientation(Orientation.LANDSCAPE);
        // 创建显示对象
        videoCell = new MeetingVideoCell(activity.get());
        contentLayout.addView(videoCell);
    }

    /**
     * 开始登录 小鱼
     */
    private void xyThirdPartyLogin(String account, String nickname) {
        if (activity==null)
            return;
        activity.get().runOnUiThread(() -> {
            NemoSDK.getInstance().loginExternalAccount(nickname, account, new ConnectNemoCallback() {
                @Override
                public void onFailed(String s) {
                    ToastWithLogin(s);
                }

                @Override
                public void onSuccess(LoginResponseData loginResponseData, boolean b) {
                    ToastWithLogin("登录成功");
                    xyJoinMeeting(meetingRoomNumber, meetingPassword);
                }

                @Override
                public void onNetworkTopologyDetectionFinished(LoginResponseData loginResponseData) {
                    ToastWithLogin("登录成功，网络探测结束");
                }
            });}
        );
    }

    /**
     * 启动会议权限检测
     */
    @SuppressLint("CheckResult")
    private void xyJoinMeeting(String meetingNumber, String meetingPassword) {
        if (activity==null)
            return;
        activity.get().runOnUiThread(() -> {
            RxPermissions permissions = new RxPermissions(((FragmentActivity) activity.get()));
            permissions.request(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                    .subscribe(aBoolean -> {
                        if (aBoolean) {
                            NemoSDK.getInstance().makeCall(meetingNumber, meetingPassword, new MakeCallResponse() {
                                @Override
                                public void onCallSuccess() {
                                    ToastWithLogin("参会成功");
                                    onJoinMeetingOk();
                                }

                                @Override
                                public void onCallFail(String s, String s1) {
                                    ToastWithLogin(s + " " + s1);
                                }
                            });
                        } else {
                            ToastWithLogin( "请打开摄像机和麦克风权限 ");
                        }
                    });
        });
    }

    /**
     * 启动会议
     */
    private void onJoinMeetingOk() {
        activity.get().runOnUiThread(() -> {
            sendNotifyDoctorVideoMsg();
            Handler mainThreadHandler = new Handler();

            //渲染local画面
            VideoInfo videoInfo = new VideoInfo();
            videoInfo.setDataSourceID(NemoSDK.getLocalVideoStreamID());
            videoInfo.setAudioMute(muteMic);
            videoInfo.setVideoMute(muteVideo);
            videoCell.setVideoInfo(videoInfo);// 启动本地视频窗口

            NemoSDK.getInstance().setNemoSDKListener(new SimpleNemoSDkListener() {
                @Override
                public void onCallStateChange(CallState state, String reason) {
                    //1.监听会议状态
                    mainThreadHandler.post(() -> {
                        if (state == CallState.CONNECTED) {
                            ToastWithLogin("入会成功: ");
                            if (listener!=null)
                                listener.onVideoSuccessLinked();
                        } else if (state == CallState.DISCONNECTED) {
                            ToastWithLogin("退出会议: " + reason);
                            // 释放资源一定要写在退出会议的后面
                            releaseProxy();
                        }
                    });
                }

                @Override
                public void onRosterChange(RosterWrapper rosterWrapper) {
                    //参会者信息回调
                    mainThreadHandler.post(() -> {
                        //2.计算请流集合
                        NemoSDK.getInstance().setLayoutBuilder(policy -> {
                            List<LayoutElement> elements = new ArrayList<>();
                            //请求所有参会者
                            ArrayList<Roster> rosters = rosterWrapper.getRosters();
                            if (rosters != null && !rosters.isEmpty()) {
                                LayoutElement element;
                                for (Roster roster : rosters) {
                                    element = new LayoutElement();
                                    element.setParticipantId(roster.getParticipantId());
                                    element.setResolutionRatio(ResolutionRatio.RESO_360P_NORMAL);
                                    elements.add(element);
                                }
                            }
                            return elements;
                        });
                    });
                }

                @Override
                public void onVideoDataSourceChange(List<VideoInfo> videoInfos, boolean hasVideoContent) {
                    mainThreadHandler.post(() -> {
                        if (videoInfos.size()>0){
//                            if (videoCell !=null)
//                                videoCell.setVideoInfo(videoInfos.get(0));
                        }
                    });
                }
            });
        });
    }

    /**
     * 向Docotor的视频端发送用户信息。
     */
    private void sendNotifyDoctorVideoMsg() {
        if (webSocketClient==null)
            return;
        //{"topic":"TX_RTC_START_INVOKE","endPoint":{"patientUserId":"62933bcbcf8912669abf4b98","doctorUserId":"5f339ceb9cd0500a923af577","patientName":"胡江","remark":"未知","orderId":815463559,"roomId":"910007727377","thirdAppVideoConsult":"xyLink","requestMode":"4"}}
        RtcStartInvokeRequest request = new RtcStartInvokeRequest();
        request.setTopic("TX_RTC_START_INVOKE");
        RtcStartInvokeEndPoint endPoint = new RtcStartInvokeEndPoint();
        endPoint.setPatientUserId(xlPatientUserId);
        endPoint.setPatientName("todo改掉名字");
        endPoint.setDoctorUserId(doctorUserId);
        endPoint.setOrderId(consultId);
        endPoint.setRemark("未知");
        endPoint.setRoomId(meetingRoomNumber);
        endPoint.setThirdAppVideoConsult("xyLink");
        endPoint.setRequestMode("4");

        request.setEndPoint(endPoint);
        String cmd = JsonUtil.toJson(request);

        webSocketClient.send(cmd);
    }

    /**
     * 挂断视频
     */
    public void closeVideoProxy(){
        // 释放视频资源
        NemoSDK.getInstance().hangup();// 挂断通话
    }

    /**
     * 释放资源
     */
    private void releaseProxy(){
        // 释放对象资源
        if (activity!=null)
            activity = null;
        if (contentLayout !=null)
            contentLayout = null;
        if (videoCell !=null){
            videoCell = null;
        }
        // 释放 webSoekctClient 信令
        if (webSocketClient!=null){
            webSocketClient.close();
            webSocketClient = null;
        }
        // 释放 环信
        EMClient.getInstance().logout(true,emcallback);
    }

    /** 对外监听 */
    private ProxyEventListener listener;

    /** 对外监听设置入口 */
    public void setListener(ProxyEventListener listener) {
        this.listener = listener;
    }

    /**
     * 对外监听
     */
    public interface ProxyEventListener{
        /** 当医生进入的时候，将医生的 video Info 给予外部 */
        void onVideoSuccessLinked();
    }

    /**
     * 打印并提示用户
     */
    private void ToastWithLogin(String msg){
        if (activity!=null){
            Log.d("EaseModeProxy",msg);
            activity.get().runOnUiThread(() -> Toast.makeText(activity.get(), msg, Toast.LENGTH_SHORT).show());
        }
    }

    /**
     * 启动全屏
     * 想法是这样的，当启动全屏的时候，容器会把 videoCell 放进这个容器
     * 所以这个容器其实是个全屏的容器
     * @param viewGroup 全屏的容器对象
     */
    public void doFullScreen(ViewGroup viewGroup){
        if (viewGroup==null)
            return;
        if (videoCell.getParent()!=null)
            ((ViewGroup) videoCell.getParent()).removeView(videoCell);
        viewGroup.addView(videoCell);
    }

    /**
     * 从全屏返回
     * 会将 videoCell 方回到原来的容器里面
     */
    public void doNotFullScreen(){
        if (videoCell.getParent()!=null)
            ((ViewGroup) videoCell.getParent()).removeView(videoCell);
        contentLayout.addView(videoCell);
    }
}
