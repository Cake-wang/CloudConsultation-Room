package com.aries.template.module.mine;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.retrofit.FastObserver;
import com.aries.library.fast.util.ToastUtil;
import com.aries.template.GlobalConfig;
import com.aries.template.R;
import com.aries.template.entity.CancelregisterResultEntity;
import com.aries.template.entity.ConfigurationToThirdForPatientEntity;
import com.aries.template.entity.GetRecipeListByConsultIdEntity;
import com.aries.template.entity.PatientFinishGraphicTextConsultEntity;
import com.aries.template.module.base.BaseEventFragment;
import com.aries.template.module.main.HomeFragment;
import com.aries.template.retrofit.repository.ApiRepository;
import com.aries.template.thridapp.JTJKThirdAppUtil;
import com.aries.template.utils.ActivityUtils;
import com.aries.template.utils.DefenceUtil;
import com.aries.template.utils.JTJKLogUtils;
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

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
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
        return R.layout.fragment_video;
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
            // 创建视频处理器
            ViewGroup viewGroup = getActivity().findViewById(R.id.videoContent);
            EaseModeProxy.with().onStart(getActivity(),viewGroup);
            EaseModeProxy.with().setListener(new EaseModeProxy.ProxyEventListener() {
                @Override
                public void onDoctorInRoom() {
                    // 医生进入的TAG
                    isDoctorInRoomFlag = true;
                    // 如果医生进入了，则直接释放XL的socket
                    XLMessage.with().destroy();
                    // 大屏接口，启动大屏
                    DapinSocketProxy.with()
                            .initWithOld(getActivity(),GlobalConfig.machineIp)
                            .startSocket(DapinSocketProxy.FLAG_SCREENFLAG_CONTROLSCREEN);

                    // 可以点击身体检测
                    btn_stjc.setEnabled(true);
                }

                @Override
                public void onDoctorOutRoom() {
                    // 医生离开了
                    // 身体检测不可使用
                    btn_stjc.setEnabled(false);

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

    /**
     * 页面的动作，包括数据传输和界面改变
     */
    @Override
    public void initView(Bundle savedInstanceState) {
        jtjk_video_doctorname.setText("复诊医生: " +doctorName);
//        btn_full_screen.setVisibility(View.GONE);
//        btn_stjc.setEnabled(true); //todo cc
    }

    @SingleClick
    @OnClick({R.id.btn_stjc, R.id.btn_finish,R.id.jtjk_video_close_full,R.id.btn_full_screen})
    public void onViewClicked(View view) {
        DefenceUtil.checkReSubmit("VideoConsultFragment.onViewClicked");
        switch (view.getId()) {
            case R.id.btn_stjc:
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
                    ToastUtil.show("没有第三方应用信息，无法跳转");
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
                showSimpleConfirmDialog();
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
    private void showSimpleConfirmDialog() {
        // 结束问诊框是否已经打开
        if (!isDialogOpened){
            ShineButtonDialog dialog = new ShineButtonDialog(this.mContext);
            dialog.tv_title_tip.setText("结束问诊");
            dialog.tv_content_tip.setText("是否结束问诊");
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
                            ToastUtil.show("处方正在被医生确认，请稍后再试");
                    } else{
                        // 结束问诊接口
                        // 成功后，跳转确认复诊单
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
    /**
     * 定时循环任务
     * 入会成功后执行
     * - 5秒循环查询新的待处理处方信息
     * - 5秒用于信令，医生端接口反馈循环
     */
    private void timeLoop() {
        mDisposable = Observable.interval(DELAY, PERIOD, TimeUnit.MILLISECONDS)
                .map((aLong -> aLong + 1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {requestGetRecipeListByConsultId();
                    if (!isDoctorInRoomFlag)
                        // 启动向医生发送消息
                        // 如果医生没有进入房间，则不停的继续call
                        // 如果医生进入房间 onDoctorInRoom 的时候会被释放 XLMessage
                        EaseModeProxy.with().sendNotifyDoctorVideoMsg();
                });//getUnreadCount()执行的任务
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
                                    Log.d("JTJK","患者取消复诊服务");
                                    // 医生不曾进入到视频中
                                    start(HomeFragment.newInstance(), SupportFragment.SINGLETASK);
                                    isHomeBack = true;
                                    // 清理
                                    onDismiss();
                                }else{
                                    // 可能出现医生已经主动回复，无法取消的情况
                                    ToastUtil.show(entity.getData().errorMessage);
                                    start(HomeFragment.newInstance(), SupportFragment.SINGLETASK);
                                    isHomeBack = true;
                                    // 清理
                                    onDismiss();
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            JTJKLogUtils.message(e.toString());
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
                            JTJKLogUtils.message(e.toString());
                        }
                    }
                });

    }

    /**
     * 通过复诊单获得处方单
     * 拿到处方单后，开始轮训
     */
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
                                if (rv_video_wait.getVisibility()==View.VISIBLE)
                                    rv_video_wait.setVisibility(View.GONE);
                                if (rv_video_tip.getVisibility()==View.GONE)
                                    rv_video_tip.setVisibility(View.VISIBLE);

                                if (entity.data.jsonResponseBean.body.size()<1)
                                    return;

                                // 筛选处方
                                // 去掉被取消的处方
                                ArrayList<GetRecipeListByConsultIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO> newData = new ArrayList<>();
                                for (GetRecipeListByConsultIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO item : entity.data.jsonResponseBean.body) {
                                    // 9 是取消处方
                                    if (item.status != 9)
                                        newData.add(item);
                                }
                                //刷新 RV 处方单界面
                                recipeId = String.valueOf(entity.data.jsonResponseBean.body.get(0).recipeId);
                                reflashRecyclerView(rv_video_tip,newData);
                                currentRecipes = entity.data.jsonResponseBean.body;

                                //查看返回的所有处方单的处方信息，状态是不是1或者不是2，则不能支付。即不能结束问诊
                                // 9 是取消处方
                                isRecipeCheckedFlag = true;
                                for (GetRecipeListByConsultIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO currentRecipe : currentRecipes) {
                                    if (currentRecipe.status!=1 && currentRecipe.status!=2 && currentRecipe.status!=9){
                                        isRecipeCheckedFlag = false;
                                    }
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            JTJKLogUtils.message(e.toString());
                        }
                    }
                });
    }

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
                                Log.d("JTJK","结束问诊");
                                // 跳转
                                if (isHaveRecipe){
                                    // 启动确定处方单
                                    start(ConfirmRecipesFragment.newInstance(recipeId,currentRecipes));
                                    // 清理
                                    onDismiss();
                                }else {
                                    // 启动返回首页
                                    start(HomeFragment.newInstance(), SupportFragment.SINGLETASK);
                                    isHomeBack = true;
                                    // 清理
                                    onDismiss();
                                }
                            }else{
                                ToastUtil.show(entity.message);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            JTJKLogUtils.message(e.toString());
                        }
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
                        perDayUse = String.valueOf(((Double) vo.useDose).intValue()) + "片";


                    String drugName = (position+1)+"、"+vo.drugName;
                    String wayToUse = "(1天"+vo.useTotalDose/vo.useDays+"次，每次"+perDayUse+")";
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
        // 清理大屏socket
        DapinSocketProxy.with().failDestroy();
        // 释放自己，让 onCreate 下次进来的时候有效
        // 让 fragment 出栈
        Log.d("JTJK", "pop: start");
//        if (!isHomeBack)
//            pop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
