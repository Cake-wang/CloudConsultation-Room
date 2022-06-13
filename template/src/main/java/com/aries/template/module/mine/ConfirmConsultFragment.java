package com.aries.template.module.mine;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.aries.library.fast.util.SPUtil;
import com.aries.template.R;
import com.aries.template.module.base.BaseEventFragment;
import com.aries.ui.view.title.TitleBarView;
import com.xuexiang.xui.widget.flowlayout.FlowTagLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import butterknife.BindView;

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

    Integer returnVisitStatus = 0;

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
