package com.aries.template.module.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.util.SPUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.template.GlobalConfig;
import com.aries.template.R;
import com.aries.template.entity.FindMedicineStockEntity;
import com.aries.template.entity.GetConsultsAndRecipesResultEntity;
import com.aries.template.entity.GetStockInfoEntity;
import com.aries.template.module.base.BaseEventFragment;
import com.aries.template.retrofit.repository.ApiRepository;
import com.aries.template.utils.ActivityUtils;
import com.aries.template.view.ShineButtonDialog;
import com.aries.template.widget.autoadopter.AutoAdaptorProxy;
import com.aries.template.widget.autoadopter.AutoObjectAdaptor;
import com.aries.ui.view.title.TitleBarView;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.xuexiang.xaop.annotation.SingleClick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/******
 * 待支付处方单 界面
 * 前置必须有一个请求处方单信息请求查询，然后把结果输入进来，如果请求成功则跳入该接口
 * 由于取消的 处方单 是不会存在的，所以不需要判断9，取消订单的问题
 * @author  ::: louis luo
 * Date ::: 2022/6/16 4:54 PM
 */
public class OrderRecipesFragment extends BaseEventFragment {

    /** 传入处理处方单的数据 */
    private GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes obj;

    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_card)
    TextView tv_card;
    @BindView(R.id.tv_age)
    TextView tv_age;
    @BindView(R.id.tv_dept_r)
    TextView tv_dept_r;
    @BindView(R.id.tv_date_r)
    TextView tv_date_r;
    @BindView(R.id.tv_age_tv)
    TextView tv_age_tv;
    @BindView(R.id.tv_age_l)
    TextView tv_age_l;
    @BindView(R.id.tv_doc_tv)
    TextView tv_doc_tv;
    @BindView(R.id.tv_doc)
    TextView tv_doc;
    @BindView(R.id.tv_dept_tv)
    TextView tv_dept_tv;
    @BindView(R.id.tv_dept)
    TextView tv_dept;
    @BindView(R.id.tv_result_tv)
    TextView tv_result_tv;
    @BindView(R.id.tv_result)
    TextView tv_result;
    @BindView(R.id.tv_date_tv)
    TextView tv_date_tv;
    @BindView(R.id.tv_date)
    TextView tv_date;
    @BindView(R.id.ll_order_text_r)
    LinearLayout ll_order_text_r;
    @BindView(R.id.ll_order_r)
    LinearLayout ll_order_r;
    @BindView(R.id.ll_prescription)
    LinearLayout ll_prescription;
    @BindView(R.id.tv_tip_message)
    TextView tv_tip_message;
    @BindView(R.id.btn_cancel)
    Button btn_cancel;
    @BindView(R.id.btn_inquiry)
    Button btn_inquiry;
    @BindView(R.id.rv_contentFastLib)
    RecyclerView rv_contentFastLib; // 藏的很深的RV对象，显示处方信息


    @Override
    public int getContentLayout() {
        return R.layout.fragment_recipe_order;
    }

    /**
     * 获取数据
     * @param obj 传入的数据，注意这个对象必须实现序列化
     */
    public static OrderRecipesFragment newInstance(GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes obj) {
        Bundle args = new Bundle();
        OrderRecipesFragment fragment = new OrderRecipesFragment();
        args.putSerializable("obj", obj);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 注入数据
        Bundle args = getArguments();
        if (args != null) {
            obj = (GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes) args.getSerializable("obj");
        }
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        tv_age_tv.setVisibility(View.VISIBLE);
        tv_age_l.setVisibility(View.VISIBLE);
        tv_doc_tv.setVisibility(View.GONE);
        tv_doc.setVisibility(View.GONE);
        tv_dept_tv.setVisibility(View.VISIBLE);
        tv_dept.setVisibility(View.VISIBLE);
        tv_result_tv.setVisibility(View.VISIBLE);
        tv_result.setVisibility(View.VISIBLE);
        tv_date_tv.setVisibility(View.VISIBLE);
        tv_date.setVisibility(View.VISIBLE);
        ll_order_text_r.setVisibility(View.GONE);
        ll_order_r.setVisibility(View.GONE);
        ll_prescription.setVisibility(View.VISIBLE);
        tv_tip_message.setText("您已有处方记录，是否需要支付");
        btn_cancel.setText("取消支付");
        btn_inquiry.setText("去支付");

//        String sex = GlobalConfig.ssCard.getSex().equals("0")?"女":"男";
        String sex = GlobalConfig.ssCard.getSex();
        tv_name.setText(GlobalConfig.ssCard.getName().trim()+"("+sex+")");
        tv_card.setText(GlobalConfig.ssCard.getCardNum());
        tv_age_l.setText(String.valueOf(GlobalConfig.age));
        tv_result.setText(obj.organDiseaseName);
        tv_date.setText(obj.signDate+"");

        // 处理处方信息，并展示
        reflashRecyclerView(rv_contentFastLib,obj);
    }


    /**
     * 处理按钮
     * 注意，super.onViewClicked 必须些，不然父类的按钮将无效
     * OnClick 必须声明点击对象，否则，点击无效
     * @param view 点击对象
     */
    @SingleClick
    @OnClick({R.id.btn_back, R.id.btn_main, R.id.btn_cancel, R.id.btn_inquiry})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                    showSimpleConfirmDialog();
                break;
            case R.id.btn_inquiry:
                    //先查库存，再跳转支付页
                    ArrayList<Map<String,Object>> list = new ArrayList<>();
                    for (GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes.RecipeDetail item : obj.recipeDetailBeans) {
                        Map<String,Object > map = new HashMap<>();
                        map.put(item.organDrugCode,item.useTotalDose);
//                        list.add(item.organDrugCode, item.sendNumber);
                        list.add(map);
                    }
                    requestGetStockInfo(GlobalConfig.cabinetId,list);
                break;
            default:
                break;
        }
    }

    /**
     * 创建取消对话框
     */
    private void showSimpleConfirmDialog() {
        ShineButtonDialog dialog = new ShineButtonDialog(this.mContext);
        dialog.tv_title_tip.setText("取消支付订单");
        dialog.tv_content_tip.setText("取消后将无法再次支付，是否确认取消");
        dialog.btn_inquiry.setOnClickListener(v -> {
            dialog.dismiss();
            // todo 取消处方单的支付返回
        });
        dialog.btn_cancel.setOnClickListener(v -> dialog.dismiss());
        dialog.iv_close.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    /**
     * 获取数据后，显示处方信息列表
     * @param recyclerView 显示对象
     * @param newDatas 传入的数据列
     */
    protected void reflashRecyclerView(RecyclerView recyclerView, GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes newDatas){
        // 没有可以显示的数据
        if (newDatas==null || newDatas.getRecipeDetailBeans()==null)
            return;

        List<GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes.RecipeDetail> allRecipe =new ArrayList<>();
        allRecipe.addAll(newDatas.getRecipeDetailBeans());

        AutoAdaptorProxy<GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes.RecipeDetail> proxy
                = new AutoAdaptorProxy<>(recyclerView, R.layout.item_recipes, 1, allRecipe, getContext());

       proxy.setListener(new AutoAdaptorProxy.IItemListener<GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes.RecipeDetail>() {
           @Override
           public void onItemClick(AutoObjectAdaptor.ViewHolder holder, int position, GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes.RecipeDetail itemData) {
           }

           @Override
           public void onItemViewDraw(AutoObjectAdaptor.ViewHolder holder, int position, GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes.RecipeDetail itemData) {
               int perDayUse = itemData.getUseDose().intValue();

               String drugName = (position+1)+"、"+itemData.getDrugName();
               String wayToUse = "(1天"+itemData.getUseTotalDose()/itemData.getUseDays()+"次，每次"+perDayUse+"片)";
               String[] orders = {"#333333",drugName,"#38ABA0",wayToUse};
               ((TextView)holder.itemView.findViewById(R.id.tv_useDose)).setText(ActivityUtils.formatTextView(orders));//使用方法
           }
       });
        //刷新
        proxy.notifyDataSetChanged();
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {

    }

    /**
     * 查询药品库存是否还有
     * @param clinicSn 诊亭编号
     * @param skus 药品编码 列表
     */
    public void requestGetStockInfo(String clinicSn, ArrayList<Map<String,Object>> skus){
//        skus = new ArrayList<String>(){{add("6901339924484");}};//todo cc
//        skus = new ArrayList<String>(){{add("4895013208569");}};//todo cc
        skus = new ArrayList<Map<String,Object>>(){{
            Map<String,Object> map =new HashMap<>();
            map.put("6901339924484",1);
            add(map);
        }};//todo cc
        ApiRepository.getInstance().findMedicineStock(clinicSn,skus)
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastLoadingObserver<FindMedicineStockEntity>("请稍后...") {
                    @Override
                    public void _onNext(FindMedicineStockEntity entity) {
                        if (entity == null) {
                            ToastUtil.show("请检查网络，返回首页后重试");
                            return;
                        }
                        try {
                            if (entity.success){
                                // 判定药品是否还有库存
//                             data 的返回类型 {\"1\":0,\"2\":0}
//                                Map<String,Object> objectMap = (Map<String, Object>) JSON.parse(entity.data().getData());
//                                for (String key : objectMap.keySet()) {
//                                    if (String.valueOf(objectMap.get(key)).equals("0")){
//                                        // 药品编码 的 这个药没有，提示用户
////                                ToastUtil.show("药品库存不够");
//                                        start(ResultFragment.newInstance("stockFail"));
//                                        return;
//                                    }
//                                }

                                // 检查每一种药物
                                for (FindMedicineStockEntity.DataDTO.DrugListDTO item : entity.data.drugList) {
                                    // 包括没有库存，库存小于取药数
                                    if (item.stockAmount-item.total<=0){
                                        // 没有库存
                                        // 药品编码 的 这个药没有，提示用户
                                        start(ResultFragment.newInstance("stockFail"));
                                        return;
                                    }
                                }

                                // 启动处方单推送接口
                                // 拉到数据了，有库存
                                // 然后取支付页面请求支付，合并处方单
                                ArrayList<String> recipeids = new ArrayList<>();
                                ArrayList<String> recipeCodes = new ArrayList<>();
                                ArrayList<PayRecipeFragment.DrugObject> drugs = new ArrayList<>();
                                for (GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes.RecipeDetail item : obj.recipeDetailBeans) {
                                    // 由于取消的 处方单 是不会存在的，所以不需要判断9，取消订单的问题
                                    // 用量
                                    String howToUse = "(1天"+item.getUseTotalDose()/item.getUseDays()+"次，每次"+ item.getUseDose().intValue()+"片)";
                                    PayRecipeFragment.DrugObject drug= new PayRecipeFragment.DrugObject();
                                    //            drugs.put("direction","口服");
                                    drug.dosageUnit = item.drugUnit;
                                    drug.drugCommonName = item.drugName;
                                    drug.drugTradeName = item.drugName;
                                    drug.eachDosage = String.valueOf(item.defaultUseDose);
                                    drug.itemDays = String.valueOf(item.useDays);
                                    drug.price = String.valueOf(item.drugCost);
                                    drug.quantity =String.valueOf( item.sendNumber);
                                    drug.quantityUnit = item.drugUnit;
                                    drug.sku = item.organDrugCode;
                                    drug.spec =String.valueOf( item.drugSpec);
                                    drug.howToUse =howToUse;// 用量
                                    drugs.add(drug);
                                    recipeids.add(String.valueOf(item.recipeId));
                                    recipeCodes.add(String.valueOf(item.organDrugCode));
                                }
                                //当处方单产生订单，并且订单有效时取的是订单的真实金额，其他时候取的处方的总金额保留两位小数
                                String orderId = obj.orderId==null?"":String.valueOf(obj.orderId);
                                start(PayRecipeFragment.newInstance(recipeids,recipeCodes,drugs,orderId));
                            }else {
                                ToastUtil.show("药品查询失败");
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            ToastUtil.show("药品查询失败");
                        }
                    }
                });
    }

    /**
     * 查询药品库存是否还有
     * @param clinicSn 诊亭编号
     * @param skus 药品编码 列表
     */
//    public void requestGetStockInfo(String clinicSn, ArrayList<Map<String,String>> skus){
////        skus = new ArrayList<String>(){{add("6901339924484");}};//todo cc
////        skus = new ArrayList<String>(){{add("4895013208569");}};//todo cc
//        ApiRepository.getInstance().getStockInfo(clinicSn,skus)
//                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
//                .subscribe(new FastLoadingObserver<GetStockInfoEntity>("请稍后...") {
//                    @Override
//                    public void _onNext(GetStockInfoEntity entity) {
//                        if (entity == null) {
//                            ToastUtil.show("请检查网络，返回首页后重试");
//                            return;
//                        }
//                        try {
//                            if (entity.getData().isSuccess()){
//                                // 判定药品是否还有库存
////                             data 的返回类型 {\"1\":0,\"2\":0}
//                                Map<String,Object> objectMap = (Map<String, Object>) JSON.parse(entity.getData().getData());
//                                for (String key : objectMap.keySet()) {
//                                    if (String.valueOf(objectMap.get(key)).equals("0")){
//                                        // 药品编码 的 这个药没有，提示用户
////                                ToastUtil.show("药品库存不够");
//                                        start(ResultFragment.newInstance("stockFail"));
//                                        return;
//                                    }
//                                }
//
//                                // 启动处方单推送接口
//                                // 拉到数据了，有库存
//                                // 然后取支付页面请求支付，合并处方单
//                                ArrayList<String> recipeids = new ArrayList<>();
//                                ArrayList<String> recipeCodes = new ArrayList<>();
//                                ArrayList<PayRecipeFragment.DrugObject> drugs = new ArrayList<>();
//                                for (GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes.RecipeDetail item : obj.recipeDetailBeans) {
//                                    // 由于取消的 处方单 是不会存在的，所以不需要判断9，取消订单的问题
//                                    // 用量
//                                    String howToUse = "(1天"+item.getUseTotalDose()/item.getUseDays()+"次，每次"+ item.getUseDose().intValue()+"片)";
//                                    PayRecipeFragment.DrugObject drug= new PayRecipeFragment.DrugObject();
//                                    //            drugs.put("direction","口服");
//                                    drug.dosageUnit = item.drugUnit;
//                                    drug.drugCommonName = item.drugName;
//                                    drug.drugTradeName = item.drugName;
//                                    drug.eachDosage = String.valueOf(item.defaultUseDose);
//                                    drug.itemDays = String.valueOf(item.useDays);
//                                    drug.price = String.valueOf(item.drugCost);
//                                    drug.quantity =String.valueOf( item.sendNumber);
//                                    drug.quantityUnit = item.drugUnit;
//                                    drug.sku = item.organDrugCode;
//                                    drug.spec =String.valueOf( item.drugSpec);
//                                    drug.howToUse =howToUse;// 用量
//                                    drugs.add(drug);
//                                    recipeids.add(String.valueOf(item.recipeId));
//                                    recipeCodes.add(String.valueOf(item.organDrugCode));
//                                }
//                                //当处方单产生订单，并且订单有效时取的是订单的真实金额，其他时候取的处方的总金额保留两位小数
//                                String orderId = obj.orderId==null?"":String.valueOf(obj.orderId);
//                                start(PayRecipeFragment.newInstance(recipeids,recipeCodes,drugs,orderId));
//                            }else {
//                                ToastUtil.show("药品查询失败");
//                            }
//                        }catch (Exception e){
//                            e.printStackTrace();
//                            ToastUtil.show("药品查询失败");
//                        }
//                    }
//                });
//    }


}
