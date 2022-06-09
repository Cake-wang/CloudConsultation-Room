package com.aries.template.module.mine;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.EditText;

import com.aries.library.fast.module.fragment.FastTitleFragment;
import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.retrofit.FastObserver;
import com.aries.library.fast.util.ToastUtil;
import com.aries.template.R;
import com.aries.template.entity.RegisterResultEntity;
import com.aries.template.retrofit.repository.ApiRepository;
import com.aries.ui.view.title.TitleBarView;
import com.decard.NDKMethod.BasicOper;
import com.decard.NDKMethod.EGovernment;
import com.decard.entitys.SSCard;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.utils.CountDownButtonHelper;
import com.xuexiang.xui.utils.ViewUtils;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.textview.supertextview.SuperButton;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.yokeyword.fragmentation.ExtraTransaction;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportFragmentDelegate;
import me.yokeyword.fragmentation.SupportHelper;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * @Author: AriesHoo on 2018/7/13 17:09
 * @E-Mail: AriesHoo@126.com
 * @Function: 我的
 * @Description:
 */
public class PutRecordFragment extends FastTitleFragment implements ISupportFragment {
    private  String idCard= "",name= "",smkcard = "";

    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;
    @BindView(R.id.et_verify_code)
    EditText etVerifyCode;
    @BindView(R.id.btn_get_verify_code)
    RoundButton btnGetVerifyCode;

    @BindView(R.id.cb_protocol)
    CheckBox cbProtocol;
    @BindView(R.id.btn_login)
    SuperButton btnLogin;

    private CountDownButtonHelper mCountDownHelper;
    boolean focusFlagnum ;

