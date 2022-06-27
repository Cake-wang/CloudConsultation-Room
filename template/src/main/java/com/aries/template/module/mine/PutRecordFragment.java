package com.aries.template.module.mine;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.util.SPUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.template.FakeDataExample;
import com.aries.template.GlobalConfig;
import com.aries.template.R;
import com.aries.template.entity.AuthCodeResultEntity;
import com.aries.template.entity.RegisterResultEntity;
import com.aries.template.module.base.BaseEventFragment;
import com.aries.template.retrofit.repository.ApiRepository;
import com.aries.ui.view.title.TitleBarView;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.utils.CountDownButtonHelper;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import me.yokeyword.fragmentation.ISupportFragment;

import static com.aries.template.utils.RegUtils.isMobileNO;
import static com.aries.template.utils.RegUtils.isVerifyCode;

/**
 * 手机注册界面
 * @Author: AriesHoo on 2018/7/13 17:09
 * @E-Mail: AriesHoo@126.com
 * @Function: 我的
 * @Description:
 */
public class PutRecordFragment extends BaseEventFragment implements ISupportFragment {
    // 外部传入数据
    private  String idCard= "";
    private  String name = "";
    private  String smkcard= "";

    // 内部数据
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
    public static PutRecordFragment newInstance(String idCard,String name,String smkcard) {
        Bundle args = new Bundle();
        PutRecordFragment fragment = new PutRecordFragment();
        args.putString("idCard",idCard);
        args.putString("name",name);
        args.putString("smkcard",smkcard);
        fragment.setArguments(args);
        return fragment;
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
    }

    @SingleClick
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
                if (isMobileNO(etPhoneNumber.getEditableText().toString().trim())) {
                    if (isVerifyCode(etVerifyCode.getEditableText().toString().trim())) {
                        if(cbProtocol.isChecked()){
                            loginByVerifyCode(etPhoneNumber.getEditableText().toString().trim(), etVerifyCode.getEditableText().toString().trim());
                        }else {
                            new MaterialDialog.Builder(getContext())
                                    .content(R.string.tip_next_register)
                                    .positiveText(R.string.lab_yes)
                                    .negativeText(R.string.lab_no)
                                    .onPositive((dialog, which) ->  loginByVerifyCode(etPhoneNumber.getEditableText().toString().trim(), etVerifyCode.getEditableText().toString().trim()))
                                    .show();
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
                    etPhoneNumber.setText(null);
                }
                if (focusFlagcode){
                    etVerifyCode.setText(null);
                }
                break;
            case R.id.btn_back_text:
                // 退格按钮
                if (focusFlagnum){
                    if(etPhoneNumber.getText().toString().trim().length()>1) {
                        String str0  = etPhoneNumber.getText().toString().trim().substring(0, etPhoneNumber.getText().toString().trim().length() - 1);
                        etPhoneNumber.setText(str0);
                    }else {
                        etPhoneNumber.setText(null);
                    }
                }
                if (focusFlagcode){
                    if(etVerifyCode.getText().toString().trim().length()>1) {
                        String str0   = etVerifyCode.getText().toString().trim().substring(0, etVerifyCode.getText().toString().trim().length() - 1);
                        etVerifyCode.setText(str0);
                    }else {
                        etVerifyCode.setText(null);
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
                break;
//            case R.id.tv_forget_password:
//                XToastUtils.info("忘记密码");
//                break;
//            case R.id.tv_user_protocol:
//                openPage(ServiceProtocolFragment.class, KEY_PROTOCOL_TITLE, ResUtils.getString(R.string.title_user_protocol));
//                break;
//            case R.id.tv_privacy_protocol:
//                openPage(ServiceProtocolFragment.class, KEY_PROTOCOL_TITLE, ResUtils.getString(R.string.title_privacy_protocol));
//                break;
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
            String str9 = etPhoneNumber.getText().toString().trim();
            str9 += inputNum;
            etPhoneNumber.setText(str9);
        }
        if (focusFlagcode){
            String str9 = etVerifyCode.getText().toString().trim();
            str9 += inputNum;
            etVerifyCode.setText(str9);
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
    }

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
                                    //todo 向全局注入数据
                                    String tag = (String) SPUtil.get(mContext,"tag","fzpy");
                                    SPUtil.put(mContext,"mobile",mobile);
                                    if(tag.contains("stjc")){
                                            Intent intent = new Intent(Intent.ACTION_MAIN);
                                            /**知道要跳转应用的包命与目标Activity*/
                                            ComponentName componentName = new ComponentName("com.garea.launcher", "com.garea.launcher.login.LauncherLogin");
                                            intent.setComponent(componentName);
                                            intent.putExtra("userName", name);//这里Intent传值
                                            intent.putExtra("idCard", idCard);
                                            intent.putExtra("mobile",mobile);
                                            startActivity(intent);
                                    }else {
                                        //判断有挂号或处方
                                        start(DepartmentFragment.newInstance(new Object()));
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
                        if (entity.success){
                            // 先获取验证码，再执行注册
                            authCodeId = entity.data.authCodeId;
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        if (mCountDownHelper != null) {
            mCountDownHelper.recycle();
        }
        super.onDestroyView();
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setBgColor(Color.WHITE)
                .setTitleMainText(R.string.mine);
    }

}
