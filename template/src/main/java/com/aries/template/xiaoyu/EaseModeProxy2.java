//package com.aries.template.xiaoyu;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Context;
//import android.os.Build;
//import android.util.Log;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import androidx.annotation.IntDef;
//import androidx.fragment.app.FragmentActivity;
//
//import com.ainemo.sdk.otf.ConnectNemoCallback;
//import com.ainemo.sdk.otf.LayoutElement;
//import com.ainemo.sdk.otf.LoginResponseData;
//import com.ainemo.sdk.otf.MakeCallResponse;
//import com.ainemo.sdk.otf.NemoSDK;
//import com.ainemo.sdk.otf.NemoSDKInitCallBack;
//import com.ainemo.sdk.otf.Orientation;
//import com.ainemo.sdk.otf.ResolutionRatio;
//import com.ainemo.sdk.otf.Roster;
//import com.ainemo.sdk.otf.RosterWrapper;
//import com.ainemo.sdk.otf.Settings;
//import com.ainemo.sdk.otf.SimpleNemoSDkListener;
//import com.ainemo.sdk.otf.VideoConfig;
//import com.ainemo.sdk.otf.VideoInfo;
//import com.ainemo.util.JsonUtil;
//import com.aries.library.fast.retrofit.FastObserver;
//import com.aries.library.fast.util.ToastUtil;
//import com.aries.template.GlobalConfig;
//import com.aries.template.MainActivity;
//import com.aries.template.R;
//import com.aries.template.entity.RoomIdInsAuthEntity;
//import com.aries.template.retrofit.repository.ApiRepository;
//import com.aries.template.utils.DefenceUtil;
//import com.aries.template.xiaoyu.dapinsocket.SocThread;
//import com.aries.template.xiaoyu.meeting.MeetingVideoCell;
//import com.aries.template.xiaoyu.model.EndPoint;
//import com.aries.template.xiaoyu.model.RegEndPoint;
//import com.aries.template.xiaoyu.model.RegReponse;
//import com.aries.template.xiaoyu.model.RegRequest;
//import com.aries.template.xiaoyu.model.RtcStartInvokeRequest;
//import com.aries.template.xiaoyu.uvc.UVCCameraPresenter;
//import com.aries.template.xiaoyu.xinlin.XLMessage;
//import com.hyphenate.EMCallBack;
//import com.hyphenate.EMMessageListener;
//import com.hyphenate.chat.EMClient;
//import com.hyphenate.chat.EMMessage;
//import com.hyphenate.chat.EMOptions;
//import com.tbruyelle.rxpermissions2.RxPermissions;
//import com.trello.rxlifecycle3.android.ActivityEvent;
//
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.ref.WeakReference;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import tech.gusavila92.websocketclient.WebSocketClient;
//
///******
// * 环信 + 信令 + 小鱼
// * 点对点直播
// * 这的所有请求都是第三方的请求，不会包含我们自己的请求在内
// *
// * @author  ::: louis luo
// * Date ::: 2022/6/30 2:26 PM
// *
// * todo 提示医生患者挂断视频
// *
// */
//public class EaseModeProxy2 {
//
//    //弱应用 activity
//    private WeakReference<Activity> activity;
//    // 非全屏时，MeetingVideoCell 的容器
//    private ViewGroup contentLayout;
//    // 用来显示医生界面的cell
//    private MeetingVideoCell videoCell;
//    // 是否静音
//    private boolean muteMic=false;
//    // 是否关闭画面
//    private boolean muteVideo=false;
//
//
//    // 信令 Socket 对象, 这个socket 不需要设置心跳包
//    private WebSocketClient webSocketClient;
//    // 向大屏进行通讯的socket，不需要设置心跳包
//    private SocThread dapinSocket;
//    // 使用 UCV
//    private UVCCameraPresenter uvcCameraPresenter;
//
//    // 复诊单
//    // 复诊单号，用于请求小鱼房间号和密码
//    private Integer consultId = 815423874; //复诊单id 复诊单拿
//    private String nickname = "eric"; //复诊人姓名 复诊单拿
//    private String doctorUserId = "627a861baa36e516a612dc80"; //医生userId 复诊单拿
//
//    // 环信的配置
//    private String easemobUserName = "dev_patient_5494620"; //环信用户id getConfigurationToThirdForPatient
//    private String easemobPassword = "patient123"; //固定值，不用改 getConfigurationToThirdForPatient
//
//    // 信令的配置
//    private String xlPatientUserId = "627dd085cc2f202b1d2146f3"; //用户userId getConfigurationToThirdForPatient
////    private final String XL_URL = "ws://172.21.1.95:9090/";
//    private final String XL_URL = "wss://app-DEV.ngarihealth.com/";
//
//    //------------小鱼的配置
//    private static final String xyAppId = "5886885697deb9f4760b3a5e1ab912b9a3b7dfd3"; //小鱼appid 固定
//    private String account = "8827"; //患者小鱼id，实际上是从信令获取到的
//    private String meetingRoomNumber = "9038284649"; //会议室房间号,从接口获取到的 还没有
//    private String meetingPassword = "348642"; //会议室密码，从接口获取到的 还没有
//
//
//    //单例
//    private static volatile EaseModeProxy2 sInstance;
//    // 环信 callback
//    private EMCallBack emcallback;
//    // 是否已经进入启动视频问诊
//    private boolean isEasemodStarted = false;
//    // 是否监听到了医生发送了消息
//    private boolean isDoctorMessaged = false;
//    // 医生是否进入过
////    private boolean isDoctorEnterRoom=false;
//    // 信令专用信息传送器
//    private XLMessage xlMessage;
//
//    // 环信监听会执行这个方法，当他在视频外被执行时isEasemodStarted为false，无法打开socket但是会更改参数
//    public void setDoctorMessaged(boolean doctorMessaged) {
//        if (isEasemodStarted) {
//            try {
//                createWebSocketClient();
//            } catch (URISyntaxException e) {
//                e.printStackTrace();
//            }
//        }
//        isDoctorMessaged = doctorMessaged;
//    }
//
//    private EaseModeProxy2() {
//    }
//    public static EaseModeProxy2 with() {
//        if (sInstance == null) {
//            synchronized (EaseModeProxy2.class){
//                if (sInstance==null)
//                    sInstance = new EaseModeProxy2();
//            }
//        }
//        return sInstance;
//    }
//
//    /**
//     * 弱引用配置 activity
//     */
//    public void setActivity(Activity activity){
//        this.activity = new WeakReference<>(activity);
//    }
//
//    /**
//     * easeMode初始化 视频三方组件
//     * 这个初始化必须写在 application 里面
//     * 这个初始化只全局初始化一次
//     */
//    public void initInAPP(Context context) {
//        //参看：https://docs-im.easemob.com/im/android/sdk/basic
//        EMOptions options = new EMOptions();
//        // 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
//        options.setAutoTransferMessageAttachments(false);
//        // 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
//        options.setAutoDownloadThumbnail(true);
//        //初始化
//        EMClient.getInstance().init(context, options);
//        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
//        EMClient.getInstance().setDebugMode(true);
//
////        // 用于 EMClient 登录初始化
////        EMClient.getInstance().groupManager().loadAllGroups();
////        EMClient.getInstance().chatManager().loadAllConversations();
//
//        //如果没有，则继续初始化
//        Settings settings = new Settings(xyAppId);
//        settings.setPrivateCloudAddress("cloud.xylink.com");
//        settings.setVideoMaxResolutionTx(VideoConfig.VD_1280x720);
//        settings.setDefaultCameraId(1);
//
//        // 初始化 NEmoSDK
//        NemoSDK.getInstance().init(context, settings, new NemoSDKInitCallBack() {
//            @Override
//            public void nemoSdkInitSuccess() {
////                ToastWithLogin("初始化成功，开始登陆");
//            }
//
//            @Override
//            public void nemoSdkInitFail(String s, String s1) {
//                ToastWithLogin(s + " " + s1);
//            }
//        });
//
//        // 启动之初执行释放，防止由于错误原因导致的没有释放资源
//        releaseProxy();
//
//        // GPUS
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NemoSDK.getInstance().getGpusInfo(gpus -> {
//                //ThirdPart filter logic
//                NemoSDK.getInstance().setEnableGPUs(gpus);
//            });
//        }
//    }
//
//    /**
//     * 初始化参数
//     * 和 onStartVideo 一起放在 onstart上面执行
//     * 这个初始化，是初始化界面用的，写在activity或者 fragment里面
//     * @param inputAc 初始化 activity对象，全局使用
//     * @param layout 初始化 存储显示对象容器
//     */
//    public EaseModeProxy2 initView(Activity inputAc, ViewGroup layout){
//        setActivity(inputAc);
//        contentLayout = layout;
//        uvcCameraPresenter = new UVCCameraPresenter(activity.get());
//        return this;
//    }
//
//    /**
//     * 启动摄像头
//     * 在Fragement的 onstart 上面执行，和init 一起执行
//     * EaseModeProxy.with().init(getActivity(),viewGroup).onStartVideo();
//     */
//    public void onStartVideo(){
//        if (uvcCameraPresenter!=null){
//            uvcCameraPresenter.onStart();
//        }
//    }
//
//
//
//    /**
//     * 专门登录环信
//     */
//    public void loginEmClient(){
//        //注册监听消息的回调地址 参看：https://docs-im.easemob.com/im/android/basics/message#%E6%8E%A5%E6%94%B6%E6%B6%88%E6%81%AF
//        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
//            @Override
//            public void onMessageReceived(List<EMMessage> messages) {
//                activity.get().runOnUiThread(() -> {
//                    //接收消息
//                    for (EMMessage message : messages) {
//                        Log.d("main", "onMessageReceived " + message.toString());
//                        Map<String, Object> ext = message.ext();
//                        if (ext == null) {
//                            continue;
//                        }
//                        Object msgTypeObj = ext.get("msgType");
//                        if (msgTypeObj == null) {
//                            continue;
//                        }
//                        String msgType = (String) msgTypeObj;
//                        if (msgType == null || !msgType.equals("26")) {
//                            continue;
//                        }
//                        // 信令请求 socket 没有心跳，返回医生请求状态
////                        setDoctorMessaged(true);
//                        //todo 提示患者，医生挂断视频
//                        try {
//                            createWebSocketClient();
//                        } catch (URISyntaxException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onCmdMessageReceived(List<EMMessage> messages) {
//            }
//
//            @Override
//            public void onMessageRead(List<EMMessage> messages) {
//            }
//
//            @Override
//            public void onMessageDelivered(List<EMMessage> messages) {
//            }
//
//            @Override
//            public void onMessageRecalled(List<EMMessage> messages) {
//            }
//        });
//
//        //登陆 https://docs-im.easemob.com/im/android/sdk/basic
////        EMClient.getInstance().logout(true);
//        emcallback = new EMCallBack() {//回调
//            @Override
//            public void onSuccess() {
//                EMClient.getInstance().groupManager().loadAllGroups();
//                EMClient.getInstance().chatManager().loadAllConversations();
//                ToastWithLogin("登录环信聊天服务器成功");
//            }
//
//            @Override
//            public void onProgress(int progress, String status) {
//            }
//
//            @Override
//            public void onError(int code, String message) {
//                ToastWithLogin("登录环信聊天服务器失败, message:" + message);
//            }
//        };
//        // 防止用户由于特殊原因登出，然后再进来的时候，被提示已经登录
//        EMClient.getInstance().logout(true);
//        EMClient.getInstance().login(easemobUserName, easemobPassword,emcallback );
//    }
//
//    /**
//     * 启动视频问诊
//     */
//    public void start(String consultId,String nickname,String doctorUserId){
//        // 获取第三方信息
////        requestConfigurationToThirdForPatient(consultId, nickname, doctorUserId);
//    }
//
//    /**
//     * 启动视频问诊
//     * 如果不初始化，会导致 MeetingVideoCell 崩溃
//     * 医生端有消息返回后，才会调用onMessageReceived 否则视作医生无响应。
//     * @param consultId 复诊单id
//     * @param nickname 复诊人姓名
//     * @param doctorUserId 医生userId
//     * @param userName 环信的用户名
//     * @param password 环信的密码
//     * @param xlPatientUserId 信令的用户ID
//     */
//    public void easemobStart(Activity inputAc,
//                             String consultId,
//                             String nickname,
//                             String doctorUserId,
//                             String userName,
//                             String password,
//                             String xlPatientUserId) {
//        if (consultId==null ||nickname==null ||doctorUserId==null ||userName==null || password==null || xlPatientUserId==null)
//            return;
//        // 设置成员变量
//        this.consultId = Integer.valueOf(consultId);
//        this.nickname = nickname;
//        this.doctorUserId = doctorUserId;
//        easemobUserName = userName;
//        easemobPassword = password;
//        this.xlPatientUserId = xlPatientUserId;
//
//        // 添加 Activity 弱引用
//        setActivity(inputAc);
//        isEasemodStarted = true;
//
//        loginEmClient();
//
////        if (isDoctorMessaged){
////            try {
////                createWebSocketClient();
////            } catch (URISyntaxException e) {
////                e.printStackTrace();
////            }
////        }
//    }
//
//    /**
//     * 信令登录
//     * 创造socket 常链接
//     * 获取房间信息，房间 account
//     */
//    public void createWebSocketClient() throws URISyntaxException {
//        URI uri = new URI(XL_URL);
//        webSocketClient = new WebSocketClient(uri) {
//            @Override
//            public void onOpen() {
//                Log.i("WebSocket", "Session is starting");
//
//                //连接成功服务器后必须要注册
//                //{"topic":"REG","endPoint":{"userId":"62933bcbcf8912669abf4b98","roleId":"patient","appVersion":"4.1.1","isInVideo":0,"appType":4}}
//                RegEndPoint regEndPoint = new RegEndPoint();
//                regEndPoint.setUserId(xlPatientUserId);
//                regEndPoint.setRoleId("patient");
//                regEndPoint.setAppVersion("4.1.1");
//                regEndPoint.setIsInVideo(0);
//                regEndPoint.setAppType(4);
//
//                RegRequest regRequest = new RegRequest();
//                regRequest.setTopic("REG");
//                regRequest.setEndPoint(regEndPoint);
//                String registerMsg = JsonUtil.toJson(regRequest);
//                webSocketClient.send(registerMsg);
//            }
//
//            @Override
//            public void onTextReceived(String s) {
//                final String message = s;
//                ToastWithLogin(message);
//                activity.get().runOnUiThread(() -> {
//                    // todo：这里改成用json来解析
//                    if (message.contains("REG_SUCCESS")) {
//                        onRegSuccess(message);
//                    }else if (message.contains("TX_RTC_SHUTDOWN_RES")){
//                        // 医生已经离开
//                        // TX_RTC_SHUTDOWN_RES
//                        ToastWithLogin("医生已经离开");
//                    }
//                });
//            }
//
//            @Override
//            public void onBinaryReceived(byte[] data) {
//            }
//
//            @Override
//            public void onPingReceived(byte[] data) {
//            }
//
//            @Override
//            public void onPongReceived(byte[] data) {
//            }
//
//            @Override
//            public void onException(Exception e) {
//                System.out.println(e.getMessage());
//            }
//
//            @Override
//            public void onCloseReceived() {
//                Log.i("WebSocket", "Closed ");
//                System.out.println("onCloseReceived");
//            }
//        };
//        webSocketClient.setConnectTimeout(10000);
//        webSocketClient.setReadTimeout(60000);
//        webSocketClient.enableAutomaticReconnection(5000);
//        webSocketClient.connect();
//    }
//
//    /**
//     *  登录成功
//     */
//    private void onRegSuccess(String regResponseMsg) {
//        //{"topic":"REG_SUCCESS","payload":70893}
//        //注册成功后，获取于用户在小鱼的第三方账号
//        RegReponse regReponse = JsonUtil.toObject(regResponseMsg, RegReponse.class);
//        Integer payLoad = regReponse.getPayload();
//        account = String.valueOf(payLoad);
//        xyInit();
//    }
//
//    /**
//     * 小鱼登录启动初始化配置
//     * todo 可能需要在这里请求房间号和密码
//     */
//    public void xyInit() {
//        // 如果没有uvc则使用前置摄像头，包括中途拔出 USB 摄像头的情况
//        // 不能写在 onstart 上面，会让USB摄像头使用无效。
//        if (!uvcCameraPresenter.hasUvcCamera()) {
//            NemoSDK.getInstance().switchCamera(0);
//        }
//
//        // 初始化界面
//        // 设置手机系统方向
//        NemoSDK.getInstance().setOrientation(Orientation.LANDSCAPE);
//
//        // 创建显示对象
//        videoCell = new MeetingVideoCell(activity.get());
//        contentLayout.addView(videoCell);
//
//        // 启动小鱼登录
//        // 这个启动要在小鱼 NemoSDK.getInstance().init 执行之后
//        // 反重复提交
//        if (DefenceUtil.checkReSubmit("EaseModeProxy.xyThirdPartyLogin")){
//            xyThirdPartyLogin(account, nickname);
//        }
////        xyThirdPartyLogin(account, nickname);
//    }
//
//    /**
//     * 开始登录 小鱼
//     */
//    private void xyThirdPartyLogin(String account, String nickname) {
//        if (activity==null)
//            return;
//        activity.get().runOnUiThread(() -> {
////            NemoSDK.getInstance().logout();
//            NemoSDK.getInstance().loginExternalAccount(nickname, account, new ConnectNemoCallback() {
//                @Override
//                public void onFailed(String s) {
//                    ToastWithLogin("登录失败"+s);
//                }
//
//                @Override
//                public void onSuccess(LoginResponseData loginResponseData, boolean b) {
//                    ToastWithLogin("登录成功");
//                    requestGetRoomIdInsAuth(String.valueOf(consultId));
//                }
//
//                @Override
//                public void onNetworkTopologyDetectionFinished(LoginResponseData loginResponseData) {
//                    ToastWithLogin("登录成功，网络探测结束");
//                }
//            });}
//        );
//    }
//
//    /**
//     * 查询复诊单的小鱼视频会议室房间号和密码
//     */
//    private void requestGetRoomIdInsAuth(String consultId){
//        ApiRepository.getInstance().getRoomIdInsAuth(consultId, GlobalConfig.NALI_APPKEY)
//                .compose(((MainActivity) activity.get()).bindUntilEvent(ActivityEvent.DESTROY))
//                .subscribe(new FastObserver<RoomIdInsAuthEntity>() {
//                    @Override
//                    public void _onNext(RoomIdInsAuthEntity entity) {
//                        if (entity == null) {
//                            ToastUtil.show("请检查网络");
//                            return;
//                        }
//                        if (entity.getData().isSuccess()){
//                            if (entity.getData().getJsonResponseBean().getBody()!=null){
//                                ToastWithLogin("获取房间信息成功");
//                                meetingRoomNumber = entity.getData().getJsonResponseBean().getBody().getDetail().getMeetingNumber();
//                                meetingPassword = entity.getData().getJsonResponseBean().getBody().getDetail().getControlPassword();
//                                xyJoinMeeting(meetingRoomNumber, meetingPassword);
//                            }
//                        }
//                    }
//                });
//    }
//
//    /**
//     * 启动会议权限检测
//     */
//    @SuppressLint("CheckResult")
//    private void xyJoinMeeting(String meetingNumber, String meetingPassword) {
//        if (activity==null)
//            return;
//        activity.get().runOnUiThread(() -> {
//            RxPermissions permissions = new RxPermissions(((FragmentActivity) activity.get()));
//            permissions.request(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
//                    .subscribe(aBoolean -> {
//                        if (aBoolean) {
//                            NemoSDK.getInstance().makeCall(meetingNumber, meetingPassword,new MakeCallResponse() {
//                                @Override
//                                public void onCallSuccess() {
//                                    ToastWithLogin("参会成功");
//
////                                    //渲染local画面
////                                    VideoInfo videoInfo = new VideoInfo();
////                                    videoInfo.setDataSourceID(NemoSDK.getLocalVideoStreamID());
////                                    videoInfo.setAudioMute(muteMic);
////                                    videoInfo.setVideoMute(muteVideo);
////                                    videoCell.setVideoInfo(videoInfo);// 启动本地视频窗口
//
//                                    // 向医生端口发送消息
//                                    sendNotifyDoctorVideoMsg();
//
//                                    // 开始入会
//                                    onJoinMeetingOk();
//                                }
//
//                                @Override
//                                public void onCallFail(String s, String s1) {
//                                    ToastWithLogin("参会不成功"+s + " " + s1);
//                                }
//                            });
//                        } else {
//                            ToastWithLogin( "请打开摄像机和麦克风权限 ");
//                        }
//                    });
//        });
//    }
//
//    /**
//     * 启动会议
//     */
//    public void onJoinMeetingOk() {
////        activity.get().runOnUiThread(() -> {
////            // 会不会造成内存泄漏
////            Handler mainThreadHandler = new Handler();
////            NemoSDK.getInstance().setNemoSDKListener(new SimpleNemoSDkListener() {
////                @Override
////                public void onCallStateChange(CallState state, String reason) {
////                    //1.监听会议状态
////                    mainThreadHandler.post(() -> {
////
////                    });
////                    activity.get().runOnUiThread(()->{
////                        if (state == CallState.CONNECTED) {
////                            ToastWithLogin("入会成功: ");
////                            // 可能会造成多次入会成功
////                            if (listener!=null)
////                                listener.onVideoSuccessLinked();
////                        } else if (state == CallState.DISCONNECTED) {
////                            ToastWithLogin("退出会议: " + reason);
////                            // 释放资源一定要写在退出会议的后面
////                            releaseProxy();
////                        }
////                    });
////                }
////
////                @Override
////                public void onRosterChange(RosterWrapper rosterWrapper) {
////                    //参会者信息回调
////                    mainThreadHandler.post(() -> {
////                        //2.计算请流集合
////                        NemoSDK.getInstance().setLayoutBuilder(policy -> {
////                            List<LayoutElement> elements = new ArrayList<>();
////                            //请求所有参会者
////                            ArrayList<Roster> rosters = rosterWrapper.getRosters();
////                            if (rosters != null && !rosters.isEmpty()) {
////                                LayoutElement element;
////                                for (Roster roster : rosters) {
////                                    element = new LayoutElement();
////                                    element.setParticipantId(roster.getParticipantId());
////                                    element.setResolutionRatio(ResolutionRatio.RESO_360P_NORMAL);
////                                    elements.add(element);
////                                }
////                            }
////                            return elements;
////                        });
////                    });
////                }
////
////                @Override
////                public void onVideoStatusChange(int videoStatus) {
////                    super.onVideoStatusChange(videoStatus);
////                    // 提示用户当前信息
////                    showVideoStatusChange(videoStatus);
////                }
////
////                @Override
////                public void onVideoDataSourceChange(List<VideoInfo> videoInfos, boolean hasVideoContent) {
////                    mainThreadHandler.post(() -> {
////                        if (videoInfos.size()>0){
//////                            if (videoCell !=null)
//////                                videoCell.setVideoInfo(videoInfos.get(0));
////                        }
////                    });
////                }
////            });
////        });
//
//        NemoSDK.getInstance().setNemoSDKListener(new SimpleNemoSDkListener() {
//            @Override
//            public void onCallStateChange(CallState state, String reason) {
//                //1.监听会议状态
//                if (state == CallState.CONNECTED) {
////                        ToastWithLogin("入会成功: ");
//                    Log.e("TAG", "onCallStateChange: 入会成功");
//                    // 可能会造成多次入会成功
//                    if (listener!=null)
//                        listener.onVideoSuccessLinked();
//                } else if (state == CallState.DISCONNECTED) {
////                        ToastWithLogin("退出会议: " + reason);
//                    // 释放资源一定要写在退出会议的后面
//                    releaseProxy();
//                }
//            }
//
//            @Override
//            public void onRosterChange(RosterWrapper rosterWrapper) {
//                //参会者信息回调
//                    //2.计算请流集合
//                    NemoSDK.getInstance().setLayoutBuilder(policy -> {
//                        List<LayoutElement> elements = new ArrayList<>();
//                        //请求所有参会者
//                        ArrayList<Roster> rosters = rosterWrapper.getRosters();
//                        if (rosters != null && !rosters.isEmpty()) {
//                            LayoutElement element;
//                            for (Roster roster : rosters) {
//                                element = new LayoutElement();
//                                element.setParticipantId(roster.getParticipantId());
//                                element.setResolutionRatio(ResolutionRatio.RESO_360P_NORMAL);
//                                elements.add(element);
//                            }
//                        }
//                        return elements;
//                    });
//            }
//
//            @Override
//            public void onVideoStatusChange(int videoStatus) {
//                super.onVideoStatusChange(videoStatus);
//                // 提示用户当前信息
//                showVideoStatusChange(videoStatus);
//            }
//
//            @Override
//            public void onVideoDataSourceChange(List<VideoInfo> videoInfos, boolean hasVideoContent) {
//                    if (videoInfos.size()>0){
//                        if (videoCell !=null)
//                                videoCell.setVideoInfo(videoInfos.get(0));
//                        if (listener!=null)
//                            listener.onDoctorInRoom();
////                        isDoctorEnterRoom = true;
//                    }
////                    else if (videoInfos.size()==0){
////                        // 现在的房间没有其他人了
////                        if (isDoctorEnterRoom){
////                            // 医生曾进入过
////                            ToastUtil.show("医生已经离开");
////                        }
////                    }
//            }
//        });
//    }
//
//    /**
//     * 信令 通信
//     * 向Docotor的视频端发送用户信息。
//     */
//    private void sendNotifyDoctorVideoMsg() {
//        if (webSocketClient==null) {
//            try {
//                createWebSocketClient();
//            } catch (URISyntaxException e) {
//                e.printStackTrace();
//            }
//        }
//        ToastWithLogin("TX_RTC_START_INVOKE");
//        //{"topic":"TX_RTC_START_INVOKE","endPoint":{"patientUserId":"62933bcbcf8912669abf4b98","doctorUserId":"5f339ceb9cd0500a923af577","patientName":"胡江","remark":"未知","orderId":815463559,"roomId":"910007727377","thirdAppVideoConsult":"xyLink","requestMode":"4"}}
//        RtcStartInvokeRequest request = new RtcStartInvokeRequest();
//        request.setTopic("TX_RTC_START_INVOKE");
//        EndPoint endPoint = new EndPoint();
//        endPoint.setPatientUserId(xlPatientUserId);
//        endPoint.setPatientName(nickname.trim());
//        endPoint.setDoctorUserId(doctorUserId);
//        endPoint.setOrderId(consultId);
//        endPoint.setRemark("未知");
//        endPoint.setRoomId(meetingRoomNumber);
//        endPoint.setThirdAppVideoConsult("xyLink");
//        endPoint.setRequestMode("4");
//
//        request.setEndPoint(endPoint);
//        String cmd = JsonUtil.toJson(request);
//
//        webSocketClient.send(cmd);
//    }
//
//    /**
//     * 用户已经离开，进入支付
//     */
//    private void sendNotifyPaintLiveMsg(){
//        if (webSocketClient==null) {
//            try {
//                createWebSocketClient();
//            } catch (URISyntaxException e) {
//                e.printStackTrace();
//            }
//        }else{
//            webSocketClient.connect();
//        }
//        Map<String, Object> endPoint = new HashMap<>();
//        endPoint.put("doctorUserId",doctorUserId);
//        endPoint.put("patientUserId",xlPatientUserId);
//        endPoint.put("role","patient");
//        endPoint.put("thirdAppVideoConsult","xyLink");
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("topic","TX_RTC_SHUTDOWN");
//        map.put("endPoint",endPoint);
//
//        String cmd = JsonUtil.toJson(endPoint);
//        webSocketClient.send(cmd);
//    }
//
//    /**
//     * 挂断视频
//     */
//    public void closeVideoProxy(){
//        // 病人离席
//        // todo 向大屏幕socket传递离开信息
//        // 如果医生没有进入房间
//        if (!NemoSDK.getInstance().inCalling()){
//            releaseProxy();
//        }
//        // 如果医生已经进入房间
//        NemoSDK.getInstance().hangup();// 挂断通话
//    }
//
//    /**
//     * 释放资源
//     */
//    private void releaseProxy(){
//        // 告诉医生，你已经离开了
//        sendNotifyPaintLiveMsg();
//        // 释放对象资源
//        if (uvcCameraPresenter!=null){
//            uvcCameraPresenter.onStop();
//            uvcCameraPresenter.onDestroy();
//            Log.e("TAG", "releaseProxy: done" );
//        }
//        if (activity!=null)
//            activity = null;
//        if (contentLayout !=null)
//            contentLayout = null;
//        if (videoCell !=null){
//            videoCell = null;
//        }
//        // 释放 webSoekctClient 信令
//        if (webSocketClient!=null){
//            webSocketClient.close();
//            webSocketClient = null;
//        }
//        // 释放 环信
//        if (emcallback!=null)
//            EMClient.getInstance().logout(true,emcallback);
//
//        // 释放小鱼
//        NemoSDK.getInstance().setNemoSDKListener(null);
//        NemoSDK.getInstance().releaseLayout();
//        NemoSDK.getInstance().releaseCamera();
//        NemoSDK.getInstance().releaseAudioMic();
//    }
//
//    /**
//     * 打印并提示用户
//     */
//    private void ToastWithLogin(String msg){
//        if (activity!=null && activity.get()!=null){
//            Log.d("EaseModeProxy",msg);
//            activity.get().runOnUiThread(() -> Toast.makeText(activity.get(), msg, Toast.LENGTH_SHORT).show());
//        }
//    }
//
//    /**
//     * 启动全屏
//     * 想法是这样的，当启动全屏的时候，容器会把 videoCell 放进这个容器
//     * 所以这个容器其实是个全屏的容器
//     * @param viewGroup 全屏的容器对象
//     */
//    public void doFullScreen(ViewGroup viewGroup){
//        if (viewGroup==null)
//            return;
//        if (videoCell!=null && videoCell.getParent()!=null)
//            ((ViewGroup) videoCell.getParent()).removeView(videoCell);
//        viewGroup.addView(videoCell);
//    }
//
//    /**
//     * 从全屏返回
//     * 会将 videoCell 方回到原来的容器里面
//     */
//    public void doNotFullScreen(){
//        if (videoCell!=null && videoCell.getParent()!=null)
//            ((ViewGroup) videoCell.getParent()).removeView(videoCell);
//        contentLayout.addView(videoCell);
//    }
//
//    /**
//     * 小鱼网络状态值对照表
//     */
//    @IntDef({
//            VideoStatus.VIDEO_STATUS_NORMAL, VideoStatus.VIDEO_STATUS_LOW_AS_LOCAL_BW,
//            VideoStatus.VIDEO_STATUS_LOW_AS_LOCAL_HARDWARE, VideoStatus.VIDEO_STATUS_LOW_AS_REMOTE,
//            VideoStatus.VIDEO_STATUS_NETWORK_ERROR, VideoStatus.VIDEO_STATUS_LOCAL_WIFI_ISSUE
//    })
//    @Retention(RetentionPolicy.SOURCE)
//    public @interface VideoStatus {
//        int VIDEO_STATUS_NORMAL = 0;
//        int VIDEO_STATUS_LOW_AS_LOCAL_BW = 1;
//        int VIDEO_STATUS_LOW_AS_LOCAL_HARDWARE = 2;
//        int VIDEO_STATUS_LOW_AS_REMOTE = 3;
//        int VIDEO_STATUS_NETWORK_ERROR = 4;
//        int VIDEO_STATUS_LOCAL_WIFI_ISSUE = 5;
//    }
//
//    /**
//     * 小鱼视频网络状态提示
//     * @param videoStatus 状态值
//     */
//    public void showVideoStatusChange(int videoStatus) {
//        if (activity==null)
//            return;
//        activity.get().runOnUiThread(() -> {
//            if (videoStatus == VideoStatus.VIDEO_STATUS_NORMAL) {
//                ToastWithLogin( activity.get().getString(R.string.video_status_normal));
//            } else if (videoStatus == VideoStatus.VIDEO_STATUS_LOW_AS_LOCAL_BW) {
//                ToastWithLogin( activity.get().getString(R.string.video_status_as_low_local_bw));
//            } else if (videoStatus == VideoStatus.VIDEO_STATUS_LOW_AS_LOCAL_HARDWARE) {
//                ToastWithLogin( activity.get().getString(R.string.video_status_as_low_local_hardware));
//            } else if (videoStatus == VideoStatus.VIDEO_STATUS_LOW_AS_REMOTE) {
//                ToastWithLogin( activity.get().getString(R.string.video_status_as_low_local_remote));
//            } else if (videoStatus == VideoStatus.VIDEO_STATUS_NETWORK_ERROR) {
//                ToastWithLogin( activity.get().getString(R.string.video_status_network_error));
//            } else if (videoStatus == VideoStatus.VIDEO_STATUS_LOCAL_WIFI_ISSUE) {
//                ToastWithLogin( activity.get().getString(R.string.video_status_local_wifi_issue));
//            }
//        });
//    }
//
//    /** 对外监听 */
//    private ProxyEventListener listener;
//
//    /** 对外监听设置入口 */
//    public void setListener(ProxyEventListener listener) {
//        this.listener = listener;
//    }
//
//    /**
//     * 对外监听
//     */
//    public interface ProxyEventListener{
//        default void onVideoSuccessLinked(){};// 入会成功
//        default void onDoctorInRoom(){};//医生进入的时候，将医生的 video Info 给予外部
//    }
//
//
////    /**
////     * 获取第三方配置信息
////     */
////    private void requestConfigurationToThirdForPatient(String inputconsultId,String inputnickname,String inputdoctorUserId){
////        ApiRepository.getInstance().getConfigurationToThirdForPatient(GlobalConfig.NALI_TID,GlobalConfig.NALI_APPKEY)
////                .compose(((MainActivity) activity.get()).bindUntilEvent(ActivityEvent.DESTROY))
////                .subscribe(new FastLoadingObserver<ConfigurationToThirdForPatientEntity>("请稍后...") {
////                    @Override
////                    public void _onNext(ConfigurationToThirdForPatientEntity entity) {
////                        if (entity == null) {
////                            ToastUtil.show("请检查网络");
////                            return;
////                        }
////                        if (entity.getData().isSuccess()){
////                            consultId = inputconsultId;
////                            nickname = nickname;
////                            doctorUserId = doctorUserId;
////                            easemobUserName = userName;
////                            easemobPassword = password;
////                            this.xlPatientUserId = xlPatientUserId;
////                            EaseModeProxy.with().easemobStart(activity.get(),
////                                    consultId,
////                                    nickname,
////                                    doctorUserId,
////                                    entity.getData().getJsonResponseBean().getBody().getUsername(),
////                                    entity.getData().getJsonResponseBean().getBody().getUserpwd(),
////                                    entity.getData().getJsonResponseBean().getBody().getUserId());
////                        }
////                    }
////                });
////    }
//}