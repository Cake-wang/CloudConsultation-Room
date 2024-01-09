package com.aries.template.module.mine;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.retrofit.FastObserver;
import com.aries.library.fast.util.ToastUtil;
import com.aries.template.GlobalConfig;
import com.aries.template.MainActivity;
import com.aries.template.R;
import com.aries.template.entity.CancelregisterResultEntity;
import com.aries.template.entity.ConfigurationToThirdForPatientEntity;
import com.aries.template.entity.GetConsultsAndRecipesResultEntity;
import com.aries.template.entity.GetRecipeListByConsultIdEntity;
import com.aries.template.entity.PatientFinishGraphicTextConsultEntity;
import com.aries.template.module.base.BaseEventFragment;
import com.aries.template.module.main.HomeFragment;
import com.aries.template.retrofit.repository.ApiRepository;
import com.aries.template.thridapp.JTJKThirdAppUtil;
import com.aries.template.utils.ActivityUtils;
import com.aries.template.utils.DefenceUtil;
import com.aries.template.utils.RegUtils;
import com.aries.template.utils.ScaleTextView;
import com.aries.template.view.ShineButtonDialog;
import com.aries.template.widget.autoadopter.AutoAdaptorProxy;
import com.aries.template.widget.autoadopter.AutoObjectAdaptor;
import com.aries.template.xiaoyu.EaseModeProxy;
import com.aries.template.xiaoyu.dapinsocket.DapinSocketProxy;
import com.aries.template.xiaoyu.xinlin.XLMessage;
import com.aries.ui.view.title.TitleBarView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.widget.popupwindow.ViewTooltip;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * 视频问诊
 *
 * 视频业务逻辑
 * 如果医生没有进入，不论是否有处方：返回首页【取消复诊单】
 * 如果医生进入，如果没有开处方：返回首页【结束问诊接口】
 * 如果医生进入，如果开了处方：进入确认处方单【结束问诊接口】
 *
 * @author louisluo
 * @Author: AriesHoo on 2018/7/13 17:09
 * @E-Mail: AriesHoo@126.com
 * @Function: 我的
 */
public class VideoConsultFragment extends BaseEventFragment {

    private String consultId; //复诊单id 复诊单拿
    private String recipeId; //处方单id 轮训时拿到
    private String nickname; //复诊人姓名 复诊单拿
    private String doctorUserId; //医生userId 复诊单拿
    private String doctorName; //医生姓名 复诊单拿

    private String username; //医生userId 复诊单拿
    private String userpwd; //医生userId 复诊单拿
    private String userId; //医生userId 复诊单拿

    private boolean isDoctorInRoomFlag =false;// 医生是否进入
    // 是否从未支付处方单进来的，true为是，
    // 如果是，那么结束问诊时，直接使用结束问诊接口，而不是取消复诊单
    private boolean isBackFromOrder=false;
    private boolean isRecipeCheckedFlag=false;// 医生开出的处方状态是不是1或者不是2，则不能支付。即不能结束问诊.true 可以结束问诊

    private List<GetRecipeListByConsultIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO> currentRecipes=new ArrayList<>();// 从后端传入的数据

    private boolean isBodyTestingFlag;// 是否从身体检测回来，如果曾经出过身体检测，则为true

    private AutoAdaptorProxy<GetRecipeListByConsultIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO> proxy; //处方显示对象

    // 是否是回到首页，如果是，则不执行pop
    private boolean isHomeBack;
    // 是否已经打开了结束问诊对话框
    private boolean isDialogOpened;

    /**
     * 输入显示对象
     */
    @Override
    public int getContentLayout() {
        if(GlobalConfig.thirdFactory.equals("3")||GlobalConfig.thirdFactory.equals("2")){
            return R.layout.fragment_video_l;
        }else {
            return R.layout.fragment_video;
        }
    }

    @BindView(R.id.btn_stjc)
    Button btn_stjc;
    @BindView(R.id.btn_finish)
    Button btn_finish;
    @BindView(R.id.jtjk_video_content)
    RelativeLayout video_content;// 全屏视频容器
    @BindView(R.id.jtjk_video_content_parent)
    RelativeLayout video_content_parent;// 全屏视频容器的父类
    @BindView(R.id.jtjk_video_close_full)
    TextView video_close_full;// 全屏按钮
    @BindView(R.id.btn_full_screen)
    TextView btn_full_screen;// 全屏按钮
    @BindView(R.id.rv_video_tip)
    RecyclerView rv_video_tip;
    @BindView(R.id.rv_video_wait)
    LinearLayout rv_video_wait;
    @BindView(R.id.jtjk_video_doctorname)
    TextView jtjk_video_doctorname;
    @BindView(R.id.tv_video_wait)
    ScaleTextView tv_video_wait;
    @BindView(R.id.tv_video_tip)
    ScaleTextView tv_video_tip;

    @BindView(R.id.tv_process_tip)
    ScaleTextView tv_process_tip;

    @BindView(R.id.iv_process_l_x)
    ImageView iv_process_l_x;
    @BindView(R.id.iv_process_l_x_x)
    ImageView iv_process_l_x_x;
    @BindView(R.id.iv_process_l_x_x_x)
    ImageView iv_process_l_x_x_x;

    @BindView(R.id.iv_process_2_x)
    ImageView iv_process_2_x;
    @BindView(R.id.iv_process_3_x)
    ImageView iv_process_3_x;
    @BindView(R.id.iv_process_4_x)
    ImageView iv_process_4_x;

    @BindView(R.id.tv_process_2_x)
    ScaleTextView tv_process_2_x;
    @BindView(R.id.tv_process_3_x)
    ScaleTextView tv_process_3_x;
    @BindView(R.id.tv_process_4_x)
    ScaleTextView tv_process_4_x;

//    @BindView(R.id.x5_webview)
//    WebView x5_webview;
    @BindView(R.id.videoview)
    VideoView videoview;

    @BindView(R.id.jtjk_recipe_name)
    TextView jtjk_recipe_name;


    @BindView(R.id.fl_new)
    FrameLayout fl_new;

    @BindView(R.id.ll_older)
    LinearLayout ll_older;

    @BindView(R.id.videoview_x)
    VideoView videoview_x;

    @BindView(R.id.tv_process_tip_x)
    ScaleTextView tv_process_tip_x;

    @BindView(R.id.btn_stjc_x)
    Button btn_stjc_x;
    @BindView(R.id.btn_finish_x)
    Button btn_finish_x;

    /**
     * 跳转科室，需要带的数据
     * @param consultId 复诊单id 复诊单拿
     * @param nickname 复诊人姓名 复诊单拿
     * @param doctorUserId 医生userId 复诊单拿 是医生里面的loginID
     * @param doctorName 医生姓名
     * @param isBackFromOrder 是否是从未支付进来的，true 为是。如果是从未支付进来的，那么医生进来后，结束问诊，返回首页。
     */
    public static VideoConsultFragment newInstance(String consultId,
                                                   String nickname,
                                                   String  doctorUserId,
                                                   String doctorName,
                                                   boolean isBackFromOrder) {
        // 复诊单的配置
        VideoConsultFragment fragment = new VideoConsultFragment();
        Bundle args = new Bundle();
        args.putString("consultId",consultId);
        args.putString("nickname",nickname);
        args.putString("doctorUserId",doctorUserId);
        args.putString("doctorName",doctorName);
        args.putBoolean("isBackFromOrder",isBackFromOrder);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 构造函数
     * @param savedInstanceState 输入进来的数据
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 停止界面的时间计时器
        dismissCountTimeStop();
        // 注入数据
        Bundle args = getArguments();
        consultId = args.getString("consultId");
        nickname = args.getString("nickname");
        doctorUserId = args.getString("doctorUserId");
        doctorName = args.getString("doctorName");
        isBackFromOrder = args.getBoolean("isBackFromOrder");
        // 启动请求
        requestConfigurationToThirdForPatient();
    }

