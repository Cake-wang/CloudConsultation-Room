package com.aries.template.module.mine;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aries.template.R;
import com.aries.template.module.base.BaseEventFragment;
import com.aries.ui.view.title.TitleBarView;
import com.decard.NDKMethod.BasicOper;

import java.io.UnsupportedEncodingException;

import androidx.annotation.Nullable;
import butterknife.BindView;
import me.yokeyword.fragmentation.ISupportFragment;

/**
 * 显示结果页
 * 根据传入的信息，变化各种文字和图片信息的结果页
 *
 * @author louisluo
 * @Author: AriesHoo on 2018/7/13 17:09
 * @E-Mail: AriesHoo@126.com
 * @Function: 我的
 * @Description:
 */
public class ResultFragment extends BaseEventFragment implements ISupportFragment {
    private  String result= "";

    @BindView(R.id.tv_result_title)
    TextView tv_result_title;
    @BindView(R.id.tv_result_bg)
    ImageView tv_result_bg;
    @BindView(R.id.tv_result_tip)
    TextView tv_result_tip;
    @BindView(R.id.tv_result_contet)
    TextView tv_result_contet;
    @BindView(R.id.tv_result_code)
    TextView tv_result_code;



    public static ResultFragment newInstance(String result) {
        Bundle args = new Bundle();
        ResultFragment fragment = new ResultFragment();
        args.putString("result",result);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            result = args.getString("result");
        }
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_result;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setBgColor(Color.WHITE)
                .setTitleMainText(R.string.mine);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (result.contains("cancelConsult")){
             tv_result_title.setText("取消成功");
            tv_result_bg.setBackgroundResource(R.drawable.bg_suc_yzs);
             tv_result_tip.setText("取消成功");
             tv_result_contet.setText("您的结算单已取消，如需开药请再次发起问诊");
            tv_result_code.setVisibility(View.GONE);
        }else  if (result.contains("paySuc")){
            tv_result_title.setText("支付成功");
            tv_result_bg.setBackgroundResource(R.drawable.bg_pay_suc_yzs);
            tv_result_tip.setText("支付成功");
            tv_result_contet.setText("请取走凭条，凭取药码至药柜取药");
            tv_result_code.setVisibility(View.VISIBLE);
        }else {
            tv_result_title.setText("支付失败");
            tv_result_bg.setBackgroundResource(R.drawable.bg_fail_yzs);
            tv_result_tip.setText("支付失败");
            tv_result_contet.setText("当前药品库存不足");
            tv_result_code.setVisibility(View.GONE);
        }

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                /**
//                 *要执行的操作
//                 */
//                openSerialport();
//            }
//        }, 500);//3秒后执行Runnable中的run方法
//        mCountDownHelper = new CountDownButtonHelper(btnGetVerifyCode, 60);
//
////        SettingSPUtils spUtils = SettingSPUtils.getInstance();
//
//        cbProtocol.setOnCheckedChangeListener((buttonView, isChecked) -> {
////            spUtils.setIsAgreePrivacy(isChecked);
//            ViewUtils.setEnabled(btnLogin, isChecked);
//        });
//        ViewUtils.setEnabled(btnLogin, false);
//        ViewUtils.setChecked(cbProtocol, false);
    }

    @Override
    public void loadData() {
        if (result.contains("paySuc")){
            //打印机参数设置
             String setPrint = BasicOper.dc_setprint(0x02, 0x01, 0, 0, 10, 0x00);
             Log.d("print", "BasicOper.dc_setprint:" + setPrint);
            // 打印机进纸设置
             String enter = BasicOper.dc_printenter(50);
             Log.d("print", "BasicOper.dc_printenter:" + enter);

            String printString = "取药码：888888";
            try {
                byte[] strByte = printString.getBytes("GBK");
                //打印字符
                 String print_char = BasicOper.dc_printcharacter(strByte);
                 Log.d("print", "BasicOper.dc_printcharacter:" + print_char);
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }


}
