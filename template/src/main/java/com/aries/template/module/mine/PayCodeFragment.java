package com.aries.template.module.mine;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.retrofit.FastObserver;
import com.aries.library.fast.util.ToastUtil;
import com.aries.template.R;
import com.aries.template.entity.RequestConsultAndCdrOtherdocResultEntity;
import com.aries.template.module.base.BaseEventFragment;
import com.aries.template.retrofit.repository.ApiRepository;
import com.aries.ui.view.title.TitleBarView;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.xuexiang.xaop.annotation.IOThread;
import com.xuexiang.xaop.annotation.MainThread;
import com.xuexiang.xaop.enums.ThreadType;
import com.xuexiang.xqrcode.XQRCode;

import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 科室展示页面
 * 用于显示一级部门，二级部门
 * @author louisluo
 * @Author: AriesHoo on 2018/7/13 17:09
 * @E-Mail: AriesHoo@126.com
 * @Function: 我的
 */
public class PayCodeFragment extends BaseEventFragment {
    /**
     * 输入显示对象
     */
    @Override
    public int getContentLayout() {
        return R.layout.fragment_pay;
    }

    /** 从外部传入的数据  */
    private  Object inputObj;
    /** 120  秒倒计时间 */
    private int timeCount = 120;

    @BindView(R.id.jtjk_fz_fragment_timer)
    TextView timerTV; //时间计时器显示对象
    @BindView(R.id.btn_cancel)
    Button btn_cancel;// 上一页按钮
    @BindView(R.id.btn_inquiry)
    Button btn_inquiry;// 下一页按钮

    @BindView(R.id.tv_name)
    TextView tv_name; //时间计时器显示对象
    @BindView(R.id.tv_fee_type)
    TextView tv_fee_type; //时间计时器显示对象
    @BindView(R.id.tv_fee_all)
    TextView tv_fee_all; //时间计时器显示对象
    @BindView(R.id.tv_fee_yb)
    TextView tv_fee_yb; //时间计时器显示对象
    @BindView(R.id.tv_fee_zf)
    TextView tv_fee_zf; //时间计时器显示对象

    @BindView(R.id.iv_qrcode)
    ImageView mIvQrcode;
    /**
     * 跳转科室，需要带的数据
     */
    public static PayCodeFragment newInstance(Object inputObj) {
        PayCodeFragment fragment = new PayCodeFragment();
        return fragment;
    }

    /**
     * 构造函数
     * @param savedInstanceState 输入进来的数据
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 启动计时器
        timeStart();
    }

    /**
     * 页面的动作，包括数据传输和界面改变
     */
    @Override
    public void initView(Bundle savedInstanceState) {
//        int organid = 1;//浙大附属邵逸夫医院
//        ApiRepository.getInstance().findValidOrganProfessionForRevisit(organid, getContext())
//                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
//                .subscribe(new FastLoadingObserver<FindValidOrganProfessionForRevisitResultEntity>() {
//                    @Override
//                    public void _onNext(FindValidOrganProfessionForRevisitResultEntity entity) {
//                        if (entity == null) {
//                            ToastUtil.show("请检查网络");
//                            return;
//                        }
////                        entity.data.requestId;
//                    }
//                });
    }

    private static final int PERIOD = 3* 1000;
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
                .subscribe(aLong -> paySuccess());//getUnreadCount()执行的任务

    }

    private void paySuccess() {


        ApiRepository.getInstance().paySuccess("","","",mContext)
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(true ?
                        new FastLoadingObserver<RequestConsultAndCdrOtherdocResultEntity>("请稍后...") {
                            @Override
                            public void _onNext(@NonNull RequestConsultAndCdrOtherdocResultEntity entity) {
                                if (entity == null) {
                                    ToastUtil.show("请检查网络");
                                    return;
                                }
//                                checkVersion(entity);
                                if (entity.isSuccess()){

                                    if (entity.getData().isSuccess()){

                                        //跳视频问诊
                                        start(VideoConsultFragment.newInstance(new Object()));

                                    }


                                }else {

//                                    ToastUtil.show(entity.getRespDesc());
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

//                                ToastUtil.show("请检查网络和ip地址");
                                if (true) {
                                    super.onError(e);
                                }
                            }
                        } :
                        new FastObserver<RequestConsultAndCdrOtherdocResultEntity>() {
                            @Override
                            public void _onNext(@NonNull RequestConsultAndCdrOtherdocResultEntity entity) {
                                if (entity == null) {
                                    ToastUtil.show("请检查网络");
                                    return;
                                }



                            }

                            @Override
                            public void onError(Throwable e) {
                                if (false) {
                                    super.onError(e);
                                }
                            }
                        });


    }

    @Override
    public void loadData() {
        ApiRepository.getInstance().presettlement("","","",mContext)
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(true ?
                        new FastLoadingObserver<RequestConsultAndCdrOtherdocResultEntity>("请稍后...") {
                            @Override
                            public void _onNext(@NonNull RequestConsultAndCdrOtherdocResultEntity entity) {
                                if (entity == null) {
                                    ToastUtil.show("请检查网络");
                                    return;
                                }
//                                checkVersion(entity);
                                if (entity.isSuccess()){

                                    if (entity.getData().isSuccess()){

                                      tv_name.setText(""); //时间计时器显示对象
                                      tv_fee_type.setText(""); //时间计时器显示对象
                                      tv_fee_all.setText(""); //时间计时器显示对象
                                      tv_fee_yb.setText(""); //时间计时器显示对象
                                      tv_fee_zf.setText("");


                                        BitmapFactory.Options bfoOptions =new BitmapFactory.Options();

                                        bfoOptions.inScaled =false;

                                        Bitmap img1 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher, bfoOptions);

                                        createQRCodeWithLogo(img1);

                                        timeLoop();

                                    }


                                }else {

//                                    ToastUtil.show(entity.getRespDesc());
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

//                                ToastUtil.show("请检查网络和ip地址");
                                if (true) {
                                    super.onError(e);
                                }
                            }
                        } :
                        new FastObserver<RequestConsultAndCdrOtherdocResultEntity>() {
                            @Override
                            public void _onNext(@NonNull RequestConsultAndCdrOtherdocResultEntity entity) {
                                if (entity == null) {
                                    ToastUtil.show("请检查网络");
                                    return;
                                }



                            }

                            @Override
                            public void onError(Throwable e) {
                                if (false) {
                                    super.onError(e);
                                }
                            }
                        });
    }

    /**
     * 生成简单的带logo的二维码
     * @param logo
     */
    @IOThread(ThreadType.Single)
    private void createQRCodeWithLogo(Bitmap logo) {
        showQRCode(XQRCode.createQRCodeWithLogo("00000000", 400, 400, logo));
//        isQRCodeCreated = true;
    }

    @MainThread
    private void showQRCode(Bitmap QRCode) {
        mIvQrcode.setImageBitmap(QRCode);
    }

    /**
     * 计时器任务处理
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void timeProcess() {
        super.timeProcess();
        timerTV.setText(--timeCount+"秒");
        if (timeCount==0){
            gotoMain();
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
}
