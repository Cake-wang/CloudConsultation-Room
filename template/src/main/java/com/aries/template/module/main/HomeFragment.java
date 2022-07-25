package com.aries.template.module.main;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.retrofit.FastObserver;
import com.aries.library.fast.util.SPUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.template.GlobalConfig;
import com.aries.template.MainActivity;
import com.aries.template.R;
import com.aries.template.entity.ConfigurationToThirdForPatientEntity;
import com.aries.template.entity.MachineEntity;
import com.aries.template.entity.PatientFinishGraphicTextConsultEntity;
import com.aries.template.module.base.BaseEventFragment;
import com.aries.template.module.mine.MineCardFragment;
import com.aries.template.module.mine.ResultFragment;
import com.aries.template.retrofit.repository.ApiRepository;
import com.aries.template.xiaoyu.EaseModeProxy;
import com.aries.ui.view.title.TitleBarView;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.trello.rxlifecycle3.android.FragmentEvent;

import androidx.annotation.Nullable;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 首页
 * 返回首页后，需要清空一些全局数据
 *
 * @Author: AriesHoo on 2018/8/10 12:22
 * @E-Mail: AriesHoo@126.com
 * Function: 主页演示
 * Description:
 */
public class HomeFragment extends BaseEventFragment{

    @BindView(R.id.iv_stjc)
    ImageView iv_stjc;//身体检查
    @BindView(R.id.iv_fzpy)
    ImageView iv_fzpy;//复诊配药
    @BindView(R.id.jtjk_machine_id)
    TextView jtjk_machine;//机器编号
    @BindView(R.id.jtjk_hospital_name)
    TextView jtjk_hospital;//医院名称

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
        return R.layout.fragment_home;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        // 不显示倒计时，不进行倒计时计算
        dismissCountTimeStop();
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
//                start(VideoConsultFragment.newInstance(FakeDataExample.consultId,FakeDataExample.nickname,FakeDataExample.doctorUserId));// todo cc
//                start(PayCodeFragment.newInstance(FakeDataExample.recipeFee,FakeDataExample.recipeIds,FakeDataExample.recipeCode));// todo cc
//            startActivity(new Intent(getActivity(), MeetingActivity.class));//todo cc
        });

        // 点击复诊
        iv_fzpy.setOnClickListener(v -> {
            SPUtil.put(mContext,"tag","fzpy");
            start(MineCardFragment.newInstance("fzpy"));
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 启动时，需要立刻请求，机器相关的数据，并保存在全局
        requestMachineInfo();
        // 启动环信视频
        requestConfigurationToThirdForPatient();
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
                        if (entity.getData().isSuccess()){
                            loginEmClient(entity.getData().getJsonResponseBean().getBody().getUsername(),entity.getData().getJsonResponseBean().getBody().getUserpwd());
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
                ToastUtil.show("环信登录成功");
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
        EMClient.getInstance().login(easemobUserName, easemobPassword,emcallback );
    }

    /**
     * 通过机器编号，获得全局的数据
     * 并保存在全局
     */
    /**
     * 通过机器编号，获得全局的数据
     * 并保存在全局
     */
    public void requestMachineInfo(){
        String deviceId = ApiRepository.getDeviceId();
        ApiRepository.getInstance().findByMachineId(deviceId)
            .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
            .subscribe(new FastLoadingObserver<MachineEntity>("请稍后...") {
                @Override
                public void _onNext(@io.reactivex.annotations.NonNull MachineEntity entity) {
                    if (entity == null) {
                        ToastUtil.show("请检查网络，返回首页后重试");
                        return;
                    }
                    if (entity.success){
                        GlobalConfig.machineId = entity.data.machineId;
                        GlobalConfig.cabinetId = entity.data.cabinetId;
                        GlobalConfig.hospitalName = entity.data.hospitalName;
//                             GlobalConfig.machineId = entity.data.machineStatus;// 暂定
                        GlobalConfig.organId = Integer.valueOf(entity.data.hospitalNo);
                        GlobalConfig.machineIp = entity.data.machineIp;
                        if (jtjk_hospital!=null)
                            jtjk_hospital.setText(GlobalConfig.hospitalName);
                        if (jtjk_machine!=null)
                            jtjk_machine.setText("机器编号:"+GlobalConfig.machineId);
                    }else {
                        ToastUtil.show(entity.message);
                    }
                }
            });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            GlobalConfig.clear();
        }
    }
}
