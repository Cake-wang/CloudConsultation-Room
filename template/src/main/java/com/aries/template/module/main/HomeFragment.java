package com.aries.template.module.main;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.util.SPUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.template.GlobalConfig;
import com.aries.template.MainActivity;
import com.aries.template.R;
import com.aries.template.entity.ConfigurationToThirdForPatientEntity;
import com.aries.template.entity.FindUserResultEntity;
import com.aries.template.entity.MachineEntity;
import com.aries.template.module.base.BaseEventFragment;
import com.aries.template.module.mine.MineCardFragment;
import com.aries.template.retrofit.repository.ApiRepository;
import com.aries.template.thridapp.JTJKThirdAppUtil;
import com.aries.template.utils.DefenceUtil;
import com.aries.template.view.SplashDialog;
import com.aries.template.xiaoyu.dapinsocket.DapinSocketProxy;
import com.aries.template.xiaoyu.xinlin.XLMessage;
import com.aries.ui.view.title.TitleBarView;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.trello.rxlifecycle3.android.FragmentEvent;

import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 首页
 * 返回首页后，需要清空一些全局数据
 *
 * 首页启动大屏 必须跟在获取全局信息之后
 *
 * @Author: AriesHoo on 2018/8/10 12:22
 * @E-Mail: AriesHoo@126.com
 * Function: 主页演示
 * Description:
 */
public class HomeFragment extends BaseEventFragment{

    @BindView(R.id.iv_stjc)
    TextView iv_stjc;//身体检查
    @BindView(R.id.iv_fzpy)
    TextView iv_fzpy;//复诊配药
    @BindView(R.id.jtjk_machine_id)
    TextView jtjk_machine;//机器编号
    @BindView(R.id.jtjk_hospital_name)
    TextView jtjk_hospital;//医院名称
    @BindView(R.id.tv_setting)
    TextView tv_setting;//医院名称

    private static final int PERIOD = 999* 1000;
    private static final int DELAY = 15*1000;
    private Disposable mDisposable;
    private SplashDialog dialog;
    /**
     * 定时循环任务
     * 入会成功后执行
     * - 5秒循环查询新的待处理处方信息
     * - 5秒用于信令，医生端接口反馈循环
     */
    private void timeLoop() {
        if (mDisposable!=null){
            mDisposable.dispose();
            mDisposable = null;
        }
        mDisposable = Observable.interval(DELAY, PERIOD, TimeUnit.MILLISECONDS)
                .map((aLong -> aLong + 1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    // 会导致程序再被15秒后，拉起来
                    if (dialog==null){
                        // 启动显示 广告框
                        if (getActivity()==null)
                            return;
                        dialog = new SplashDialog(getActivity());
                        dialog.getBGLayout().setOnClickListener(view -> {
                            dialog.onDismiss();
                            dialog=null;
                            timeLoop();
                        });
                        dialog.show();
                        if (mDisposable!=null){
                            mDisposable.dispose();
                            mDisposable = null;
                        }
                    }
                });//getUnreadCount()执行的任务
    }

