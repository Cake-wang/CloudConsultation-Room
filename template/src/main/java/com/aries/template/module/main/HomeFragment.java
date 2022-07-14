package com.aries.template.module.main;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.util.SPUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.template.FakeDataExample;
import com.aries.template.GlobalConfig;
import com.aries.template.MainActivity;
import com.aries.template.R;
import com.aries.template.entity.BatchCreateOrderEntity;
import com.aries.template.entity.GetMedicalInfoEntity;
import com.aries.template.entity.GetStockInfoEntity;
import com.aries.template.entity.MachineEntity;
import com.aries.template.entity.PrescriptionPushEntity;
import com.aries.template.entity.RoomIdInsAuthEntity;
import com.aries.template.module.base.BaseEventFragment;
import com.aries.template.module.mine.MineFragment;
import com.aries.template.module.mine.PayCodeFragment;
import com.aries.template.module.mine.VideoConsultFragment;
import com.aries.template.retrofit.repository.ApiRepository;
import com.aries.template.xiaoyu.EaseModeProxy;
import com.aries.ui.view.title.TitleBarView;
import com.trello.rxlifecycle3.android.FragmentEvent;

import androidx.annotation.Nullable;
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
//            start(MineFragment.newInstance("stjc"));
//                start(DepartmentFragment.newInstance("stjc"));// todo cc
//                start(PutRecordFragment.newInstance("idcard","name","smkcard"));// todo cc
//                ((MainActivity) getActivity()).requestConsultsAndRecipes();//todo cc
//                start(ResultFragment.newInstance("cancelConsult"));//todo ccss
//                start(ConfirmRecipesFragment.newInstance(null));// todo cc
//                start(PayCodeFragment.newInstance(new Object()));// todo cc
//                start(VideoConsultFragment.newInstance(FakeDataExample.consultId,FakeDataExample.nickname,FakeDataExample.doctorUserId));// todo cc
                start(PayCodeFragment.newInstance(FakeDataExample.recipeFee,FakeDataExample.recipeIds,FakeDataExample.recipeCode));// todo cc
//            startActivity(new Intent(getActivity(), MeetingActivity.class));//todo cc
        });

        // 点击复诊
        iv_fzpy.setOnClickListener(v -> {
            SPUtil.put(mContext,"tag","fzpy");
            start(MineFragment.newInstance("fzpy"));
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 启动时，需要立刻请求，机器相关的数据，并保存在全局
        requestMachineInfo();
        //todo cc
        requestText();
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

    /**
     * 测试入口
     * todo cc
     */
    public void requestText(){
        /**
         * 查询复诊单的小鱼视频会议室房间号和密码
         */
        ApiRepository.getInstance().batchCreateOrder(FakeDataExample.recipeFee,FakeDataExample.recipeIds,FakeDataExample.recipeCode)
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastLoadingObserver<BatchCreateOrderEntity>("请稍后...") {
                    @Override
                    public void _onNext(BatchCreateOrderEntity entity) {
                        if (entity == null) {
                            ToastUtil.show("请检查网络");
                            return;
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
