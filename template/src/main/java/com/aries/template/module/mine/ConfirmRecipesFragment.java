package com.aries.template.module.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.util.SPUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.template.FakeDataExample;
import com.aries.template.GlobalConfig;
import com.aries.template.R;
import com.aries.template.entity.CanRequestOnlineConsultResultEntity;
import com.aries.template.entity.CreateOrderResultEntity;
import com.aries.template.entity.GetConsultsAndRecipesResultEntity;
import com.aries.template.entity.GetRecipeListByConsultIdEntity;
import com.aries.template.entity.GetStockInfoEntity;
import com.aries.template.entity.PrescriptionPushEntity;
import com.aries.template.module.base.BaseEventFragment;
import com.aries.template.module.main.HomeFragment;
import com.aries.template.retrofit.repository.ApiRepository;
import com.aries.template.utils.ActivityUtils;
import com.aries.template.view.ShineButtonDialog;
import com.aries.template.widget.autoadopter.AutoAdaptorProxy;
import com.aries.template.widget.autoadopter.AutoObjectAdaptor;
import com.aries.ui.view.title.TitleBarView;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.xuexiang.xaop.annotation.SingleClick;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportFragment;

/******
 * 确认处方单
 * 是否已经有处方需要支付为分界
 * 前置必须有一个请求处方单信息请求查询，然后把结果输入进来，如果请求成功则跳入该接口
 * 请确认，输入本类的输入类的类型，他和 obj 还有 RV的输入类型有关系
 * 挂号费支付完成，但是还没有完成视频问诊
 * @author  ::: louis luo
 * Date ::: 2022/6/16 4:54 PM
 */
public class ConfirmRecipesFragment extends BaseEventFragment {

    /** 传入处理处方单的数据 */
    private  List<GetRecipeListByConsultIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO> currentRecipes;

    // 外部输入参数
    public String recipeId = "";//电子处方ID

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
    @BindView(R.id.btn_back)
    Button btn_back;
    @BindView(R.id.btn_inquiry)
    Button btn_inquiry;
    @BindView(R.id.rv_contentFastLib)
    RecyclerView rv_contentFastLib; // 藏的很深的RV对象，显示处方信息

    private AutoAdaptorProxy<GetRecipeListByConsultIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO> proxy; //处方显示对象

    @Override
    public int getContentLayout() {
        return R.layout.fragment_recipe_order;
    }