    @Override
    public void onStart() {
        super.onStart();
        // 从外部身体检测应用回来，大屏接口，身体检测结束
        if (isBodyTestingFlag){
            isBodyTestingFlag = false;
            // 重新启动视频问诊投屏
            DapinSocketProxy.with()
                    .initWithOld(getActivity(),GlobalConfig.machineIp)
                    .startSocket(DapinSocketProxy.FLAG_SCREENFLAG_CONTROLSCREEN);
        }else{
            // 启动视频
            // 启动轮训处方单状态
//        timeLoop();

            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.
                    permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 1);
            }
//加载视频资源
            Uri uri = Uri.parse( "android.resource://"  + getActivity().getApplicationContext().getPackageName() +  "/"  +R.raw.b);
//            String file_path=getActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString()+"/video";

//            String path=file_path+"/b.mp4";
//            Log.d("播放地址",path);
            videoview.setVideoURI(uri);
//            videoview.setVideoURI(Uri.parse(uri));
//创建MediaController对象
            MediaController mediaController = new MediaController(getActivity());
//绑定mediaController
            videoview.setMediaController(mediaController);
            videoview.requestFocus();

            videoview.start();
            videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setVolume(0f, 0f);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                    if (videoview!=null){
                        videoview.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                            @Override
                            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                                videoview.setBackgroundColor(Color.TRANSPARENT);
                                return false;
                            }
                        });
                    }


                }
            });


            videoview_x.setVideoURI(uri);
//            videoview.setVideoURI(Uri.parse(uri));
//创建MediaController对象
            MediaController mediaControllerx = new MediaController(getActivity());
//绑定mediaController
            videoview_x.setMediaController(mediaControllerx);
            videoview_x.requestFocus();

            videoview_x.start();
            videoview_x.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setVolume(0f, 0f);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                    if (videoview_x!=null){
                        videoview_x.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                            @Override
                            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                                videoview_x.setBackgroundColor(Color.TRANSPARENT);
                                return false;
                            }
                        });
                    }


                }
            });



//            LinearLayout ll_webview = getActivity().findViewById(R.id.ll_webview);
//
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            x5_webview = new WebView(this.getContext());
//            x5_webview.setLayoutParams(params);
//            ll_webview.addView(x5_webview);
//            x5_webview.getSettings().setDefaultTextEncodingName("UTF-8");
////            webview.setWebViewClient(new InnerWebViewClient());
//            x5_webview.getSettings().setUseWideViewPort(true);
//            x5_webview.getSettings().setLoadWithOverviewMode(true);
//            x5_webview.getSettings().setJavaScriptEnabled(true);
//            x5_webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//            x5_webview.getSettings().setSupportZoom(true);
//            x5_webview.getSettings().setAppCacheEnabled(true);
//            x5_webview.getSettings().setDatabaseEnabled(true);
//            x5_webview.getSettings().setDomStorageEnabled(true);
//            x5_webview.getSettings().setMediaPlaybackRequiresUserGesture(false);//设置音频自动播放
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                // chromium, enable hardware acceleration
//                x5_webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//            } else {
//                // older android version, disable hardware acceleration
//                x5_webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//            }
//
//            int screenDensity = getResources().getDisplayMetrics().densityDpi ;
//            WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.MEDIUM ;
//            switch (screenDensity){
//                case DisplayMetrics.DENSITY_LOW :
//                    zoomDensity = WebSettings.ZoomDensity.CLOSE;
//                    break;
//                case DisplayMetrics.DENSITY_MEDIUM:
//                    zoomDensity = WebSettings.ZoomDensity.MEDIUM;
//                    break;
//                case DisplayMetrics.DENSITY_HIGH:
//                    zoomDensity = WebSettings.ZoomDensity.FAR;
//                    break ;
//            }
//            x5_webview.getSettings().setDefaultZoom(zoomDensity);
//
//            x5_webview.setWebViewClient(new WebViewClient(){
//                /**
//                 * 当前网页的链接仍在webView中跳转
//                 */
//                @Override
//                public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                    view.loadUrl(url);
//                    return true;
//                }
//
//
//                @Override
//                public void onReceivedSslError(WebView var1, SslErrorHandler handler, SslError var3) {
//                    handler.proceed(); //表示等待证书响应
//                    //handler.cancel(); //表示挂起连接，为默认方式
//                    // handler.handleMessage(null); //可做其他处理
//
//                }
//
//
////                /**
////                 * 处理ssl请求
////                 */
////                @Override
////                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
////                    handler.proceed();
////                }
//
//
//                /**
//                 * 页面载入完成回调
//                 */
//                @Override
//                public void onPageFinished(WebView view, String url) {
//                    super.onPageFinished(view, url);
//                    view.loadUrl("javascript:try{autoplay();}catch(e){}");
//                }
//            });
//
//
//            x5_webview.setWebChromeClient(new WebChromeClient() {
//                /**
//                 * 显示自定义视图，无此方法视频不能播放
//                 */
//                @Override
//                public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback callback) {
//                    super.onShowCustomView(view, callback);
//                }
//            });
////            webview.loadUrl("file:///sdcard/html/video.html");
//            x5_webview.loadUrl("file:///android_asset/index.html");
////            x5_webview.loadUrl("https://www.baidu.com/");

            // 创建视频处理器
            ViewGroup viewGroup = getActivity().findViewById(R.id.videoContent);
            EaseModeProxy.with().onStart(getActivity(),viewGroup);
            EaseModeProxy.with().setListener(new EaseModeProxy.ProxyEventListener() {
                @Override
                public void onDoctorInRoom() {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            ll_webview.setVisibility(View.GONE);
                            videoview.setVisibility(View.GONE);
                            videoview_x.setVisibility(View.GONE);
                    viewGroup.setVisibility(View.VISIBLE);
//                            tv_process_tip.setVisibility(View.VISIBLE);
//                            tv_process_tip.setText("问诊开方...");
                            tv_process_tip_x.setText("医生问诊中...");
                            iv_process_l_x.setImageResource(R.mipmap.process_x);
                            iv_process_2_x.setImageResource(R.mipmap.process_2);
                            tv_process_2_x.setTextColor(Color.parseColor("#333333"));

                            if (rv_video_wait.getVisibility()==View.VISIBLE)
                                rv_video_wait.setVisibility(View.GONE);

                            if (tv_video_wait.getVisibility()==View.GONE)
                                tv_video_wait.setVisibility(View.VISIBLE);
                                tv_video_wait.setText("视频通话进行中");

                        }
                    });


                    // 医生进入的TAG
                    isDoctorInRoomFlag = true;
                    // 如果医生进入了，则直接释放XL的socket
                    if (mDisposableNew != null) {
                        mDisposableNew.dispose();
                        mDisposableNew = null;
                    }
                    XLMessage.with().destroy();
                    // 大屏接口，启动大屏
                    DapinSocketProxy.with()
                            .initWithOld(getActivity(),GlobalConfig.machineIp)
                            .startSocket(DapinSocketProxy.FLAG_SCREENFLAG_CONTROLSCREEN);

                    // 可以点击身体检测
