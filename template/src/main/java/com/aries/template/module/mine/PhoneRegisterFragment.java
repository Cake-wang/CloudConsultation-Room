package com.aries.template.module.mine;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.util.SPUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.template.GlobalConfig;
import com.aries.template.MainActivity;
import com.aries.template.R;
import com.aries.template.WebViewActivity;
import com.aries.template.entity.AuthCodeResultEntity;
import com.aries.template.entity.RegisterResultEntity;
import com.aries.template.module.base.BaseEventFragment;
import com.aries.template.module.main.HomeFragment;
import com.aries.template.retrofit.repository.ApiRepository;
import com.aries.template.thridapp.JTJKThirdAppUtil;
import com.aries.template.utils.DefenceUtil;
import com.aries.ui.view.title.TitleBarView;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.xuexiang.xui.utils.CountDownButtonHelper;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportFragment;

import static com.aries.template.utils.RegUtils.isMobileNO;
import static com.aries.template.utils.RegUtils.isVerifyCode;

/**
 * 手机注册界面
 * @Author: AriesHoo on 2018/7/13 17:09
 * @E-Mail: AriesHoo@126.com
 * @Function: 我的
 * @Description:
 */
public class PhoneRegisterFragment extends BaseEventFragment implements ISupportFragment {
    // 外部传入数据
    /** 身份证 */
    private  String idCard= "";
    /** 姓名 */
    private  String name = "";
    /** 市民卡 */
    private  String smkcard= "";

    // 内部数据
    /** 验证码 id */
    private  String authCodeId= "";

    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;
    @BindView(R.id.et_verify_code)
    EditText etVerifyCode;
    @BindView(R.id.btn_get_verify_code)
    RoundButton btnGetVerifyCode;
    @BindView(R.id.cb_register_protocol)
    AppCompatCheckBox cbProtocol;
    @BindView(R.id.btn_register)
    View btnLogin;

    /** 获取验证码的倒计时插件 */
    private CountDownButtonHelper mCountDownHelper;
    /** 当前手机号输入框的焦点状态 */
    boolean focusFlagnum ;
    /** 当前验证码输入框的焦点状态 */
    boolean focusFlagcode ;

