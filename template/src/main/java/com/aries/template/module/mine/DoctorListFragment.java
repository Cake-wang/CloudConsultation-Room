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
import com.aries.template.widget.updownbtn.UpDownProxy;
import com.aries.ui.view.title.TitleBarView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.trello.rxlifecycle3.android.FragmentEvent;

import org.json.JSONException;
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
    /** 当前1级机构的page位置 */
    private int currentPageNum = 0;
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
        // 创建 上一页，下一页管理器
        upDownProxy = new UpDownProxy<>();
        upDownProxy.setOnEventListener(new UpDownProxy.EventListener<Map>() {
            @Override
            public void reFlashRV(ArrayList<Map> newDatas) {
                // 刷新时间
                timeCount = 120;
                AutoAdaptor adaptor =  new AutoAdaptor(recyclerView,R.layout.item_doctor,2,newDatas,getContext());
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
        btn_inquiry.setOnClickListener(v -> {upDownProxy.doNextReFlash();});
        btn_cancel.setOnClickListener(v -> {upDownProxy.doProReFlash();});
        title.setText("请选择就诊医生");
        // 请求一级科室
        requestDoctorInfo();
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
                        if (totalDatas.size()>0){
                            upDownProxy.setParamMaxNumber(9);
                            upDownProxy.setTotalDatas(totalDatas);
                            upDownProxy.doStartReFlash();
                        }
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
                        if (entity.data.isSuccess()){
                            // 将医生信息设置为全局复诊医生信息
                            GlobalConfig.doc = doc;
                            // 医生可以进行复诊 跳转确认
                            start(ConfirmConsultFragment.newInstance("ok"));
                        }else{
                            // 医生不可以进行复诊

                            // 如果不能复诊，则检查异常原因
                            errorCheck(entity.data.jsonResponseBean.msg,entity.data.jsonResponseBean.code);
                        }
                    }
                });
    }

    /**
     * 复诊 异常情况处理
     */
    public void errorCheck(String msg, int code) {
        String toastTip = "复诊单异常"; //默认提示
        String cid = "0";// 默认是0
        try {
            switch (code){
                case 611:
                    JSONObject json611 = new JSONObject(msg);
                    cid = json611.get("consultId").toString();
                    toastTip = json611.get("title").toString();
                    break;
                case 613:
                    JSONObject json613 = new JSONObject(msg);
                    cid = json613.get("cid").toString();
                    toastTip ="有一条未结束的非团队复诊单";
                    break;
                case 614:
                    JSONObject json614 = new JSONObject(msg);
                    cid = json614.get("cid").toString();
                    toastTip ="有一条未结束的团队复诊单";
                    break;
                case 608:
                    cid = "0";
                    toastTip =msg;
                    break;
            }
            Toast.makeText(mContext, toastTip, Toast.LENGTH_LONG).show();
        }catch (Exception e){
            e.printStackTrace();
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
