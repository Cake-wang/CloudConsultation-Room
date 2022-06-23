package com.aries.template.module.mine;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.util.SPUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.template.GlobalConfig;
import com.aries.template.R;
import com.aries.template.entity.FindUserResultEntity;
import com.aries.template.module.base.BaseEventFragment;
import com.aries.template.retrofit.repository.ApiRepository;
import com.aries.ui.view.title.TitleBarView;
import com.decard.NDKMethod.BasicOper;
import com.decard.NDKMethod.EGovernment;
import com.decard.entitys.SSCard;
import com.trello.rxlifecycle3.android.FragmentEvent;

import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 医保卡
 * 获得医保卡的信息，不过这些动作现在都在MainActivity 里面了
 * @author louisluo
 * @Author: AriesHoo on 2018/7/13 17:09
 * @E-Mail: AriesHoo@126.com
 * @Function: 我的
 * @Description:
 */
public class MineFragment extends BaseEventFragment{
    private  String tag = "";

    public static MineFragment newInstance(String tag) {
        Bundle args = new Bundle();
        MineFragment fragment = new MineFragment();
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
    public void onStart() {
        super.onStart();
        // 每次进入读卡界面，会重置请求用户数据，检测是否已经注册
        GlobalConfig.isFindUserDone = false;
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

}