    boolean focusFlagcode ;

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
        mDelegate.onCreate(savedInstanceState);
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

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setBgColor(Color.WHITE)
                .setTitleMainText(R.string.mine);
    }

    @Override
    public void initView(Bundle savedInstanceState) {

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
        mCountDownHelper = new CountDownButtonHelper(btnGetVerifyCode, 60);

//        SettingSPUtils spUtils = SettingSPUtils.getInstance();

        cbProtocol.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            spUtils.setIsAgreePrivacy(isChecked);
            ViewUtils.setEnabled(btnLogin, isChecked);
        });
        ViewUtils.setEnabled(btnLogin, false);
        ViewUtils.setChecked(cbProtocol, false);

        hideSoftInput();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            etPhoneNumber.setShowSoftInputOnFocus(false);//设置获取焦点后，不弹出键盘
            etVerifyCode.setShowSoftInputOnFocus(false);//设置获取焦点后，不弹出键盘
        }

        etPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                focusFlagnum = hasFocus;
            }
        });

        etVerifyCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                focusFlagcode = hasFocus;
            }
        });

    }

    @SingleClick
    @OnClick({R.id.btn_get_verify_code, R.id.btn_login, R.id.tv_other_login, R.id.tv_forget_password,
            R.id.num_1, R.id.num_2, R.id.num_3, R.id.num_4,
            R.id.num_5, R.id.num_6, R.id.num_7, R.id.num_8,
            R.id.num_9, R.id.num_0, R.id.btn_clear, R.id.btn_back_text,
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_get_verify_code:

                if (isMobileNO(etPhoneNumber.getEditableText().toString().trim())) {
                    getVerifyCode(etPhoneNumber.getEditableText().toString().trim());
                }
                break;
            case R.id.btn_login:
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
                    }
                }
                break;
            case R.id.btn_clear:
                if (focusFlagnum){
                    etPhoneNumber.setText(null);
                }
                if (focusFlagcode){
                    etVerifyCode.setText(null);
                }

                break;
            case R.id.btn_back_text:
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
                if (focusFlagnum){
                    String str0 = etPhoneNumber.getText().toString().trim();
                    str0 += "0";
                    etPhoneNumber.setText(str0);
                }
                if (focusFlagcode){
                    String str0 = etVerifyCode.getText().toString().trim();
                    str0 += "0";
                    etVerifyCode.setText(str0);
                }

                break;
            case R.id.num_1:
                if (focusFlagnum){
                    String str1 = etPhoneNumber.getText().toString().trim();
                    str1 += "1";
                    etPhoneNumber.setText(str1);
                }
                if (focusFlagcode){
                    String str1 = etVerifyCode.getText().toString().trim();
                    str1 += "1";
                    etVerifyCode.setText(str1);
                }

                break;
            case R.id.num_2:
                if (focusFlagnum){
                    String str2 = etPhoneNumber.getText().toString().trim();
                    str2 += "2";
                    etPhoneNumber.setText(str2);
                }
                if (focusFlagcode){
                    String str2 = etVerifyCode.getText().toString().trim();
                    str2 += "2";
                    etVerifyCode.setText(str2);
                }

                break;
            case R.id.num_3:
                if (focusFlagnum){
                    String str3 = etPhoneNumber.getText().toString().trim();
                    str3 += "3";
                    etPhoneNumber.setText(str3);
                }
                if (focusFlagcode){
                    String str3 = etVerifyCode.getText().toString().trim();
                    str3 += "3";
                    etVerifyCode.setText(str3);
                }

                break;
            case R.id.num_4:
                if (focusFlagnum){
                    String str4 = etPhoneNumber.getText().toString().trim();
                    str4 += "4";
                    etPhoneNumber.setText(str4);
                }
                if (focusFlagcode){
                    String str4 = etVerifyCode.getText().toString().trim();
                    str4 += "4";
                    etVerifyCode.setText(str4);
                }

                break;
            case R.id.num_5:
                if (focusFlagnum){
                    String str5 = etPhoneNumber.getText().toString().trim();
                    str5 += "5";
                    etPhoneNumber.setText(str5);
                }
                if (focusFlagcode){
                    String str5 = etVerifyCode.getText().toString().trim();
                    str5 += "5";
                    etVerifyCode.setText(str5);
                }

                break;
            case R.id.num_6:
                if (focusFlagnum){
                    String str6 = etPhoneNumber.getText().toString().trim();
                    str6 += "6";
                    etPhoneNumber.setText(str6);
                }
                if (focusFlagcode){
                    String str6 = etVerifyCode.getText().toString().trim();
                    str6 += "6";
                    etVerifyCode.setText(str6);
                }

                break;
            case R.id.num_7:
                if (focusFlagnum){
                    String str7 = etPhoneNumber.getText().toString().trim();
                    str7 += "7";
                    etPhoneNumber.setText(str7);
                }
                if (focusFlagcode){
                    String str7 = etVerifyCode.getText().toString().trim();
                    str7 += "7";
                    etVerifyCode.setText(str7);
                }

                break;
            case R.id.num_8:
                if (focusFlagnum){
                    String str8 = etPhoneNumber.getText().toString().trim();
                    str8 += "8";
                    etPhoneNumber.setText(str8);
                }
                if (focusFlagcode){
                    String str8 = etVerifyCode.getText().toString().trim();
                    str8 += "8";
                    etVerifyCode.setText(str8);
                }

                break;
            case R.id.num_9:
                if (focusFlagnum){
                    String str9 = etPhoneNumber.getText().toString().trim();
                    str9 += "9";
                    etPhoneNumber.setText(str9);
                }
                if (focusFlagcode){
                    String str9 = etVerifyCode.getText().toString().trim();
                    str9 += "9";
                    etVerifyCode.setText(str9);
                }

                break;
//            case R.id.tv_other_login:
//                XToastUtils.info("其他登录方式");
//                break;
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

    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(16[6])|(17[0,1,3,5-8])|(18[0-9])|(19[8,9]))\\\\d{8}$");
        Matcher m = p.matcher(mobiles);
