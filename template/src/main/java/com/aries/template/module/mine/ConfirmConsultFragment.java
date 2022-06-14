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

import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.retrofit.FastObserver;
import com.aries.library.fast.util.SPUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.template.R;
import com.aries.template.adapter.FlowTagAdapter;
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
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;

/**
 * 科室展示页面
 * 用于显示一级部门，二级部门
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
    /** 120  秒倒计时间 */
    private int timeCount = 120;

    @BindView(R.id.jtjk_fz_fragment_timer)
    TextView timerTV; //时间计时器显示对象
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
        // 启动计时器
        timeStart();
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
//        int organid = 1;//浙大附属邵逸夫医院
//        ApiRepository.getInstance().findValidOrganProfessionForRevisit(organid, getContext())
//                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
//                .subscribe(new FastLoadingObserver<FindValidOrganProfessionForRevisitResultEntity>() {
//                    @Override
//                    public void _onNext(FindValidOrganProfessionForRevisitResultEntity entity) {
//                        if (entity == null) {
//                            ToastUtil.show("请检查网络");
//                            return;
//                        }
////                        entity.data.requestId;
//                    }
//                });
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


    @Override
    @SingleClick
    @OnClick({R.id.btn_back, R.id.btn_main, R.id.btn_cancel, R.id.btn_inquiry})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            case R.id.btn_back:
//
//                break;
//            case R.id.btn_main:
//
//                break;
            case R.id.tv_date:

                showDatePickerDialog(getContext(), DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT, tv_date, Calendar.getInstance());


                break;
            case R.id.btn_inquiry:

                ApiRepository.getInstance().requestConsultAndCdrOtherdoc("","","",mContext)
                        .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                        .subscribe(true ?
                                new FastLoadingObserver<RequestConsultAndCdrOtherdocResultEntity>("请稍后...") {
                                    @Override
                                    public void _onNext(@NonNull RequestConsultAndCdrOtherdocResultEntity entity) {
                                        if (entity == null) {
                                            ToastUtil.show("请检查网络");
                                            return;
                                        }
//                                checkVersion(entity);
                                        if (entity.isSuccess()){

                                            if (entity.getData().isSuccess()){
                                                start(PayCodeFragment.newInstance(new Object()));
                                            }


                                        }else {

//                                    if(TextUtils.isEmpty(tag)){
//                                        ToastUtil.show("参数缺失");
//                                    }else {
//                                        start(PutRecordFragment.newInstance( idCard, name, smkcard));
//                                    }

//                                    ToastUtil.show(entity.getRespDesc());
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {

//                                ToastUtil.show("请检查网络和ip地址");
                                        if (true) {
                                            super.onError(e);
                                        }
                                    }
                                } :
                                new FastObserver<RequestConsultAndCdrOtherdocResultEntity>() {
                                    @Override
                                    public void _onNext(@NonNull RequestConsultAndCdrOtherdocResultEntity entity) {
                                        if (entity == null) {
                                            ToastUtil.show("请检查网络");
                                            return;
                                        }



                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        if (false) {
                                            super.onError(e);
                                        }
                                    }
                                });


                break;
            default:
                break;
        }
    }

    public void showDatePickerDialog(Context context, int themeResId, final TextView tv, Calendar calendar) {
        new DatePickerDialog(context
                , themeResId
                , (view, year, monthOfYear, dayOfMonth) -> tv.setText(String.format("%d-%d-%d", year, (monthOfYear + 1), dayOfMonth))
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    /**
     * 计时器任务处理
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void timeProcess() {
        super.timeProcess();
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
