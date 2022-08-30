package com.aries.template.module.mine;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import com.aries.template.entity.OrderPreSettleEntity;
import com.aries.template.entity.PayOrderEntity;
import com.aries.template.entity.PrescriptionPushEntity;
import com.aries.template.module.base.BaseEventFragment;
import com.aries.template.retrofit.repository.ApiRepository;
import com.aries.template.utils.ActivityUtils;
import com.aries.template.utils.JTJKLogUtils;
import com.aries.ui.view.title.TitleBarView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.xuexiang.xaop.annotation.IOThread;
import com.xuexiang.xaop.annotation.MainThread;
import com.xuexiang.xaop.enums.ThreadType;
import com.xuexiang.xqrcode.XQRCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.collection.ArraySet;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 支付处方单
 * 仅处理处方单界面
 * 包括未支付的处方单和新处方单。
 * 先进行合并订单
 * - 如果合并订单已经有了，则通过外部输入 orderid解决
 * - 如果外部没有输入orderid，则使用一次合并订单并存储订单号备用
 *
 * @author louisluo
 * @Author: AriesHoo on 2018/7/13 17:09
 * @E-Mail: AriesHoo@126.com
 * @Function: 我的
 */
public class PayRecipeFragment extends BaseEventFragment {
    // logo 图片
    private Bitmap logoBmp;
    // 支付图片
    private Bitmap payBmp;

    // 用于合并处方单的数据源
//    private List<GetPatientRecipeByIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO.RecipedetailsDTO> drugList;

    /**
     * 输入显示对象
     */
    @Override
    public int getContentLayout() {
        return R.layout.fragment_pay;
    }

