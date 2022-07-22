package com.aries.template.module.mine;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.util.ToastUtil;
import com.aries.template.GlobalConfig;
import com.aries.template.R;
import com.aries.template.entity.FindValidOrganProfessionForRevisitResultEntity;
import com.aries.template.entity.GetConsultsAndRecipesResultEntity;
import com.aries.template.module.base.BaseEventFragment;
import com.aries.template.retrofit.repository.ApiRepository;
import com.aries.template.utils.ActivityUtils;
import com.aries.template.utils.DateUtils;
import com.aries.template.widget.autoadopter.AutoAdaptorProxy;
import com.aries.template.widget.autoadopter.AutoObjectAdaptor;
import com.aries.template.widget.autoadopter.DefenceAutoAdaptorProxy;
import com.aries.template.widget.updownbtn.DefenceUpDownProxy;
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
    private DefenceUpDownProxy<GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes> upDownProxy;

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
        upDownProxy = new DefenceUpDownProxy<>();
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
                            // 跳转到详细页
                            start(OrderRecipesFragment.newInstance(obj.get(position)));
                        });

                        // 添加文字
                        int minute = ((Float)(Float.valueOf(itemData.recipeSurplusHours)*60)).intValue();
                        String openTime = itemData.signDate.split(" ")[0];
                        ((TextView)holder.itemView.findViewById(R.id.jtjk_recipe_disastname)).setText(itemData.organDiseaseName);
                        ((TextView)holder.itemView.findViewById(R.id.jtjk_recipe_optime)).setText("开方时间:  "+openTime);
                        ((TextView)holder.itemView.findViewById(R.id.jtjk_recipe_closetime)).setText("失效时间:  "+DateUtils.addHour(openTime,"yyyy-MM-dd",minute));



                        // 添加金额样式
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
                    button.setBackgroundResource(R.drawable.btn_next_yzs);
                }else {
                    button.setTextColor(Color.parseColor("#999999"));
                    button.setBackgroundResource(R.drawable.btn_ago_yzs);
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


        title.setText("未支付的处方单");

        upDownProxy.setParamMaxNumber(2);
        upDownProxy.setTotalDatas(obj);
        upDownProxy.doStartReFlash();

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
