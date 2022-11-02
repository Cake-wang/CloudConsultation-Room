package com.aries.template.module.mine;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.util.ToastUtil;
import com.aries.template.GlobalConfig;
import com.aries.template.R;
import com.aries.template.entity.TopexampageResultEntity;
import com.aries.template.module.base.BaseEventFragment;
import com.aries.template.retrofit.repository.ApiRepository;
import com.aries.ui.view.title.TitleBarView;
import com.decard.NDKMethod.BasicOper;
import com.trello.rxlifecycle3.android.FragmentEvent;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
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
    private  boolean isFirstTimePrint=true; // 第一次打印判定，如果不是，则释放资源. true 为第一次

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
            tv_result_bg.setBackgroundResource(R.mipmap.bg_suc_yzs);
             tv_result_tip.setText("取消成功");
             tv_result_contet.setText("您的结算单已取消，如需开药请再次发起问诊");
            tv_result_code.setVisibility(View.GONE);
        }else  if (result.contains("paySuc")){
            tv_result_title.setText("支付成功");
            tv_result_bg.setBackgroundResource(R.mipmap.bg_pay_suc_yzs);
            tv_result_tip.setText("支付成功");
            tv_result_contet.setText("请取走凭条，凭取药码至药柜取药");
            tv_result_code.setVisibility(View.VISIBLE);
            tv_result_code.setText("取号码"+takeCode);
        }else {
            tv_result_title.setText("支付失败");
            tv_result_bg.setBackgroundResource(R.mipmap.bg_fail_yzs);
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

    public void printCode(){
        String printContent  = "123685";
//        Log.d("111111MODEL", Build.MODEL);
        //打开端口，usb模式，打开之前必须确保已经获取到USB权限，返回值为设备句柄号。
        int devHandle = BasicOper.dc_open("AUSB",getActivity(),"",0);
//        Log.d("111111MODEL", devHandle+"");
        if(devHandle>0){
//            Log.d("open","dc_open success devHandle = "+devHandle);
            if (!TextUtils.isEmpty(printContent)){
                //打印机参数设置
                String setPrint = BasicOper.dc_setprint(0x02, 0x01, 0, 0, 10, 0x00);
//                Log.d("print", "BasicOper.dc_setprint:" + setPrint);
                // 打印机进纸设置
                String enter = BasicOper.dc_printenter(50);
//                Log.d("print", "BasicOper.dc_printenter:" + enter);

                // 打印基础信息
                // 姓名
                // 卡号
                // 机器编号
                try {
                    String name = "姓名：王郭亮";
                    String cardNum= "卡号：W30103722";
                    String machineId= "设备号：widdnidvjvfkv";
//                    byte[] strByte = printString.getBytes("GBK");
                    //打印字符
                    BasicOper.dc_printcharacter(name.getBytes("GBK"));
                    BasicOper.dc_printcharacter(cardNum.getBytes("GBK"));
                    BasicOper.dc_printcharacter(machineId.getBytes("GBK"));
//                    Log.d("print", "BasicOper.dc_printcharacter:" + name);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                // 取药码
                String printString = "取药码："+printContent;
                try {
                    byte[] strByte = printString.getBytes("GBK");
                    //打印字符
                    String print_char = BasicOper.dc_printcharacter(strByte);
//                    Log.d("print", "BasicOper.dc_printcharacter:" + print_char);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                //一维码内容
                String strTemp = printContent;
                try {
                    byte[] byteTemp = strTemp.getBytes("GBK");
                    //打印一维码
                    String temp = BasicOper.dc_printOnedimensional(50, 0x01, 0x01, byteTemp);
//                    Log.d("print", "PrintLineByLinePicture:" + temp);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                // 处方用量
                String[] split = {"铝碳酸镁咀嚼片（1天1.0次，每次10片）","阿司匹林（1天1.0次，每次10片）","氯雷他定（1天1.0次，每次10片）"};
                try {
                    BasicOper.dc_printcharacter("处方信息：".getBytes("GBK"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                for (String str : split) {
                    try {
                        byte[] strByte = str.getBytes("GBK");
                        //打印字符
                        String print_char = BasicOper.dc_printcharacter(strByte);
//                    Log.d("print", "BasicOper.dc_printcharacter:" + print_char);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

                //打印空白
                try {
                    // 使用系统换行符
                    String strBlank = " ";
                    byte[] byteTemp = strBlank.getBytes("GBK");
                    //打印一维码
                    BasicOper.dc_printcharacter(byteTemp);
                    BasicOper.dc_printcharacter(byteTemp);
                    BasicOper.dc_printcharacter(byteTemp);
//                    Log.d("print", "PrintLineByLinePicture:" + byteTemp);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 打印取药码
     * 先打开设备，上电
     * 再打印
     */
    public void printCode(String printContent){
//        Log.d("111111MODEL", Build.MODEL);
        //打开端口，usb模式，打开之前必须确保已经获取到USB权限，返回值为设备句柄号。
        int devHandle = BasicOper.dc_open("AUSB",getActivity(),"",0);
//        Log.d("111111MODEL", devHandle+"");
        if(devHandle>0){
//            Log.d("open","dc_open success devHandle = "+devHandle);
            if (!TextUtils.isEmpty(printContent)){
                //打印机参数设置
                String setPrint = BasicOper.dc_setprint(0x02, 0x01, 0, 0, 10, 0x00);
//                Log.d("print", "BasicOper.dc_setprint:" + setPrint);
                // 打印机进纸设置
                String enter = BasicOper.dc_printenter(50);
//                Log.d("print", "BasicOper.dc_printenter:" + enter);

                // 打印基础信息
                // 姓名
                // 卡号
                // 机器编号
                try {
                    String name = "姓名："+GlobalConfig.ssCard.getName();
                    String cardNum= "卡号："+GlobalConfig.ssCard.getCardNum();
                    String machineId= "设备号："+GlobalConfig.machineId;
//                    byte[] strByte = printString.getBytes("GBK");
                    //打印字符
                    BasicOper.dc_printcharacter(name.getBytes("GBK"));
                    BasicOper.dc_printcharacter(cardNum.getBytes("GBK"));
                    BasicOper.dc_printcharacter(machineId.getBytes("GBK"));
//                    Log.d("print", "BasicOper.dc_printcharacter:" + name);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                // 取药码
                String printString = "取药码："+printContent;
                try {
                    byte[] strByte = printString.getBytes("GBK");
                    //打印字符
                    String print_char = BasicOper.dc_printcharacter(strByte);
//                    Log.d("print", "BasicOper.dc_printcharacter:" + print_char);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                //一维码内容
                String strTemp = printContent;
                try {
                    byte[] byteTemp = strTemp.getBytes("GBK");
                    //打印一维码
                    String temp = BasicOper.dc_printOnedimensional(50, 0x01, 0x01, byteTemp);
//                    Log.d("print", "PrintLineByLinePicture:" + temp);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                // 处方用量
                String[] split = stuckUse.split("&&");
                try {
                    BasicOper.dc_printcharacter("处方信息：".getBytes("GBK"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                for (String str : split) {
                    try {
                        byte[] strByte = str.getBytes("GBK");
                        //打印字符
                    String print_char = BasicOper.dc_printcharacter(strByte);
//                    Log.d("print", "BasicOper.dc_printcharacter:" + print_char);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

                //打印空白
                try {
                    // 使用系统换行符
                    String strBlank = " ";
                    byte[] byteTemp = strBlank.getBytes("GBK");
                    //打印一维码
                    BasicOper.dc_printcharacter(byteTemp);
                    BasicOper.dc_printcharacter(byteTemp);
                    BasicOper.dc_printcharacter(byteTemp);
//                    Log.d("print", "PrintLineByLinePicture:" + byteTemp);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void loadData() {
        if (GlobalConfig.thirdFactory.equals("3")){
            // 处方用量
            String[] split = stuckUse.split("&&");
            ApiRepository.getInstance().printcode(takeCode,split)
                    .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                    .subscribe(new FastLoadingObserver<TopexampageResultEntity>() {
                        @Override
                        public void _onNext(TopexampageResultEntity entity) {
                            if (entity == null) {
                                ToastUtil.show("请检查网络");
                                return;
                            }
                            try {
                                if (entity.success){



                                }else {
                                    ToastUtil.show(entity.getMessage());
                                }
                            }catch (Exception e){
                                ToastUtil.show("打印小票异常");
                                e.printStackTrace();
                            }
                        }
                    });

        }else {
            timeLoop();
        }

    }

    private static final int PERIOD = 6* 1000;
    private static final int DELAY = 1*1000;
    private Disposable mDisposable;

    /**
     * 定时循环任务
     */
    private void timeLoop() {
        mDisposable = Observable.interval(DELAY, PERIOD, TimeUnit.MILLISECONDS)
                .map((aLong -> aLong + 1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    // 如果从处方单支付过来
                    if (isFirstTimePrint){
//                        result = "paySuc";
                        if (result.contains("paySuc")){
                            // 打印 取药码 一维码
//                            printCode();
                            printCode(takeCode);
                        }
                        isFirstTimePrint = false;

                        if (mDisposable != null) {
                            mDisposable.dispose();
                            mDisposable = null;
                        }
                    }else {
                        if (mDisposable != null) {
                            mDisposable.dispose();
                            mDisposable = null;
                        }
                    }
                });//getUnreadCount()执行的任务
    }
}
