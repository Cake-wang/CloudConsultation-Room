package com.aries.template.xiaoyu;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.IntDef;
import androidx.fragment.app.FragmentActivity;

import com.ainemo.sdk.otf.ConnectNemoCallback;
import com.ainemo.sdk.otf.LayoutElement;
import com.ainemo.sdk.otf.LoginResponseData;
import com.ainemo.sdk.otf.MakeCallResponse;
import com.ainemo.sdk.otf.NemoSDK;
import com.ainemo.sdk.otf.NemoSDKInitCallBack;
import com.ainemo.sdk.otf.ResolutionRatio;
import com.ainemo.sdk.otf.Roster;
import com.ainemo.sdk.otf.RosterWrapper;
import com.ainemo.sdk.otf.Settings;
import com.ainemo.sdk.otf.SimpleNemoSDkListener;
import com.ainemo.sdk.otf.VideoConfig;
import com.ainemo.sdk.otf.VideoInfo;
import com.ainemo.util.JsonUtil;
import com.aries.library.fast.retrofit.FastObserver;
import com.aries.library.fast.util.ToastUtil;
import com.aries.template.GlobalConfig;
import com.aries.template.MainActivity;
import com.aries.template.R;
import com.aries.template.entity.RoomIdInsAuthEntity;
import com.aries.template.retrofit.repository.ApiRepository;
import com.aries.template.utils.DefenceUtil;
import com.aries.template.xiaoyu.meeting.MeetingVideoCell;
import com.aries.template.xiaoyu.model.RegReponse;
import com.aries.template.xiaoyu.uvc.UVCAndroidCameraPresenter;
import com.aries.template.xiaoyu.xinlin.XLMessage;
import com.aries.template.xiaoyu.xinlin.XLSend;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/******
 * 环信 + 信令 + 小鱼
 * 点对点直播
 * 这的所有请求都是第三方的请求，不会包含我们自己的请求在内
 *
 * @author  ::: louis luo
 * Date ::: 2022/6/30 2:26 PM
 *
 * 提示医生患者挂断视频
 *
 */
public class EaseModeProxy {

    //弱应用 activity
    private WeakReference<Activity> activity;
    // 非全屏时，MeetingVideoCell 的容器
    private ViewGroup contentLayout;
    // 用来显示医生界面的cell
    private MeetingVideoCell videoCell;
    // 是否静音
    private boolean muteMic=false;
    // 是否关闭画面
    private boolean muteVideo=false;


    // 信令 Socket 对象, 这个socket 不需要设置心跳包
//    private WebSocketClient webSocketClient;
    // 使用 UCV
    private UVCAndroidCameraPresenter uvcCameraPresenter;

    // 复诊单
    // 复诊单号，用于请求小鱼房间号和密码
    private Integer consultId = 815423874; //复诊单id 复诊单拿
    private String nickname = "eric"; //复诊人姓名 复诊单拿
    private String doctorUserId = "627a861baa36e516a612dc80"; //医生userId 复诊单拿

    // 环信的配置
    private String easemobUserName = "dev_patient_5494620"; //环信用户id getConfigurationToThirdForPatient
    private String easemobPassword = "patient123"; //固定值，不用改 getConfigurationToThirdForPatient

    // 信令的配置
    private String xlPatientUserId = "627dd085cc2f202b1d2146f3"; //用户userId getConfigurationToThirdForPatient
//    private final String XL_URL = "ws://172.21.1.95:9090/";
    private final String XL_URL = "wss://app-DEV.ngarihealth.com/";

    //------------小鱼的配置
    private static final String xyAppId = "5886885697deb9f4760b3a5e1ab912b9a3b7dfd3"; //小鱼appid 固定
    private String account = "8827"; //患者小鱼id，实际上是从信令获取到的
    private String meetingRoomNumber = "9038284649"; //会议室房间号,从接口获取到的 还没有
    private String meetingPassword = "348642"; //会议室密码，从接口获取到的 还没有


    // 环信 callback
    private EMCallBack emcallback;
    // 是否已经进入启动视频问诊
    private boolean isEasemodStarted = false;
    // 医生是否进入过
    private boolean isDoctorEnterRoom=false;