    /** 从外部传入的数据  */
    private String recipeId;//处方单ID，这个处方单不是合并订单的结果，而是合并订单后的第一个处方单ID
    private String organDiseaseName;//疾病名称
    private String orderid;//订单号，如果是从未支付进来的，那么进来后，不需要合并订单，直接用orderid来获取支付二维码
//    private String busId;//合并订单号，如果有，则刷新二维码，如果没有，就再次尝试合并订单
    private String recipeFee="";//药品费 当处方单产生订单，并且订单有效时取的是订单的真实金额，其他时候取的处方的总金额保留两位小数,总费用用接口获取
    private ArrayList<String> recipes = new ArrayList<>();//处方ID集合
    private ArrayList<String> recipeCodes = new ArrayList<>();//HIS处方编码集合，可以从处方详情中获取
    private boolean isFirstLoop=true;// 是否是第一次执行timeloop，true为是


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
//     * @param recipeId 处方单ID
     * @param recipes 处方ID集合
     * @param recipeCodes 处方编号集合
     * @param obj 处方单信息
     * @param orderid 订单号，
     *                如果是从未支付进来的ongoing，那么进来后，不需要合并订单，直接用orderid来获取支付二维码.
     *                如果是从未支付进来的onready，那么进来后，需要合并订单，orderId为空
     *                如果从确认进来的，那么进来后，需要合并订单，orderId使用空字符
     */
    public static PayRecipeFragment newInstance(
                                                ArrayList<String> recipes,
                                                ArrayList<String> recipeCodes,
                                                ArrayList<DrugObject> obj,
                                                String orderid) {
        PayRecipeFragment fragment = new PayRecipeFragment();
        Bundle args = new Bundle();
//        args.putString("recipeId", recipeId);
        args.putSerializable("recipes", recipes);
        args.putSerializable("recipeCodes", recipeCodes);
        args.putSerializable("obj", obj);
        if (orderid==null)
            orderid = "";
        args.putString("orderid", orderid);
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
            recipes = ((ArrayList<String>) args.getSerializable("recipes"));
            recipeCodes = ((ArrayList<String>) args.getSerializable("recipeCodes"));
            obj = (ArrayList<DrugObject>) args.getSerializable("obj");
            orderid = args.getString("orderid");
//            recipeId = args.getString("recipeId");
            if (recipes.size()>0)
                recipeId = recipes.get(0);
        }
    }

    /**
     * 页面的动作，包括数据传输和界面改变
     */
    @Override
    public void initView(Bundle savedInstanceState) {
        //数据展示
        if (GlobalConfig.ssCard!=null)
            tv_name.setText(GlobalConfig.ssCard.getName());
        // 支付提示
        String[] orders = {"#38ABA0","支付宝·","#333333","扫一扫"};
        jtjk_pay_text.setText(ActivityUtils.formatTextView(orders));
        // 支付类型
        tv_fee_type.setText("处方费");
        // 支付二维码刷新
        jtjk_pay_reflash_tip.setOnClickListener(v -> {
            requestOrderPreSettle(recipes);
        });
    }

    @Override
    public void loadData() {
        super.loadData();
        // 执行时间loop
        timeLoop();
        // 查询金额
        requestOrderPreSettle(recipes);
    }

    @Override
    public void onStart() {
        super.onStart();
        // 覆盖返回监听
        // 返回直接回首页
        getView().findViewById(R.id.btn_back).setOnClickListener(view -> {
            onDismiss();
            gotoMain();
        });
    }

    private static final int PERIOD = 6* 1000;
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
        if (TextUtils.isEmpty(recipeId))
            ToastUtil.show("处方单不正确");
        try {
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
                                // 注入数据
                                if (entity.getData().getJsonResponseBean().getBody()!=null &&
                                        entity.getData().getJsonResponseBean().getBody().getRecipe().getPayFlag()==1){
                                    try {
                                        // 停止循环体
                                        try {
                                            if (mDisposable != null) {
                                                mDisposable.dispose();
                                                mDisposable = null;
                                            }
                                        }catch (Exception e){
                                            e.printStackTrace();
                                            JTJKLogUtils.message(e.toString());
                                        }
                                        // 4.1.4 处方药品推送接口
                                        requestPrescriptionPush(GlobalConfig.cabinetId);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                        JTJKLogUtils.message(e.toString());
                                    }
                                }
                            }
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
            JTJKLogUtils.message(e.toString());
        }
    }


    /**
     * 推送处方单
     * 有库存，详细信息推送到服务端，准备进入支付阶段
     * 支付的基础是创建一个可以支付的处方单。里面有很多处方。
     */
    public void requestPrescriptionPush(String clinicSn){
        if (TextUtils.isEmpty(recipeFee)){
            ToastUtil.show("处方单价格异常，请重试");
            return;
        }

        try {
            // 生成处方单药物集，遍历生成数据，准备输入
            ArrayList<Map> drugs = new ArrayList<>(); // 存储所有处方药品信息的容器
            int current = 0;
            for (DrugObject item : obj) {
                // 药品发放数量必须是整型
                if (current==0){
                    item.sku = "6901339924484";//todo cc 6901339924484 4895013208569
                }else{
                    item.sku = "4895013208569";//todo cc 6901339924484 4895013208569
                }
                current++;
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
            int recipeFeeTrans = (((Float) (Float.valueOf(recipeFee) * 100)).intValue());
            ApiRepository.getInstance().prescriptionPush(clinicSn,
                            GlobalConfig.hospitalName,
                            GlobalConfig.ssCard.getSSNum(),
                            GlobalConfig.ssCard.getSex(),
                            GlobalConfig.ssCard.getName(),
                            organDiseaseName,
                            orderid,
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
                            // 获取打药单
                            try {
                                if (entity.getData().isSuccess()){
                                    // 打印取药单
                                    //"data": "{\"orderNo\":\"\",\"takeCode\":\"34811555\"}",
                                    // 判定药品是否还有库存
                                    // data 的返回类型 {\"1\":0,\"2\":0}
                                    Map<String,Object> objectMap = (Map<String, Object>) JSON.parse(entity.getData().getData());
                                    String drug = "";
                                    if (!TextUtils.isEmpty(objectMap.get("takeCode").toString()))
                                        // 格式化打印数据
                                        // 药物用量
                                        for (DrugObject item : obj) {
                                            drug+=item.drugCommonName +" "+ item.howToUse+"&&";
                                        }
//                                        for (GetPatientRecipeByIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO.RecipedetailsDTO item : drugList) {
//                                           String howToUse =  "(1天"+item.getUseTotalDose()/item.getUseDays()+"次，每次"+ item.getUseDoseStr()+"片)";
//                                            drug += item.getDrugName() +" "+ howToUse+"&&";
//                                        }
                                    // 释放资源。只要进入到这里，就结束请求支付轮训
                                    onDismiss();
                                    start(ResultFragment.newInstance("paySuc:"+objectMap.get("takeCode")+":"+drug));
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                JTJKLogUtils.message(e.toString());
                            }
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
            JTJKLogUtils.message(e.toString());
        }
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
     * 如果有 orderid 了那么就是已经合并过的，不需要再合并。
     * 如果没有 orderid 那么，需要合并一次处方。
     */
    private void requestBatchCreateOrder(String recipeFee){
        try{

            // 如果已经有订单了
            if (!TextUtils.isEmpty(orderid)){
                requestPayOrder(orderid);
                return;
            }

            // 没有可以提交的处方
            if (recipes ==null || recipes.size()<=0)
                return;

//            // 如果没有 orderid 或 busId
//            // 走正常流程
//            drugs.clear();
//            recipes.clear();
//            recipeCodes.clear();
//            for (GetPatientRecipeByIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO.RecipedetailsDTO item : drugList) {
//                // 药品发放数量必须是整型
//                item.setOrganDrugCode("6901339924484");//todo cc 6901339924484 4895013208569
//                int quantityInt = (Double.valueOf(item.getSendNumber())).intValue();
//                // 药品发放数量不小于1
//                if (quantityInt<=0)quantityInt=1;
//                // 价格必须是整型，单位是分
//                final int priceInt = ((Double)(Double.valueOf(item.getDrugCost())*100)).intValue();
//                // 组织药品数据
//                final  Map<String, Object> drug= new HashMap<>();
//                //            drugs.put("direction","口服");
//                drug.put("dosageUnit",item.getDosageUnit());
//                drug.put("drugCommonName",item.getDrugName());
//                drug.put("drugTradeName",item.getDrugName());
//                drug.put("eachDosage",item.getDefaultUseDose());
//                drug.put("itemDays",item.getUseDays());
//                drug.put("price",String.valueOf(priceInt));
//                drug.put("quantity",String.valueOf(quantityInt));
//                drug.put("quantityUnit",item.getDrugUnit());
//                drug.put("sku",item.getOrganDrugCode());
//                drug.put("spec",item.getDrugSpec());
//                drugs.add(drug);
//                recipes.add(String.valueOf(item.getRecipeId()));
//                recipeCodes.add(String.valueOf(item.getOrganDrugCode()));
//            }

            ApiRepository.getInstance().batchCreateOrder(recipeFee, recipes, recipeCodes)
                    .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                    .subscribe(new FastLoadingObserver<BatchCreateOrderEntity>("请稍后...") {
                        @Override
                        public void _onNext(BatchCreateOrderEntity entity) {
                            if (entity == null) {
                                ToastUtil.show("请检查网络");
                                return;
                            }
                            try {
                                if (entity.getData().isSuccess()){
                                    String inputPusId = String.valueOf(entity.getData().getJsonResponseBean().getBody());
                                    if (!TextUtils.isEmpty(inputPusId)){
                                        requestPayOrder(inputPusId);
                                    }
                                }else{
                                    ToastUtil.show("合并订单失败");
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                JTJKLogUtils.message(e.toString());
                            }
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
            JTJKLogUtils.message(e.toString());
        }
    }

    /**
     * 获得订单二维码和其他详细数据
     * @param orderid 订单号
     */
    private void requestPayOrder(String orderid){
        try{
            if (TextUtils.isEmpty(orderid)){
                ToastUtil.show("订单号为空，无法获取支付二维码");
                return;
            }
            // 注入数据
            this.orderid = orderid;
            ApiRepository.getInstance().payOrder(orderid,"recipe")
                    .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                    .subscribe(new FastLoadingObserver<PayOrderEntity>("请稍后...") {
                        @Override
                        public void _onNext(PayOrderEntity entity) {
                            if (entity == null) {
                                ToastUtil.show("请检查网络");
                                return;
                            }
                            try {
                                if (entity.getData().isSuccess()){
                                    // 显示二维码
                                    jtjk_pay_reflash_tip.setVisibility(View.GONE);
                                    String qrStr = entity.getData().getJsonResponseBean().getBody().qr_code;
                                    Resources res = getActivity().getResources();
//                                    Bitmap bmp= BitmapFactory.decodeResource(res, R.drawable.pay_alilogo);
//                                    showQRCode(XQRCode.createQRCodeWithLogo(qrStr, 400, 400, bmp));
                                    logoBmp = BitmapFactory.decodeResource(res, R.mipmap.pay_alilogo);
                                    payBmp = XQRCode.createQRCodeWithLogo(qrStr, 400, 400, logoBmp);
                                    Drawable drawable = new BitmapDrawable(payBmp);
                                    RequestOptions requestOptions =new RequestOptions().centerCrop()
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .error(drawable)//放在出错位置
                                            .placeholder(drawable);//放在占位符位置
                                    Glide.with(getContext())
                                            .setDefaultRequestOptions(requestOptions)
                                            .load("https://${System.currentTimeMillis()}")//随便给个不可用的url
                                            .into(mIvQrcode);

//                                    showQRCode(payBmp);
                                }else{
                                    jtjk_pay_reflash_tip.setVisibility(View.VISIBLE);
                                    ToastUtil.show("获取支付宝二维码失败");
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                JTJKLogUtils.message(e.toString());
                            }
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
            JTJKLogUtils.message(e.toString());
        }
    }

    /**
     * 处方预结算
     * 获取处方单价格
     * @param recipes 订单号集合
     */
    private void requestOrderPreSettle(ArrayList<String> recipes ){
        ApiRepository.getInstance().orderPreSettle(recipes)
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastLoadingObserver<OrderPreSettleEntity>("请稍后...") {
                    @Override
                    public void _onNext(OrderPreSettleEntity entity) {
                        if (entity == null) {
                            ToastUtil.show("请检查网络");
                            return;
                        }
                        try {
                            if (entity.data.success){
                                // 刷新价格
                                // 总费用
                                tv_fee_all.setText(entity.data.jsonResponseBean.body.preSettleTotalAmount+"元");
                                // 医保
                                tv_fee_yb.setText(entity.data.jsonResponseBean.body.preSettleTotalAmount+"元");
                                // 自费
                                tv_fee_zf.setText(entity.data.jsonResponseBean.body.preSettleTotalAmount+"元");
                                // 启动支付二维码
                                // 获得总费用后，请求二维码，这里的价格需要和上面的总费用对齐
                                recipeFee = entity.data.jsonResponseBean.body.preSettleTotalAmount;
                                // 处方合并生成订单接口
                                requestBatchCreateOrder(entity.data.jsonResponseBean.body.preSettleTotalAmount);
                            }else{
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            JTJKLogUtils.message(e.toString());
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
//        //清除mDisposable不再进行验证
//        if (mDisposable != null) {
//            mDisposable.dispose();
//            mDisposable = null;
//        }
        //清除mDisposable不再进行验证
        try {
            if (mDisposable != null) {
                mDisposable.dispose();
                mDisposable = null;
            }
        }catch (Exception e){
            e.printStackTrace();
            JTJKLogUtils.message(e.toString());
        }

        // 清理 logo 图片
        try {
            if (logoBmp!=null){
//                logoBmp.recycle();
                logoBmp = null;
            }
        }catch (Exception e){
            e.printStackTrace();
            JTJKLogUtils.message(e.toString());
        }

        // 清理支付图片
        try {
            if (payBmp!=null){
//                payBmp.recycle();
                payBmp = null;
            }
        }catch (Exception e){
            e.printStackTrace();
            JTJKLogUtils.message(e.toString());
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
        public String howToUse;
    }
}