//                    btn_stjc.setEnabled(true);
                }

                @Override
                public void onDoctorOutRoom() {
                    // 医生离开了
                    // 身体检测不可使用
//                    btn_stjc.setEnabled(false);
//                    viewGroup.setVisibility(View.GONE);
//                    NemoSDK.getInstance().hangup();


                    mDisposable = Observable.interval(DELAY, PERIOD, TimeUnit.MILLISECONDS)
                            .map((aLong -> aLong + 1))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(aLong -> {requestGetRecipeListByConsultId();

                            });//getUnreadCount()执行的任务

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            fl_new.setVisibility(View.GONE);
                            ll_older.setVisibility(View.VISIBLE);

//                            ll_webview.setVisibility(View.VISIBLE);
                            if (videoview!=null){
                                videoview.setVisibility(View.VISIBLE);
                            }

                            if (viewGroup!=null){
                                viewGroup.setVisibility(View.GONE);
                            }

                            if (tv_video_wait!=null){
                                tv_video_wait.setText("等待医生开具处方信息");
                            }


//                            vtip =   ViewTooltip
//                                    .on(btn_finish)
//                                    .position(ViewTooltip.Position.TOP)
//                                    .textSize(TypedValue.COMPLEX_UNIT_PX, DensityUtils.dp2px(22))
//                                    .color(Color.parseColor("#EEF5F7"))
//                                    .textColor(Color.parseColor("#303030"))
//                                    .autoHide(false, 3000)
//                                    .text("即将进入处方结算")
//                                    .show();
//                            vtip.setVisibility(View.GONE);

                        }
                    });

//                    NemoSDK.getInstance().hangup();

//                    NemoSDK.getInstance().logout();
//                    EaseModeProxy.with().releaseProxy();

                    // 大屏接口，医生离席，关闭大屏视频
                    DapinSocketProxy.with()
                            .initWithOld(getActivity(),GlobalConfig.machineIp)
                            .startSocket(DapinSocketProxy.FLAG_SCREENFLAG_CLOSESCREEN);

                    // 启动倒计时
                    // 启动后，如果没有正常的处方单，则不可以结束
//                    if (isRecipeCheckedFlag){
//                        // todo 时间用尽后，执行结束问诊
//                        getActivity().runOnUiThread(() -> getView().findViewById(R.id.jtjk_fz_fragment_timer).setVisibility(View.VISIBLE));
//                        timeStart();
//                    }
                }

                @Override
                public void onVideoSuccessLinked() {
                    //入会成功
                    // 启动全屏展示
//                btn_full_screen.setVisibility(View.VISIBLE);
                    //启动轮训处方单状态
                    timeLoop();
                }
            });
        }

