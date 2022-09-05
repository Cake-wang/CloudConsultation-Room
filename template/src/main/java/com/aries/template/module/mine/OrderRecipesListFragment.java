package com.aries.template.module.mine;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.util.ToastUtil;
import com.aries.template.GlobalConfig;
import com.aries.template.R;
import com.aries.template.entity.GetConsultsAndRecipesResultEntity;
import com.aries.template.entity.GetTakeCodeEntity;
import com.aries.template.entity.SearchDoctorListByBusTypeV2ResultEntity;
import com.aries.template.module.base.BaseEventFragment;
import com.aries.template.retrofit.repository.ApiRepository;
import com.aries.template.utils.ActivityUtils;
import com.aries.template.utils.DateUtils;
import com.aries.template.utils.DefenceUtil;
import com.aries.template.widget.autoadopter.AutoAdaptorProxy;
import com.aries.template.widget.autoadopter.AutoObjectAdaptor;
import com.aries.template.widget.autoadopter.DefenceAutoAdaptorProxy;
import com.aries.template.widget.updownbtn.UpDownProxy;
import com.aries.ui.view.title.TitleBarView;
import com.trello.rxlifecycle3.android.FragmentEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 未支付处方单列表
 * 用于显示未支付处方单
 * 每一次显示2个处方单
 *
 * 视频问诊已经完成，但是没有完成处方单支付
 *
 * 先处理处方单的列表，然后进入处方单详细页处理未支付
 *
 * 如果视频问诊里面开了3个处方单，那么，这3个单子会分合并，并获取相同并统一的 orderid。
 * 如果是由于网络原因没有拿到药品，那么会在这个列表中，判断并显示按钮，通过orderid，获取取药码，并进入取药界面。
 *
 *
 * @author louisluo
 */
public class OrderRecipesListFragment extends BaseEventFragment {

    /** 传入处理处方单的数据 */
    private ArrayList<GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes> obj;

    /**
     * 输入显示对象
     */
    @Override
    public int getContentLayout() {
        return R.layout.fragment_order_recipe_list;
    }

    /** 上一页，下一页管理器 */
    private UpDownProxy<GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes> upDownProxy;

    @BindView(R.id.btn_cancel)
    Button btn_cancel;// 上一页按钮
    @BindView(R.id.btn_inquiry)
    Button btn_inquiry;// 下一页按钮
    @BindView(R.id.btn_goto_dep)
    Button btn_goto_dep;// 下一页按钮
    @BindView(R.id.jtjk_recipe_fragment_rv)
    RecyclerView recyclerView;// 网格显示
    @BindView(R.id.jtjk_fz_fragment_title)
    TextView title;// 网格显示
    @BindView(R.id.jtjk_recipe_name)
    TextView jtjk_recipe_name;


    /**
     * 获取数据
     * @param obj 传入的数据，注意这个对象必须实现序列化
     */
    public static OrderRecipesListFragment newInstance(ArrayList<GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes> obj) {
        Bundle args = new Bundle();
        OrderRecipesListFragment fragment = new OrderRecipesListFragment();
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
            obj = (ArrayList<GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes>) args.getSerializable("obj");
        }