    /**
     * 获取数据
     */
    public static ConfirmRecipesFragment newInstance(String recipeId, List<GetRecipeListByConsultIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO> currentRecipes) {
        Bundle args = new Bundle();
        ConfirmRecipesFragment fragment = new ConfirmRecipesFragment();
        args.putString("recipeId", recipeId);
        args.putSerializable("currentRecipes", (Serializable) currentRecipes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 注入数据
        Bundle args = getArguments();
        if (args != null) {
            recipeId = args.getString("recipeId", "");
            currentRecipes = (List<GetRecipeListByConsultIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO>) args.getSerializable("currentRecipes");
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
        tv_tip_message.setVisibility(View.GONE);
        btn_back.setVisibility(View.GONE);// 不显示返回
        btn_cancel.setText("取消");
        btn_inquiry.setText("确认结算");
        String sex = GlobalConfig.ssCard.getSex().equals("0")?"女":"男";
        tv_name.setText(GlobalConfig.ssCard.getName().trim()+"("+sex+")");
        tv_card.setText(GlobalConfig.ssCard.getCardNum());
        tv_age_l.setText(String.valueOf(GlobalConfig.age));
        tv_dept.setText(currentRecipes.get(0).appointDepartName);
        tv_result.setText(currentRecipes.get(0).organDiseaseName);
        tv_date.setText(currentRecipes.get(0).createDate);

        // 将数据里面的 9 去除掉，是取消的处方单
        final List<GetRecipeListByConsultIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO> no9Recipes = new ArrayList<>();
        for (GetRecipeListByConsultIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO currentRecipe : currentRecipes) {
            // 去除 9 的 取消单
            if (currentRecipe.status!=9)
                no9Recipes.add(currentRecipe);
        }

        // 处理处方信息，并展示
        reflashRecyclerView(rv_contentFastLib,no9Recipes);
    }

    @Override
    public void loadData() {
        super.loadData();
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
//            case R.id.btn_back:
//                break;
//            case R.id.btn_main:
//                break;
            case R.id.btn_cancel:
                    showSimpleConfirmDialog();
                break;
            case R.id.btn_inquiry:
                     //先查询库存，再创建新处方单
                    requestCreateOrder();
                break;
            default:
                break;
        }
    }

    /**
     * 确定处方单
     */
    public void requestCreateOrder(){
        ArrayList<String> list = new ArrayList<>();
        for (GetRecipeListByConsultIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO currentRecipe : currentRecipes) {
            list.add(currentRecipe.recipeDetailBeanList.get(0).organDrugCode);
        }
        requestGetStockInfo(GlobalConfig.cabinetId,list);

//        ApiRepository.getInstance().createOrder(recipeId)
//                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
//                .subscribe(new FastLoadingObserver<CreateOrderResultEntity>() {
//                    @Override
//                    public void _onNext(CreateOrderResultEntity entity) {
//                        if (entity == null) {
//                            ToastUtil.show("请检查网络");
//                            return;
//                        }
//                        if (entity.isSuccess()){
//                            ArrayList<String> list = new ArrayList<>();
//                            for (GetRecipeListByConsultIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO currentRecipe : currentRecipes) {
//                                list.add(currentRecipe.recipeDetailBeanList.get(0).organDrugCode);
//                            }
//                            requestGetStockInfo(GlobalConfig.cabinetId,list);
//                        }else{
//                            ToastUtil.show(entity.message);
//                        }
//                    }
//                });
    }

    /**
     * 查询药品库存是否还有
     * @param clinicSn 诊亭编号
     * @param skus 药品编码 列表
     */
    public void requestGetStockInfo(String clinicSn, ArrayList<String> skus){
        skus = new ArrayList<String>(){{add("6901339924484");}};//todo cc
//        skus = new ArrayList<String>(){{add("4895013208569");}};//todo cc
        ApiRepository.getInstance().getStockInfo(clinicSn,skus)
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastLoadingObserver<GetStockInfoEntity>("请稍后...") {
                    @Override
                    public void _onNext(GetStockInfoEntity entity) {
                        if (entity == null) {
                            ToastUtil.show("请检查网络，返回首页后重试");
                            return;
                        }
                        if (entity.getData().isSuccess()){
                            // 判定药品是否还有库存
                            // data 的返回类型 {\"1\":0,\"2\":0}
                            Map<String,Object> objectMap = (Map<String, Object>) JSON.parse(entity.getData().getData());
                            for (String key : objectMap.keySet()) {
                                if (String.valueOf(objectMap.get(key)).equals("0")){
                                    // 药品编码 的 这个药没有，提示用户
//                                ToastUtil.show("药品库存不够");
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
                            for (GetRecipeListByConsultIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO currentRecipe : currentRecipes) {
                                // 9 是取消
                                if (currentRecipe.status==9)
                                    continue;

                                GetRecipeListByConsultIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO.RecipeDetailBeanListDTO vo = currentRecipe.recipeDetailBeanList.get(0);
                                // 用量
                                String howToUse = "(1天"+vo.useTotalDose/vo.useDays+"次，每次"+((Double) vo.useDose).intValue()+"片)";
                                PayRecipeFragment.DrugObject drug= new PayRecipeFragment.DrugObject();
                                //            drugs.put("direction","口服");
                                drug.dosageUnit = vo.drugUnit;
                                drug.drugCommonName = vo.drugName;
                                drug.drugTradeName = vo.drugName;
                                drug.eachDosage = String.valueOf(vo.defaultUseDose);
                                drug.itemDays = String.valueOf(vo.useDays);
                                drug.price = String.valueOf(vo.drugCost);
                                drug.quantity =String.valueOf( vo.sendNumber);
                                drug.quantityUnit = vo.drugUnit;
                                drug.sku = vo.organDrugCode;
                                drug.spec =String.valueOf( vo.drugSpec);
                                drug.howToUse =howToUse; // 用量
                                drugs.add(drug);
                                recipeids.add(String.valueOf(vo.recipeId));
                                recipeCodes.add(String.valueOf(vo.organDrugCode));
                            }
                            // 拉到数据了，有库存
                            // 然后取支付页面请求支付，合并处方单
                            //当处方单产生订单，并且订单有效时取的是订单的真实金额，其他时候取的处方的总金额保留两位小数
                            start(PayRecipeFragment.newInstance(recipeids,recipeCodes,drugs,null));
                        }
                    }
                });
    }

    /**
     * 创建取消对话框
     */
    private void showSimpleConfirmDialog() {
        ShineButtonDialog dialog = new ShineButtonDialog(this.mContext);
        dialog.tv_title_tip.setText("取消结算");
        dialog.tv_content_tip.setText("取消后将无法再次支付，是否确认取消");
        dialog.btn_inquiry.setOnClickListener(v -> {
            dialog.dismiss();
                start(HomeFragment.newInstance(), SupportFragment.SINGLETASK);
        });
        dialog.btn_cancel.setOnClickListener(v -> dialog.dismiss());
        dialog.iv_close.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    /**
     * 获取数据后，显示处方信息列表
     * 整个逻辑和输入的 newDatas 的类型有关系，只需要换这个类型即可
     * @param recyclerView 显示对象
     * @param newDatas 传入的数据列
     */
    protected void reflashRecyclerView(RecyclerView recyclerView, List<GetRecipeListByConsultIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO> newDatas){
        // 安全检测
        if (newDatas==null)
            return;

        if (proxy==null){
            proxy = new AutoAdaptorProxy<>(recyclerView, R.layout.item_recipes, 1, newDatas, getContext());
            proxy.setListener(new AutoAdaptorProxy.IItemListener<GetRecipeListByConsultIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO>() {
                @Override
                public void onItemClick(AutoObjectAdaptor.ViewHolder holder, int position, GetRecipeListByConsultIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO itemData) {

                }

                @Override
                public void onItemViewDraw(AutoObjectAdaptor.ViewHolder holder, int position, GetRecipeListByConsultIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO itemData) {
                    GetRecipeListByConsultIdEntity.DataDTO.JsonResponseBeanDTO.BodyDTO.RecipeDetailBeanListDTO vo = itemData.recipeDetailBeanList.get(0);
                    int perDayUse = ((Double) vo.useDose).intValue();

                    String drugName = (position+1)+"、"+vo.drugName;
                    String wayToUse = "(1天"+vo.useTotalDose/vo.useDays+"次，每次"+perDayUse+"片)";
                    String[] orders = {"#333333",drugName,"#38ABA0",wayToUse};
                    ((TextView)holder.itemView.findViewById(R.id.tv_useDose)).setText(ActivityUtils.formatTextView(orders));//使用方法
                } });
        }
        //刷新
        proxy.flashData(new ArrayList<>(newDatas));
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {

    }
}
