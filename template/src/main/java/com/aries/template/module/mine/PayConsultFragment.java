package com.aries.template.module.mine;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.retrofit.FastObserver;
import com.aries.library.fast.util.ToastUtil;
import com.aries.template.GlobalConfig;
import com.aries.template.R;
import com.aries.template.entity.BatchCreateOrderEntity;
import com.aries.template.entity.GetConsultAndPatientAndDoctorByIdEntity;
import com.aries.template.entity.GetConsultsAndRecipesResultEntity;
import com.aries.template.entity.GetPatientRecipeByIdEntity;
import com.aries.template.entity.PayOrderEntity;
import com.aries.template.entity.PrescriptionPushEntity;
import com.aries.template.module.base.BaseEventFragment;
import com.aries.template.retrofit.repository.ApiRepository;
import com.aries.template.utils.ActivityUtils;
import com.aries.ui.view.title.TitleBarView;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.xuexiang.xaop.annotation.IOThread;
import com.xuexiang.xaop.annotation.MainThread;
import com.xuexiang.xaop.enums.ThreadType;
import com.xuexiang.xqrcode.XQRCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 支付复诊
 * 仅处理复诊的支付挂号费界面
 * @author louisluo
 * @Author: AriesHoo on 2018/7/13 17:09
 * @E-Mail: AriesHoo@126.com
 * @Function: 我的
 */
public class PayConsultFragment extends BaseEventFragment {
    /**
     * 输入显示对象
     */
    @Override
    public int getContentLayout() {
        return R.layout.fragment_pay;
    }

    /** 从外部传入的数据  */
    private String consultId;//处方单ID
    private String patientName;//病人姓名
    private String doctorId;//医生ID
    private String doctorName;

    @BindView(R.id.tv_name)
    TextView tv_name; //患者姓名
    @BindView(R.id.tv_fee_type)
    TextView tv_fee_type; //费用类型
    @BindView(R.id.tv_fee_all)
    TextView tv_fee_all; //费用总额
    @BindView(R.id.tv_fee_yb)
    TextView tv_fee_yb; //医保支付费用显示
    @BindView(R.id.tv_fee_zf)
    TextView tv_fee_zf; //自费支付费用显示
    @BindView(R.id.iv_qrcode)
    ImageView mIvQrcode;// 二维码
    @BindView(R.id.jtjk_pay_text)
    TextView jtjk_pay_text;
    @BindView(R.id.jtjk_pay_reflash_tip)
    TextView jtjk_pay_reflash_tip;

    /**
     * 跳转科室，需要带的数据
     * @param consultId 复诊单ID
     * @param patientName 病人姓名
     * @param doctorId 医生ID
     * @param doctorName 医生姓名
     */
    public static PayConsultFragment newInstance(String consultId , String patientName, String doctorId, String doctorName) {
        PayConsultFragment fragment = new PayConsultFragment();
        Bundle args = new Bundle();
        args.putString("consultId", consultId);
        args.putString("patientName", patientName);
        args.putSerializable("doctorId", doctorId);
        args.putSerializable("doctorName", doctorName);
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

        // 注入数据
        Bundle args = getArguments();
        if (args != null) {
            consultId = args.getString("consultId");
            patientName = args.getString("patientName");
            doctorId = args.getString("doctorId");
            doctorName = args.getString("doctorName");
        }
    }

    /**
     * 页面的动作，包括数据传输和界面改变
     */
    @Override
    public void initView(Bundle savedInstanceState) {
        // 轮训复诊单
        timeLoop();
        // 获得支付二维码
        requestPayOrder(consultId);

        //数据展示
        if (GlobalConfig.ssCard!=null)
            tv_name.setText(GlobalConfig.ssCard.getName());
//        tv_fee_all.setText(recipeFee);

        String[] orders = {"#38ABA0","支付宝·","#333333","扫一扫"};
        jtjk_pay_text.setText(ActivityUtils.formatTextView(orders));

        jtjk_pay_reflash_tip.setOnClickListener(v -> {
            requestPayOrder(consultId);
        });
    }

    private static final int PERIOD = 5* 1000;
    private static final int DELAY = 100;
    private Disposable mDisposable;

    /**
     * 定时循环任务
     */
    private void timeLoop() {
        mDisposable = Observable.interval(DELAY, PERIOD, TimeUnit.MILLISECONDS)
                .map((aLong -> aLong + 1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> requestPaySuccess());//getUnreadCount()执行的任务
    }

    /**
     * 检查当前支付是否完成
     * 循环任务，每5秒检查一次
     * 检查处方单详情
     * FastObserver
     */
    private void requestPaySuccess() {
        ApiRepository.getInstance().getConsultAndPatientAndDoctorById(consultId)
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastObserver<GetConsultAndPatientAndDoctorByIdEntity>() {
                    @Override
                    public void _onNext(GetConsultAndPatientAndDoctorByIdEntity entity) {
                        if (entity == null) {
                            ToastUtil.show("请检查网络");
                            return;
                        }
                        // 检查 payFlag 如果是 1 就是支付成功
                        if (entity.isSuccess()){
                            if (entity.getData().getJsonResponseBean().getBody().getConsult().getPayflag()==1){
                                // 跳转到视频
                                start(VideoConsultFragment.newInstance(consultId,
                                        patientName,
                                        doctorId,
                                        doctorName,
                                        false));
                                // 释放对象资源
                                onDismiss();
                            }
                        }
                    }
                });
    }

    @Override
    public void loadData() {
    }

    /**
     * 生成简单的带logo的二维码
     * @param logo
     */
    @IOThread(ThreadType.Single)
    private void createQRCodeWithLogo(Bitmap logo) {
//        showQRCode(XQRCode.createQRCodeWithLogo("00000000", 400, 400, logo));
//        isQRCodeCreated = true;
    }

    @MainThread
    private void showQRCode(Bitmap QRCode) {
        mIvQrcode.setImageBitmap(QRCode);
    }

    /**
     * 获得订单二维码和其他详细数据
     * @param busId 订单号 直接使用 复诊id 815423957
     */
    private void requestPayOrder(String busId){
        ApiRepository.getInstance().payOrder(busId,"onlinerecipe")
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastLoadingObserver<PayOrderEntity>("请稍后...") {
                    @Override
                    public void _onNext(PayOrderEntity entity) {
                        if (entity == null) {
                            ToastUtil.show("请检查网络");
                            return;
                        }
                        if (entity.getData().isSuccess()){
                            // 显示二维码
                           String qrStr = entity.getData().getJsonResponseBean().getBody().qr_code;
                           Resources res = getActivity().getResources();
                           Bitmap bmp= BitmapFactory.decodeResource(res, R.drawable.pay_alilogo);
                           showQRCode(XQRCode.createQRCodeWithLogo(qrStr, 400, 400, bmp));
                            jtjk_pay_reflash_tip.setVisibility(View.GONE);
                        }else {
                            jtjk_pay_reflash_tip.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }


    /**
     * 设置title的信息
     */
    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setBgColor(Color.WHITE)
                .setTitleMainText(R.string.mine);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden){
            //清除mDisposable不再进行验证
            if (mDisposable != null) {
                mDisposable.dispose();
                mDisposable = null;
            }
        }
    }

    @Override
    public void onDismiss() {
        super.onDismiss();
        //清除mDisposable不再进行验证
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }
}
