package com.aries.template.module.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.util.SPUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.template.FakeDataExample;
import com.aries.template.GlobalConfig;
import com.aries.template.R;
import com.aries.template.entity.CancelregisterResultEntity;
import com.aries.template.entity.GetConsultAndPatientAndDoctorByIdEntity;
import com.aries.template.entity.GetConsultsAndRecipesResultEntity;
import com.aries.template.entity.ReceiveMessageFromPatientWithRequestModeEntity;
import com.aries.template.module.base.BaseEventFragment;
import com.aries.template.retrofit.repository.ApiRepository;
import com.aries.template.utils.DateUtils;
import com.aries.template.view.ShineButtonDialog;
import com.aries.ui.view.title.TitleBarView;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.xuexiang.xaop.annotation.SingleClick;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.OnClick;

/******
 * 已挂号界面
 *
 * 挂号费费用已经结清，但是没有完成视频问诊
 *
 * 前置必须有一个请求挂号单信息请求查询，然后把结果输入进来，如果请求成功则跳入该接口
 * @author  ::: louis luo
 * Date ::: 2022/6/16 3:45 PM
 *
 */
public class OrderConsultFragment extends BaseEventFragment {
    private GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Consults obj;
    private String consultId ;
    private String patientName ;
    private String doctorId ;
    private String doctorName;

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
    RecyclerView rv_contentFastLib;


    @Override
    public int getContentLayout() {
        return R.layout.fragment_order;
    }

    /**
     * 获取数据
     * @param obj 传入的数据，注意这个对象必须实现序列化
     * @return
     */
    public static OrderConsultFragment newInstance(GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Consults obj) {
        Bundle args = new Bundle();
        OrderConsultFragment fragment = new OrderConsultFragment();
        args.putSerializable("obj", (Serializable) obj);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 注入数据
        Bundle args = getArguments();
        if (args != null) {
            obj = (GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Consults) args.getSerializable("obj");
        }
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        tv_age_tv.setVisibility(View.GONE);
        tv_age_l.setVisibility(View.GONE);
        tv_doc_tv.setVisibility(View.VISIBLE);
        tv_doc.setVisibility(View.VISIBLE);
        tv_dept_tv.setVisibility(View.GONE);
        tv_dept.setVisibility(View.GONE);
        tv_result_tv.setVisibility(View.GONE);
        tv_result.setVisibility(View.GONE);
        tv_date_tv.setVisibility(View.GONE);
        tv_date.setVisibility(View.GONE);

        ll_order_text_r.setVisibility(View.VISIBLE);
        ll_order_r.setVisibility(View.VISIBLE);
        ll_prescription.setVisibility(View.GONE);
        btn_cancel.setVisibility(View.VISIBLE);

        tv_tip_message.setText("您已有挂号记录，是否发起问诊");
        btn_cancel.setText("取消挂号");
        btn_inquiry.setText("去问诊");

        String sex = GlobalConfig.ssCard.getSex().equals("0")?"女":"男";
        tv_name.setText(obj.getConsults().getMpiName()+"("+sex+")");
        tv_card.setText(GlobalConfig.ssCard.getCardNum());
        tv_age.setText(String.valueOf(GlobalConfig.age));
        tv_doc.setText(obj.getConsults().getConsultDoctorText());
        tv_dept_r.setText(obj.getConsults().getConsultDepartText());
        tv_date_r.setText(obj.getConsults().getRequestTime());
        consultId = String.valueOf(obj.getConsults().getConsultId());

        requestPaySuccess();
    }

    /**
     * 获取复诊单详情
     */
    private void requestPaySuccess() {
        ApiRepository.getInstance().getConsultAndPatientAndDoctorById(consultId)
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastLoadingObserver<GetConsultAndPatientAndDoctorByIdEntity>("请等待") {
                    @Override
                    public void _onNext(GetConsultAndPatientAndDoctorByIdEntity entity) {
                        if (entity == null) {
                            ToastUtil.show("请检查网络");
                            return;
                        }
                        // 检查 payFlag 如果是 1 就是支付成功
                        if (entity.isSuccess()){
                            patientName = GlobalConfig.ssCard.getName().trim();
                            doctorId=String.valueOf(entity.getData().getJsonResponseBean().getBody().getDoctor().getLoginId());
                            doctorName=String.valueOf(entity.getData().getJsonResponseBean().getBody().getDoctor().getName());
                        }
                    }
                });
    }

    /**
     * 从去问诊进入，向医生发送消息，请求开视频问诊
     */
    private void requestReceiveMessageFromPatientWithRequestMode(){
        String imID = ApiRepository.getUUID(); // 统一使用 UUID 来传参
        String today = DateUtils.getTodayWidthFormat("yyyy-MM-dd HH:mm");
        ApiRepository.getInstance().receiveMessageFromPatientWithRequestMode(imID,
                        consultId,
                        "我向您发起视频问诊，请重新开始视频问诊",
                        today)
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastLoadingObserver<ReceiveMessageFromPatientWithRequestModeEntity>("请等待") {
                    @Override
                    public void _onNext(ReceiveMessageFromPatientWithRequestModeEntity entity) {
                        if (entity == null) {
                            ToastUtil.show("请检查网络");
                            return;
                        }
                        // 如果成功了，就跳转视频问诊。
                        if (entity.data.success){
                            start(VideoConsultFragment.newInstance(consultId,
                                    patientName,
                                    doctorId,
                                    doctorName,
                                    true));
                        }else{
                            ToastUtil.show("向医生发送消息失败，请重试去问诊");
                        }
                    }
                });
    }

    @SingleClick
    @OnClick({R.id.btn_cancel, R.id.btn_inquiry})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                showSimpleConfirmDialog("consults");
                break;
            case R.id.btn_inquiry:
                // 如果进入的是去问诊，则调整状态
                GlobalConfig.isIntoVideoFromOrder = true;
                // 跳视频问诊
                // 动态输入参数
                if (!TextUtils.isEmpty(patientName))
                    requestReceiveMessageFromPatientWithRequestMode();
                break;
            default:
                break;
        }
    }

    /**
     * 显示对话框
     */
    private void showSimpleConfirmDialog(String opflag) {
        ShineButtonDialog dialog = new ShineButtonDialog(this.mContext);
        if (opflag.contains("consults")){
            dialog.tv_title_tip.setText("取消挂号订单");
            dialog.tv_content_tip.setText("取消后问诊需重新挂号，是否确认取消");
        }
        dialog.btn_inquiry.setOnClickListener(v -> {
            dialog.dismiss();
            if (opflag.contains("consults")){
                requestCancelConsult(String.valueOf(consultId));
            }
        });
        dialog.btn_cancel.setOnClickListener(v -> dialog.dismiss());
        dialog.iv_close.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    /**
     * 取消待支付挂号单
     * @param consultId 挂号单id
     */
    public void requestCancelConsult(String consultId) {
        ApiRepository.getInstance().patientCancelGraphicTextConsult(consultId)
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastLoadingObserver<CancelregisterResultEntity>("请稍后...") {
                    @Override
                    public void _onNext(CancelregisterResultEntity entity) {
                        if (entity == null) {
                            ToastUtil.show("请检查网络");
                            return;
                        }
                        if (entity.getData().isSuccess()){
                            if (entity.data.jsonResponseBean.body)
                                start(ResultFragment.newInstance("cancelConsult"));
                            else
                                ToastUtil.show("取消失败，请重试");
                        }else {
                            ToastUtil.showWarning("取消失败: "+entity.data.errorMessage);
                        }
                    }
                });
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {

    }
}