//        // 启动请求权限
//        // 检测PHONE_STATE 如果未授权
//        if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            //申请权限
//            String[] permission = new String[]{Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO};
//            ActivityCompat.requestPermissions(getActivity(), permission, 0x101);
//        }

        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                .subscribe(aBoolean->{});
    }

    @Override
    public void onPause() {
//        if(null != x5_webview) {
//            x5_webview.onPause();
//        }
        super.onPause();
    }

     ViewTooltip.TooltipView vtip;

    /**
     * 页面的动作，包括数据传输和界面改变
     */
    @Override
    public void initView(Bundle savedInstanceState) {

//        vtip =   ViewTooltip
//                .on(btn_finish)
//                .position(ViewTooltip.Position.TOP)
//                .textSize(TypedValue.COMPLEX_UNIT_PX, DensityUtils.dp2px(22))
//                .color(Color.parseColor("#EEF5F7"))
//                .textColor(Color.parseColor("#303030"))
//                .autoHide(false, 3000)
//                .text("即将进入处方结算").show();

//        vtip.remove();
        jtjk_video_doctorname.setText("复诊医生: " +doctorName);
//        btn_full_screen.setVisibility(View.GONE);
//        btn_stjc.setEnabled(true); //todo cc
        // 显示名称
        jtjk_recipe_name.setText(RegUtils.nameDesensitization(GlobalConfig.ssCard.getName())+"，您好");


    }

    @SingleClick
    @OnClick({R.id.btn_stjc, R.id.btn_finish,R.id.jtjk_video_close_full,R.id.btn_full_screen,R.id.btn_stjc_x, R.id.btn_finish_x})
    public void onViewClicked(View view) {
        DefenceUtil.checkReSubmit("VideoConsultFragment.onViewClicked");
        switch (view.getId()) {
            case R.id.btn_stjc:

                if (GlobalConfig.thirdFactory.equals("3")){
//                    if (GlobalConfig.thirdFactory.equals("1")){

                    ((MainActivity)getActivity()).gotoYYPJ();

                }else {
                    // 跳向身体检测，则更新flag准备回来
                    isBodyTestingFlag = true;
                    // 跳向身体检测
                    // 启动第三方跳转
                    // 并告知大屏，启动身体检测，大屏的通信代理 在里面
                    if (!TextUtils.isEmpty(GlobalConfig.factoryResource)){
                        new JTJKThirdAppUtil().gotoBodyTestingFromVideo(getActivity(),
                                GlobalConfig.factoryResource,
                                GlobalConfig.factoryMainPage,
                                GlobalConfig.ssCard.getName(),
                                GlobalConfig.ssCard.getSSNum(),
                                GlobalConfig.mobile);

                        // 关闭视频问诊投屏
//                    DapinSocketProxy.with()
//                            .initWithOld(getActivity(),GlobalConfig.machineIp)
//                            .startSocket(DapinSocketProxy.FLAG_SCREENFLAG_CLOSESCREEN);
                    }else {
                        ToastUtil.show("请您移步到旁边的健康管理设备进行检测");
                    }
                }


//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                /**知道要跳转应用的包命与目标Activity*/
//                ComponentName componentName = new ComponentName("com.garea.launcher", "com.garea.launcher.login.LauncherLogin");
//                intent.setComponent(componentName);
//                intent.putExtra("userName", SPUtil.get(mContext,"userName","")+"");//这里Intent传值
//                intent.putExtra("idCard", SPUtil.get(mContext,"idCard","")+"");
//                intent.putExtra("mobile", SPUtil.get(mContext,"mobile","")+"");
//                startActivity(intent);
                break;
            case R.id.btn_finish:

//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {

                showSimpleConfirmDialog();
//                dialog.show();
//                    }
//                });

                break;
            case R.id.btn_stjc_x:

                if (GlobalConfig.thirdFactory.equals("3")){
//                    if (GlobalConfig.thirdFactory.equals("1")){

                    ((MainActivity)getActivity()).gotoYYPJ();

                }else {
                    // 跳向身体检测，则更新flag准备回来
                    isBodyTestingFlag = true;
                    // 跳向身体检测
                    // 启动第三方跳转
                    // 并告知大屏，启动身体检测，大屏的通信代理 在里面
                    if (!TextUtils.isEmpty(GlobalConfig.factoryResource)){
                        new JTJKThirdAppUtil().gotoBodyTestingFromVideo(getActivity(),
                                GlobalConfig.factoryResource,
                                GlobalConfig.factoryMainPage,
                                GlobalConfig.ssCard.getName(),
                                GlobalConfig.ssCard.getSSNum(),
                                GlobalConfig.mobile);

                        // 关闭视频问诊投屏
//                    DapinSocketProxy.with()
//                            .initWithOld(getActivity(),GlobalConfig.machineIp)
//                            .startSocket(DapinSocketProxy.FLAG_SCREENFLAG_CLOSESCREEN);
                    }else {
                        ToastUtil.show("请您移步到旁边的健康管理设备进行检测");
                    }
                }


//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                /**知道要跳转应用的包命与目标Activity*/
//                ComponentName componentName = new ComponentName("com.garea.launcher", "com.garea.launcher.login.LauncherLogin");
//                intent.setComponent(componentName);
//                intent.putExtra("userName", SPUtil.get(mContext,"userName","")+"");//这里Intent传值
//                intent.putExtra("idCard", SPUtil.get(mContext,"idCard","")+"");
//                intent.putExtra("mobile", SPUtil.get(mContext,"mobile","")+"");
//                startActivity(intent);
                break;
            case R.id.btn_finish_x:

//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {

                showSimpleConfirmDialog();
//                dialog.show();
//                    }
//                });

                break;
            case R.id.jtjk_video_close_full:
                EaseModeProxy.with().doNotFullScreen();
                video_content_parent.setVisibility(View.GONE);
                break;
            case R.id.btn_full_screen:
                EaseModeProxy.with().doFullScreen(video_content);
                video_content_parent.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    /**
     * 显示对话框
     * 如果医生没有进入，不论是否有处方：返回首页【取消复诊单】
     * 如果医生进入，如果没有开处方：返回首页【结束问诊接口】
     * 如果医生进入，如果开了处方：进入确认处方单【结束问诊接口】
     */
    ShineButtonDialog dialog;
    private void showSimpleConfirmDialog() {
        // 结束问诊框是否已经打开
//        if (!isDialogOpened){
//         dialog = new Dialog(this.mContext,R.style.Dialog);
//
//        View dialogView = LayoutInflater.from(this.mContext).inflate(R.layout.dialog_custom_l, null);
//
//        dialog.setCancelable(true);
//
//            TextView tv_title_tip = dialogView.findViewById(R.id.tv_title_tip);
//            tv_title_tip.setText("结束问诊");
//
//            // 启动业务
//            if (isDoctorInRoomFlag){
//                // 结束问诊，如果有问诊单，则根据这个问诊单进入支付
//                // 在栈内的HomeFragment以SingleTask模式启动（即在其之上的Fragment会出栈）
//                if (!isRecipeCheckedFlag){
//                    // 患者进入视频后，医生没有开处方, 则回到首页
//                    if (currentRecipes!=null && currentRecipes.size()==0){
//                        // 结束问诊
//                        // 成功后，跳转到首页
//                        tv_title_tip.setText("医生还未开方，确认是否退出");
//                    }
//                    else
//                        tv_title_tip.setText("药师还未审方，确认是否退出");
//                } else{
//                    // 结束问诊接口
//                    // 成功后，跳转确认复诊单
//                    tv_title_tip.setText("确认是否结束问诊");
//                }
//            }else{
//                if (isBackFromOrder){
//                    // 如果是从未支付进来的
//                    // 医生没有进入视频
//                    // 没有处方单
//                    // 那么结束问诊
//                    tv_title_tip.setText("医生还未接诊，是否确认退出");
//                    // 清理
////                    onDismiss();//todo cc
//                    // 启动返回首页
////                    start(HomeFragment.newInstance(), SupportFragment.SINGLETASK); //todo cc
//
//                }
//                else
//                    // 医生不曾进入
//                    // 取消复诊单
//                    // 成功后，跳转到首页
//                    tv_title_tip.setText("医生还未接诊，是否确认退出");
//            }
//
//            Button btn_inquiry = dialogView.findViewById(R.id.btn_inquiry);
//
//            btn_inquiry.setOnClickListener(v -> {
//                // 防御代码
//                if (!DefenceUtil.checkReSubmit("VideoConsultFragment.showSimpleConfirmDialog"))
//                    return;
//
//                // 关闭对话框
//                dialog.dismiss();
//
//                // 启动业务
//                if (isDoctorInRoomFlag){
//                    // 结束问诊，如果有问诊单，则根据这个问诊单进入支付
//                    // 在栈内的HomeFragment以SingleTask模式启动（即在其之上的Fragment会出栈）
//                    if (!isRecipeCheckedFlag){
//                        // 患者进入视频后，医生没有开处方, 则回到首页
//                        if (currentRecipes!=null && currentRecipes.size()==0){
//                            // 结束问诊
//                            // 成功后，跳转到首页
//                            requestPatientFinishGraphicTextConsult(consultId,false);
//                        }
//                        else
//                            ToastUtil.show("处方正在被药师审方核，请稍后再试");
//                    } else{
//                        // 结束问诊接口
//                        // 成功后，跳转确认复诊单
//                        requestPatientFinishGraphicTextConsult(consultId,true);
//                    }
//                }else{
//                    if (isBackFromOrder){
//                        // 如果是从未支付进来的
//                        // 医生没有进入视频
//                        // 没有处方单
//                        // 那么结束问诊
//                        requestPatientFinishGraphicTextConsult(consultId,false);
//                        // 清理
////                    onDismiss();//todo cc
//                        // 启动返回首页
////                    start(HomeFragment.newInstance(), SupportFragment.SINGLETASK); //todo cc
//
//                    }
//                    else
//                        // 医生不曾进入
//                        // 取消复诊单
//                        // 成功后，跳转到首页
//                        requestPatientCancelGraphicTextConsult(consultId);
//                }
//                isDialogOpened = false;
//            });
//
//            Button btn_cancel = dialogView.findViewById(R.id.btn_cancel);
//            XUIAlphaImageView iv_close = dialogView.findViewById(R.id.iv_close);
//            btn_cancel.setOnClickListener(v ->  {dialog.dismiss();isDialogOpened=false;});
//            iv_close.setOnClickListener(v -> {dialog.dismiss();isDialogOpened=false;});
//
//        dialog.setContentView(dialogView);
//        dialog.setCanceledOnTouchOutside(false);
//
//        DisplayMetrics dm = new DisplayMetrics();
//        this.mContext.getWindowManager().getDefaultDisplay().getMetrics(dm);
//        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();  //获取对话框当前的参数值
//        p.width = 650;
//        p.height = 350;
//        p.gravity = Gravity.CENTER;
//        dialog.getWindow().setAttributes(p);     //设置生效
//
//
//
//            isDialogOpened = true;
//        }

        if (!isDialogOpened){
             dialog = new ShineButtonDialog(this.mContext);
            dialog.tv_title_tip.setText("结束问诊");

            // 启动业务
            if (isDoctorInRoomFlag){
                // 结束问诊，如果有问诊单，则根据这个问诊单进入支付
                // 在栈内的HomeFragment以SingleTask模式启动（即在其之上的Fragment会出栈）
                if (!isRecipeCheckedFlag){
                    // 患者进入视频后，医生没有开处方, 则回到首页
                    if (currentRecipes!=null && currentRecipes.size()==0){
                        // 结束问诊
                        // 成功后，跳转到首页
                        dialog.tv_content_tip.setText("医生还未开方，确认是否退出");
                    }
                    else
                        dialog.tv_content_tip.setText("药师还未审方，确认是否退出");
                } else{
                    // 结束问诊接口
                    // 成功后，跳转确认复诊单
                    dialog.tv_content_tip.setText("确认是否结束问诊");
                }
            }else{
                if (isBackFromOrder){
                    // 如果是从未支付进来的
                    // 医生没有进入视频
                    // 没有处方单
                    // 那么结束问诊
                    dialog.tv_content_tip.setText("医生还未接诊，是否确认退出");
                    // 清理
//                    onDismiss();//todo cc
                    // 启动返回首页
//                    start(HomeFragment.newInstance(), SupportFragment.SINGLETASK); //todo cc

                }
                else
                    // 医生不曾进入
                    // 取消复诊单
                    // 成功后，跳转到首页
                    dialog.tv_content_tip.setText("医生还未接诊，是否确认退出");
            }

            dialog.btn_inquiry.setOnClickListener(v -> {
                // 防御代码
                if (!DefenceUtil.checkReSubmit("VideoConsultFragment.showSimpleConfirmDialog"))
                    return;

                // 关闭对话框
                dialog.dismiss();

                // 启动业务
                if (isDoctorInRoomFlag){
                    // 结束问诊，如果有问诊单，则根据这个问诊单进入支付
                    // 在栈内的HomeFragment以SingleTask模式启动（即在其之上的Fragment会出栈）
                    if (!isRecipeCheckedFlag){
                        // 患者进入视频后，医生没有开处方, 则回到首页
                        if (currentRecipes!=null && currentRecipes.size()==0){
                            // 结束问诊
                            // 成功后，跳转到首页
                            requestPatientFinishGraphicTextConsult(consultId,false);
                        }
                        else
                            ToastUtil.show("处方正在被药师审方核，请稍后再试");
                    } else{
                        // 结束问诊接口
                        // 成功后，跳转确认复诊单
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                vtip.close();

                            }
                        });

                        requestPatientFinishGraphicTextConsult(consultId,true);
                    }
                }else{
                    if (isBackFromOrder){
                        // 如果是从未支付进来的
                        // 医生没有进入视频
                        // 没有处方单
                        // 那么结束问诊
                        requestPatientFinishGraphicTextConsult(consultId,false);
                        // 清理
//                    onDismiss();//todo cc
                        // 启动返回首页
//                    start(HomeFragment.newInstance(), SupportFragment.SINGLETASK); //todo cc

                    }
                    else
                        // 医生不曾进入
                        // 取消复诊单
                        // 成功后，跳转到首页
                        requestPatientCancelGraphicTextConsult(consultId);
                }
                isDialogOpened = false;
            });
            dialog.btn_cancel.setOnClickListener(v ->  {dialog.dismiss();isDialogOpened=false;});
            dialog.iv_close.setOnClickListener(v -> {dialog.dismiss();isDialogOpened=false;});
            dialog.show();
            isDialogOpened = true;
        }
    }

    private static final int PERIOD = 6* 1000;
    private static final int DELAY = 100;
    private Disposable mDisposable;

    private static final int PERIODNEW = 240* 1000;
    private static final int DELAYNEW = 100;
    public Disposable mDisposableNew;
    /**
     * 定时循环任务
     * 入会成功后执行
     * - 5秒循环查询新的待处理处方信息
     * - 5秒用于信令，医生端接口反馈循环
     */
    private void timeLoop() {
//        mDisposable = Observable.interval(DELAY, PERIOD, TimeUnit.MILLISECONDS)
//                .map((aLong -> aLong + 1))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(aLong -> {requestGetRecipeListByConsultId();
//
//                });//getUnreadCount()执行的任务


//        if(GlobalConfig.thirdFactory.equals("2")){
            mDisposableNew = Observable.interval(DELAYNEW, PERIODNEW, TimeUnit.MILLISECONDS)
                    .map((aLong -> aLong + 1))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> {
                        if (!isDoctorInRoomFlag)
                            // 启动向医生发送消息
                            // 如果医生没有进入房间，则不停的继续call
                            // 如果医生进入房间 onDoctorInRoom 的时候会被释放 XLMessage
                            EaseModeProxy.with().sendNotifyDoctorVideoMsg();
                    });//getUnreadCount()执行的任务
