package com.aries.template.module.mine;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.retrofit.FastObserver;
import com.aries.library.fast.util.SPUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.template.GlobalConfig;
import com.aries.template.R;
import com.aries.template.entity.CancelregisterResultEntity;
import com.aries.template.entity.ConfigurationToThirdForPatientEntity;
import com.aries.template.entity.FindRecipesForPatientAndTabStatusEntity;
import com.aries.template.entity.GetConsultsAndRecipesResultEntity;
import com.aries.template.entity.GetRecipeListByConsultIdEntity;
import com.aries.template.entity.PatientFinishGraphicTextConsultEntity;
import com.aries.template.entity.RoomIdInsAuthEntity;
import com.aries.template.module.base.BaseEventFragment;
import com.aries.template.module.main.HomeFragment;
import com.aries.template.retrofit.repository.ApiRepository;
import com.aries.template.utils.ActivityUtils;
import com.aries.template.view.ShineButtonDialog;
import com.aries.template.widget.autoadopter.AutoAdaptorProxy;
import com.aries.template.widget.autoadopter.AutoObjectAdaptor;
import com.aries.template.xiaoyu.EaseModeProxy;
import com.aries.template.xiaoyu.dapinsocket.DapinSocketProxy;
import com.aries.ui.view.title.TitleBarView;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.xuexiang.xaop.annotation.SingleClick;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * 视频问诊
 * 用于显示一级部门，二级部门
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

    private boolean doctorInRoomFlag=false;// 医生是否进入

    private boolean isRecipeCheckedFlag=false;// 医生开出的处方状态是不是1或者不是2，则不能支付。即不能结束问诊

    private List<GetRecipeListByConsultIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO> currentRecipes;// 从后端传入的数据

    private DapinSocketProxy dapinSocketProxy;// 大屏的通信代理

    private boolean isBodyTestingFlag;// 是否从身体检测回来，如果曾经出过身体检测，则为true

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
     */
    public static VideoConsultFragment newInstance(String consultId,String nickname, String  doctorUserId, String doctorName) {
        // 复诊单的配置
        VideoConsultFragment fragment = new VideoConsultFragment();
        Bundle args = new Bundle();
        args.putString("consultId",consultId);
        args.putString("nickname",nickname);
        args.putString("doctorUserId",doctorUserId);
        args.putString("doctorName",doctorName);
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
        // 启动请求
        requestConfigurationToThirdForPatient();
    }

    @Override
    public void onStart() {
        super.onStart();
        // 创建视频处理器
        ViewGroup viewGroup = getActivity().findViewById(R.id.videoContent);
        EaseModeProxy.with().initView(getActivity(),viewGroup).onStartVideo();
        EaseModeProxy.with().setListener(new EaseModeProxy.ProxyEventListener() {
            @Override
            public void onDoctorInRoom() {
                doctorInRoomFlag = true;
            }

            @Override
            public void onVideoSuccessLinked() {
                //入会成功
                // 大屏接口，启动大屏
                dapinSocketProxy.startSocket(DapinSocketProxy.SCREENFLAG_CONTROLSCREEN);
                //入会成功, 启动轮训处方单状态
                timeLoop();
            }
        });
        // 创建大屏代理
        if (dapinSocketProxy==null)
            dapinSocketProxy = new DapinSocketProxy(getActivity(),GlobalConfig.machineIp);

        // 从外部身体检测应用回来，大屏接口，身体检测结束
        if (isBodyTestingFlag){
            isBodyTestingFlag = false;
            dapinSocketProxy.startSocket(DapinSocketProxy.SCREENFLAG_BODYTESTING_FINISH);
        }
    }

    /**
     * 页面的动作，包括数据传输和界面改变
     */
    @Override
    public void initView(Bundle savedInstanceState) {
        jtjk_video_doctorname.setText("复诊医生: " +doctorName);
    }

    @SingleClick
    @OnClick({R.id.btn_stjc, R.id.btn_finish,R.id.jtjk_video_close_full,R.id.btn_full_screen})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_stjc:
                // 向大屏通信，告知跳转视频
                dapinSocketProxy.startSocket(DapinSocketProxy.SCREENFLAG_BODYTESTING_OPEN);
                // 跳向身体检测，则更新flag准备回来
                isBodyTestingFlag = true;
                // 跳向身体检测
                Intent intent = new Intent(Intent.ACTION_MAIN);
                /**知道要跳转应用的包命与目标Activity*/
                ComponentName componentName = new ComponentName("com.garea.launcher", "com.garea.launcher.login.LauncherLogin");
                intent.setComponent(componentName);
                intent.putExtra("userName", SPUtil.get(mContext,"userName","")+"");//这里Intent传值
                intent.putExtra("idCard", SPUtil.get(mContext,"idCard","")+"");
                intent.putExtra("mobile", SPUtil.get(mContext,"mobile","")+"");
                startActivity(intent);
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
     */
    private void showSimpleConfirmDialog() {
        ShineButtonDialog dialog = new ShineButtonDialog(this.mContext);
        dialog.tv_title_tip.setText("结束问诊");
        dialog.tv_content_tip.setText("是否结束问诊");
        dialog.btn_inquiry.setOnClickListener(v -> {
            dialog.dismiss();
            if (doctorInRoomFlag){
                // 结束问诊，如果有问诊单，则根据这个问诊单进入支付
                // 在栈内的HomeFragment以SingleTask模式启动（即在其之上的Fragment会出栈）
                if (!isRecipeCheckedFlag){
                    ToastUtil.show("处方正在被医生确认，请稍后再试");
                } else{
                    // 关闭，并释放所有资源
                    EaseModeProxy.with().closeVideoProxy();
                    // 关闭问诊接口
                    requestPatientFinishGraphicTextConsult();
                    // 大屏接口，病人离席
                    dapinSocketProxy.startSocket(DapinSocketProxy.SCREENFLAG_CLOSESCREEN);
                    // 启动确定处方单
                    start(ConfirmRecipesFragment.newInstance(recipeId,currentRecipes));
                }
            }else{
                // 取消复诊
                requestPatientFinishGraphicTextConsult(consultId);
            }
        });
        dialog.btn_cancel.setOnClickListener(v -> dialog.dismiss());
        dialog.iv_close.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private static final int PERIOD = 5* 1000;
    private static final int DELAY = 100;
    private Disposable mDisposable;
    /**
     * 定时循环任务
     * 5秒循环查询新的待处理处方信息
     */
    private void timeLoop() {
        mDisposable = Observable.interval(DELAY, PERIOD, TimeUnit.MILLISECONDS)
                .map((aLong -> aLong + 1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> requestGetRecipeListByConsultId());//getUnreadCount()执行的任务
    }

    /**
     * 患者取消复诊服务
     * @param consultId 复诊单号
     */
    private void requestPatientFinishGraphicTextConsult(String consultId) {
        ApiRepository.getInstance().patientCancelGraphicTextConsult(consultId)
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastLoadingObserver<CancelregisterResultEntity>("请稍后...") {
                    @Override
                    public void _onNext(CancelregisterResultEntity entity) {
                        if (entity == null) {
                            ToastUtil.show("请检查网络");
                            return;
                        }
                        if (entity.isSuccess()){
                            if (entity.getData().isSuccess()){
                                // 医生不曾进入到视频中
                                start(HomeFragment.newInstance(), SupportFragment.SINGLETASK);
                            }
                        }
                    }
                });
    }

    /**
     * 获取第三方配置信息
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
                        if (entity.data.success){
                            // 隐藏显示提示，等待旋转
                            if (rv_video_wait.getVisibility()==View.VISIBLE)
                                rv_video_wait.setVisibility(View.GONE);
                            if (rv_video_tip.getVisibility()==View.GONE)
                                rv_video_tip.setVisibility(View.VISIBLE);

                            if (entity.data.jsonResponseBean.body.size()<1)
                                return;

                            //刷新 RV 处方单界面
                            recipeId = String.valueOf(entity.data.jsonResponseBean.body.get(0).recipeId);
                            reflashRecyclerView(rv_video_tip,entity.data.jsonResponseBean.body);
                            currentRecipes = entity.data.jsonResponseBean.body;

                            //查看返回的所有处方单的处方信息，状态是不是1或者不是2，则不能支付。即不能结束问诊
                            isRecipeCheckedFlag = true;
                            for (GetRecipeListByConsultIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO currentRecipe : currentRecipes) {
                                if (currentRecipe.status!=1 && currentRecipe.status!=2){
                                    isRecipeCheckedFlag = false;
                                }
                            }
                        }
                    }
                });
    }

    /**
     * 结束问诊
     * 纳达接口
     * 在确定结束问诊的按钮时才触发，返回首页不算
     */
    private void requestPatientFinishGraphicTextConsult(){
        ApiRepository.getInstance().patientFinishGraphicTextConsult(consultId)
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastObserver<PatientFinishGraphicTextConsultEntity>() {
                    @Override
                    public void _onNext(PatientFinishGraphicTextConsultEntity entity) {
                        if (entity == null) {
                            ToastUtil.show("请检查网络");
                            return;
                        }
//                        if (entity.data.success){
//
//                        }
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
        AutoAdaptorProxy<GetRecipeListByConsultIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO> proxy
                = new AutoAdaptorProxy<>(recyclerView, R.layout.item_recipes, 1, newDatas, getContext());

        proxy.setListener(new AutoAdaptorProxy.IItemListener<GetRecipeListByConsultIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO>() {
            @Override
            public void onItemClick(AutoObjectAdaptor.ViewHolder holder, int position, GetRecipeListByConsultIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO itemData) {
            }

            @Override
            public void onItemViewDraw(AutoObjectAdaptor.ViewHolder holder, int position, GetRecipeListByConsultIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO itemData) {
                GetRecipeListByConsultIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO.RecipeDetailBeanListDTO vo = itemData.recipeDetailBeanList.get(0);
                int perDayUse = ((Double) vo.useDose).intValue();

                String drugName = (position+1)+"、"+vo.drugName;
                String wayToUse = "(1天"+vo.useTotalDose/vo.useDays+"次，每次"+perDayUse+"片)";
                String[] orders = {"#333333",drugName,"#38ABA0",wayToUse};
                ((TextView)holder.itemView.findViewById(R.id.tv_useDose)).setText(ActivityUtils.formatTextView(orders));//使用方法
            }
        });
        //刷新
        proxy.notifyDataSetChanged();
    }

    @Override
    public void onDismiss() {
        super.onDismiss();
        // 关闭，并释放所有资源
        EaseModeProxy.with().closeVideoProxy();
        //清除mDisposable不再进行验证
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }
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
            requestConfigurationToThirdForPatient();
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