    //单例
    private static volatile EaseModeProxy sInstance;
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
     * 这个初始化必须写在 application 里面
     * 这个初始化只全局初始化一次
     */
    public void initInAPP(Context context) {
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

        //如果没有，则继续初始化
        Settings settings = new Settings(xyAppId);
        settings.setPrivateCloudAddress("cloud.xylink.com");
        settings.setVideoMaxResolutionTx(VideoConfig.VD_1280x720);
        settings.setDefaultCameraId(0);

        // 初始化 NEmoSDK
        NemoSDK.getInstance().init(context, settings, new NemoSDKInitCallBack() {
            @Override
            public void nemoSdkInitSuccess() {
//                ToastWithLogin("初始化成功，开始登陆");
            }

            @Override
            public void nemoSdkInitFail(String s, String s1) {
                ToastWithLogin(s + " " + s1);
            }
        });

        // 启动之初执行释放，防止由于错误原因导致的没有释放资源
        releaseProxy();

        // GPUS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NemoSDK.getInstance().getGpusInfo(gpus -> {
                //ThirdPart filter logic
                NemoSDK.getInstance().setEnableGPUs(gpus);
            });
        }
    }

    /**
     * 初始化参数
     * 和 onStartVideo 一起放在 onstart上面执行
     * 这个初始化，是初始化界面用的，写在activity或者 fragment里面
     * @param inputAc 初始化 activity对象，全局使用
     * @param layout 初始化 存储显示对象容器
     */
    public void onStart(Activity inputAc, ViewGroup layout){
        setActivity(inputAc);
        contentLayout = layout;
        if (uvcCameraPresenter==null){
            uvcCameraPresenter = new UVCAndroidCameraPresenter(activity.get());
            uvcCameraPresenter.onStart();
        }
    }