//        }


    }

    /**
     * 患者取消复诊单 服务
     * @param consultId 复诊单号
     */
    private void requestPatientCancelGraphicTextConsult(String consultId) {
        ApiRepository.getInstance().patientCancelGraphicTextConsult(consultId)
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastLoadingObserver<CancelregisterResultEntity>("请稍后...") {
                    @Override
                    public void _onNext(CancelregisterResultEntity entity) {
                        if (entity == null) {
                            ToastUtil.show("请检查网络");
                            return;
                        }
                        try {
                            if (entity.data.jsonResponseBean.body){
                                if (entity.getData().isSuccess()){
//                                    Log.d("JTJK","患者取消复诊服务");
                                    // 清理
                                    onDismiss();
                                    // 医生不曾进入到视频中
                                    start(HomeFragment.newInstance(), SupportFragment.SINGLETASK);
                                    isHomeBack = true;

                                }else{
                                    ToastUtil.show(entity.data.errorMessage);
                                    // 可能出现医生已经主动回复，无法取消的情况
//                                    ToastUtil.show(entity.getData().errorMessage);
                                    // 清理
                                    onDismiss();
                                    start(HomeFragment.newInstance(), SupportFragment.SINGLETASK);
                                    isHomeBack = true;

                                }
                            }else {
                                ToastUtil.show(entity.data.errorMessage);
                                onDismiss();
                                start(HomeFragment.newInstance(), SupportFragment.SINGLETASK);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            ToastUtil.show(entity.message);
                            onDismiss();
                            start(HomeFragment.newInstance(), SupportFragment.SINGLETASK);
//                            JTJKLogUtils.message(e.toString());
                        }
                    }
                });
    }

    /**
     * 获取第三方配置信息，视频信息
     */
    private void requestConfigurationToThirdForPatient(){
        ApiRepository.getInstance().getConfigurationToThirdForPatient(GlobalConfig.NALI_TID,GlobalConfig.NALI_APPKEY)
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastLoadingObserver<ConfigurationToThirdForPatientEntity>("请稍后...") {
                    @Override
                    public void _onNext(ConfigurationToThirdForPatientEntity entity) {
                        if (entity == null) {
                            ToastUtil.show("请检查网络");
                            return;
                        }
                        try {
                            if (entity.getData().isSuccess()){
                                username = entity.getData().getJsonResponseBean().getBody().getUsername();
                                userpwd = entity.getData().getJsonResponseBean().getBody().getUserpwd();
                                userId = entity.getData().getJsonResponseBean().getBody().getUserId();
                                EaseModeProxy.with().easemobStart(getActivity(),
                                        consultId,
                                        nickname,
                                        doctorUserId,
                                        username,
                                        userpwd,
                                        userId);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
//                            JTJKLogUtils.message(e.toString());
                        }
                    }
                });

    }

    /**
     * 通过复诊单获得处方单
     * 拿到处方单后，开始轮训
     */

//    private Boolean  xFlag = true;


    private void requestGetRecipeListByConsultId(){
        ApiRepository.getInstance().getRecipeListByConsultId(consultId)
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastObserver<GetRecipeListByConsultIdEntity>() {
                    @Override
                    public void _onNext(GetRecipeListByConsultIdEntity entity) {
                        if (entity == null) {
                            ToastUtil.show("请检查网络");
                            return;
                        }
                        try {
                            if (entity.data.success){
                                // 隐藏显示提示，等待旋转
//                                if (rv_video_wait.getVisibility()==View.VISIBLE)
//                                    rv_video_wait.setVisibility(View.GONE);
//
//                                if (tv_video_wait.getVisibility()==View.GONE)
//                                    tv_video_wait.setVisibility(View.VISIBLE);
//                                    tv_video_wait.setText("等待医生开具处方信息");

                                if (entity.data.jsonResponseBean.body.size()<1)
                                    return;

                                if (tv_video_wait.getVisibility()==View.VISIBLE)
                                    tv_video_wait.setVisibility(View.GONE);



                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
//
//
//
                                            if (rv_video_tip.getVisibility()==View.GONE)
                                            rv_video_tip.setVisibility(View.VISIBLE);
//
//                                            iv_process_l_x.setImageResource(R.mipmap.process_l);
//                                            iv_process_2_x.setImageResource(R.mipmap.process_f);
//                                            tv_process_2_x.setTextColor(Color.parseColor("#666666"));
//
//                                            tv_video_tip.setVisibility(View.VISIBLE);
//                                            tv_process_tip.setVisibility(View.VISIBLE);
//                                            tv_process_tip.setText("药师审方...");
//
//                                            iv_process_l_x_x.setImageResource(R.mipmap.process_x);
//                                            iv_process_3_x.setImageResource(R.mipmap.process_3);
//                                            tv_process_3_x.setTextColor(Color.parseColor("#333333"));
                                        }
                                    });


                                // 筛选处方
                                // 去掉被取消的处方
                                ArrayList<GetRecipeListByConsultIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO> newData = new ArrayList<>();
                                for (GetRecipeListByConsultIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO item : entity.data.jsonResponseBean.body) {
                                    // 9 是取消处方
                                    if (item.status==2||item.status==8)
                                        newData.add(item);
                                }
                                //刷新 RV 处方单界面
                                recipeId = String.valueOf(entity.data.jsonResponseBean.body.get(0).recipeId);

                                        reflashRecyclerView(rv_video_tip,newData);

                                if (newData.size()>0){
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {



//                                            if (rv_video_tip.getVisibility()==View.GONE)
//                                                rv_video_tip.setVisibility(View.VISIBLE);
                                            if (!tv_process_tip.getText().toString().equals("药师审方...")){
                                                iv_process_l_x.setImageResource(R.mipmap.process_l);
                                                iv_process_2_x.setImageResource(R.mipmap.process_f);
                                                tv_process_2_x.setTextColor(Color.parseColor("#666666"));

                                                tv_video_tip.setVisibility(View.VISIBLE);
                                                tv_process_tip.setVisibility(View.VISIBLE);
                                                tv_process_tip.setText("药师审方...");

                                                iv_process_l_x_x.setImageResource(R.mipmap.process_x);
                                                iv_process_3_x.setImageResource(R.mipmap.process_3);
                                                tv_process_3_x.setTextColor(Color.parseColor("#333333"));
                                            }

                                        }
                                    });
                                }else {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (!tv_process_tip.getText().toString().equals("问诊开方...")){

                                                tv_video_tip.setVisibility(View.GONE);
                                                tv_process_tip.setVisibility(View.VISIBLE);
                                                tv_process_tip.setText("问诊开方...");

                                                iv_process_l_x.setImageResource(R.mipmap.process_x);
                                                iv_process_2_x.setImageResource(R.mipmap.process_2);
                                                tv_process_2_x.setTextColor(Color.parseColor("#333333"));

                                                iv_process_l_x_x.setImageResource(R.mipmap.process_l_x);
                                                iv_process_3_x.setImageResource(R.mipmap.process_3_x);
                                                tv_process_3_x.setTextColor(Color.parseColor("#999999"));


                                            }

                                        }
                                    });
                                }

                                currentRecipes = newData;
