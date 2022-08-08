package com.aries.template.module.mine;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
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
 * 注意，打印不能使用 换行符 \n。 可能出现无限打印的情况, 必须使用 \r\n
 * @author louisluo
 * @Author: AriesHoo on 2018/7/13 17:09
 * @E-Mail: AriesHoo@126.com
 * @Function: 我的
 * @Description:
 */
public class ResultFragment extends BaseEventFragment implements ISupportFragment {
    private  String result= "";
    private  String takeCode= ""; // 取药码
    private  String stuckUse= ""; // 药物用量

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
        if (result.contains("paySuc"))
            // "data": "{\"orderNo\":\"\",\"takeCode\":\"34811555\"}" 案例
            try {
                if (result.contains(":")){
                    String[] ss = result.split(":");
                    takeCode = ss[1];
                    stuckUse = ss[2];
//                    stuckUse = ss[2].replace("&&","\r\n");
                }
            }catch (Exception e){
                e.printStackTrace();
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
            tv_result_code.setText("取号码"+takeCode);
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

    /**
     * 打印取药码
     * 先打开设备，上电
     * 再打印
     */
    public void printCode(String printContent){
        Log.d("111111MODEL", Build.MODEL);
        //打开端口，usb模式，打开之前必须确保已经获取到USB权限，返回值为设备句柄号。
        int devHandle = BasicOper.dc_open("AUSB",getActivity(),"",0);
        Log.d("111111MODEL", devHandle+"");
        if(devHandle>0){
            Log.d("open","dc_open success devHandle = "+devHandle);
            if (!TextUtils.isEmpty(printContent)){
                //打印机参数设置
                String setPrint = BasicOper.dc_setprint(0x02, 0x01, 0, 0, 10, 0x00);
                Log.d("print", "BasicOper.dc_setprint:" + setPrint);
                // 打印机进纸设置
                String enter = BasicOper.dc_printenter(50);
                Log.d("print", "BasicOper.dc_printenter:" + enter);

                // 取药码
                String printString = "取药码："+printContent;
                try {
                    byte[] strByte = printString.getBytes("GBK");
                    //打印字符
                    String print_char = BasicOper.dc_printcharacter(strByte);
                    Log.d("print", "BasicOper.dc_printcharacter:" + print_char);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                //一维码内容
                String strTemp = printContent;
                try {
                    byte[] byteTemp = strTemp.getBytes("GBK");
                    //打印一维码
                    String temp = BasicOper.dc_printOnedimensional(50, 0x01, 0x01, byteTemp);
                    Log.d("print", "PrintLineByLinePicture:" + temp);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                // 用量
                String[] split = stuckUse.split("&&");
                for (String str : split) {
                    try {
                        byte[] strByte = str.getBytes("GBK");
                        //打印字符
                    String print_char = BasicOper.dc_printcharacter(strByte);
                    Log.d("print", "BasicOper.dc_printcharacter:" + print_char);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void loadData() {
        // 如果从处方单支付过来
        if (result.contains("paySuc")){
            // 打印 取药码 一维码
            printCode(takeCode);
        }
    }
}
