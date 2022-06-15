package com.aries.template.module.mine;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.util.ToastUtil;
import com.aries.template.GlobalConfig;
import com.aries.template.R;
import com.aries.template.entity.FindValidDepartmentForRevisitResultEntity;
import com.aries.template.entity.FindValidOrganProfessionForRevisitResultEntity;
import com.aries.template.module.base.BaseEventFragment;
import com.aries.template.retrofit.repository.ApiRepository;
import com.aries.template.widget.autoadopter.AutoAdaptor;
import com.aries.template.widget.updownbtn.UpDownProxy;
import com.aries.ui.view.title.TitleBarView;
import com.trello.rxlifecycle3.android.FragmentEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 科室展示页面
 * 二级部门
 * @author louisluo
 * @Author: AriesHoo on 2018/7/13 17:09
 * @E-Mail: AriesHoo@126.com
 * @Function: 我的
 */
public class DepartmentTwoFragment extends BaseEventFragment {
    /** 网格数据数据源 key  内部输出数据*/
    public static final String KEY_ITEM_OBJECT = "key_item_object";
    /** 获取传参 一级科室组织ID 外部输入数据*/
    public static final String KEK_BUNDLE_ORGANPROFESSIONID = "key_item_organprofessionid";
    /**  网格数据显示的最大个数 */
    public static final int PARAM_MAX_RV_NUMBER = 9;

    /**
     * 输入显示对象
     */
    @Override
    public int getContentLayout() {
        return R.layout.fragment_dept;
    }

    /** 从外部传入的数据  */
    private  String inputObj;
    /** 120  秒倒计时间 */
    private int timeCount = 120;
    /** 当前1级机构的page位置 */
    private int currentPageNum = 0;
    /** 网络获取的全一级科室数据 */
    private ArrayList<Map> totalDatas;
    /** 上一页，下一页管理器 */
    private UpDownProxy<Map> upDownProxy;

    @BindView(R.id.jtjk_fz_fragment_timer)
    TextView timerTV; //时间计时器显示对象
    @BindView(R.id.btn_cancel)
    Button btn_cancel;// 上一页按钮
    @BindView(R.id.btn_inquiry)
    Button btn_inquiry;// 下一页按钮
    @BindView(R.id.jtjk_fz_fragment_rv)
    RecyclerView recyclerView;// 网格显示
    @BindView(R.id.jtjk_fz_fragment_title)
    TextView title;// 网格显示

    /**
     * 跳转科室，需要带的数据
     */
    public static DepartmentTwoFragment newInstance(String inputObj) {
        DepartmentTwoFragment fragment = new DepartmentTwoFragment();
        Bundle args = new Bundle();
        args.putString(KEK_BUNDLE_ORGANPROFESSIONID,inputObj);
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
        inputObj = getArguments().getString(KEK_BUNDLE_ORGANPROFESSIONID);

        // 创建 上一页，下一页管理器
        upDownProxy = new UpDownProxy<>();
        upDownProxy.setOnEventListener(new UpDownProxy.EventListener<Map>() {
            @Override
            public void reFlashRV(ArrayList<Map> newDatas) {
                // 刷新时间
                timeCount = 120;
                AutoAdaptor adaptor =  new AutoAdaptor(recyclerView,R.layout.item_dept,3,newDatas,getContext());
                adaptor.setListener(new AutoAdaptor.IItemListener() {
                    @Override
                    public void onItemClick(AutoAdaptor.ViewHolder holder, int position, Map itemData) {
                        //进入医生
                        if (itemData.get(KEY_ITEM_OBJECT)!=null){
                            FindValidDepartmentForRevisitResultEntity.QueryArrearsSummary.JsonResponseBean.OrganProfessionDTO data
                                    = ((FindValidDepartmentForRevisitResultEntity.QueryArrearsSummary.JsonResponseBean.OrganProfessionDTO) itemData.get(KEY_ITEM_OBJECT));
                            start(DoctorListFragment.newInstance(data.getCode(),data.getProfessionCode()));
                        }
                    }
                    @Override
                    public void onItemViewDraw(AutoAdaptor.ViewHolder holder, int position, Map itemData) {
                        FindValidDepartmentForRevisitResultEntity.QueryArrearsSummary.JsonResponseBean.OrganProfessionDTO data
                                = ((FindValidDepartmentForRevisitResultEntity.QueryArrearsSummary.JsonResponseBean.OrganProfessionDTO) itemData.get(KEY_ITEM_OBJECT));
                        ((TextView)holder.itemView.findViewById(R.id.jtjk_fz_item_tv)).setText(data.getName());
                    }
                });
                adaptor.notifyDataSetChanged();
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

        // 启动计时器
        timeStart();
    }

    /**
     * 页面的动作，包括数据传输和界面改变
     */
    @Override
    public void initView(Bundle savedInstanceState) {
        // 事件
        btn_inquiry.setOnClickListener(v -> {upDownProxy.doNextReFlash();});
        btn_cancel.setOnClickListener(v -> {upDownProxy.doProReFlash();});
        title.setText("请选择二级科室");
        // 请求一级科室
        if (inputObj!=null)
            requestLevelTwo(inputObj);
    }

    /**
     * 请求二级科室数据
     */
    public void requestLevelTwo(String organProfessionId){
        int organid = GlobalConfig.organId;//浙大附属邵逸夫医院
        ApiRepository.getInstance().findValidDepartmentForRevisit(organid, organProfessionId, getContext())
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastLoadingObserver<FindValidDepartmentForRevisitResultEntity>() {
                    @Override
                    public void _onNext(FindValidDepartmentForRevisitResultEntity entity) {
                        if (entity == null) {
                            ToastUtil.show("请检查网络");
                            return;
                        }
                        totalDatas = new ArrayList<>();
                        for (FindValidDepartmentForRevisitResultEntity.QueryArrearsSummary.JsonResponseBean.OrganProfessionDTO item :
                                entity.data.jsonResponseBean.body) {
                            Map<String,Object> data = new HashMap<>();
                            data.put(KEY_ITEM_OBJECT,item);
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
     * 计时器任务处理
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void timeProcess() {
        super.timeProcess();
        if (timerTV!=null)
            timerTV.setText(--timeCount+"秒");
        if (timeCount==0){
            gotoMain();
        }
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