//                                List<GetRecipeListByConsultIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO>  currentRecipesx = entity.data.jsonResponseBean.body;
                                //查看返回的所有处方单的处方信息，状态是不是1或者不是2，则不能支付。即不能结束问诊
                                // 9 是取消处方
//                                isRecipeCheckedFlag = true;
                                for (GetRecipeListByConsultIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO currentRecipe : currentRecipes) {
                                    if (currentRecipe.status ==2){
                                        isRecipeCheckedFlag = true;
                                    }
                                }

                                if (isRecipeCheckedFlag){

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {


//                                            Log.d("xxyyy", tv_process_tip.getText().toString());
                                                if (!tv_process_tip.getText().toString().equals("请结束问诊...")){

                                                    tv_process_tip.setText("请结束问诊...");
                                                    iv_process_l_x.setImageResource(R.mipmap.process_l);
                                                    iv_process_2_x.setImageResource(R.mipmap.process_f);
                                                    tv_process_2_x.setTextColor(Color.parseColor("#666666"));
                                                    iv_process_l_x_x.setImageResource(R.mipmap.process_l);
                                                    iv_process_3_x.setImageResource(R.mipmap.process_f);
                                                    tv_process_3_x.setTextColor(Color.parseColor("#666666"));
                                                    iv_process_l_x_x_x.setImageResource(R.mipmap.process_x);
                                                    iv_process_4_x.setImageResource(R.mipmap.process_4);
                                                    tv_process_4_x.setTextColor(Color.parseColor("#333333"));


//                                                    xFlag = false;

                                                    if (vtip==null){
                                                        vtip =   ViewTooltip
                                                                .on(btn_finish)
                                                                .position(ViewTooltip.Position.TOP)
                                                                .textSize(TypedValue.COMPLEX_UNIT_PX, DensityUtils.dp2px(22))
                                                                .color(Color.parseColor("#EEF5F7"))
                                                                .textColor(Color.parseColor("#303030"))
                                                                .autoHide(false, 3000)
                                                                .text("即将进入处方结算")
                                                                .show();
                                                    }


//                                                    vtip.setVisibility(View.VISIBLE);
//                                                    if (xFlag){
//                                                        ToastUtil.show("2121");
//                                                        xFlag = false;
//                                        vtip.close();

//                                                    }


                                                    timeStart();

//                                                    setLayoutWidth(btn_finish, DensityUtils.dp2px(260));
                                                }

                                        }
                                    });



                                }

                            }
                        }catch (Exception e){
                            e.printStackTrace();
//                            JTJKLogUtils.message(e.toString());
                        }
                    }
                });
    }

    int timeCountxx = 30;

//    private XUIPopup mNormalPopup;

    @Override
    protected void timeProcess() {
        super.timeProcess();






        if (isRecipeCheckedFlag){

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

//                    if (!tv_process_tip.getText().equals("请结束问诊...")){



                        String[] paramx = {"#fff",--timeCountxx+"","#fff","秒"};
                        if (timeCountxx>0){
                            btn_finish.setText("结束问诊("+ActivityUtils.formatTextView(paramx)+")");
                            btn_finish.setTextSize(DensityUtils.dp2px(22));
                        }else {
                            vtip.close();
                            btn_finish.setText("结束问诊");
                            btn_finish.setTextSize(DensityUtils.dp2px(29));
                        }

//                        initNormalPopupIfNeed();
//                        mNormalPopup.setAnimStyle(XUIPopup.ANIM_GROW_FROM_CENTER);
//                        mNormalPopup.setPreferredDirection(XUIPopup.DIRECTION_TOP);
//                        mNormalPopup.show(btn_finish);



//                    if (timeCountxx==0) {
//                        vtip.close();
//                        btn_finish.setText("结束问诊");
//                        btn_finish.setTextSize(DensityUtils.dp2px(29));
//                    }
//                    }

                }
            });

            if (timeCountxx==0){

//                vtip.close();
//                vtip.remove();
                requestPatientFinishGraphicTextConsult(consultId,true);

            }

        }



    }

    /**
     * 設置View的寬度（像素）。若設置爲自適應則應該傳入MarginLayoutParams.WRAP_CONTENT
     * @param view
     * @param width
     */
    public static void setLayoutWidth(View view,int width)
    {
       /* MarginLayoutParams margin=new MarginLayoutParams(view.getLayoutParams());
        margin.setMargins(margin.leftMargin,y, margin.rightMargin, y+margin.height);
        //RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        //view.setLayoutParams(layoutParams);
        ViewGroup.MarginLayoutParams  layoutParams =newLayParms(view, margin);
        //RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        view.setLayoutParams(layoutParams);
        view.requestLayout();*/
        if (view.getParent() instanceof FrameLayout){
            FrameLayout.LayoutParams lp=(FrameLayout.LayoutParams) view.getLayoutParams();
            lp.width=width;
            view.setLayoutParams(lp);
            //view.setX(x);
            view.requestLayout();
        }
        else if (view.getParent() instanceof RelativeLayout){
            RelativeLayout.LayoutParams lp=(RelativeLayout.LayoutParams)view.getLayoutParams();
            lp.width=width;
            view.setLayoutParams(lp);
            //view.setX(x);
            view.requestLayout();
        }
        else if (view.getParent() instanceof LinearLayout){
            LinearLayout.LayoutParams lp=(LinearLayout.LayoutParams)view.getLayoutParams();
            lp.width=width;
            view.setLayoutParams(lp);
            //view.setX(x);
            view.requestLayout();
        }
    }