    /**
     * Activity 被缩下去后执行
     */
    @Override
    public void onStop() {
        super.onStop();
        // 会导致程序不再被15秒后，拉起来
//        if (mDisposable!=null){
//            mDisposable.dispose();
//            mDisposable = null;
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Log.e("onDestroy","onDestroy");
        if (mDisposable!=null){
            mDisposable.dispose();
            mDisposable = null;
        }
        if (dialog!=null){
            dialog.onDismiss();

        }
    }

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
    }

    @Override
    public int getContentLayout() {
        if(GlobalConfig.thirdFactory.equals("3")||GlobalConfig.thirdFactory.equals("2")){
            return R.layout.fragment_home_l;
        }else {
            return R.layout.fragment_home;
        }
//        return R.layout.fragment_home;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        // 不显示倒计时，不进行倒计时计算
//        dismissCountTimeStop();
        // 点击身体检查
        iv_stjc.setOnClickListener(v -> {
            SPUtil.put(mContext,"tag","stjc");
            start(MineCardFragment.newInstance("stjc")); // 进入身体检查
//                start(DepartmentFragment.newInstance("stjc"));// todo cc
//                start(PutRecordFragment.newInstance("idcard","name","smkcard"));// todo cc
//                ((MainActivity) getActivity()).requestConsultsAndRecipes();//todo cc
//                start(ResultFragment.newInstance("cancelConsult"));//todo ccss
//                start(ConfirmRecipesFragment.newInstance(null));// todo cc
//                start(PayCodeFragment.newInstance(new Object()));// todo cc
//                start(VideoConsultFragment.newInstance(FakeDataExample.consultId,FakeDataExample.nickname,FakeDataExample.doctorUserId,FakeDataExample.doctorName,true));// todo cc
//                start(PayCodeFragment.newInstance(FakeDataExample.recipeFee,FakeDataExample.recipeIds,FakeDataExample.recipeCode));// todo cc
//            startActivity(new Intent(getActivity(), MeetingActivity.class));//todo cc
//            WebViewActivity.start(getActivity(),"https://www.hfi-health.com:28181/agreement/yzs-ysxy.html"); // todo cc

//            // 打印取药单
//            //"data": "{\"orderNo\":\"\",\"takeCode\":\"34811555\"}",
//            String drug = "";
//                // 格式化打印数据
//                // 药物用量
//                for (int i = 0; i < 3; i++) {
//                    drug += "测试用药" + " " + "药量"+"&&";
//                }
//            start(ResultFragment.newInstance("paySuc:"+"34811555"+":"+drug));
        });

        // 点击复诊
        iv_fzpy.setOnClickListener(v -> {
            SPUtil.put(mContext,"tag","fzpy");
            start(MineCardFragment.newInstance("fzpy"));
        });

        // 显示医院
        if (jtjk_hospital!=null)
            jtjk_hospital.setText(GlobalConfig.hospitalName);
        // 显示机器
        if (jtjk_machine!=null)
            jtjk_machine.setText("机器编号:"+GlobalConfig.machineId);

        // 启动时，需要立刻请求，机器相关的数据，并保存在全局
        requestMachineInfo();
        // 启动环信视频
//        requestConfigurationToThirdForPatient();
        // 启动时间循环，显示广告
        timeLoop();


        tv_setting.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onDoubleClick(View v) {

                Intent intent= new Intent(Settings.ACTION_HOME_SETTINGS);
                startActivity(intent);

//                start(PhoneRegisterFragment.newInstance( "33052219861229693X", "王郭亮", "AR6114503"));

//                start(ResultFragment.newInstance("paySuc"));

//                start(ResultFragment.newInstance("fail"));

            }
        });


    }


    public  abstract class DoubleClickListener implements View.OnClickListener {
        private static final long DOUBLE_TIME = 1000;
        private  long lastClickTime = 0;

        @Override
        public void onClick(View v) {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - lastClickTime < DOUBLE_TIME) {
                onDoubleClick(v);
            }
            lastClickTime = currentTimeMillis;
        }
        public abstract void onDoubleClick(View v);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 获取第三方配置信息
     * 专门登录环信 专用
     */
    private void requestConfigurationToThirdForPatient(){
        ApiRepository.getInstance().getConfigurationToThirdForPatient(GlobalConfig.NALI_TID,GlobalConfig.NALI_APPKEY)
                .compose(((MainActivity) getActivity()).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastLoadingObserver<ConfigurationToThirdForPatientEntity>("请稍后...") {
                    @Override
                    public void _onNext(ConfigurationToThirdForPatientEntity entity) {
                        if (entity == null) {
                            ToastUtil.show("请检查网络");
                            return;
                        }
                        try {
                            if (entity.getData().isSuccess()){
                                loginEmClient(entity.getData().getJsonResponseBean().getBody().getUsername(),entity.getData().getJsonResponseBean().getBody().getUserpwd());
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 专门登录环信
     */
    public void loginEmClient(String easemobUserName,String easemobPassword){
        //登陆 https://docs-im.easemob.com/im/android/sdk/basic
//        EMClient.getInstance().logout(true);
        EMCallBack emcallback = new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
//                ToastUtil.show("环信登录成功");
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(int code, String message) {
            }
        };
        // 防止用户由于特殊原因登出，然后再进来的时候，被提示已经登录
        EMClient.getInstance().logout(true);
        EMClient.getInstance().login("zj_test_"+easemobUserName, easemobPassword,emcallback );
//        Log.e("easemob",easemobUserName);
//        Log.e("easemob",easemobPassword);
    }

    /**
     * 通过机器编号，获得全局的数据
     * 并保存在全局
     */
    public void requestMachineInfo(){
        // 防止重复提交
        if (!DefenceUtil.checkReSubmit("HomeFragment.requestMachineInfo")){
            return;
        }
        String deviceId ;
        if(GlobalConfig.thirdFactory.equals("3")){
            // 湘湖
                    GlobalConfig.machineId = "ZX1G42CPJD";
                    GlobalConfig.thirdMachineId = "ZX1G42CPJD";
            // 盈丰
//            GlobalConfig.machineId = "ZX1G42CPJE";
//            GlobalConfig.thirdMachineId = "ZX1G42CPJE";

            // 湖山
//            GlobalConfig.machineId = "ZX1G42CPJF";
//            GlobalConfig.thirdMachineId = "ZX1G42CPJF";

            // 南阳潮都
//              GlobalConfig.machineId = "ZX1G42CPJG";
//              GlobalConfig.thirdMachineId = "ZX1G42CPJG";

            // 利丰
//            GlobalConfig.machineId = "ZX1G42CPJH";
//            GlobalConfig.thirdMachineId = "ZX1G42CPJH";

            // 北干塘湾
//            GlobalConfig.machineId = "ZX1G42CPJI";
//            GlobalConfig.thirdMachineId = "ZX1G42CPJI";

            // 戴村云石
//            GlobalConfig.machineId = "ZX1G42CPJJ";
//            GlobalConfig.thirdMachineId = "ZX1G42CPJJ";

            // 义桥
//            GlobalConfig.machineId = "ZX1G42CPJK";
//            GlobalConfig.thirdMachineId = "ZX1G42CPJK";

        }
//        if(GlobalConfig.thirdFactory.equals("2")){
//            GlobalConfig.machineId = "N73MAX3CZQ";
//            GlobalConfig.thirdMachineId = "N73MAX3CZQ";
//        }
        else {
            GlobalConfig.machineId = ApiRepository.getDeviceId();
        }
//         deviceId = ApiRepository.getDeviceId();
//        Log.e("deviceId",deviceId);
//        GlobalConfig.machineId = deviceId;
        ApiRepository.getInstance().findByMachineId(GlobalConfig.machineId)
            .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
            .subscribe(new FastLoadingObserver<MachineEntity>("请稍后...") {
                @Override
                public void _onNext(@io.reactivex.annotations.NonNull MachineEntity entity) {
                    if (entity == null) {
                        ToastUtil.show("请检查网络，返回首页后重试");
                        return;
                    }
                   try {
                       if (entity.success){
                           if (entity.data==null){
                               ToastUtil.show("机器号没有配置，请联系工作人员");
                               return;
                           }
                           GlobalConfig.machineId = entity.data.machineId;
                           GlobalConfig.cabinetId = entity.data.cabinetId;
                           GlobalConfig.hospitalName = entity.data.hospitalName;
                           GlobalConfig.organId = Integer.valueOf(entity.data.hospitalNo);
                           GlobalConfig.machineIp = entity.data.machineIp + ":"+GlobalConfig.machinePort;// 端口是写死的，传入的只有ip
                           GlobalConfig.thirdFactory = entity.data.thirdFactory;
//                        GlobalConfig.thirdMachineId = entity.data.thirdMachineId;// 暂定不赋予
                           GlobalConfig.factoryResource = entity.data.factoryResource;
                           GlobalConfig.factoryMainPage = entity.data.factoryMainPage;

                           GlobalConfig.departmentID_1 = entity.data.firstDepartment;
                           GlobalConfig.departmentID_2 = entity.data.secondDepartment;

//                           GlobalConfig.departmentID_1 = "";
//                           GlobalConfig.departmentID_2 = "";
//                           Log.e("machineId",GlobalConfig.machineId);
//                           Log.e("thirdFactory",GlobalConfig.thirdFactory);
                           // 设置显示对象数据
                           // 医院
                           ((TextView) getActivity().findViewById(R.id.jtjk_hospital_name)).setText(GlobalConfig.hospitalName);
                           // 机器编号
                           ((TextView) getActivity().findViewById(R.id.jtjk_machine_id)).setText("机器编号: "+GlobalConfig.machineId);

//                           findPatIdByPatientQuery(entity.data.hospitalNo);

                           // 必须跟在获取全局信息之后
                           // 启动大屏显示
                           new JTJKThirdAppUtil().backFromBodyTestingForce(getActivity());
                       }else {
                           ToastUtil.show(entity.message);
                       }
                   }catch (Exception e){
                       e.printStackTrace();
                   }
                }
            });
    }


    private void findPatIdByPatientQuery(String organId) {


        ApiRepository.getInstance().findPatIdByPatientQuery(organId)
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastLoadingObserver<FindUserResultEntity>("请稍后...") {
                    @Override
                    public void _onNext(@io.reactivex.annotations.NonNull FindUserResultEntity entity) {
                        if (entity == null) {
                            ToastUtil.show("请检查网络");
                            return;
                        }
                        try {
                            if (entity.isSuccess()){
                                String tag = (String) SPUtil.get(mContext,"tag","fzpy");
                                if (entity.getData()!=null){

                                }else {

                                }
                            }else {

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void _onError(Throwable e) {
                        super._onError(e);

                    }
                });


    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            // 返回到首页
            // 清理全局变量
            GlobalConfig.clear();
            // 清理 信令
            XLMessage.with().destroy();
            // 如果最后一次大屏的通信是启动身体检测，则回来不打开视频广告
            if (!GlobalConfig.lastDapinSocketStr.equals(DapinSocketProxy.FLAG_SCREENFLAG_BODYTESTING_OPEN)){
                new JTJKThirdAppUtil().onScreen(getActivity());
            }else {
                // 清理全局单例
                DapinSocketProxy.with().delayDestroy();
            }

            // 广告框启动
            timeLoop();

//            // 显示医院
//            if (jtjk_hospital!=null)
//                jtjk_hospital.setText(GlobalConfig.hospitalName);
//            // 显示机器
//            if (jtjk_machine!=null)
//                jtjk_machine.setText("机器编号:"+GlobalConfig.machineId);
        }else{
            if (mDisposable!=null){
                mDisposable.dispose();
                mDisposable = null;
            }
            if (dialog!=null){
                dialog.onDismiss();

            }
        }
    }
}
