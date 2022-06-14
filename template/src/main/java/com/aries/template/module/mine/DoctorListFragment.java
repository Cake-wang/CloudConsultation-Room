package com.aries.template.module.mine;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.util.ToastUtil;
import com.aries.template.GlobalConfig;
import com.aries.template.R;
import com.aries.template.entity.CanRequestOnlineConsultResultEntity;
import com.aries.template.entity.SearchDoctorListByBusTypeV2ResultEntity;
import com.aries.template.module.base.BaseEventFragment;
import com.aries.template.retrofit.repository.ApiRepository;
import com.aries.template.widget.autoadopter.AutoAdaptor;
import com.aries.ui.view.title.TitleBarView;
import com.trello.rxlifecycle3.android.FragmentEvent;

import org.json.JSONObject;

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
public class DoctorListFragment extends BaseEventFragment {
    /** 网格数据数据源 key  */
    public static final String KEY_ITEM_VALUE = "key_item_value";
    /** 网格数据 医生信息 全数据 */
    public static final String KEY_ITEM_CURRENT_DOC = "key_item_current_doc";
    /** 获取传参 专科编码*/
    public static final String KEK_BUNDLE_PROFESSION = "key_item_organprofessionid";
    /** 获取传参 科室ID*/
    public static final String KEK_BUNDLE_DEPARTMENTID = "kek_bundle_departmentid";
    /**  网格数据显示的最大个数 */
    public static final int PARAM_MAX_RV_NUMBER = 2;

    /**
     * 输入显示对象
     */
    @Override
    public int getContentLayout() {
        return R.layout.fragment_doctor;
    }

    /** 从外部传入的数据  */
    private  String profession;
    /** 从外部传入的数据  */
    private  String departmentId;
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
     * @param departmentId 科室id
     * @param profession 一级科室id
     */
    public static DoctorListFragment newInstance(String departmentId,String profession) {
        DoctorListFragment fragment = new DoctorListFragment();
        Bundle args = new Bundle();
        args.putString(KEK_BUNDLE_DEPARTMENTID,departmentId);
        args.putString(KEK_BUNDLE_PROFESSION,profession);
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
        // 获取传参数
        departmentId = getArguments().getString(KEK_BUNDLE_DEPARTMENTID);
        profession = getArguments().getString(KEK_BUNDLE_PROFESSION);
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
        title.setText("请选择就诊医生");
        // 请求一级科室
        requestDoctorInfo();
    }

    /**
     * 通过数据刷新列表
     * @param data 列表数据
     */
    public void reFlashRV(ArrayList<Map> data){
        // 刷新时间
        timeCount = 120;
        AutoAdaptor adaptor =  new AutoAdaptor(recyclerView,R.layout.item_doctor,2,data,getContext());
        adaptor.setListener(new AutoAdaptor.IItemListener() {
            @Override
            public void onItemClick(AutoAdaptor.ViewHolder holder, int position, Map itemData) {
                //进入复诊单
                // todo 存储当前最终选择的医生信息，可以重复刷新，结束后清空
                SearchDoctorListByBusTypeV2ResultEntity.QueryArrearsSummary.JsonResponseBean.OrganProfessionDTO.DocList.Doctor doc = ((SearchDoctorListByBusTypeV2ResultEntity.QueryArrearsSummary.JsonResponseBean.OrganProfessionDTO.DocList.Doctor) itemData.get(KEY_ITEM_CURRENT_DOC));
                requestCanRequestOnlineConsult(doc.getDoctorId(),doc);
            }
            @Override
            public void onItemViewDraw(AutoAdaptor.ViewHolder holder, int position, Map itemData) {
                SearchDoctorListByBusTypeV2ResultEntity.QueryArrearsSummary.JsonResponseBean.OrganProfessionDTO.DocList.Doctor doc = ((SearchDoctorListByBusTypeV2ResultEntity.QueryArrearsSummary.JsonResponseBean.OrganProfessionDTO.DocList.Doctor) itemData.get(KEY_ITEM_CURRENT_DOC));
                ((TextView)holder.itemView.findViewById(R.id.jtjk_doc_item_name_tv)).setText(doc.getName());
                ((TextView)holder.itemView.findViewById(R.id.jtjk_doc_item_introduce_tv)).setText(doc.getIntroduce());
            }
        });
        adaptor.notifyDataSetChanged();
    }

    /**
     * 请求医生数据
     */
    public void requestDoctorInfo(){
        int organid = GlobalConfig.organId;//浙大附属邵逸夫医院
        ApiRepository.getInstance().searchDoctorListByBusTypeV2(departmentId,profession,organid)
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastLoadingObserver<SearchDoctorListByBusTypeV2ResultEntity>() {
                    @Override
                    public void _onNext(SearchDoctorListByBusTypeV2ResultEntity entity) {
                        if (entity == null) {
                            ToastUtil.show("请检查网络");
                            return;
                        }
                        if (entity.data.jsonResponseBean.body==null)
                            return;
                        //将返回的数据显示出来
                        totalDatas = new ArrayList<>();
                        for (SearchDoctorListByBusTypeV2ResultEntity.QueryArrearsSummary.JsonResponseBean.OrganProfessionDTO.DocList doc : entity.data.jsonResponseBean.body.getDocList()) {
                            doc.getDoctor().getName();
                            Map<String,Object> data = new HashMap<>();
                            data.put(KEY_ITEM_CURRENT_DOC,doc.getDoctor());
                            totalDatas.add(data);
                        }
                        if (totalDatas.size()>0)
                            beginReFlashRv();
                    }
                });
    }

    /**
     * 请求医生当前是否能够复诊
     */
    public void requestCanRequestOnlineConsult(Long doctorID, SearchDoctorListByBusTypeV2ResultEntity.QueryArrearsSummary.JsonResponseBean.OrganProfessionDTO.DocList.Doctor doc){
        ApiRepository.getInstance().canRequestOnlineConsult(doctorID)
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastLoadingObserver<CanRequestOnlineConsultResultEntity>() {
                    @Override
                    public void _onNext(CanRequestOnlineConsultResultEntity entity) {
                        if (entity == null) {
                            ToastUtil.show("请检查网络");
                            return;
                        }
                        if (entity.success){
                            // 将医生信息设置为全局复诊医生信息
                            GlobalConfig.doc = doc;
                            // 医生可以进行复诊 跳转确认
                            start(ConfirmConsultFragment.newInstance("ok"));
                        }else{
                            // 医生不可以进行复诊
                            Toast.makeText(mContext, "医生当前不能进行复诊", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /**
     * 上一页数据显示
     */
    public void preReFlashRV(){
        if (totalDatas==null)
            return;
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
