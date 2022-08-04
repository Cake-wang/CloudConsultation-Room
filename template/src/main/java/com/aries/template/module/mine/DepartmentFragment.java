package com.aries.template.module.mine;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.util.ToastUtil;
import com.aries.template.GlobalConfig;
import com.aries.template.R;
import com.aries.template.entity.FindValidOrganProfessionForRevisitResultEntity;
import com.aries.template.module.base.BaseEventFragment;
import com.aries.template.retrofit.repository.ApiRepository;
import com.aries.template.widget.autoadopter.AutoAdaptorProxy;
import com.aries.template.widget.autoadopter.AutoObjectAdaptor;
import com.aries.template.widget.autoadopter.DefenceAutoAdaptorProxy;
import com.aries.template.widget.updownbtn.DefenceUpDownProxy;
import com.aries.template.widget.updownbtn.UpDownProxy;
import com.aries.ui.view.title.TitleBarView;
import com.trello.rxlifecycle3.android.FragmentEvent;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 科室展示页面
 * 用于显示一级部门
 * @author louisluo
 * @Author: AriesHoo on 2018/7/13 17:09
 * @E-Mail: AriesHoo@126.com
 * @Function: 我的
 */
public class DepartmentFragment extends BaseEventFragment {
    /** 网格数据数据源 key  */
    public static final String KEY_ITEM_VALUE = "key_item_value";
    /** 网格数据一级科室组织ID */
    public static final String KEY_ITEM_ORGANPROFESSIONID = "key_item_organprofessionid";

    /**
     * 输入显示对象
     */
    @Override
    public int getContentLayout() {
        return R.layout.fragment_dept;
    }

    /** 从外部传入的数据  */
    private  Object inputObj;
    /** 网络获取的全一级科室数据 */
    private ArrayList<Map> totalDatas;
    /** 上一页，下一页管理器 */
    private UpDownProxy<Map> upDownProxy;

    @BindView(R.id.btn_cancel)
    Button btn_cancel;// 上一页按钮
    @BindView(R.id.btn_inquiry)
    Button btn_inquiry;// 下一页按钮
    @BindView(R.id.jtjk_fz_fragment_rv)
    RecyclerView recyclerView;// 网格显示
    @BindView(R.id.jtjk_fz_fragment_title)
    TextView title;// 网格显示
    @BindView(R.id.jtjk_recipe_name)
    TextView jtjk_recipe_name;// 病人名称

    /**
     * 跳转科室，需要带的数据
     */
    public static DepartmentFragment newInstance() {
        DepartmentFragment fragment = new DepartmentFragment();
        return fragment;
    }

    /**
     * 构造函数
     * @param savedInstanceState 输入进来的数据
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 创建 上一页，下一页管理器
        upDownProxy = new UpDownProxy<>();
        upDownProxy.setOnEventListener(new UpDownProxy.EventListener<Map>() {
            @Override
            public void reFlashRV(ArrayList<Map> newDatas) {
                // 刷新时间
                timeCount = 120;
                DefenceAutoAdaptorProxy<Map> proxy = new DefenceAutoAdaptorProxy(recyclerView,R.layout.item_dept,3,newDatas,getContext());
                proxy.setListener(new AutoAdaptorProxy.IItemListener<Map>() {
                    @Override
                    public void onItemClick(AutoObjectAdaptor.ViewHolder holder, int position, Map itemData) {
                        //进入二级
                        if (itemData.get(KEY_ITEM_ORGANPROFESSIONID)!=null)
                            start(DepartmentTwoFragment.newInstance(itemData.get(KEY_ITEM_ORGANPROFESSIONID).toString()));
                    }
                    @Override
                    public void onItemViewDraw(AutoObjectAdaptor.ViewHolder holder, int position, Map itemData) {
                        // 添加文字
                        ((TextView)holder.itemView.findViewById(R.id.jtjk_fz_item_tv)).setText(itemData.get(KEY_ITEM_VALUE).toString());
                    }
                });
                proxy.setThemeListener(new AutoAdaptorProxy.IItemThemeListener<Map>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClickTheme(AutoObjectAdaptor.ViewHolder holder, int position, Map itemData) {
                        //点击后样式
                        ((TextView)holder.itemView.findViewById(R.id.jtjk_fz_item_tv)).setBackground(getActivity().getDrawable(R.drawable.btn_register_pressed_yzs));
                        ((TextView)holder.itemView.findViewById(R.id.jtjk_fz_item_tv)).setTextColor(Color.parseColor("#ffffff"));
                    }

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void unClickTheme(AutoObjectAdaptor.ViewHolder holder, int position, Map itemData) {
                        ((TextView)holder.itemView.findViewById(R.id.jtjk_fz_item_tv)).setBackground(getActivity().getDrawable(R.drawable.btn_register_normal_yzs));
                        ((TextView)holder.itemView.findViewById(R.id.jtjk_fz_item_tv)).setTextColor(Color.parseColor("#333333"));
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
        // 显示名称
        jtjk_recipe_name.setText(GlobalConfig.ssCard.getName()+"，您好");
        // 设置标题
        title.setText("请选择一级科室");
        // 请求一级科室
        requestLevelOne();
    }

    /**
     * 请求一级科室数据
     */
    public void requestLevelOne(){
        int organid = GlobalConfig.organId;//浙大附属邵逸夫医院
        ApiRepository.getInstance().findValidOrganProfessionForRevisit(organid)
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastLoadingObserver<FindValidOrganProfessionForRevisitResultEntity>() {
                    @Override
                    public void _onNext(FindValidOrganProfessionForRevisitResultEntity entity) {
                        if (entity == null) {
                            ToastUtil.show("请检查网络");
                            return;
                        }
                        if (entity.data.jsonResponseBean.body==null)
                            return;
                        //将返回的数据显示出来
                        totalDatas = new ArrayList<>();

                        for (FindValidOrganProfessionForRevisitResultEntity.QueryArrearsSummary.JsonResponseBean.OrganProfessionDTO item : entity.data.jsonResponseBean.body) {
                            Map<String,String> data = new HashMap<>();
                            data.put(KEY_ITEM_VALUE,item.name);
                            data.put(KEY_ITEM_ORGANPROFESSIONID,String.valueOf(item.id));
                            totalDatas.add(data);
                        }
                        if (totalDatas.size()>0){
                            upDownProxy.setParamMaxNumber(9);
                            upDownProxy.setTotalDatas(totalDatas);
                            upDownProxy.doStartReFlash();
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
