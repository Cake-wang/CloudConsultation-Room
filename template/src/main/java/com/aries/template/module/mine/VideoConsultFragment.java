package com.aries.template.module.mine;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.util.SPUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.template.FakeDataExample;
import com.aries.template.R;
import com.aries.template.entity.CancelregisterResultEntity;
import com.aries.template.module.base.BaseEventFragment;
import com.aries.template.retrofit.repository.ApiRepository;
import com.aries.template.view.ShineButtonDialog;
import com.aries.template.xiaoyu.EaseModeProxy;
import com.aries.template.xiaoyu.meeting.MeetingVideoCell;
import com.aries.ui.view.title.TitleBarView;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.xuexiang.xaop.annotation.SingleClick;

import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 科室展示页面
 * 用于显示一级部门，二级部门
 * @author louisluo
 * @Author: AriesHoo on 2018/7/13 17:09
 * @E-Mail: AriesHoo@126.com
 * @Function: 我的
 */
public class VideoConsultFragment extends BaseEventFragment {

    /**
     * 输入显示对象
     */
    @Override
    public int getContentLayout() {
//        EaseModeProxy.with().setActivity(getActivity());
//        EaseModeProxy.with().xyInit(true);
        return R.layout.fragment_video;
    }

    /** 从外部传入的数据  */
    private  Object inputObj;
    @BindView(R.id.btn_stjc)
    Button btn_stjc;// 上一页按钮
    @BindView(R.id.btn_finish)
    Button btn_finish;// 下一页按钮

    /**
     * 跳转科室，需要带的数据
     */
    public static VideoConsultFragment newInstance(Object inputObj) {
        VideoConsultFragment fragment = new VideoConsultFragment();
        return fragment;
    }

    /**
     * 构造函数
     * @param savedInstanceState 输入进来的数据
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        timeCount = 1500;
        ViewGroup viewGroup = getActivity().findViewById(R.id.videoContent);
        EaseModeProxy.with().init(getActivity(),viewGroup).easemobStart(getActivity(),viewGroup,FakeDataExample.easemobUserName,FakeDataExample.password);
        EaseModeProxy.with().init(getActivity(),viewGroup).xyInit();
    }

    /**
     * 页面的动作，包括数据传输和界面改变
     */
    @Override
    public void initView(Bundle savedInstanceState) {
    }


    @SingleClick
    @OnClick({R.id.btn_stjc, R.id.btn_finish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_stjc:
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
            default:
                break;
        }
    }


    private void showSimpleConfirmDialog() {
        ShineButtonDialog dialog = new ShineButtonDialog(this.mContext);
        dialog.tv_title_tip.setText("结束问诊");
        dialog.tv_content_tip.setText("是否结束问诊");
        dialog.btn_inquiry.setOnClickListener(v -> {
            dialog.dismiss();
                // 在栈内的HomeFragment以SingleTask模式启动（即在其之上的Fragment会出栈）
            patientFinishGraphicTextConsult(0);
        });
        dialog.btn_cancel.setOnClickListener(v -> dialog.dismiss());
        dialog.iv_close.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void patientFinishGraphicTextConsult(Integer consultId) {
        ApiRepository.getInstance().patientFinishGraphicTextConsult(consultId)
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
                                start(OrderFragment.newInstance(null));
                            }
                        }
                    }
                });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden)
            // 关闭，并释放所有资源
            EaseModeProxy.with().closeProxy();
    }

    /**
     * 设置title的信息
     */
    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setBgColor(Color.WHITE)
                .setTitleMainText(R.string.mine);
    }
}
