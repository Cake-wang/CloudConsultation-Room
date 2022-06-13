package com.aries.template.module.mine;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.util.ToastUtil;
import com.aries.template.GlobalConfig;
import com.aries.template.R;
import com.aries.template.entity.FindValidDepartmentForRevisitResultEntity;
import com.aries.template.entity.FindValidOrganProfessionForRevisitResultEntity;
import com.aries.template.module.base.BaseEventFragment;
import com.aries.template.retrofit.repository.ApiRepository;
import com.aries.template.widget.autoadopter.AutoAdaptor;
import com.aries.ui.view.title.TitleBarView;
import com.trello.rxlifecycle3.android.FragmentEvent;

import androidx.annotation.Nullable;
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
    private  Object inputObj;
    /** 120  秒倒计时间 */
    private int timeCount = 120;
    /** 当前1级机构的page位置 */
    private int currentPageNum = 0;
    /** 网络获取的全一级科室数据 */
    private ArrayList<Map> totalDatas;

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
    public static DepartmentFragment newInstance(Object inputObj) {
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
        // 启动计时器
        timeStart();
    }

    /**
     * 页面的动作，包括数据传输和界面改变
     */
    @Override
    public void initView(Bundle savedInstanceState) {
        // 事件
        btn_inquiry.setOnClickListener(v -> {nextReFlashRv();});
        btn_cancel.setOnClickListener(v -> {preReFlashRV();});
        title.setText("请选择一级科室");
        // 请求一级科室
        requestLevelOne();
    }

    /**
     * 通过数据刷新列表
     * @param data 列表数据
     */
    public void reFlashRV(ArrayList<Map> data){
        // 刷新时间
        timeCount = 120;
        AutoAdaptor adaptor =  new AutoAdaptor(recyclerView,R.layout.item_dept,3,data,getContext());
        adaptor.setListener(new AutoAdaptor.IItemListener() {
            @Override
            public void onItemClick(AutoAdaptor.ViewHolder holder, int position, Map itemData) {
                //进入二级
                if (itemData.get(KEY_ITEM_ORGANPROFESSIONID)!=null)
                    start(DepartmentTwoFragment.newInstance(itemData.get(KEY_ITEM_ORGANPROFESSIONID).toString()));
            }
            @Override
            public void onItemViewDraw(AutoAdaptor.ViewHolder holder, int position, Map itemData) {
                ((TextView)holder.itemView.findViewById(R.id.jtjk_fz_item_tv)).setText(itemData.get(KEY_ITEM_VALUE).toString());
            }
        });
        adaptor.notifyDataSetChanged();
    }

    /**
     * 请求一级科室数据
     */
    public void requestLevelOne(){
        int organid = GlobalConfig.organId;//浙大附属邵逸夫医院
        ApiRepository.getInstance().findValidOrganProfessionForRevisit(organid, getContext())
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
                            data.put(KEY_ITEM_ORGANPROFESSIONID,item.professionCode);
                            totalDatas.add(data);
                        }
                        if (totalDatas.size()>0)
                            beginReFlashRv();
                    }
                });
    }

    /**
     * 上一页数据显示
     */
    public void preReFlashRV(){
        int startIndex = (currentPageNum -1)*PARAM_MAX_RV_NUMBER;
        if (startIndex<=0){
            startIndex = 0;
            // 上一页 背景设置，可点击设置
            setBtnEnable(btn_cancel,false);
        }
        ArrayList<Map> newDatas = new ArrayList<>();
        for (int i = 0; i < PARAM_MAX_RV_NUMBER; i++) {
            if (startIndex+i>totalDatas.size()){
                // 下一页 不可用
                setBtnEnable(btn_inquiry,false);
                break; // out of boundary
            }
            newDatas.add(totalDatas.get(startIndex+i));
        }
        reFlashRV(newDatas);
        currentPageNum--;
        // 如果是上一页功能就设置下一页按钮
        if (!btn_inquiry.isEnabled()){
            if (startIndex+PARAM_MAX_RV_NUMBER<=totalDatas.size()){
                setBtnEnable(btn_inquiry,true);
            }
        }else{
            if (startIndex+PARAM_MAX_RV_NUMBER>totalDatas.size()){
                setBtnEnable(btn_inquiry,false);
            }
        }
    }

    /**
     * 下一页数据显示
     */
    public void nextReFlashRv(){
        int startIndex = (currentPageNum+1)*PARAM_MAX_RV_NUMBER;
        if (startIndex<=0){
            startIndex = 0;
            // 上一页 背景设置，可点击设置
            setBtnEnable(btn_cancel,false);
        }
        ArrayList<Map> newDatas = new ArrayList<>();
        for (int i = 0; i < PARAM_MAX_RV_NUMBER; i++) {
            if (startIndex+i>=totalDatas.size()){
                // 下一页 不可用
                setBtnEnable(btn_inquiry,false);
                break; // out of boundary
            }
            newDatas.add(totalDatas.get(startIndex+i));
        }
        reFlashRV(newDatas);
        currentPageNum++;
        // 如果是下一页功能则设置上一页按钮
        if (!btn_cancel.isEnabled()){
            if (startIndex-PARAM_MAX_RV_NUMBER>=0){
                setBtnEnable(btn_cancel,true);
            }
        }else{
            if (startIndex-PARAM_MAX_RV_NUMBER<0){
                setBtnEnable(btn_cancel,false);
            }
        }
    }

    /**
     * 从0开始显示
     * 重置数据，重置上一页下一页按钮
     */
    public void beginReFlashRv(){
        //todo 修改一下样式，修改一下按钮不可用,包括上一页和下一页
        int startIndex = 0;
        ArrayList<Map> newDatas = new ArrayList<>();
        for (int i = 0; i < PARAM_MAX_RV_NUMBER; i++) {
            if (startIndex+i>=totalDatas.size()){
                break; // out of boundary
            }
            newDatas.add(totalDatas.get(startIndex+i));
        }
        reFlashRV(newDatas);
        currentPageNum =0;

        // 上一页肯定不能用
        setBtnEnable(btn_cancel,false);

        // 下一页未必不能用
        if (startIndex+PARAM_MAX_RV_NUMBER<totalDatas.size()){
            setBtnEnable(btn_inquiry,true);
        }
    }

    /**
     * 将一个button设置可用和不可用,并调整样式
     */
    public void setBtnEnable(Button button, boolean b){
        button.setEnabled(b);
        if (b){
            button.setTextColor(Color.parseColor("#ffffff"));
            button.setBackgroundResource(R.drawable.btn_next_yzs);
        }else {
            button.setTextColor(Color.parseColor("#999999"));
            button.setBackgroundResource(R.drawable.btn_ago_yzs);
        }
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