//        System.out.println(m.matches() + "---");
        return m.matches();
    }

    public static boolean isVerifyCode(String verifyCode) {
        Pattern p = Pattern
                .compile("^\\\\d{4}$");
        Matcher m = p.matcher(verifyCode);
//        System.out.println(m.matches() + "---");
        return m.matches();
    }

    /**
     * 获取验证码
     */
    private void getVerifyCode(String phoneNumber) {
        // TODO: 2019-11-18 这里只是界面演示而已

        mCountDownHelper.start();
    }

    /**
     * 根据验证码登录
     *
     * @param phoneNumber 手机号
     * @param verifyCode  验证码
     */
    private void loginByVerifyCode(String phoneNumber, String verifyCode) {
        // TODO: 2019-11-18 这里只是界面演示而已
//        String token = RandomUtils.getRandomNumbersAndLetters(16);
//        if (TokenUtils.handleLoginSuccess(token)) {
//            popToBack();
//            ActivityUtils.startActivity(MainActivity.class);
//        }
        register(idCard,name,phoneNumber);
    }

    private void openSerialport() {

        Log.d("111111MODEL",Build.MODEL);


        //打开端口，usb模式，打开之前必须确保已经获取到USB权限，返回值为设备句柄号。
        int devHandle = BasicOper.dc_open("AUSB",getActivity(),"",0);
        if(devHandle>0){
            Log.d("open","dc_open success devHandle = "+devHandle);
            timeLoop();
        }

    }

    private static final int PERIOD = 3* 1000;
    private static final int DELAY = 100;
    private Disposable mDisposable;
    /**
     * 定时循环任务
     */
    private void timeLoop() {

            mDisposable = Observable.interval(DELAY, PERIOD, TimeUnit.MILLISECONDS)
                    .map((aLong -> aLong + 1))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> readCardNew());//getUnreadCount()执行的任务

    }

    private void readCardNew() {

    //社保卡上电
        boolean bCardPowerOn = false;
        String result = null;
        String[] resultArr = null;
        result = EGovernment.EgAPP_SI_CardPowerOn(1);
        resultArr = result.split("\\|",-1);
        if(resultArr[0].equals("0000")){
            bCardPowerOn = true;
            Log.d("EgAPP_SI_CardPowerOn","success");
        }else{
            Log.d("EgAPP_SI_CardPowerOn","error code = "+resultArr[0] +" error msg = "+resultArr[1] );
        }
    //读取社保卡基本信息
        if(bCardPowerOn){
            SSCard ssCard = EGovernment.EgAPP_SI_ReadSSCardInfo();
            if(ssCard!=null){
                Log.d("EgAPP_SI_ReadSSCardInfo",ssCard.toString());
                readCardSuccess(ssCard.getSSNum(),ssCard.getName(),ssCard.getCardNum());
                if (mDisposable != null) {mDisposable.dispose();}
            }else{
                if (mDisposable != null) {mDisposable.dispose();}
                Log.d("EgAPP_SI_ReadSSCardInfo","读取社保卡信息失败");
            }}
    //社保卡下电
        if(bCardPowerOn){
            result = EGovernment.EgAPP_SI_CardPowerOff(1);
        }

    }

    public void readCardSuccess(String idCard,String name,String phoneNumber) {



    }

    public void register(String idCard,String name,String phoneNumber) {

        ApiRepository.getInstance().register(idCard,name, phoneNumber,mContext)
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(true ?
                        new FastLoadingObserver<RegisterResultEntity>("请稍后...") {
                            @Override
                            public void _onNext(@NonNull RegisterResultEntity entity) {
                                if (entity == null) {
                                    ToastUtil.show("请检查网络");
                                    return;
                                }
//                                checkVersion(entity);
                                if (entity.isSuccess()){

                                    start(DepartmentFragment.newInstance(new Object()));

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
                        new FastObserver<RegisterResultEntity>() {
                            @Override
                            public void _onNext(@NonNull RegisterResultEntity entity) {
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

    }

    @Override
    protected void onVisibleChanged(boolean isVisibleToUser) {
        super.onVisibleChanged(isVisibleToUser);
        //Fragment 可见性变化回调
    }

    final SupportFragmentDelegate mDelegate = new SupportFragmentDelegate(this);
    protected FragmentActivity _mActivity;

    @Override
    public SupportFragmentDelegate getSupportDelegate() {
        return mDelegate;
    }

    /**
     * Perform some extra transactions.
     * 额外的事务：自定义Tag，添加SharedElement动画，操作非回退栈Fragment
     */
    @Override
    public ExtraTransaction extraTransaction() {
        return mDelegate.extraTransaction();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mDelegate.onAttach(activity);
        _mActivity = mDelegate.getActivity();
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mDelegate.onCreate(savedInstanceState);
//    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return mDelegate.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDelegate.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mDelegate.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mDelegate.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mDelegate.onPause();
    }

    @Override
    public void onDestroyView() {
        mDelegate.onDestroyView();
        if (mCountDownHelper != null) {
            mCountDownHelper.recycle();
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mDelegate.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mDelegate.onHiddenChanged(hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mDelegate.setUserVisibleHint(isVisibleToUser);
    }

    /**
     * Causes the Runnable r to be added to the action queue.
     * <p>
     * The runnable will be run after all the previous action has been run.
     * <p>
     * 前面的事务全部执行后 执行该Action
     *
     * @deprecated Use {@link #post(Runnable)} instead.
     */
    @Deprecated
    @Override
    public void enqueueAction(Runnable runnable) {
        mDelegate.enqueueAction(runnable);
    }

    /**
     * Causes the Runnable r to be added to the action queue.
     * <p>
     * The runnable will be run after all the previous action has been run.
     * <p>
     * 前面的事务全部执行后 执行该Action
     */
    @Override
    public void post(Runnable runnable) {
        mDelegate.post(runnable);
    }

    /**
     * Called when the enter-animation end.
     * 入栈动画 结束时,回调
     */
    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        mDelegate.onEnterAnimationEnd(savedInstanceState);
    }


    /**
     * Lazy initial，Called when fragment is first called.
     * <p>
     * 同级下的 懒加载 ＋ ViewPager下的懒加载  的结合回调方法
     */
    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        mDelegate.onLazyInitView(savedInstanceState);
    }

    /**
     * Called when the fragment is visible.
     * 当Fragment对用户可见时回调
     * <p>
     * Is the combination of  [onHiddenChanged() + onResume()/onPause() + setUserVisibleHint()]
     */
    @Override
    public void onSupportVisible() {
        mDelegate.onSupportVisible();
    }

    /**
     * Called when the fragment is invivible.
     * <p>
     * Is the combination of  [onHiddenChanged() + onResume()/onPause() + setUserVisibleHint()]
     */
    @Override
    public void onSupportInvisible() {
        mDelegate.onSupportInvisible();
    }

    /**
     * Return true if the fragment has been supportVisible.
     */
    @Override
    final public boolean isSupportVisible() {
        return mDelegate.isSupportVisible();
    }

    /**
     * Set fragment animation with a higher priority than the ISupportActivity
     * 设定当前Fragmemt动画,优先级比在SupportActivity里高
     */
    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return mDelegate.onCreateFragmentAnimator();
    }

    /**
     * 获取设置的全局动画 copy
     *
     * @return FragmentAnimator
     */
    @Override
    public FragmentAnimator getFragmentAnimator() {
        return mDelegate.getFragmentAnimator();
    }

    /**
     * 设置Fragment内的全局动画
     */
    @Override
    public void setFragmentAnimator(FragmentAnimator fragmentAnimator) {
        mDelegate.setFragmentAnimator(fragmentAnimator);
    }

    /**
     * 按返回键触发,前提是SupportActivity的onBackPressed()方法能被调用
     *
     * @return false则继续向上传递, true则消费掉该事件
     */
    @Override
    public boolean onBackPressedSupport() {
        return mDelegate.onBackPressedSupport();
    }

    /**
     * 类似 {@link Activity#setResult(int, Intent)}
     * <p>
     * Similar to {@link Activity#setResult(int, Intent)}
     *
     * @see #startForResult(ISupportFragment, int)
     */
    @Override
    public void setFragmentResult(int resultCode, Bundle bundle) {
        mDelegate.setFragmentResult(resultCode, bundle);
    }

    /**
     * 类似  {@link Activity onActivityResult(int, int, Intent)}
     * <p>
     * Similar to {@link Activity onActivityResult(int, int, Intent)}
     *
     * @see #startForResult(ISupportFragment, int)
     */
    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        mDelegate.onFragmentResult(requestCode, resultCode, data);
    }

    /**
     * 在start(TargetFragment,LaunchMode)时,启动模式为SingleTask/SingleTop, 回调TargetFragment的该方法
     * 类似 {@link Activity onNewIntent(Intent)}
     * <p>
     * Similar to {@link Activity onNewIntent(Intent)}
     *
     * @param args putNewBundle(Bundle newBundle)
     * @see #start(ISupportFragment, int)
     */
    @Override
    public void onNewBundle(Bundle args) {
        mDelegate.onNewBundle(args);
    }

    /**
     * 添加NewBundle,用于启动模式为SingleTask/SingleTop时
     *
     * @see #start(ISupportFragment, int)
     */
    @Override
    public void putNewBundle(Bundle newBundle) {
        mDelegate.putNewBundle(newBundle);
    }


    /****************************************以下为可选方法(Optional methods)******************************************************/
    // 自定制Support时，可移除不必要的方法

    /**
     * 隐藏软键盘
     */
    protected void hideSoftInput() {
        mDelegate.hideSoftInput();
    }

    /**
     * 显示软键盘,调用该方法后,会在onPause时自动隐藏软键盘
     */
    protected void showSoftInput(final View view) {
        mDelegate.showSoftInput(view);
    }

    /**
     * 加载根Fragment, 即Activity内的第一个Fragment 或 Fragment内的第一个子Fragment
     *
     * @param containerId 容器id
     * @param toFragment  目标Fragment
     */
    public void loadRootFragment(int containerId, ISupportFragment toFragment) {
        mDelegate.loadRootFragment(containerId, toFragment);
    }

    public void loadRootFragment(int containerId, ISupportFragment toFragment, boolean addToBackStack, boolean allowAnim) {
        mDelegate.loadRootFragment(containerId, toFragment, addToBackStack, allowAnim);
    }

    public void start(ISupportFragment toFragment) {
        mDelegate.start(toFragment);
    }

    /**
     * @param launchMode Similar to Activity's LaunchMode.
     */
    public void start(final ISupportFragment toFragment, @LaunchMode int launchMode) {
        mDelegate.start(toFragment, launchMode);
    }

    /**
     * Launch an fragment for which you would like a result when it poped.
     */
    public void startForResult(ISupportFragment toFragment, int requestCode) {
        mDelegate.startForResult(toFragment, requestCode);
    }

    /**
     * Start the target Fragment and pop itself
     */
    public void startWithPop(ISupportFragment toFragment) {
        mDelegate.startWithPop(toFragment);
    }

    /**
     * @see #popTo(Class, boolean)
     * +
     * @see #start(ISupportFragment)
     */
    public void startWithPopTo(ISupportFragment toFragment, Class<?> targetFragmentClass, boolean includeTargetFragment) {
        mDelegate.startWithPopTo(toFragment, targetFragmentClass, includeTargetFragment);
    }

    public void replaceFragment(ISupportFragment toFragment, boolean addToBackStack) {
        mDelegate.replaceFragment(toFragment, addToBackStack);
    }

    public void pop() {
        mDelegate.pop();
    }

    /**
     * Pop the last fragment transition from the manager's fragment
     * back stack.
     * <p>
     * 出栈到目标fragment
     *
     * @param targetFragmentClass   目标fragment
     * @param includeTargetFragment 是否包含该fragment
     */
    public void popTo(Class<?> targetFragmentClass, boolean includeTargetFragment) {
        mDelegate.popTo(targetFragmentClass, includeTargetFragment);
    }

    /**
     * 获取栈内的fragment对象
     */
    public <T extends ISupportFragment> T findChildFragment(Class<T> fragmentClass) {
        return SupportHelper.findFragment(getChildFragmentManager(), fragmentClass);
    }
}
