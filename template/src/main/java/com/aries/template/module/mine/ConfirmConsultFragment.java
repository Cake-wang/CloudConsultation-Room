package com.aries.template.module.mine;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.util.SPUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.template.GlobalConfig;
import com.aries.template.R;
import com.aries.template.adapter.FlowTagAdapter;
import com.aries.template.entity.PatientListEntity;
import com.aries.template.entity.RequestConsultAndCdrOtherdocResultEntity;
import com.aries.template.module.base.BaseEventFragment;
import com.aries.template.retrofit.repository.ApiRepository;
import com.aries.ui.view.title.TitleBarView;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.widget.flowlayout.FlowTagLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 确认复诊页面
 * 用于输入确认复诊的详细数据
 * @author louisluo
 * @Author: AriesHoo on 2018/7/13 17:09
 * @E-Mail: AriesHoo@126.com
 * @Function: 我的
 */
public class ConfirmConsultFragment extends BaseEventFragment implements CompoundButton.OnCheckedChangeListener {
    /**
     * 输入显示对象
     */
    @Override
    public int getContentLayout() {
        return R.layout.fragment_diagnose;
    }

    /** 从外部传入的数据  */
    private  Object inputObj;
    @BindView(R.id.btn_cancel)
    Button btn_cancel;// 上一页按钮
    @BindView(R.id.btn_inquiry)
    Button btn_inquiry;// 下一页按钮

    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_date)
    TextView tv_date;

    @BindView(R.id.flowlayout_single_select_o)
    FlowTagLayout flowlayout_single_select_o;
    @BindView(R.id.flowlayout_single_select_t)
    FlowTagLayout flowlayout_single_select_t;

    @BindView(R.id.cb_protocol_o)
    AppCompatCheckBox cb_protocol_o;
    @BindView(R.id.cb_protocol_tw)
    AppCompatCheckBox cb_protocol_tw;
    @BindView(R.id.cb_protocol_tr)
    AppCompatCheckBox cb_protocol_tr;

    Integer returnVisitStatus = 0,alleric =  0,haveReaction =0;


    /**
     * 跳转科室，需要带的数据
     */
    public static ConfirmConsultFragment newInstance(Object inputObj) {
        ConfirmConsultFragment fragment = new ConfirmConsultFragment();
        return fragment;
    }

    /**
     * 构造函数
     * @param savedInstanceState 输入进来的数据
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 页面的动作，包括数据传输和界面改变
     */
    @Override
    public void initView(Bundle savedInstanceState) {
        tv_name.setText( SPUtil.get(mContext,"userName","")+"");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        tv_date.setText("");

        initSingleFlowTagLayouto();
        initSingleFlowTagLayoutt();

        cb_protocol_o.setOnCheckedChangeListener(this);
        cb_protocol_tw.setOnCheckedChangeListener(this);
        cb_protocol_tr.setOnCheckedChangeListener(this);
    }


    private void initSingleFlowTagLayouto() {
        FlowTagAdapter tagAdapter = new FlowTagAdapter(getContext());
        flowlayout_single_select_o.setAdapter(tagAdapter);
        flowlayout_single_select_o.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        flowlayout_single_select_o.setOnTagSelectListener((parent, position, selectedList) -> alleric =position);
        tagAdapter.addTags(ResUtils.getStringArray(R.array.tags_values));
        tagAdapter.setSelectedPositions(0);

    }

    private void initSingleFlowTagLayoutt() {
        FlowTagAdapter tagAdapter = new FlowTagAdapter(getContext());
        flowlayout_single_select_t.setAdapter(tagAdapter);
        flowlayout_single_select_t.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        flowlayout_single_select_t.setOnTagSelectListener((parent, position, selectedList) ->  haveReaction =position);
        tagAdapter.addTags(ResUtils.getStringArray(R.array.tags_values));
        tagAdapter.setSelectedPositions(0);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.cb_protocol_o:
                if (cb_protocol_o.isChecked()){
                    //setChecked(),更改此按钮的选中状态 如果为false,则不能选中该控件
                    cb_protocol_tw.setChecked(false);
                    cb_protocol_tr.setChecked(false);
                    returnVisitStatus = 0;
                }
                break;
            case R.id.cb_protocol_tw:
                if (cb_protocol_tw.isChecked()){
                    cb_protocol_o.setChecked(false);
                    cb_protocol_tr.setChecked(false);
                    returnVisitStatus = 1;
                }
                break;
            case R.id.cb_protocol_tr:
                if (cb_protocol_tr.isChecked()){
                    cb_protocol_o.setChecked(false);
                    cb_protocol_tw.setChecked(false);
                    returnVisitStatus = 2;
                }
                break;
            default:
                break;
        }
    }


    /**
     * 按钮 行为 集合
     */
    @SingleClick
    @OnClick({R.id.btn_back, R.id.btn_main, R.id.btn_cancel, R.id.btn_inquiry, R.id.tv_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_date:
                showDatePickerDialog(getContext(), DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT, tv_date, Calendar.getInstance());
                break;
            case R.id.btn_inquiry:
                // 确认，发起复诊请求
                requestGetPatientList();
                break;
            default:
                break;
        }
    }

    /**
     * 日期选择框 显示
     */
    public void showDatePickerDialog(Context context, int themeResId, final TextView tv, Calendar calendar) {
        DatePickerDialog dialog = new DatePickerDialog(context
                , themeResId
                , (view, year, monthOfYear, dayOfMonth) -> tv.setText(String.format("%d-%d-%d", year, (monthOfYear + 1), dayOfMonth))
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
        if (dialog.getButton(DatePickerDialog.BUTTON_POSITIVE)!=null){
            dialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#38ABA0"));
            dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#38ABA0"));
        }
    }

    /**
     * 获取病人信息
     * 为了能够获得Mpiid
     */
    public  void requestGetPatientList(){
        ApiRepository.getInstance().getPatientList()
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastLoadingObserver<PatientListEntity>() {
                    @Override
                    public void _onNext(PatientListEntity entity) {
                        if (entity == null) {
                            ToastUtil.show("请检查网络");
                            return;
                        }
                        if (entity.data.success){
                            requestConsultAndCdrOtherdoc(entity.data.jsonResponseBean.body.patient.mpiId);
                        }
                    }
                });
    }

    /**
     * 执行复诊确认
     * 发起复诊单
     */
    public void requestConsultAndCdrOtherdoc(String mpiid){
        if (GlobalConfig.doc==null && GlobalConfig.departmentID==null){
            // todo 添加用户提示
            return;
        }
        String alleric = String.valueOf(flowlayout_single_select_o.getSelectedIndex());
        String haveReaction = String.valueOf(flowlayout_single_select_t.getSelectedIndex());
        String confirmedDate = tv_date.getText().toString();
        if (cb_protocol_o.isChecked()) returnVisitStatus =0;
        if (cb_protocol_tw.isChecked())returnVisitStatus=1;
        if (cb_protocol_tr.isChecked())returnVisitStatus=2;
        ApiRepository.getInstance().requestConsultAndCdrOtherdoc(GlobalConfig.doc.getCurrentOrgan(),
                        mpiid,
                        GlobalConfig.departmentID,
                        alleric,
                        haveReaction,
                        confirmedDate,
                        String.valueOf(returnVisitStatus),
                        "0.01",
                "0.01",
                        GlobalConfig.doc.getDoctorId())
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastLoadingObserver<RequestConsultAndCdrOtherdocResultEntity>() {
                    @Override
                    public void _onNext(RequestConsultAndCdrOtherdocResultEntity entity) {
                        if (entity == null) {
                            ToastUtil.show("请检查网络");
                            return;
                        }
                        if (entity.data.success){
                            // 通过确诊单，进入到复诊单支付
//                            start(PayConsultFragment.newInstance());
                            // 医生 确认复诊，并进入复诊阶段
                            start(PayConsultFragment.newInstance(String.valueOf(entity.data.jsonResponseBean.body.consult.consultId),
                                    entity.data.jsonResponseBean.body.patient.patientName,
                                    String.valueOf(entity.data.jsonResponseBean.body.doctor.loginId),
                                    entity.data.jsonResponseBean.body.doctor.name));
                        }else{
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
//                    JSONObject json613 = new JSONObject(msg);
//                    cid = json613.get("cid").toString();
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
