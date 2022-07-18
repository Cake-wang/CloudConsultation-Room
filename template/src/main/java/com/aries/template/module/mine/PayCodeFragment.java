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
import com.aries.template.entity.GetConsultAndPatientAndDoctorByIdEntity;
import com.aries.template.entity.GetConsultsAndRecipesResultEntity;
import com.aries.template.entity.GetPatientRecipeByIdEntity;
import com.aries.template.entity.PayOrderEntity;
import com.aries.template.entity.PrescriptionPushEntity;
import com.aries.template.module.base.BaseEventFragment;
import com.aries.template.retrofit.repository.ApiRepository;
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

import androidx.annotation.Nullable;
import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 支付处方单
 * 仅处理处方单界面
 * 包括未支付的处方单和新处方单。
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
    private String recipeId;//处方单ID
    private String recipeFee;//药品费 当处方单产生订单，并且订单有效时取的是订单的真实金额，其他时候取的处方的总金额保留两位小数
    private ArrayList<String> recipeIds;//处方ID集合
    private ArrayList<String> recipeCode;//HIS处方编码集合，可以从处方详情中获取

    /** 传入处理处方单的数据 */
    private GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes obj;

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
     * @param recipeId 处方单ID
     * @param recipeFee 总费用
     * @param recipeIds 处方ID集合
     * @param recipeCode 处方编号集合
     * @param obj 处方单信息
     */
    public static PayCodeFragment newInstance(String recipeId ,String recipeFee, ArrayList<String> recipeIds, ArrayList<String> recipeCode,GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes obj) {
        PayCodeFragment fragment = new PayCodeFragment();
        Bundle args = new Bundle();
        args.putString("recipeId", recipeId);
        args.putString("recipeFee", recipeFee);
        args.putSerializable("recipeIds", recipeIds);
        args.putSerializable("recipeCode", recipeCode);
        args.putSerializable("obj", obj);
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
            recipeId = args.getString("recipeId");
            recipeFee = args.getString("recipeFee");
            recipeIds = ((ArrayList<String>) args.getSerializable("recipeIds"));
            recipeCode = ((ArrayList<String>) args.getSerializable("recipeCode"));
            obj = (GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes) args.getSerializable("obj");
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
        timeLoop();
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
                .subscribe(aLong -> requestPaySuccess());//getUnreadCount()执行的任务
    }

    /**
     * 检查当前支付是否完成
     * 循环任务，没3秒检查一次
     * 检查处方单详情
     */
    private void requestPaySuccess() {
        ApiRepository.getInstance().getPatientRecipeById(recipeId)
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastLoadingObserver<GetPatientRecipeByIdEntity>("请稍后...") {
                    @Override
                    public void _onNext(GetPatientRecipeByIdEntity entity) {
                        if (entity == null) {
                            ToastUtil.show("请检查网络");
                            return;
                        }
                        // 检查 payFlag 如果是 1 就是支付成功
                        if (entity.isSuccess()){
                            if (entity.getData().getJsonResponseBean().getBody().getRecipe().getPayFlag()==1){
                                // 4.1.4 处方药品推送接口
                                requestPrescriptionPush(GlobalConfig.cabinetId);
                            }
                        }
                    }
                });
    }


    /**
     * 推送处方单
     * 有库存，详细信息推送到服务端，准备进入支付阶段
     * 支付的基础是创建一个可以支付的处方单。里面有很多处方。
     */
    public void requestPrescriptionPush(String clinicSn){
        // 生成处方单药物集
        ArrayList<Map> drugs = new ArrayList<>();
        for (GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes.RecipeDetail item : obj.recipeDetailBeans) {
            Map<String, Object> drug= new HashMap<>();
            //            drugs.put("direction","口服");
            drug.put("dosageUnit",item.drugUnit);
            drug.put("drugCommonName",item.drugName);
            drug.put("drugTradeName",item.drugName);
            drug.put("eachDosage",item.defaultUseDose);
            drug.put("itemDays",item.dosageUnit);
            drug.put("price",item.drugCost);
            drug.put("quantity",item.sendNumber);
            drug.put("quantityUnit",item.drugUnit);
            drug.put("sku",item.organDrugCode);
            drug.put("spec",item.pack);
            drugs.add(drug);
        }
        ApiRepository.getInstance().prescriptionPush(clinicSn,
                        GlobalConfig.hospitalName,
                        GlobalConfig.ssCard.getSSNum(),
                        obj.patientSex,
                        obj.patientName,
                        obj.organDiseaseName,
                        String.valueOf(obj.recipeId),
                        String.valueOf(obj.totalMoney),
                        drugs)
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastLoadingObserver<PrescriptionPushEntity>("请稍后...") {
                    @Override
                    public void _onNext(PrescriptionPushEntity entity) {
                        if (entity == null) {
                            ToastUtil.show("请检查网络，返回首页后重试");
                            return;
                        }
                        if (entity.isSuccess()){
                            // todo 打印取药单
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