//    /**
//     * 释放
//     */
//    public void onStop(){
//        if (uvcCameraPresenter!=null){
//            uvcCameraPresenter.USBUnregister();
//        }
//    }

    /**
     * 专门登录环信
     */
    public void loginEmClient(){
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
                        // 信令请求 socket 没有心跳，返回医生请求状态
//                        setDoctorMessaged(true);
                        // 提示患者，医生挂断视频
                        XLMessage.with().init(xlPatientUserId,XL_URL,activity.get()).send(new XLSend().getLoginMsg(xlPatientUserId),
                                msg -> onRegSuccess(msg));
//                        xlMessage = new XLMessage(xlPatientUserId,XL_URL,activity.get());
//                        xlMessage.login(msg -> onRegSuccess(msg));
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
                // 如果是崩溃重新进行问诊，则向环信发送消息
//                try {
//                    if (GlobalConfig.isIntoVideoFromOrder){
//                        // 向 环信投放信息，患者重新进入问诊，请重新视频
//                        // EMClient.getInstance().getCurrentUser()
//                        EMMessage message = EMMessage.createTxtSendMessage("患者重新进入问诊，请重新视频",doctorUserId);
//                        message.setMessageStatusCallback(new EMCallBack() {
//                            @Override
//                            public void onSuccess() {
//                                ToastUtil.show("yes");
//                            }
//
//                            @Override
//                            public void onError(int code, String error) {
//                                ToastUtil.show("no");
//                            }
//
//                            @Override
//                            public void onProgress(int progress, String status) {
//                            }
//                        });
//                        EMClient.getInstance().chatManager().sendMessage(message);
//                        // 重置数据
//                        GlobalConfig.isIntoVideoFromOrder = false;
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
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
        // 防止用户由于特殊原因登出，然后再进来的时候，被提示已经登录
        EMClient.getInstance().logout(true);
        EMClient.getInstance().login(easemobUserName, easemobPassword,emcallback);
    }

    /**
     * 启动视频问诊
     * 如果不初始化，会导致 MeetingVideoCell 崩溃
     * 医生端有消息返回后，才会调用onMessageReceived 否则视作医生无响应。
     * @param consultId 复诊单id
     * @param nickname 复诊人姓名
     * @param doctorUserId 医生userId
     * @param userName 环信的用户名
     * @param password 环信的密码
     * @param xlPatientUserId 信令的用户ID
     */
    public void easemobStart(Activity inputAc,
                             String consultId,
                             String nickname,
                             String doctorUserId,
                             String userName,
                             String password,
                             String xlPatientUserId) {
        if (consultId==null ||nickname==null ||doctorUserId==null ||userName==null || password==null || xlPatientUserId==null)
            return;
        // 设置成员变量
        this.consultId = Integer.valueOf(consultId);
        this.nickname = nickname;
        this.doctorUserId = doctorUserId;
        easemobUserName = userName;
        easemobPassword = password;
        this.xlPatientUserId = xlPatientUserId;

        // 添加 Activity 弱引用
        setActivity(inputAc);

        // 开始登录
        loginEmClient();
    }

    /**
     *  登录成功
     */
    private void onRegSuccess(String regResponseMsg) {
        //{"topic":"REG_SUCCESS","payload":70893}
        //注册成功后，获取于用户在小鱼的第三方账号
        try{
            RegReponse regReponse = JsonUtil.toObject(regResponseMsg, RegReponse.class);
            Integer payLoad = regReponse.getPayload();
            account = String.valueOf(payLoad);
            // 启动小鱼
            xyInit();
        }catch (Exception e){
            e.printStackTrace();
            Log.d("JTJK", "onRegSuccess: "+e.getMessage());
        }
    }
    //{"topic":"TX_RTC_SHUTDOWN_RES","payload":{"role":"patient","doctorUserId":"627dcfd9cc2f204b0217f3a7","thirdAppVideoConsult":"xyLink","patientUserId":"62aa8f36cc2f205c7eaa4536"}}

    /**
     * 小鱼登录启动初始化配置
     * 需要在这里请求房间号和密码
     */
    public void xyInit() {
        // 创建显示对象
        videoCell = new MeetingVideoCell(activity.get());
        contentLayout.addView(videoCell);

        // 启动小鱼登录
        // 这个启动要在小鱼 NemoSDK.getInstance().init 执行之后
        // 反重复提交
        if (DefenceUtil.checkReSubmit("EaseModeProxy.xyThirdPartyLogin")){
            xyThirdPartyLogin(account, nickname);
        }
    }

    /**
     * 开始登录 小鱼
     */
    private void xyThirdPartyLogin(String account, String nickname) {
        if (activity==null && activity.get() == null)
            return;
        activity.get().runOnUiThread(() -> {
            NemoSDK.getInstance().loginExternalAccount(nickname, account, new ConnectNemoCallback() {
                @Override
                public void onFailed(String s) {
                    ToastWithLogin("登录失败"+s);
                }

                @Override
                public void onSuccess(LoginResponseData loginResponseData, boolean b) {
                    ToastWithLogin("登录成功");
                    requestGetRoomIdInsAuth(String.valueOf(consultId));
                }

                @Override
                public void onNetworkTopologyDetectionFinished(LoginResponseData loginResponseData) {
                    ToastWithLogin("登录成功，网络探测结束");
                }
            });}
        );
    }

    /**
     * 查询复诊单的小鱼视频会议室房间号和密码
     */
    private void requestGetRoomIdInsAuth(String consultId){
        ApiRepository.getInstance().getRoomIdInsAuth(consultId, GlobalConfig.NALI_APPKEY)
                .compose(((MainActivity) activity.get()).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<RoomIdInsAuthEntity>() {
                    @Override
                    public void _onNext(RoomIdInsAuthEntity entity) {
                        if (entity == null) {
                            ToastUtil.show("请检查网络");
                            return;
                        }
                        if (entity.getData().isSuccess()){
                            if (entity.getData().getJsonResponseBean().getBody()!=null){
                                ToastWithLogin("获取房间信息成功");
                                meetingRoomNumber = entity.getData().getJsonResponseBean().getBody().getDetail().getMeetingNumber();
                                meetingPassword = entity.getData().getJsonResponseBean().getBody().getDetail().getControlPassword();
                                xyJoinMeeting(meetingRoomNumber, meetingPassword);
                            }
                        }
                    }
                });
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
                            NemoSDK.getInstance().makeCall(meetingNumber, meetingPassword,new MakeCallResponse() {
                                @Override
                                public void onCallSuccess() {
                                    ToastWithLogin("参会成功");

//                                    //渲染local画面
//                                    VideoInfo videoInfo = new VideoInfo();
//                                    videoInfo.setDataSourceID(NemoSDK.getLocalVideoStreamID());
//                                    videoInfo.setAudioMute(muteMic);
//                                    videoInfo.setVideoMute(muteVideo);
//                                    videoCell.setVideoInfo(videoInfo);// 启动本地视频窗口

                                    // 向医生端口发送消息
                                    sendNotifyDoctorVideoMsg();

                                    // 开始入会
                                    onJoinMeetingOk();
                                }

                                @Override
                                public void onCallFail(String s, String s1) {
                                    ToastWithLogin("参会不成功"+s + " " + s1);
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
    public void onJoinMeetingOk() {
        NemoSDK.getInstance().setNemoSDKListener(new SimpleNemoSDkListener() {
            @Override
            public void onCallStateChange(CallState state, String reason) {
                //1.监听会议状态
                if (state == CallState.CONNECTED) {
//                        ToastWithLogin("入会成功: ");
                    // 入会成功
                    Log.e("TAG", "onCallStateChange: 入会成功");
                    // 可能会造成多次入会成功
                    if (listener!=null)
                        listener.onVideoSuccessLinked();
                } else if (state == CallState.DISCONNECTED) {
//                        ToastWithLogin("退出会议: " + reason);
                    // 告诉医生，你已经离开
                    sendNotifyPaintLiveMsg();
                    // 释放资源一定要写在退出会议的后面
                    releaseProxy();
                }
            }

            @Override
            public void onRosterChange(RosterWrapper rosterWrapper) {
                //参会者信息回调
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
            }

            @Override
            public void onVideoStatusChange(int videoStatus) {
                super.onVideoStatusChange(videoStatus);
                // 提示用户当前信息
                showVideoStatusChange(videoStatus);
            }

            @Override
            public void onVideoDataSourceChange(List<VideoInfo> videoInfos, boolean hasVideoContent) {
                    if (videoInfos.size()>0){
                        // 如果医生没有进来过，那么就执行
                        // 如果医生已经进来过了，就不再执行
                        if (!isDoctorEnterRoom){
                            isDoctorEnterRoom = true;
                            if (videoCell !=null)
                                videoCell.setVideoInfo(videoInfos.get(0));
                            if (listener!=null)
                                listener.onDoctorInRoom();
                        }
                    }
                    else if (videoInfos.size()==0){
                        // 现在的房间没有其他人了
                        // 医生曾进入过
//                            ToastUtil.show("医生已经离开");
                        if (listener!=null)
                            listener.onDoctorOutRoom();
                    }
            }
        });
    }

    /**
     * 信令 通信
     * 向Docotor的视频端发送用户信息。
     * 这个任务特别重要，需要持续保持监控医生是否进入房间，如果没有进入房间，则继续监听
     */
    public void sendNotifyDoctorVideoMsg() {
        XLMessage.with().init(xlPatientUserId,XL_URL,activity.get()).send(new XLSend().getDoctorMsg(xlPatientUserId, nickname.trim(), doctorUserId, consultId, meetingRoomNumber),
                msg -> ToastWithLogin("xlMessage: 向医生端发送信息 开始视频"));
//        if (xlMessage!=null)
//        xlMessage.send(new XLSend().getDoctorMsg(xlPatientUserId, nickname.trim(), doctorUserId, consultId, meetingRoomNumber),
//                message -> ToastWithLogin("xlMessage: TX_RTC_START_INVOKE"));
    }

    /**
     * 提示医生病人已经离开
     */
    public void sendNotifyPaintLiveMsg(){
        XLMessage.with().init(xlPatientUserId,XL_URL,activity.get()).send(new XLSend().getPatientLeaveMsg(doctorUserId,xlPatientUserId),
                msg -> ToastWithLogin("xlMessage: 向医生端发送信息 病人离开"));
//        if (xlMessage!=null)
//        xlMessage.send(new XLSend() .getPatientLeaveMsg(doctorUserId,xlPatientUserId),
//                message -> {ToastWithLogin("xlMessage :病人离开");
//        });
    }

    /**
     * 挂断视频
     */
    public void closeVideoProxy(){
        // 病人离席
        // 向大屏幕socket传递离开信息
        // 如果医生没有进入房间
        if (!NemoSDK.getInstance().inCalling()){
            releaseProxy();
        }else {
            // 如果医生已经进入房间
            NemoSDK.getInstance().hangup();// 挂断通话
        }
    }

    /**
     * 释放资源
     */
    private void releaseProxy(){
        // 重置全局数据
        isDoctorEnterRoom = false;
        Log.e("JTJK", "releaseProxy: start" );
        // 释放对象资源
        if (uvcCameraPresenter!=null){
            uvcCameraPresenter.onDestroy();
            uvcCameraPresenter = null;
            Log.e("JTJK", "uvcCameraPresenter: done" );
        }
        // 释放在这里为保证这个对象被释放了，如果在监听里面，可能没有被释放该怎么办？
        XLMessage.with().delayDestroy();
        Log.e("JTJK", "XLMessage: done" );
        if (activity!=null)
            activity.clear();
        if (contentLayout !=null)
            contentLayout = null;
        if (videoCell !=null){
            videoCell = null;
        }
        Log.e("JTJK", "failed: done" );
        // 释放 环信
        if (emcallback!=null)
            EMClient.getInstance().logout(true,emcallback);

        Log.e("JTJK", "EMClient: done" );
        // 释放小鱼
        NemoSDK.getInstance().setNemoSDKListener(null);
        NemoSDK.getInstance().releaseLayout();
        NemoSDK.getInstance().releaseCamera();
        NemoSDK.getInstance().releaseAudioMic();
        Log.e("JTJK", "NemoSDK: done" );
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
     * 启动全屏
     * 想法是这样的，当启动全屏的时候，容器会把 videoCell 放进这个容器
     * 所以这个容器其实是个全屏的容器
     * @param viewGroup 全屏的容器对象
     */
    public void doFullScreen(ViewGroup viewGroup){
        if (viewGroup==null)
            return;
        if (videoCell!=null && videoCell.getParent()!=null)
            ((ViewGroup) videoCell.getParent()).removeView(videoCell);
        if (videoCell!=null)
            viewGroup.addView(videoCell);
    }

    /**
     * 从全屏返回
     * 会将 videoCell 方回到原来的容器里面
     */
    public void doNotFullScreen(){
        if (videoCell!=null && videoCell.getParent()!=null)
            ((ViewGroup) videoCell.getParent()).removeView(videoCell);
        if (videoCell!=null)
            contentLayout.addView(videoCell);
    }

    /**
     * 小鱼网络状态值对照表
     */
    @IntDef({
            VideoStatus.VIDEO_STATUS_NORMAL, VideoStatus.VIDEO_STATUS_LOW_AS_LOCAL_BW,
            VideoStatus.VIDEO_STATUS_LOW_AS_LOCAL_HARDWARE, VideoStatus.VIDEO_STATUS_LOW_AS_REMOTE,
            VideoStatus.VIDEO_STATUS_NETWORK_ERROR, VideoStatus.VIDEO_STATUS_LOCAL_WIFI_ISSUE
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface VideoStatus {
        int VIDEO_STATUS_NORMAL = 0;
        int VIDEO_STATUS_LOW_AS_LOCAL_BW = 1;
        int VIDEO_STATUS_LOW_AS_LOCAL_HARDWARE = 2;
        int VIDEO_STATUS_LOW_AS_REMOTE = 3;
        int VIDEO_STATUS_NETWORK_ERROR = 4;
        int VIDEO_STATUS_LOCAL_WIFI_ISSUE = 5;
    }

    /**
     * 小鱼视频网络状态提示
     * @param videoStatus 状态值
     */
    public void showVideoStatusChange(int videoStatus) {
        if (activity==null)
            return;
        activity.get().runOnUiThread(() -> {
            if (videoStatus == VideoStatus.VIDEO_STATUS_NORMAL) {
                ToastWithLogin( activity.get().getString(R.string.video_status_normal));
            } else if (videoStatus == VideoStatus.VIDEO_STATUS_LOW_AS_LOCAL_BW) {
                ToastWithLogin( activity.get().getString(R.string.video_status_as_low_local_bw));
            } else if (videoStatus == VideoStatus.VIDEO_STATUS_LOW_AS_LOCAL_HARDWARE) {
                ToastWithLogin( activity.get().getString(R.string.video_status_as_low_local_hardware));
            } else if (videoStatus == VideoStatus.VIDEO_STATUS_LOW_AS_REMOTE) {
                ToastWithLogin( activity.get().getString(R.string.video_status_as_low_local_remote));
            } else if (videoStatus == VideoStatus.VIDEO_STATUS_NETWORK_ERROR) {
                ToastWithLogin( activity.get().getString(R.string.video_status_network_error));
            } else if (videoStatus == VideoStatus.VIDEO_STATUS_LOCAL_WIFI_ISSUE) {
                ToastWithLogin( activity.get().getString(R.string.video_status_local_wifi_issue));
            }
        });
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
        default void onVideoSuccessLinked(){};// 入会成功
        default void onDoctorInRoom(){};
        default void onDoctorOutRoom(){};//医生进入的时候，将医生的 video Info 给予外部
    }
}