        // 创建 上一页，下一页管理器
        upDownProxy = new UpDownProxy<>();
        upDownProxy.setOnEventListener(new UpDownProxy.EventListener<GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes>() {
            @Override
            public void reFlashRV(ArrayList<GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes> newDatas) {
                // 刷新时间
                timeCount = 120;
                DefenceAutoAdaptorProxy<GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes> proxy = new DefenceAutoAdaptorProxy(recyclerView,R.layout.item_recipe,1,newDatas,getContext());
                proxy.setListener(new AutoAdaptorProxy.IItemListener<GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes>() {
                    @Override
                    public void onItemClick(AutoObjectAdaptor.ViewHolder holder, int position, GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes itemData) {
                    }

                    @Override
                    public void onItemViewDraw(AutoObjectAdaptor.ViewHolder holder, int position, GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes itemData) {
                        holder.itemView.findViewById(R.id.jtjk_recipe_btn).setOnClickListener(v -> {
                            // 如果是处方详情状态是3 表示这个处方已经支付，但是没有取药成功
                            if (obj.get(position).status==3){
                                //请求获得药品
                                requestDoctorInfo(String.valueOf(obj.get(position).orderId),obj.get(position));
                            }else{
                                // 跳转到详细页
                                start(OrderRecipesFragment.newInstance(obj.get(position)));
                            }
                        });

                        // 添加文字
//                        int minute = 0;
//                        if (!TextUtils.isEmpty(itemData.recipeSurplusHours)){
//                            minute = ((Float)(Float.valueOf(itemData.recipeSurplusHours)*60)).intValue();
//                        }
                        String openTime = itemData.signDate.split(" ")[0];
                        String endTime = itemData.recipeSurplusHours.split(" ")[0];
                        ((TextView)holder.itemView.findViewById(R.id.jtjk_recipe_disastname)).setText(itemData.organDiseaseName);
                        ((TextView)holder.itemView.findViewById(R.id.jtjk_recipe_optime)).setText("开方时间:  "+openTime);
                        ((TextView)holder.itemView.findViewById(R.id.jtjk_recipe_closetime)).setText("失效时间:  "+endTime);
                        ((TextView)holder.itemView.findViewById(R.id.jtjk_recipe_btn)).setText("处方详情");

                        // 如果是处方详情状态是3 表示这个处方已经支付，但是没有取药成功
                        if (obj.get(position).status==3){
                            // 变化样式
                            ((TextView)holder.itemView.findViewById(R.id.jtjk_recipe_btn)).setText("拿取药码");
                        }

                        // 添加金额样式
                        if (itemData.totalMoney==null)itemData.totalMoney = 0.0d;
                        String[] orders = {"#38ABA0",itemData.totalMoney.toString(),"#333333","元"};
                        ((TextView)holder.itemView.findViewById(R.id.jtjk_recipe_totoalpayment)).setText(ActivityUtils.formatTextView(orders));//使用方法
                    }
                });
                proxy.notifyDataSetChanged();
            }

            @Override
            public void setBtnEnable(int nowCase, boolean b) {
                Button button = new Button(getContext());
                if (nowCase==UpDownProxy.CASE_RV_UP)
                    button = btn_cancel;
                if (nowCase==UpDownProxy.CASE_RV_DOWN)
                    button = btn_inquiry;
                button.setEnabled(b);
                if (b){
                    button.setTextColor(Color.parseColor("#ffffff"));
                    button.setBackgroundResource(R.mipmap.btn_next_yzs);
                }else {
                    button.setTextColor(Color.parseColor("#999999"));
                    button.setBackgroundResource(R.mipmap.btn_ago_yzs);
                }
            }
        });
    }

    /**
     * 页面的动作，包括数据传输和界面改变
     */
    @Override
    public void initView(Bundle savedInstanceState) {
        // 事件
        // 点击上一页
        btn_inquiry.setOnClickListener(v -> {upDownProxy.doNextReFlash();});
        // 点击下一页
        btn_cancel.setOnClickListener(v -> {upDownProxy.doProReFlash();});
        // 点击跳过
        btn_goto_dep.setOnClickListener(v->{start(DepartmentFragment.newInstance());});
        // 显示名称
        jtjk_recipe_name.setText(GlobalConfig.ssCard.getName()+"，您好");


        title.setText("未支付的处方单");

        upDownProxy.setParamMaxNumber(2);
        upDownProxy.setTotalDatas(obj);
        upDownProxy.doStartReFlash();
    }


    /**
     * 请求取药码
     */
    public void requestDoctorInfo(String orderid, GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes recipes){
        // 防重复提交
        if (!DefenceUtil.checkReSubmit("OrderRecipesListFragment.requestDoctorInfo"))
            return;

        // 获取取药码后，跳转到取药成功界面
        ApiRepository.getInstance().getTakeCode(orderid)
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastLoadingObserver<GetTakeCodeEntity>("请稍后...") {
                    @Override
                    public void _onNext(GetTakeCodeEntity entity) {
                        if (entity == null) {
                            ToastUtil.show("请检查网络");
                            return;
                        }
                        try {
                            if (entity.success){
                                Map<String,Object> objectMap = new HashMap<>();
                                objectMap.put("takeCode",entity.data.takeCode);
                                String drug = "";
                                if (!TextUtils.isEmpty(objectMap.get("takeCode").toString()))
                                    // 格式化打印数据
                                    // 药物用量
//                                    for (PayRecipeFragment.DrugObject item : obj) {
//                                        drug+=item.drugCommonName +" "+ item.howToUse+"&&";
//                                    }
                                for (GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes.RecipeDetail item : recipes.recipeDetailBeans) {
                                    String perDayUse = "适量";
                                    if (item.getUseDose()!=null)
                                        perDayUse = String.valueOf(item.getUseDose().intValue()) + "片";
                                    String howToUse = "(1天"+item.getUseTotalDose()/item.getUseDays()+"次，每次"+ perDayUse+")";
                                    drug+= item.drugName +" "+howToUse+"&&";
                                }
                                // 释放资源。只要进入到这里，就结束请求支付轮训
                                onDismiss();
                                start(ResultFragment.newInstance("paySuc:"+objectMap.get("takeCode")+":"+drug));
                            }else{
                                ToastUtil.show("获取药品失败");
                            }
                        }catch (Exception e){
                            e.printStackTrace();
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
