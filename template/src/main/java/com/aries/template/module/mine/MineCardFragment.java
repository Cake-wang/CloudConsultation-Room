package com.aries.template.module.mine;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.aries.template.GlobalConfig;
import com.aries.template.MainActivity;
import com.aries.template.R;
import com.aries.template.module.base.BaseEventFragment;
import com.aries.ui.view.title.TitleBarView;

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
        Log.d("111111MODEL", "111111MODEL");
        handler = new Handler();
        handler.postDelayed(() -> {
            // 要执行的操作 启动卡片读取操作。
            if (getActivity() !=null)
                ((MainActivity)getActivity()).openSerialport();
        }, 500);//3秒后执行Runnable中的run方法
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            GlobalConfig.isFindUserDone = false;
            callIDMachine();
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