//    private void initNormalPopupIfNeed() {
//        if (mNormalPopup == null) {
//            mNormalPopup = new XUIPopup(getContext());
//            TextView textView = new TextView(getContext());
//            textView.setLayoutParams(mNormalPopup.generateLayoutParam(
//                    DensityUtils.dp2px(getContext(), 250),
//                    WRAP_CONTENT
//            ));
//            textView.setLineSpacing(DensityUtils.dp2px(4), 1.0f);
//            int padding = DensityUtils.dp2px(20);
//            textView.setPadding(padding, padding, padding, padding);
//            textView.setText("即将进入处方结算");
//            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.xui_config_color_content_text));
//            textView.setTypeface(XUI.getDefaultTypeface());
//            mNormalPopup.setContentView(textView);
//            mNormalPopup.setOnDismissListener(() -> {
////                if (mBtnCommonPopup != null) {
////                    mBtnCommonPopup.setText("显示普通浮层");
////                }
//            });
//        }
//    }

    /**
     * 结束问诊
     * 纳达接口
     * 在确定结束问诊的按钮时才触发，返回首页不算
     * @param isHaveRecipe 医生是否开过处方单 true 开过，这个标志会最后跳转到确认处方单处
     */
    private void requestPatientFinishGraphicTextConsult(String consultId, boolean isHaveRecipe){
        ApiRepository.getInstance().patientFinishGraphicTextConsult(consultId)
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastLoadingObserver<PatientFinishGraphicTextConsultEntity>("请稍后...") {
                    @Override
                    public void _onNext(PatientFinishGraphicTextConsultEntity entity) {
                        if (entity == null) {
                            ToastUtil.show("请检查网络");
                            return;
                        }
                        try {
                            if (entity.data.jsonResponseBean.body){
//                                Log.d("JTJK","结束问诊");
                                // 跳转
                                if (isHaveRecipe){

                                    // 清理
                                    onDismiss();
                                    // 启动确定处方单
                                    if (currentRecipes!=null&&currentRecipes.size()>1){
                                        requestConsultsAndRecipes();
                                    }else {
                                        start(ConfirmRecipesFragment.newInstance(recipeId,currentRecipes));
                                    }


                                }else {
                                    // 清理
                                    onDismiss();
                                    // 启动返回首页
                                    start(HomeFragment.newInstance(), SupportFragment.SINGLETASK);
                                    isHomeBack = true;

                                }
                            }else{
//                                ToastUtil.show(entity.message);
                                onDismiss();
                                // 启动返回首页
                                start(HomeFragment.newInstance(), SupportFragment.SINGLETASK);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
//                            ToastUtil.show(entity.message);
                            onDismiss();
                            // 启动返回首页
                            start(HomeFragment.newInstance(), SupportFragment.SINGLETASK);
//                            JTJKLogUtils.message(e.toString());
                        }
                    }
                });
    }

    /**
     * 检测是否有未支付
     * 获取未支付复诊单挂号
     * 获取未支付处方单
     * 如果2个都没有，则跳转一级部门选择界面
     *
     * 如果复诊单挂号有多余的无效单，批量进行取消。
     */
    public void requestConsultsAndRecipes() {
        // 需要将 ongoing 和 onready 合并
        // 所有复诊单集合
        final List<GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Consults> allConsult = new ArrayList<>();
        // 所有处方单
        final ArrayList<GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes> allRecipes = new ArrayList<>();

        ApiRepository.getInstance().getConsultsAndRecipes("ongoing")
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastLoadingObserver<GetConsultsAndRecipesResultEntity>("请稍后...") {
                    @Override
                    public void _onNext(@io.reactivex.annotations.NonNull GetConsultsAndRecipesResultEntity entity) {
                        if (entity == null) {
                            ToastUtil.show("请检查网络，返回首页后重试");
                            return;
                        }
                        try {
                            if (entity.isSuccess()){
                                // 这里只收集数据，并不做处理
                                allConsult.addAll(entity.data.getConsults());
                                allRecipes.addAll(entity.data.recipes);

                                // 请求 onready
                                ApiRepository.getInstance().getConsultsAndRecipes("onready")
                                        .compose(VideoConsultFragment.this.bindUntilEvent(FragmentEvent.DESTROY))
                                        .subscribe(new FastLoadingObserver<GetConsultsAndRecipesResultEntity>("请稍后...") {
                                            @Override
                                            public void _onNext(@io.reactivex.annotations.NonNull GetConsultsAndRecipesResultEntity entity) {
                                                if (entity == null) {
                                                    ToastUtil.show("请检查网络，返回首页后重试");
                                                    return;
                                                }
                                                try {
                                                    if (entity.isSuccess()){
                                                        // 这里只收集数据，并不做处理
                                                        allConsult.addAll(entity.data.getConsults());
                                                        allRecipes.addAll(entity.data.recipes);

                                                        // 如果2个都不满足则跳转科室
                                                        boolean isDepartTag = true;
                                                        // 复诊单挂号和处方单不会同时出现，如果同时出现，则需要调整逻辑
                                                        // 查看挂号是否多余1条
                                                        if(allConsult.size()>0){
                                                            for (GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Consults item : allConsult) {
                                                                int status = item.getConsults().getStatus();
                                                                if (item.getConsults().getConsultOrgan() != GlobalConfig.organId)
                                                                    // 如果不是同一家机构，则跳过不处理
                                                                    break;
                                                                if ( item.getConsults().getPayflag()==1 &&
                                                                        (status==1 || status ==2 || status == 3 || status == 4)){
                                                                    //status=4 问诊中
                                                                    isDepartTag = false;
                                                                    // 去往复诊单挂号
                                                                    start(OrderConsultFragment.newInstance(item));
                                                                    return;
                                                                }
                                                                //  如果复诊单挂单有多余的无效单，批量进行取消。
                                                                if (item.getConsults().getPayflag()==0 && status!=8){
                                                                    // 取消复诊单 status = 8 已经取消
                                                                    ApiRepository.getInstance().patientCancelGraphicTextConsult(String.valueOf(item.getConsults().getConsultId()))
                                                                            .compose(VideoConsultFragment.this.bindUntilEvent(FragmentEvent.DESTROY))
                                                                            .subscribe(new FastObserver<CancelregisterResultEntity>() {
                                                                                @Override
                                                                                public void _onNext(CancelregisterResultEntity entity) {
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        }
                                                        // 查看处方单是否多余1条处方
                                                        if (allRecipes.size()>0){
                                                            // 每一个处方单中，都有一个处方信息，这个处方信息是需要合并的
//                                                            Log.d("090090",allRecipes.size()+"");
                                                            ArrayList<GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes> recipes = new ArrayList();
                                                            for (GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes item : allRecipes) {
//                                                                Log.d("090090",item.getOrganId()+"");
//                                                                Log.d("090090",GlobalConfig.organId+"");
                                                                if (item.getOrganId() == GlobalConfig.organId){
                                                                    // 如果不是同一家机构，则跳过不处理

                                                                    // 1 待审核, 2 待处理, 3 待取药
//                                                                if (item.status==2 || item.status==3){
//                                                                    Log.d("090090",item.status+"");
                                                                    if (item.status==2){
                                                                        recipes.add(item);
                                                                    }
                                                                }

                                                            }
                                                            // 遍历未支付处方单，如果有orderid 一样的，则合并处方
                                                            // 每合并一次，就会减少 recipes
                                                            // 每次结束，把0位置去掉，并添加到新
                                                            ArrayList<GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes> recipeGroup = new ArrayList();
                                                            if (recipes.size()>0){
                                                                while (recipes.size()>0) {
                                                                    // 被移除的对象
                                                                    final ArrayList<GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes> removeItems = new ArrayList();
                                                                    // 合并对象
                                                                    final GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes currentRecipes = recipes.get(0);
                                                                    for (int j = 1; j < recipes.size(); j++) {
                                                                        // orderId = null 表示没有合并支付过的，那么就不需要合并
                                                                        if ( recipes.get(j).orderId!=null && recipes.get(j).orderId.equals(currentRecipes.orderId)){
                                                                            currentRecipes.getRecipeDetailBeans().addAll(recipes.get(j).getRecipeDetailBeans());
                                                                            removeItems.add(recipes.get(j));
                                                                        }
                                                                    }
                                                                    // 移除合并单位
                                                                    if (removeItems.size()>0){
                                                                        for (GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes removeItem : removeItems) {
                                                                            recipes.remove(removeItem);
                                                                        }
                                                                    }
                                                                    // 获得数据后，将合并后的集合单位移除
                                                                    recipes.remove(0);
                                                                    recipeGroup.add(currentRecipes);
                                                                }
                                                            }


                                                            // 如果未支付处方单有，则进入批量处理界面
                                                            if (recipeGroup.size()>0){
                                                                isDepartTag = false;
                                                                start(OrderRecipesListFragment.newInstance(recipeGroup));
                                                                return;
                                                            }
                                                        }
                                                        // 如果即没有未支付处方单，也没有未支付复诊单
                                                        if (isDepartTag){
                                                            // 如果2个都不满足则跳转科室
                                                            start(DepartmentFragment.newInstance());
                                                        }
                                                    }else {
                                                        ToastUtil.show("获取未支付失败，请稍后重试");
                                                        start(HomeFragment.newInstance(), SupportFragment.SINGLETASK);
                                                    }
//                                                    isReadCardProcessing = false;
                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void _onError(Throwable e) {
                                                super._onError(e);
                                                ToastUtil.show("网络问题，返回首页后重试");
                                                start(HomeFragment.newInstance(), SupportFragment.SINGLETASK);
//                                                isReadCardProcessing = false;
                                            }
                                        });
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void _onError(Throwable e) {
                        super._onError(e);
                        ToastUtil.show("网络问题，返回首页后重试");
                        start(HomeFragment.newInstance(), SupportFragment.SINGLETASK);
//                        isReadCardProcessing = false;
                    }
                });
    }

    /**
     * 获取数据后，显示处方信息列表
     * 整个逻辑和输入的 newDatas 的类型有关系，只需要换这个类型即可
     * @param recyclerView 显示对象
     * @param newDatas 传入的数据列
     */
    protected void reflashRecyclerView(RecyclerView recyclerView, List<GetRecipeListByConsultIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO> newDatas){
        // 安全检测
        if (newDatas==null)
            return;

        if (recyclerView.getVisibility()==View.GONE)
            recyclerView.setVisibility(View.VISIBLE);

        if (proxy==null){
            proxy = new AutoAdaptorProxy<>(recyclerView, R.layout.item_recipes, 1, newDatas, getContext());
            proxy.setListener(new AutoAdaptorProxy.IItemListener<GetRecipeListByConsultIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO>() {
                @Override
                public void onItemClick(AutoObjectAdaptor.ViewHolder holder, int position, GetRecipeListByConsultIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO itemData) {
                }

                @Override
                public void onItemViewDraw(AutoObjectAdaptor.ViewHolder holder, int position, GetRecipeListByConsultIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO itemData) {
                    GetRecipeListByConsultIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO.RecipeDetailBeanListDTO vo = itemData.recipeDetailBeanList.get(0);
//                    int perDayUse = ((Double) vo.useDose).intValue();
                    String perDayUse = "适量";
                    if ((Double) vo.useDose!=null)
//                        perDayUse = String.valueOf(((Double) vo.useDose).intValue()) + vo.useDoseUnit;
                    perDayUse = Double.toString(vo.useDose) + vo.useDoseUnit;


                    String drugName = (position+1)+"、"+vo.drugName;
//                    String wayToUse = "(1天"+vo.useTotalDose/vo.useDays+"次，每次"+perDayUse+")";
                    String wayToUse = "("+vo.usingRateText+"，每次"+perDayUse+")";
                    String[] orders = {"#333333",drugName,"#38ABA0",wayToUse};
                    ((TextView)holder.itemView.findViewById(R.id.tv_useDose)).setText(ActivityUtils.formatTextView(orders));//使用方法
                }
            });
        }
        //刷新
        proxy.flashData(new ArrayList<>(newDatas));
    }

    @Override
    public void onDismiss() {
        super.onDismiss();
        // 关闭，并释放所有资源
        // 包括向医生端发送socket消息
        EaseModeProxy.with().closeVideoProxy();
        // 大屏接口，病人离席，关闭大屏视频
        DapinSocketProxy.with()
                .initWithOld(getActivity(),GlobalConfig.machineIp)
                .startSocket(DapinSocketProxy.FLAG_SCREENFLAG_CLOSESCREEN);
        //清除mDisposable不再进行验证
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }
        if (mDisposableNew != null) {
            mDisposableNew.dispose();
            mDisposableNew = null;
        }
        // 清理大屏socket
        DapinSocketProxy.with().failDestroy();
        // 释放自己，让 onCreate 下次进来的时候有效
        // 让 fragment 出栈
//        Log.d("JTJK", "pop: start");
//        if (!isHomeBack)
//            pop();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
//        if (x5_webview != null) {
//            x5_webview.removeAllViews();
//            x5_webview.destroy();
//        }


//        if (webview != null) {
//            webview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
//            webview.clearHistory();
//
//            ((ViewGroup) webview.getParent()).removeView(webview);
//
//            webview.destroy();
//            webview = null;
//        }



//        Log.d("JTJK", "onDestroy: start");
//        // 关闭，并释放所有资源
//        // 包括向医生端发送socket消息
//        EaseModeProxy.with().closeVideoProxy();
//        // 大屏接口，病人离席，关闭大屏视频
//        DapinSocketProxy.with()
//                .initWithOld(getActivity(),GlobalConfig.machineIp)
//                .startSocket(DapinSocketProxy.FLAG_SCREENFLAG_CLOSESCREEN);
//        //清除mDisposable不再进行验证
//        if (mDisposable != null) {
//            mDisposable.dispose();
//            mDisposable = null;
//        }
//        // 清理大屏socket
//        DapinSocketProxy.with().failDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden){
            if (mDisposable != null) {
                mDisposable.dispose();
                mDisposable = null;
            }
            if (mDisposableNew != null) {
                mDisposableNew.dispose();
                mDisposableNew = null;
            }
        } else {
            // 启动请求
//            requestConfigurationToThirdForPatient();
        }
    }

    /**
     * 设置title的信息
     */
    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setBgColor(Color.WHITE)
                .setTitleMainText(R.string.mine);
    }


//    /**
//     * 查询复诊单的小鱼视频会议室房间号和密码
//     */
//    private void requestGetRoomIdInsAuth(){
//        ApiRepository.getInstance().getRoomIdInsAuth(consultId,GlobalConfig.NALI_APPKEY)
//                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
//                .subscribe(new FastLoadingObserver<RoomIdInsAuthEntity>("请稍后...") {
//                    @Override
//                    public void _onNext(RoomIdInsAuthEntity entity) {
//                        if (entity == null) {
//                            ToastUtil.show("请检查网络");
//                            return;
//                        }
//                        if (entity.getData().isSuccess()){
//                                    EaseModeProxy.with().easemobStart(getActivity(),
//                                            consultId,
//                                            nickname,
//                                            doctorUserId,
//                                            username,
//                                            userpwd,
//                                            userId,
//                                            String.valueOf(entity.getData().getJsonResponseBean().getBody().getDetail().getMeetingNumber()),
//                                            String.valueOf(entity.getData().getJsonResponseBean().getBody().getDetail().getControlPassword())
//                                            );
////                            EaseModeProxy.with().xyInit();
//                        }
//                    }
//                });
//    }

//    /**
//     * 3.1.3 患者最新待处理处方
//     */
//    private void requestFindRecipesForPatientAndTabStatus(){
//        if (TextUtils.isEmpty(recipeId))
//        ApiRepository.getInstance().findRecipesForPatientAndTabStatus()
//                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
//                .subscribe(new FastLoadingObserver<FindRecipesForPatientAndTabStatusEntity>("请稍后...") {
//                    @Override
//                    public void _onNext(FindRecipesForPatientAndTabStatusEntity entity) {
//                        if (entity == null) {
//                            ToastUtil.show("请检查网络");
//                            return;
//                        }
//                        if (entity.getData().isSuccess()){
//                            // todo 刷新 RV 处方单界面
//                            // todo 查看返回的所有处方单的处方信息，状态是不是1或者不是2，则不能支付。即不能结束问诊
//                            // 处理处方信息，并展示
//                            if (rv_video_tip.getVisibility()!=View.VISIBLE)
//                                rv_video_tip.setVisibility(View.VISIBLE);
////                         reflashRecyclerView(rv_video_tip,entity.getData().getJsonResponseBean().getBody());
//                        }
//                    }
//                });
//    }


}
