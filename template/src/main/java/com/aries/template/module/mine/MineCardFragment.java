package com.aries.template.module.mine;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.util.ToastUtil;
import com.aries.template.GlobalConfig;
import com.aries.template.MainActivity;
import com.aries.template.R;
import com.aries.template.entity.SbkcardResultEntity;
import com.aries.template.module.base.BaseEventFragment;
import com.aries.template.retrofit.repository.ApiRepository;
import com.aries.template.thridapp.JTJKSSDCard;
import com.aries.ui.view.title.TitleBarView;
import com.trello.rxlifecycle3.android.FragmentEvent;

import androidx.annotation.Nullable;


/**
 * 医保卡
 * 获得医保卡的信息，不过这些动作现在都在MainActivity 里面了
 * @author louisluo
 * @Author: AriesHoo on 2018/7/13 17:09
 * @E-Mail: AriesHoo@126.com
 * @Function: 我的
 * @Description:
 */
public class MineCardFragment extends BaseEventFragment{

    private  String tag = "";
    private Handler handler;

    public static MineCardFragment newInstance(String tag) {
        Bundle args = new Bundle();
        MineCardFragment fragment = new MineCardFragment();
        args.putString("tag",tag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            tag = args.getString("tag");
        }
    }

    @Override
    public void loadData() {
        // 启动社保
        callIDMachine();
    }

    /**
     * 启动社保卡
     */
    private void callIDMachine() {
//        if (getActivity() !=null)
//        ((MainActivity)getActivity()).setSSDCardData(FakeDataExample.fakeSSCard()); //todo cc

//       Log.d("111111MODEL", "111111MODEL");



        if (GlobalConfig.thirdFactory.equals("3")){

            ApiRepository.getInstance().sbkcard()
                    .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                    .subscribe(new FastLoadingObserver<SbkcardResultEntity>() {
                        @Override
                        public void _onNext(SbkcardResultEntity entity) {
                            if (entity == null) {
                                ToastUtil.show("请检查网络");
                                return;
                            }
                            try {
                                if (entity.success){

                                    JTJKSSDCard ssCard = null;
                                    try {
                                        ssCard = JTJKSSDCard.buildsbkcard(entity.data);
                                        //                SSCard ssCard = EGovernment.EgAPP_SI_ReadSSCardInfo();
                                    } catch (Exception e) {
                                        ToastUtil.show("未查询到卡信息");
                                        e.printStackTrace();
                                    }

                                    // 注入数据
                                    if(ssCard!=null) {

                                        // 输入社保数据
                                        ((MainActivity)getActivity()).setSSDCardData(ssCard);


                                    }else {
                                        ToastUtil.show("未查询到卡信息");
                                    }

                                }else {
                                    ToastUtil.show(entity.getMessage());
                                }
                            }catch (Exception e){
                                ToastUtil.show("卡信息异常");
                                e.printStackTrace();
                            }
                        }
                    });

        }else {
            handler = new Handler();
            handler.postDelayed(() -> {
                // 要执行的操作 启动卡片读取操作。
                if (getActivity() !=null)
                    ((MainActivity)getActivity()).openSerialport();
            }, 500);//3秒后执行Runnable中的run方法
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            if ((MainActivity)getActivity()!=null){
                if (((MainActivity)getActivity()).getTopFragment() instanceof MineCardFragment){
                    GlobalConfig.isFindUserDone = false;
                    callIDMachine();
                }
            }


        }else{
            if (handler!=null){
                handler.removeCallbacksAndMessages(null);
                handler = null;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // 每次进入读卡界面，会重置请求用户数据，检测是否已经注册
        GlobalConfig.isFindUserDone = false;
//        callIDMachine();
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setBgColor(Color.WHITE)
                .setTitleMainText(R.string.mine);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
