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

import com.alibaba.fastjson.JSON;
import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.retrofit.FastObserver;
import com.aries.library.fast.util.ToastUtil;
import com.aries.template.GlobalConfig;
import com.aries.template.R;
import com.aries.template.entity.BatchCreateOrderEntity;
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
public class PayRecipeFragment extends BaseEventFragment {
    /**
     * 输入显示对象
     */
    @Override
    public int getContentLayout() {
        return R.layout.fragment_pay;
    }

    /** 从外部传入的数据  */
    private String recipeId;//处方单ID
    private String patientSex;//性别
    private String patientName;//姓名
    private String organDiseaseName;//疾病名称
    private String busId;//合并订单号，如果有，则刷新二维码，如果没有，就再次尝试合并订单
    private String recipeFee="0.01";//药品费 当处方单产生订单，并且订单有效时取的是订单的真实金额，其他时候取的处方的总金额保留两位小数,总费用用接口获取
    private ArrayList<String> recipes;//处方ID集合
    private ArrayList<String> recipeCodes;//HIS处方编码集合，可以从处方详情中获取

    /** 传入处理处方单的数据 */
    private  ArrayList<DrugObject> obj;

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
     * @param recipeId 处方单ID
     * @param recipes 处方ID集合
     * @param recipeCodes 处方编号集合
     * @param obj 处方单信息
     */
    public static PayRecipeFragment newInstance(String recipeId, ArrayList<String> recipes, ArrayList<String> recipeCodes, ArrayList<DrugObject> obj) {
        PayRecipeFragment fragment = new PayRecipeFragment();
        Bundle args = new Bundle();
        args.putString("recipeId", recipeId);
        args.putSerializable("recipes", recipes);
        args.putSerializable("recipeCodes", recipeCodes);
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
            recipes = ((ArrayList<String>) args.getSerializable("recipes"));
            recipeCodes = ((ArrayList<String>) args.getSerializable("recipeCodes"));
            obj = (ArrayList<DrugObject>) args.getSerializable("obj");
        }
    }

    /**
     * 页面的动作，包括数据传输和界面改变
     */
    @Override
    public void initView(Bundle savedInstanceState) {
        requestBatchCreateOrder(recipeFee, recipes, recipeCodes);
        timeLoop();

        //数据展示
        if (GlobalConfig.ssCard!=null)
            tv_name.setText(GlobalConfig.ssCard.getName());
//        tv_fee_all.setText(recipeFee);
        // 支付提示
        String[] orders = {"#38ABA0","支付宝·","#333333","扫一扫"};
        jtjk_pay_text.setText(ActivityUtils.formatTextView(orders));
        // 支付类型
        tv_fee_type.setText("处方费");
        // 支付二维码刷新
        jtjk_pay_reflash_tip.setOnClickListener(v -> {
            if (TextUtils.isEmpty(busId))
                requestBatchCreateOrder(recipeFee, recipes, recipeCodes);
            else
                requestPayOrder(busId);
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
     */
    private void requestPaySuccess() {
        ApiRepository.getInstance().getPatientRecipeById(recipeId)
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastObserver<GetPatientRecipeByIdEntity>() {
                    @Override
                    public void _onNext(GetPatientRecipeByIdEntity entity) {
                        if (entity == null) {
                            ToastUtil.show("请检查网络");
                            return;
                        }
                        // 检查 payFlag 如果是 3 就是支付成功
                        if (entity.getData().isSuccess()){
                            if (entity.getData().getJsonResponseBean().getBody()!=null &&
                                    entity.getData().getJsonResponseBean().getBody().getRecipe().getPayFlag()==1){
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
        // 生成处方单药物集，遍历生成数据，准备输入
        ArrayList<Map> drugs = new ArrayList<>();
        for (DrugObject item : obj) {
            // 药品发放数量必须是整型
            item.sku = "6901339924484";//todo cc
            int quantityInt = (Double.valueOf(item.quantity)).intValue();
            // 药品发放数量不小于1
            if (quantityInt<=0)quantityInt=1;
            // 价格必须是整型，单位是分
            int priceInt = ((Double)(Double.valueOf(item.price)*100)).intValue();
            // 组织药品数据
            Map<String, Object> drug= new HashMap<>();
            //            drugs.put("direction","口服");
            drug.put("dosageUnit",item.dosageUnit);
            drug.put("drugCommonName",item.drugCommonName);
            drug.put("drugTradeName",item.drugTradeName);
            drug.put("eachDosage",item.eachDosage);
            drug.put("itemDays",item.itemDays);
            drug.put("price",String.valueOf(priceInt));
            drug.put("quantity",String.valueOf(quantityInt));
            drug.put("quantityUnit",item.quantityUnit);
            drug.put("sku",item.sku);
            drug.put("spec",item.spec);
            drugs.add(drug);
        }
//        for (GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes.RecipeDetail item : obj.recipeDetailBeans) {
//            Map<String, Object> drug= new HashMap<>();
//            //            drugs.put("direction","口服");
//            drug.put("dosageUnit",item.drugUnit);
//            drug.put("drugCommonName",item.drugName);
//            drug.put("drugTradeName",item.drugName);
//            drug.put("eachDosage",item.defaultUseDose);
//            drug.put("itemDays",item.dosageUnit);
//            drug.put("price",item.drugCost);
//            drug.put("quantity",item.sendNumber);
//            drug.put("quantityUnit",item.drugUnit);
//            drug.put("sku",item.organDrugCode);
//            drug.put("spec",item.pack);
//            drugs.add(drug);
//        }
        int recipeFeeTrans = (((Float) (Float.valueOf(recipeFee) * 100)).intValue());
        ApiRepository.getInstance().prescriptionPush(clinicSn,
                        GlobalConfig.hospitalName,
                        GlobalConfig.ssCard.getSSNum(),
                        patientSex,
                        patientName,
                        organDiseaseName,
                        recipeId,
                        String.valueOf(recipeFeeTrans),
                        drugs)
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastLoadingObserver<PrescriptionPushEntity>("请稍后...") {
                    @Override
                    public void _onNext(PrescriptionPushEntity entity) {
                        if (entity == null) {
                            ToastUtil.show("请检查网络，返回首页后重试");
                            return;
                        }
                        // 释放资源。只要进入到这里，就结束请求支付轮训
                        onDismiss();
                        // 获取打药单
                        if (entity.getData().isSuccess()){
                            // todo 打印取药单
                            //"data": "{\"orderNo\":\"\",\"takeCode\":\"34811555\"}",
                            // 判定药品是否还有库存
                            // data 的返回类型 {\"1\":0,\"2\":0}
                            Map<String,Object> objectMap = (Map<String, Object>) JSON.parse(entity.getData().getData());
                            if (!TextUtils.isEmpty(objectMap.get("takeCode").toString()))
                                start(ResultFragment.newInstance("paySuc:"+objectMap.get("takeCode")));
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
                        if (entity.getData().isSuccess()){
                            String inputPusId = String.valueOf(entity.getData().getJsonResponseBean().getBody());
                            if (!TextUtils.isEmpty(inputPusId)){
                                busId = inputPusId;
                                requestPayOrder(busId);
                            }
                        }else{
                            ToastUtil.show("合并订单失败");
                        }
                    }
                });
    }

    /**
     * 获得订单二维码和其他详细数据
     * @param busId 订单号
     */
    private void requestPayOrder(String busId){
        ApiRepository.getInstance().payOrder(busId,"recipe")
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
                            jtjk_pay_reflash_tip.setVisibility(View.GONE);
                            String qrStr = entity.getData().getJsonResponseBean().getBody().qr_code;
                            Resources res = getActivity().getResources();
                            Bitmap bmp= BitmapFactory.decodeResource(res, R.drawable.pay_alilogo);
                            showQRCode(XQRCode.createQRCodeWithLogo(qrStr, 400, 400, bmp));
                        }else{
                            jtjk_pay_reflash_tip.setVisibility(View.VISIBLE);
                            ToastUtil.show("获取支付宝二维码失败");
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

    /**
     * 将外部的药品数据转换成内部可以传送的数据
     * 推送处方单专用
     */
    public static class DrugObject {
        public String dosageUnit;
        public String drugCommonName;
        public String drugTradeName;
        public String eachDosage;
        public String itemDays;
        public String price;
        public String quantity;
        public String quantityUnit;
        public String sku;
        public String spec;
    }
}
