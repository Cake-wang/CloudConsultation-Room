package com.aries.template.module.mine;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.aries.library.fast.util.SPUtil;
import com.aries.template.R;
import com.aries.template.entity.GetConsultsAndRecipesResultEntity;
import com.aries.template.module.base.BaseEventFragment;
import com.aries.template.module.main.HomeFragment;
import com.aries.template.utils.ActivityUtils;
import com.aries.template.view.ShineButtonDialog;
import com.aries.template.widget.autoadopter.AutoAdaptorProxy;
import com.aries.template.widget.autoadopter.AutoObjectAdaptor;
import com.aries.ui.view.title.TitleBarView;
import com.xuexiang.xaop.annotation.SingleClick;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportFragment;

/******
 * 待支付处方/处方信息 界面
 * 是否已经有处方需要支付为分界
 * @author  ::: louis luo
 * Date ::: 2022/6/16 4:54 PM
 *
 * todo obj 这个类需要换成 处方 的类型
 * todo 启动判定是否是待支付处方
 * todo (obj.getRecipes().size()>0 待支付判定需要更改
 */
public class OrderRecipesFragment extends BaseEventFragment {

    /** 新的处方单 */
    public static final String TYPE_RECIPE_NEW = "type_recipe_new";
    /** 已有的处方单 */
    public static final String TYPE_RECIPE_OLD = "type_recipe_old";
    /** RV的存取数据的map KEY */
    private static final String KEY_ITEM_CURRENT_RECIPES = "key_item_current_recipes";
    /** 传入处理处方单的数据 */
    private ArrayList<GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes> obj;
    /** 当前启动状态 TYPE_RECIPE_NEW为新处方单，为TYPE_RECIPE_OLD已有处方单 */
    private String openType;

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
        return R.layout.fragment_order;
    }

    /**
     * 获取数据
     * @param obj 传入的数据，注意这个对象必须实现序列化
     */
    public static OrderRecipesFragment newInstance(ArrayList<GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes> obj,
                                                   String openType) {
        Bundle args = new Bundle();
        OrderRecipesFragment fragment = new OrderRecipesFragment();
        args.putSerializable("obj", obj);
        args.putString("openType", openType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 注入数据
        Bundle args = getArguments();
        if (args != null) {
            obj = (ArrayList<GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes>) args.getSerializable("obj");
            openType = args.getString("openType");
        }
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (openType==TYPE_RECIPE_OLD){
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
            tv_name.setText(SPUtil.get(mContext,"userName","")+""+SPUtil.get(mContext,"sex",""));
            tv_card.setText(SPUtil.get(mContext,"smkCard","")+"");
            tv_age_l.setText(SPUtil.get(mContext,"age","")+"");
//            tv_dept.setText("");
            tv_result.setText(obj.get(0).organDiseaseName);
            tv_date.setText(obj.get(0).signDate+"");
        }else if (openType==TYPE_RECIPE_NEW){
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
            btn_cancel.setText("取消");
            btn_inquiry.setText("确认结算");
            tv_name.setText(SPUtil.get(mContext,"userName","")+""+SPUtil.get(mContext,"sex",""));
            tv_card.setText(SPUtil.get(mContext,"smkCard","")+"");
            tv_age_l.setText(SPUtil.get(mContext,"age","")+"");
        }
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
//            case R.id.btn_back:
//                break;
//            case R.id.btn_main:
//                break;
            case R.id.btn_cancel:
                if (openType==TYPE_RECIPE_OLD){
                    // todo 旧处方取消接口
                    showSimpleConfirmDialog("recipes");
                }else if (openType==TYPE_RECIPE_NEW){
                    showSimpleConfirmDialog("backMain");
                }
                break;
            case R.id.btn_inquiry:
                 if (openType==TYPE_RECIPE_OLD){
                    //todo 先查库存，再跳转支付页
                     start(PayCodeFragment.newInstance(new Object()));
                }else if (openType==TYPE_RECIPE_NEW){
                     //todo 先查询库存，再创建新处方单
                    start(PayCodeFragment.newInstance(new Object()));
                }
                break;
            default:
                break;
        }
    }

    /**
     * 创建取消对话框
     */
    private void showSimpleConfirmDialog(String opflag) {
        ShineButtonDialog dialog = new ShineButtonDialog(this.mContext);
        if(opflag.contains("recipes")){
            dialog.tv_title_tip.setText("取消支付订单");
            dialog.tv_content_tip.setText("取消后将无法再次支付，是否确认取消");
        }else {
            dialog.tv_title_tip.setText("取消结算");
            dialog.tv_content_tip.setText("取消后将无法再次支付，是否确认取消");
        }
        dialog.btn_inquiry.setOnClickListener(v -> {
            dialog.dismiss();
            if (opflag.contains("recipes")){
            }else {
                start(HomeFragment.newInstance(), SupportFragment.SINGLETASK);
            }
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
    protected void reflashRecyclerView(RecyclerView recyclerView, ArrayList<GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes> newDatas){
        List<GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes.RecipeDetail> allRecipe =new ArrayList<>();
        for (GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes newData : newDatas) {
            if (newData.getRecipeDetailBeans()!=null)
                allRecipe.addAll(newData.getRecipeDetailBeans());
        }

        AutoAdaptorProxy<GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes.RecipeDetail> proxy
                = new AutoAdaptorProxy<>(recyclerView, R.layout.item_recipes, 1, allRecipe, getContext());

       proxy.setListener(new AutoAdaptorProxy.IItemListener<GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes.RecipeDetail>() {
           @Override
           public void onItemClick(AutoObjectAdaptor.ViewHolder holder, int position, GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes.RecipeDetail itemData) {
           }

           @Override
           public void onItemViewDraw(AutoObjectAdaptor.ViewHolder holder, int position, GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes.RecipeDetail itemData) {
               String drugName = (position+1)+"、"+itemData.getDrugName();
               String wayToUse = "(1天"+itemData.getUseTotalDose()/itemData.getUseDays()+"次，每次"+itemData.getUseDose()+"片)";
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
}