    /**
     * 获取输入数据
     * @param idCard 身份证
     * @param name 姓名
     * @param smkcard 市民卡
     */
    public static PhoneRegisterFragment newInstance(String idCard, String name, String smkcard) {
        Bundle args = new Bundle();
        PhoneRegisterFragment fragment = new PhoneRegisterFragment();
        args.putString("idCard",idCard);
        args.putString("name",name);
        args.putString("smkcard",smkcard);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        super.beforeInitView(savedInstanceState);
//        WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
//        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE;
//        getActivity().getWindow().setAttributes(params);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            idCard = args.getString("idCard");
            name = args.getString("name");
            smkcard = args.getString("smkcard");
        }
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_putrc;
    }

    /**
     * 初始化设置
     */
    @Override
    public void initView(Bundle savedInstanceState) {
        mCountDownHelper = new CountDownButtonHelper(btnGetVerifyCode, 60);
        // 缩回软键盘
        hideSoftInput();
        // 去焦点
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            etPhoneNumber.setShowSoftInputOnFocus(false);//设置获取焦点后，不弹出键盘
            etVerifyCode.setShowSoftInputOnFocus(false);//设置获取焦点后，不弹出键盘
        }
        // 手机输入框
        etPhoneNumber.setOnFocusChangeListener((v, hasFocus) -> focusFlagnum = hasFocus);
        // 验证码输入框
        etVerifyCode.setOnFocusChangeListener((v, hasFocus) -> focusFlagcode = hasFocus);
        // 增加焦点
        etPhoneNumber.requestFocus();
    }

    @OnClick({R.id.btn_get_verify_code,R.id.btn_register, R.id.tv_other_login, R.id.tv_forget_password,
            R.id.num_1, R.id.num_2, R.id.num_3, R.id.num_4,
            R.id.num_5, R.id.num_6, R.id.num_7, R.id.num_8,
            R.id.num_9, R.id.num_0, R.id.btn_clear, R.id.btn_back_text,
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_get_verify_code:
                // 获取验证码
                if (isMobileNO(etPhoneNumber.getEditableText().toString().trim())) {
                    // 启动验证码倒计时
                    mCountDownHelper.start();
                    requestAuthCode(etPhoneNumber.getEditableText().toString().trim());
                }else {
                    ToastUtil.show("手机号格式错误");
                }
                break;
            case R.id.btn_register:
                if (!DefenceUtil.checkReSubmit("PutRecordFragment.btn_register"))
                    return;
                if (isMobileNO(etPhoneNumber.getEditableText().toString().trim())) {
                    if (isVerifyCode(etVerifyCode.getEditableText().toString().trim())) {
                        if(cbProtocol.isChecked()){
                            loginByVerifyCode(etPhoneNumber.getEditableText().toString().trim(), etVerifyCode.getEditableText().toString().trim());
                        }else {

//                            AlertDialog dialog = new AlertDialog.Builder(mContext)
////                                    .setIcon(R.mipmap.icon)//设置标题的图片
//                                    .setTitle("提示")//设置对话框的标题
//                                    .setMessage(R.string.tip_next_register)//设置对话框的内容
//                                    //设置对话框的按钮
//                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
////                                            Toast.makeText(MainActivity.this, "点击了取消按钮", Toast.LENGTH_SHORT).show();
//                                            dialog.dismiss();
//                                        }
//                                    })
//                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
////                                            Toast.makeText(MainActivity.this, "点击了确定的按钮", Toast.LENGTH_SHORT).show();
//                                            loginByVerifyCode(etPhoneNumber.getEditableText().toString().trim(), etVerifyCode.getEditableText().toString().trim());
//                                            dialog.dismiss();
//                                        }
//                                    }).create();
//                            dialog.show();


                            new MaterialDialog.Builder(getContext())
                                    .content(R.string.tip_next_register)
                                    .positiveText(R.string.lab_yes)
                                    .negativeText(R.string.lab_no)
                                    .onPositive((dialog, which) ->  loginByVerifyCode(etPhoneNumber.getEditableText().toString().trim(), etVerifyCode.getEditableText().toString().trim()))
                                    .show();
//                            WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
//                            params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE;
//                            getActivity().getWindow().setAttributes(params);
                        }
                    }else{
                        ToastUtil.show("验证码必须是6位的");
                    }
                }else{
                    ToastUtil.show("手机号格式错误");
                }
                break;
            case R.id.btn_clear:
                // 清除按钮
                if (focusFlagnum){
                    clearText(etPhoneNumber);
//                    etPhoneNumber.setText(null);
                }
                if (focusFlagcode){
                    clearText(etVerifyCode);
//                    etVerifyCode.setText(null);
                }
                break;
            case R.id.btn_back_text:
                // 退格按钮
                if (focusFlagnum){
                    //删除选中状态的字符串（文本多选状态下的删除）
                    if (etPhoneNumber.hasSelection()){
                        deleteSelection(etPhoneNumber);
                        return;
                    }
                    //删除光标处单个字符
                    String number = getEditTextViewString(etPhoneNumber);
                    if (!(number.equals(null)) && !(number.trim().equals(""))){
                        int index = getEditSelection(etPhoneNumber);//获取光标位置
                        //防止光标处于起始位置时，点击删除按钮，程序自动退出
                        if (index > 0){
                            deleteEditValue(index,etPhoneNumber);
                        }
                        return;
                    }
                }
                if (focusFlagcode){
                    //删除选中状态的字符串（文本多选状态下的删除）
                    if (etVerifyCode.hasSelection()){
                        deleteSelection(etVerifyCode);
                        return;
                    }
                    //删除光标处单个字符
                    String number = getEditTextViewString(etVerifyCode);
                    if (!(number.equals(null)) && !(number.trim().equals(""))){
                        int index = getEditSelection(etVerifyCode);//获取光标位置
                        //防止光标处于起始位置时，点击删除按钮，程序自动退出
                        if (index > 0){
                            deleteEditValue(index,etVerifyCode);
                        }
                        return;
                    }
                }
                break;
            case R.id.num_0:
                enterNumber(0);
                break;
            case R.id.num_1:
                enterNumber(1);
                break;
            case R.id.num_2:
                enterNumber(2);
                break;
            case R.id.num_3:
                enterNumber(3);
                break;
            case R.id.num_4:
                enterNumber(4);
                break;
            case R.id.num_5:
                enterNumber(5);
                break;
            case R.id.num_6:
                enterNumber(6);
                break;
            case R.id.num_7:
                enterNumber(7);
                break;
            case R.id.num_8:
                enterNumber(8);
                break;
            case R.id.num_9:
                enterNumber(9);
                break;
            case R.id.tv_other_login:
                // 点击协议
                WebViewActivity.start(getActivity(),"https://www.hfi-health.com:28181/agreement/yzs-ysxy.html");
                break;
            default:
                break;
        }
    }

    /**
     * 向当前焦点的目标输入数据
     * 密码输入框
     * 验证码输入框
     */
    public void enterNumber(int inputNum){
        if (focusFlagnum){
            addNumber(inputNum+"",etPhoneNumber);
        }
        if (focusFlagcode){
            addNumber(inputNum+"",etVerifyCode);
        }
    }

    /**
     * 根据验证码登录
     *
     * @param phoneNumber 手机号
     * @param verifyCode  验证码
     */
    private void loginByVerifyCode(String phoneNumber, String verifyCode) {
        if (!TextUtils.isEmpty(authCodeId)){
            requestRegister(idCard,name,phoneNumber,verifyCode,authCodeId);
        }else{
            ToastUtil.show("请先获取验证码");
        }
//        requestRegister(idCard,name,phoneNumber,verifyCode,"123456"); //todo cc
    }

    Handler   handler;

    /**
     * 请求注册
     * 478465
     */
    public void requestRegister(String idCard,String name,String mobile,String authCode,String authCodeId) {
        timeCount = 120;
        ApiRepository.getInstance().register(idCard, name, mobile, authCode, authCodeId)
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastLoadingObserver<RegisterResultEntity>("请稍后...") {
                            @Override
                            public void _onNext(@NonNull RegisterResultEntity entity) {
                                if (entity == null) {
                                    ToastUtil.show("请检查网络");
                                    return;
                                }
                                if (entity.success){
                                    // 向全局注入数据
                                    String tag = (String) SPUtil.get(mContext,"tag","fzpy");
                                    SPUtil.put(mContext,"mobile",mobile);
                                    GlobalConfig.mobile = mobile;
                                    if(tag.contains("stjc")){

                                        if (GlobalConfig.thirdFactory.equals("3")){

                                            ((MainActivity)getActivity()).gotoYYPJ();

                                        }else {
                                            // 启动第三方跳转
                                            if (!TextUtils.isEmpty(GlobalConfig.factoryResource)){
                                                start(HomeFragment.newInstance(), SupportFragment.SINGLETASK);
                                                handler = new Handler();
                                                handler.postDelayed(() -> {
                                                    //
                                                    new JTJKThirdAppUtil().gotoBodyTesting(getActivity(),
                                                            GlobalConfig.factoryResource,
                                                            GlobalConfig.factoryMainPage,
                                                            name,
                                                            idCard,
                                                            mobile);
                                                }, 500);//3秒后执行Runnable中的run方法

                                            }else {
                                                ToastUtil.show("请您移步到旁边的健康管理设备进行检测");
                                            }
                                        }

//                                            Intent intent = new Intent(Intent.ACTION_MAIN);
//                                            /**知道要跳转应用的包命与目标Activity*/
//                                            ComponentName componentName = new ComponentName("com.garea.launcher", "com.garea.launcher.login.LauncherLogin");
//                                            intent.setComponent(componentName);
//                                            intent.putExtra("userName", name);//这里Intent传值
//                                            intent.putExtra("idCard", idCard);
//                                            intent.putExtra("mobile",mobile);
//                                            startActivity(intent);
                                    }else {
                                        //判断有挂号或处方
                                        // 返回读卡，重新返回 tid 数据
                                        start(MineCardFragment.newInstance("fzpy"));
                                    }
                                }else {
                                    ToastUtil.show(entity.message);
                                }
                            }
                        });
    }

    /**
     * 通过注册手机号获得验证码
     * @param phoneNumber 手机
     */
    public void requestAuthCode(String phoneNumber){
        ApiRepository.getInstance().authCode(phoneNumber)
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastLoadingObserver<AuthCodeResultEntity>("请稍后...") {
                    @Override
                    public void _onNext(@NonNull AuthCodeResultEntity entity) {
                        if (entity == null) {
                            ToastUtil.show("请检查网络");
                            return;
                        }
                        try {
                            if (entity.success){
                                // 先获取验证码，再执行注册
                                authCodeId = entity.data.authCodeId;
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        if (mCountDownHelper != null) {
            mCountDownHelper.recycle();
        }
        if (handler!=null){
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        super.onDestroyView();
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setBgColor(Color.WHITE)
                .setTitleMainText(R.string.mine);
    }

    //获取光标当前位置
    public int getEditSelection(EditText et){
        return et.getSelectionStart();
    }
    //获取文本框的内容
    public String getEditTextViewString(EditText et){
        return et.getText().toString();
    }
    //清楚文本框中的内容
    public void clearText(EditText et){
        et.getText().clear();
    }
    //向文本框指定位置添加内容
    public void addNumber(String str,EditText et){
        int index = getEditSelection(et);
        if (index < 0 || index >= getEditTextViewString(et).length()){
            et.append(str);
        }else {
            et.getEditableText().insert(index,str);
        }
    }
    //删除指定位置的单个字符
    public void deleteEditValue(int index,EditText et){
        et.getText().delete(index-1,index);
    }
    //删除选中状态的多个字符串
    public void deleteSelection(EditText et){
        et.getText().delete(et.getSelectionStart(),et.getSelectionEnd());
    }


}
