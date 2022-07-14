package com.aries.template.module.mine;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.util.ToastUtil;
import com.aries.template.FakeDataExample;
import com.aries.template.GlobalConfig;
import com.aries.template.R;
import com.aries.template.entity.BatchCreateOrderEntity;
import com.aries.template.entity.GetConsultsAndRecipesResultEntity;
import com.aries.template.entity.PayOrderEntity;
import com.aries.template.entity.RequestConsultAndCdrOtherdocResultEntity;
import com.aries.template.module.base.BaseEventFragment;
import com.aries.template.retrofit.repository.ApiRepository;
import com.aries.ui.view.title.TitleBarView;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.xuexiang.xaop.annotation.IOThread;
import com.xuexiang.xaop.annotation.MainThread;
import com.xuexiang.xaop.enums.ThreadType;
import com.xuexiang.xqrcode.XQRCode;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
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
    private String recipeFee;
    private ArrayList<String> recipeIds;
    private ArrayList<String> recipeCode;

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

    /**
     * 跳转科室，需要带的数据
     */
    public static PayCodeFragment newInstance(String recipeFee, ArrayList<String> recipeIds, ArrayList<String> recipeCode) {
        PayCodeFragment fragment = new PayCodeFragment();
        Bundle args = new Bundle();
        args.putString("recipeFee", recipeFee);
        args.putSerializable("recipeIds", recipeIds);
        args.putSerializable("recipeCode", recipeCode);
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
            recipeFee = args.getString("recipeFee");
            recipeIds = ((ArrayList<String>) args.getSerializable("recipeIds"));
            recipeCode = ((ArrayList<String>) args.getSerializable("recipeCode"));
        }

        //数据展示
//        if (GlobalConfig.ssCard!=null)
//        tv_name.setText(GlobalConfig.ssCard.getName());
//        tv_fee_all.setText(recipeFee);
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

        requestBatchCreateOrder(recipeFee,recipeIds,recipeCode);
//        requestPayOrder(String.valueOf(74521));
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

    /**
     * 检查当前支付是否完成
     * 循环任务，没3秒检查一次
     */
    private void paySuccess() {
//        ApiRepository.getInstance().paySuccess("","","",mContext)
//                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
//                .subscribe(
//                        new FastLoadingObserver<RequestConsultAndCdrOtherdocResultEntity>("请稍后...") {
//                            @Override
//                            public void _onNext(@NonNull RequestConsultAndCdrOtherdocResultEntity entity) {
//                                if (entity == null) {
//                                    ToastUtil.show("请检查网络");
//                                    return;
//                                }
//                                if (entity.isSuccess()){
//                                    if (entity.getData().isSuccess()){
//                                        //跳视频问诊
////                                        start(VideoConsultFragment.newInstance(new Object()));
//                                    }
//                                }else {
//                                    ToastUtil.show(entity.getRespDesc());
//                                }
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                ToastUtil.show("请检查网络和ip地址");
//                                if (true) {
//                                    super.onError(e);
//                                }
//                            }
//                        });
    }

    @Override
    public void loadData() {
        //  发起复诊
//        ApiRepository.getInstance().presettlement("","","",mContext)
//                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
//                .subscribe(true ?
//                        new FastLoadingObserver<RequestConsultAndCdrOtherdocResultEntity>("请稍后...") {
//                            @Override
//                            public void _onNext(@NonNull RequestConsultAndCdrOtherdocResultEntity entity) {
//                                if (entity == null) {
//                                    ToastUtil.show("请检查网络");
//                                    return;
//                                }
////                                checkVersion(entity);
//                                if (entity.isSuccess()){
//
//                                    if (entity.getData().isSuccess()){
//
//                                      tv_name.setText(""); //时间计时器显示对象
//                                      tv_fee_type.setText(""); //时间计时器显示对象
//                                      tv_fee_all.setText(""); //时间计时器显示对象
//                                      tv_fee_yb.setText(""); //时间计时器显示对象
//                                      tv_fee_zf.setText("");
//
//
//                                        BitmapFactory.Options bfoOptions =new BitmapFactory.Options();
//
//                                        bfoOptions.inScaled =false;
//
//                                        Bitmap img1 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher, bfoOptions);
//
//                                        createQRCodeWithLogo(img1);
//
//                                        timeLoop();
//
//                                    }
//
//
//                                }else {
//
////                                    ToastUtil.show(entity.getRespDesc());
//                                }
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//
////                                ToastUtil.show("请检查网络和ip地址");
//                                if (true) {
//                                    super.onError(e);
//                                }
//                            }
//                        } :
//                        new FastObserver<RequestConsultAndCdrOtherdocResultEntity>() {
//                            @Override
//                            public void _onNext(@NonNull RequestConsultAndCdrOtherdocResultEntity entity) {
//                                if (entity == null) {
//                                    ToastUtil.show("请检查网络");
//                                    return;
//                                }
//
//
//
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                if (false) {
//                                    super.onError(e);
//                                }
//                            }
//                        });
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
     * 处方合并生成订单接口
     * 这个接口的返回是3.12.	支付请求接口的输入
     * todo 返回的数据没有通
     */
    private void requestBatchCreateOrder(String recipeFee, ArrayList<String> recipeIds, ArrayList<String> recipeCode){
        ApiRepository.getInstance().batchCreateOrder(recipeFee, recipeIds, recipeCode)
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastLoadingObserver<BatchCreateOrderEntity>("请稍后...") {
                    @Override
                    public void _onNext(BatchCreateOrderEntity entity) {
                        if (entity == null) {
                            ToastUtil.show("请检查网络");
                            return;
                        }
                        if (entity.isSuccess()){
                            String busId = String.valueOf(entity.getData().getJsonResponseBean().getBody());
                            if (!busId.equals("0"))
                                requestPayOrder(busId);
                        }
                    }
                });
    }

    /**
     * 获得订单二维码和其他详细数据
     * @param busId 订单号
     */
    private void requestPayOrder(String busId){
        ApiRepository.getInstance().payOrder(busId)
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastLoadingObserver<PayOrderEntity>("请稍后...") {
                    @Override
                    public void _onNext(PayOrderEntity entity) {
                        if (entity == null) {
                            ToastUtil.show("请检查网络");
                            return;
                        }
                        if (entity.isSuccess()){
                            // 显示二维码
                           String qrStr = entity.getData().getJsonResponseBean().getBody().qr_code;
                           showQRCode(XQRCode.createQRCodeWithLogo(qrStr, 400, 400, null));
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
}
