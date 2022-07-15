//package com.aries.template.module.mine;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.view.animation.Animation;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.aries.library.fast.FastManager;
//import com.aries.library.fast.module.fragment.FastTitleRefreshLoadFragment;
//import com.aries.library.fast.retrofit.FastLoadingObserver;
//import com.aries.library.fast.retrofit.FastObserver;
//import com.aries.library.fast.util.SPUtil;
//import com.aries.library.fast.util.ToastUtil;
//import com.aries.template.FakeDataExample;
//import com.aries.template.R;
//import com.aries.template.adapter.RecipesAdapter;
//import com.aries.template.entity.CancelregisterResultEntity;
//import com.aries.template.entity.GetConsultsAndRecipesResultEntity;
//import com.aries.template.module.main.HomeFragment;
//import com.aries.template.retrofit.repository.ApiRepository;
//import com.aries.template.view.ShineButtonDialog;
//import com.aries.ui.view.title.TitleBarView;
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.chad.library.adapter.base.viewholder.BaseViewHolder;
//import com.decard.NDKMethod.BasicOper;
//import com.decard.NDKMethod.EGovernment;
//import com.decard.entitys.SSCard;
//import com.trello.rxlifecycle3.android.FragmentEvent;
//import com.xuexiang.xaop.annotation.SingleClick;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.concurrent.TimeUnit;
//
//import androidx.annotation.Nullable;
//import androidx.fragment.app.FragmentActivity;
//import androidx.recyclerview.widget.RecyclerView;
//import butterknife.BindView;
//import butterknife.OnClick;
//import io.reactivex.Observable;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.annotations.NonNull;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.schedulers.Schedulers;
//import me.yokeyword.fragmentation.ExtraTransaction;
//import me.yokeyword.fragmentation.ISupportFragment;
//import me.yokeyword.fragmentation.SupportFragment;
//import me.yokeyword.fragmentation.SupportFragmentDelegate;
//import me.yokeyword.fragmentation.SupportHelper;
//import me.yokeyword.fragmentation.anim.FragmentAnimator;
//
///**
// * 已挂号页面
// * 这个类已经被 OrderConsultFragment 和 OrderRecipiesFragment 所分解了。
// *
// * @author louisluo
// * @Author: AriesHoo on 2018/7/13 17:09
// * @E-Mail: AriesHoo@126.com
// * @Function: 我的
// * @Description:
// */
//@Deprecated
//public class OrderFragment extends FastTitleRefreshLoadFragment<GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes.RecipeDetail> implements ISupportFragment {
//    private GetConsultsAndRecipesResultEntity.QueryArrearsSummary obj;
//    private  String appKey= "",tid= "";
//    private Integer consultId ;
//
//    @BindView(R.id.btn_back)
//    Button btn_back;
//    @BindView(R.id.btn_main)
//    Button btn_main;
//
//
//    @BindView(R.id.tv_name)
//    TextView tv_name;
//    @BindView(R.id.tv_card)
//    TextView tv_card;
//    @BindView(R.id.tv_age)
//    TextView tv_age;
//    @BindView(R.id.tv_dept_r)
//    TextView tv_dept_r;
//    @BindView(R.id.tv_date_r)
//    TextView tv_date_r;
//
//
//    @BindView(R.id.tv_age_tv)
//    TextView tv_age_tv;
//    @BindView(R.id.tv_age_l)
//    TextView tv_age_l;
//    @BindView(R.id.tv_doc_tv)
//    TextView tv_doc_tv;
//    @BindView(R.id.tv_doc)
//    TextView tv_doc;
//    @BindView(R.id.tv_dept_tv)
//    TextView tv_dept_tv;
//    @BindView(R.id.tv_dept)
//    TextView tv_dept;
//    @BindView(R.id.tv_result_tv)
//    TextView tv_result_tv;
//    @BindView(R.id.tv_result)
//    TextView tv_result;
//    @BindView(R.id.tv_date_tv)
//    TextView tv_date_tv;
//    @BindView(R.id.tv_date)
//    TextView tv_date;
//
//    @BindView(R.id.ll_order_text_r)
//    LinearLayout ll_order_text_r;
//    @BindView(R.id.ll_order_r)
//    LinearLayout ll_order_r;
//    @BindView(R.id.ll_prescription)
//    LinearLayout ll_prescription;
//
//
//    @BindView(R.id.tv_tip_message)
//    TextView tv_tip_message;
//    @BindView(R.id.btn_cancel)
//    Button btn_cancel;
//    @BindView(R.id.btn_inquiry)
//    Button btn_inquiry;
//
//    private BaseQuickAdapter mAdapter;
//
//
//    @BindView(R.id.rv_contentFastLib)
//    RecyclerView rv_contentFastLib;
//
//
//    /**
//     * 获取数据
//     * @param obj 传入的数据，注意这个对象必须实现序列化
//     * @return
//     */
//    public static OrderFragment newInstance(GetConsultsAndRecipesResultEntity.QueryArrearsSummary obj) {
//        Bundle args = new Bundle();
//        OrderFragment fragment = new OrderFragment();
//        args.putSerializable("obj", (Serializable) obj);
//
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mDelegate.onCreate(savedInstanceState);
//        Bundle args = getArguments();
//        if (args != null) {
//            obj = (GetConsultsAndRecipesResultEntity.QueryArrearsSummary) args.getSerializable("obj");
//
//        }
//    }
//
//    @Override
//    public int getContentLayout() {
//        return R.layout.fragment_order;
//    }
//
//    @Override
//    public boolean isLoadMoreEnable() {
//        return false;
//    }
//
//    @Override
//    public boolean isRefreshEnable() {
//        return false;
//    }
//
//
//    @Override
//    public BaseQuickAdapter<GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes.RecipeDetail, BaseViewHolder> getAdapter() {
//        mAdapter = new RecipesAdapter();
//
////        mAdapter.setEmptyView();
//        return mAdapter;
//    }
//
//    @Override
//    public void loadData(int page) {
//        if (obj.getRecipes().size()>0){
//
//            FastManager.getInstance().getHttpRequestControl().httpRequestSuccess(getIHttpRequestControl(), obj.getRecipes().size()>0  ? new ArrayList<>() :obj.getRecipes().get(0).getRecipeDetailBeans(), null);
//
//        }
//
//    }
//
//    @Override
//    public void onItemClicked(BaseQuickAdapter<GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes.RecipeDetail, BaseViewHolder> adapter, View view, int position) {
//        super.onItemClicked(adapter, view, position);
////        WidgetEntity entity = adapter.getItem(position);
////        if (position == 1) {
////            SwipeBackActivity.start(mContext, entity.title);
////        } else {
////            FastUtil.startActivity(mContext, entity.activity);
////        }
//    }
//
//    @Override
//    public void setTitleBar(TitleBarView titleBar) {
//        titleBar.setBgColor(Color.WHITE)
//                .setTitleMainText(R.string.mine);
//    }
//
//    @Override
//    public void initView(Bundle savedInstanceState) {
//
////        Handler handler = new Handler();
////        handler.postDelayed(new Runnable() {
////            @Override
////            public void run() {
////                /**
////                 *要执行的操作
////                 */
////                openSerialport();
////            }
////        }, 500);//3秒后执行Runnable中的run方法
//
//        if (obj.getConsults().size()>0){
//
//             tv_age_tv.setVisibility(View.GONE);
//            tv_age_l.setVisibility(View.GONE);
//           tv_doc_tv.setVisibility(View.VISIBLE);
//             tv_doc.setVisibility(View.VISIBLE);
//             tv_dept_tv.setVisibility(View.GONE);
//             tv_dept.setVisibility(View.GONE);
//            tv_result_tv.setVisibility(View.GONE);
//             tv_result.setVisibility(View.GONE);
//             tv_date_tv.setVisibility(View.GONE);
//            tv_date.setVisibility(View.GONE);
//
//             ll_order_text_r.setVisibility(View.VISIBLE);
//             ll_order_r.setVisibility(View.VISIBLE);
//            ll_prescription.setVisibility(View.GONE);
//
//            tv_tip_message.setText("您已有挂号记录，是否发起问诊");
//            btn_cancel.setText("取消挂号");
//            btn_inquiry.setText("去问诊");
//
//
//            for (int i = 0; i < obj.getConsults().size(); i++) {
//                if (obj.getConsults().get(i).getConsults().getPayflag() == 1){
//                    tv_name.setText(obj.getConsults().get(i).getConsults().getMpiName()+SPUtil.get(mContext,"sex",""));
//                    tv_card.setText(SPUtil.get(mContext,"smkCard","")+"");
//                    tv_age.setText(SPUtil.get(mContext,"age","")+"");
//                    tv_doc.setText(obj.getConsults().get(i).getConsults().getConsultDoctorText());
//                    tv_dept_r.setText(obj.getConsults().get(i).getConsults().getConsultDepartText());
//                    tv_date_r.setText(obj.getConsults().get(i).getConsults().getRequestTime());
//                    consultId = obj.getConsults().get(i).getConsults().getConsultId();
//                }
//            }
//
//
//        }else  if (obj.getRecipes().size()>0){
//            tv_age_tv.setVisibility(View.VISIBLE);
//            tv_age_l.setVisibility(View.VISIBLE);
//            tv_doc_tv.setVisibility(View.GONE);
//            tv_doc.setVisibility(View.GONE);
//            tv_dept_tv.setVisibility(View.VISIBLE);
//            tv_dept.setVisibility(View.VISIBLE);
//            tv_result_tv.setVisibility(View.VISIBLE);
//            tv_result.setVisibility(View.VISIBLE);
//            tv_date_tv.setVisibility(View.VISIBLE);
//            tv_date.setVisibility(View.VISIBLE);
//
//            ll_order_text_r.setVisibility(View.GONE);
//            ll_order_r.setVisibility(View.GONE);
//            ll_prescription.setVisibility(View.VISIBLE);
//
//            tv_tip_message.setText("您已有处方记录，是否需要支付");
//            btn_cancel.setText("取消支付");
//            btn_inquiry.setText("去支付");
//
//
//            tv_name.setText(SPUtil.get(mContext,"userName","")+""+SPUtil.get(mContext,"sex",""));
//            tv_card.setText(SPUtil.get(mContext,"smkCard","")+"");
//            tv_age_l.setText(SPUtil.get(mContext,"age","")+"");
////            tv_dept.setText("");
//            tv_result.setText(obj.getRecipes().get(0).getOrganDiseaseName());
//            tv_date.setText(obj.getRecipes().get(0).getSignDate()+"");
//        }else {
//
//            tv_age_tv.setVisibility(View.VISIBLE);
//            tv_age_l.setVisibility(View.VISIBLE);
//            tv_doc_tv.setVisibility(View.GONE);
//            tv_doc.setVisibility(View.GONE);
//            tv_dept_tv.setVisibility(View.VISIBLE);
//            tv_dept.setVisibility(View.VISIBLE);
//            tv_result_tv.setVisibility(View.VISIBLE);
//            tv_result.setVisibility(View.VISIBLE);
//            tv_date_tv.setVisibility(View.VISIBLE);
//            tv_date.setVisibility(View.VISIBLE);
//
//            ll_order_text_r.setVisibility(View.GONE);
//            ll_order_r.setVisibility(View.GONE);
//            ll_prescription.setVisibility(View.VISIBLE);
//
//            tv_tip_message.setVisibility(View.GONE);
//            btn_cancel.setText("取消");
//            btn_inquiry.setText("确认结算");
//
//
//            tv_name.setText(SPUtil.get(mContext,"userName","")+""+SPUtil.get(mContext,"sex",""));
//            tv_card.setText(SPUtil.get(mContext,"smkCard","")+"");
//            tv_age_l.setText(SPUtil.get(mContext,"age","")+"");
//
//        }
//
//    }
//
//    @SingleClick
//    @OnClick({R.id.btn_back, R.id.btn_main, R.id.btn_cancel, R.id.btn_inquiry})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
////            case R.id.btn_back:
////
////                break;
////            case R.id.btn_main:
////
////                break;
//            case R.id.btn_cancel:
//                if (obj.getConsults().size()>0){
//                    showSimpleConfirmDialog("consults");
//                }else if (obj.getRecipes().size()>0){
//                    showSimpleConfirmDialog("recipes");
//                }else {
//                    showSimpleConfirmDialog("backMain");
//                }
//
//
//                break;
//            case R.id.btn_inquiry:
//
//                if (obj.getConsults().size()>0){
//                    //跳视频问诊
//                    // todo 动态输入参数
//                    start(VideoConsultFragment.newInstance(FakeDataExample.consultId,FakeDataExample.nickname,FakeDataExample.doctorUserId));
//                }else if (obj.getRecipes().size()>0){
//                    //先查库存，再跳转支付页
//                    queryInventory();
//                }else {
//                    start(PayCodeFragment.newInstance(FakeDataExample.recipeFee,FakeDataExample.recipeIds,FakeDataExample.recipeCode));// todo cc
//                }
//                break;
//            default:
//                break;
//        }
//    }
//
//    private void queryInventory() {
//        start(PayCodeFragment.newInstance(FakeDataExample.recipeFee,FakeDataExample.recipeIds,FakeDataExample.recipeCode));// todo cc
//    }
//
//    private void showSimpleConfirmDialog(String opflag) {
////        new MaterialDialog.Builder(getContext())
////                .content(R.string.tip_cancel_register)
////                .positiveText(R.string.lab_yes)
////                .negativeText(R.string.lab_no)
////                .onPositive((dialog, which) -> cancelregister(appKey,tid,consultId))
////                .show();
//
//
//        ShineButtonDialog dialog = new ShineButtonDialog(this.mContext);
//
//        if (opflag.contains("consults")){
//
//            dialog.tv_title_tip.setText("取消挂号订单");
//            dialog.tv_content_tip.setText("取消后问诊需重新挂号，是否确认取消");
//
//        }else if (opflag.contains("recipes")){
//
//            dialog.tv_title_tip.setText("取消支付订单");
//            dialog.tv_content_tip.setText("取消后将无法再次支付，是否确认取消");
//
//        }else {
//            dialog.tv_title_tip.setText("取消结算");
//            dialog.tv_content_tip.setText("取消后将无法再次支付，是否确认取消");
//        }
//
//        dialog.btn_inquiry.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                if (opflag.contains("consults")){
//                    tid = (String) SPUtil.get(mContext,"tid","");
//                    cancelregister(appKey,tid,consultId);
//                }else if (opflag.contains("recipes")){
//                }else {
////                    HomeFragment fragment = findFragment(HomeFragment.class);
////                Bundle newBundle = new Bundle();
////                fragment.putNewBundle(newBundle);
//                    // 在栈内的HomeFragment以SingleTask模式启动（即在其之上的Fragment会出栈）
//                    start(HomeFragment.newInstance(), SupportFragment.SINGLETASK);
//                }
//            }
//        });
//        dialog.btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        dialog.iv_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
//    }
//
//    /**
//     * 获取验证码
//     */
//    private void getVerifyCode(String phoneNumber) {
//        // TODO: 2019-11-18 这里只是界面演示而已
//
//
//    }
//
//    /**
//     * 根据验证码登录
//     *
//     * @param phoneNumber 手机号
//     * @param verifyCode  验证码
//     */
//    private void loginByVerifyCode(String phoneNumber, String verifyCode) {
//        // TODO: 2019-11-18 这里只是界面演示而已
////        String token = RandomUtils.getRandomNumbersAndLetters(16);
////        if (TokenUtils.handleLoginSuccess(token)) {
////            popToBack();
////            ActivityUtils.startActivity(MainActivity.class);
////        }
////        cancelregister();
//    }
//
//    private void openSerialport() {
//
//        Log.d("111111MODEL",Build.MODEL);
//
//
//        //打开端口，usb模式，打开之前必须确保已经获取到USB权限，返回值为设备句柄号。
//        int devHandle = BasicOper.dc_open("AUSB",getActivity(),"",0);
//        if(devHandle>0){
//            Log.d("open","dc_open success devHandle = "+devHandle);
//            timeLoop();
//        }
//
//    }
//
//    private static final int PERIOD = 3* 1000;
//    private static final int DELAY = 100;
//    private Disposable mDisposable;
//    /**
//     * 定时循环任务
//     */
//    private void timeLoop() {
//
//            mDisposable = Observable.interval(DELAY, PERIOD, TimeUnit.MILLISECONDS)
//                    .map((aLong -> aLong + 1))
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(aLong -> readCardNew());//getUnreadCount()执行的任务
//
//    }
//
//    private void readCardNew() {
//
//    //社保卡上电
//        boolean bCardPowerOn = false;
//        String result = null;
//        String[] resultArr = null;
//        result = EGovernment.EgAPP_SI_CardPowerOn(1);
//        resultArr = result.split("\\|",-1);
//        if(resultArr[0].equals("0000")){
//            bCardPowerOn = true;
//            Log.d("EgAPP_SI_CardPowerOn","success");
//        }else{
//            Log.d("EgAPP_SI_CardPowerOn","error code = "+resultArr[0] +" error msg = "+resultArr[1] );
//        }
//    //读取社保卡基本信息
//        if(bCardPowerOn){
//            SSCard ssCard = EGovernment.EgAPP_SI_ReadSSCardInfo();
//            if(ssCard!=null){
//                Log.d("EgAPP_SI_ReadSSCardInfo",ssCard.toString());
//                readCardSuccess(ssCard.getSSNum(),ssCard.getName(),ssCard.getCardNum());
//                if (mDisposable != null) {mDisposable.dispose();}
//            }else{
//                if (mDisposable != null) {mDisposable.dispose();}
//                Log.d("EgAPP_SI_ReadSSCardInfo","读取社保卡信息失败");
//            }}
//    //社保卡下电
//        if(bCardPowerOn){
//            result = EGovernment.EgAPP_SI_CardPowerOff(1);
//        }
//
//    }
//
//    public void readCardSuccess(String idCard,String name,String phoneNumber) {
//
//
//
//    }
//
//    public void cancelregister(String appKey,String tid,Integer consultId) {
//        ApiRepository.getInstance().patientCancelGraphicTextConsult(String.valueOf(consultId))
//                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
//                .subscribe(true ?
//                        new FastLoadingObserver<CancelregisterResultEntity>("请稍后...") {
//                            @Override
//                            public void _onNext(@NonNull CancelregisterResultEntity entity) {
//                                if (entity == null) {
//                                    ToastUtil.show("请检查网络");
//                                    return;
//                                }
////                                checkVersion(entity);
//                                if (entity.isSuccess()){
//
//                                    if (entity.getData().isSuccess()){
//                                        start(ResultFragment.newInstance("cancelConsult"));
//                                    }
//
//
//                                }else {
//
////                                    if(TextUtils.isEmpty(tag)){
////                                        ToastUtil.show("参数缺失");
////                                    }else {
////                                        start(PutRecordFragment.newInstance( idCard, name, smkcard));
////                                    }
//
////                                    ToastUtil.show(entity.getRespDesc());
//                                }
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//
////                                ToastUtil.show("请检查网络和ip地址");
//                                if (true) {
//                                    super.onError(e);
//                                }
//                            }
//                        } :
//                        new FastObserver<CancelregisterResultEntity>() {
//                            @Override
//                            public void _onNext(@NonNull CancelregisterResultEntity entity) {
//                                if (entity == null) {
//                                    ToastUtil.show("请检查网络");
//                                    return;
//                                }
//
//
//
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                if (false) {
//                                    super.onError(e);
//                                }
//                            }
//                        });
//
//    }
//
//    @Override
//    protected void onVisibleChanged(boolean isVisibleToUser) {
//        super.onVisibleChanged(isVisibleToUser);
//        //Fragment 可见性变化回调
//    }
//
//    final SupportFragmentDelegate mDelegate = new SupportFragmentDelegate(this);
//    protected FragmentActivity _mActivity;
//
//    @Override
//    public SupportFragmentDelegate getSupportDelegate() {
//        return mDelegate;
//    }
//
//    /**
//     * Perform some extra transactions.
//     * 额外的事务：自定义Tag，添加SharedElement动画，操作非回退栈Fragment
//     */
//    @Override
//    public ExtraTransaction extraTransaction() {
//        return mDelegate.extraTransaction();
//    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        mDelegate.onAttach(activity);
//        _mActivity = mDelegate.getActivity();
//    }
//
////    @Override
////    public void onCreate(@Nullable Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        mDelegate.onCreate(savedInstanceState);
////    }
//
//    @Override
//    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
//        return mDelegate.onCreateAnimation(transit, enter, nextAnim);
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mDelegate.onActivityCreated(savedInstanceState);
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        mDelegate.onSaveInstanceState(outState);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        mDelegate.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        mDelegate.onPause();
//    }
//
//    @Override
//    public void onDestroyView() {
//        mDelegate.onDestroyView();
//
//        super.onDestroyView();
//    }
//
//    @Override
//    public void onDestroy() {
//        mDelegate.onDestroy();
//        super.onDestroy();
//    }
//
//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        mDelegate.onHiddenChanged(hidden);
//    }
//
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        mDelegate.setUserVisibleHint(isVisibleToUser);
//    }
//
//    /**
//     * Causes the Runnable r to be added to the action queue.
//     * <p>
//     * The runnable will be run after all the previous action has been run.
//     * <p>
//     * 前面的事务全部执行后 执行该Action
//     *
//     * @deprecated Use {@link #post(Runnable)} instead.
//     */
//    @Deprecated
//    @Override
//    public void enqueueAction(Runnable runnable) {
//        mDelegate.enqueueAction(runnable);
//    }
//
//    /**
//     * Causes the Runnable r to be added to the action queue.
//     * <p>
//     * The runnable will be run after all the previous action has been run.
//     * <p>
//     * 前面的事务全部执行后 执行该Action
//     */
//    @Override
//    public void post(Runnable runnable) {
//        mDelegate.post(runnable);
//    }
//
//    /**
//     * Called when the enter-animation end.
//     * 入栈动画 结束时,回调
//     */
//    @Override
//    public void onEnterAnimationEnd(Bundle savedInstanceState) {
//        mDelegate.onEnterAnimationEnd(savedInstanceState);
//    }
//
//
//    /**
//     * Lazy initial，Called when fragment is first called.
//     * <p>
//     * 同级下的 懒加载 ＋ ViewPager下的懒加载  的结合回调方法
//     */
//    @Override
//    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
//        mDelegate.onLazyInitView(savedInstanceState);
//    }
//
//    /**
//     * Called when the fragment is visible.
//     * 当Fragment对用户可见时回调
//     * <p>
//     * Is the combination of  [onHiddenChanged() + onResume()/onPause() + setUserVisibleHint()]
//     */
//    @Override
//    public void onSupportVisible() {
//        mDelegate.onSupportVisible();
//    }
//
//    /**
//     * Called when the fragment is invivible.
//     * <p>
//     * Is the combination of  [onHiddenChanged() + onResume()/onPause() + setUserVisibleHint()]
//     */
//    @Override
//    public void onSupportInvisible() {
//        mDelegate.onSupportInvisible();
//    }
//
//    /**
//     * Return true if the fragment has been supportVisible.
//     */
//    @Override
//    final public boolean isSupportVisible() {
//        return mDelegate.isSupportVisible();
//    }
//
//    /**
//     * Set fragment animation with a higher priority than the ISupportActivity
//     * 设定当前Fragmemt动画,优先级比在SupportActivity里高
//     */
//    @Override
//    public FragmentAnimator onCreateFragmentAnimator() {
//        return mDelegate.onCreateFragmentAnimator();
//    }
//
//    /**
//     * 获取设置的全局动画 copy
//     *
//     * @return FragmentAnimator
//     */
//    @Override
//    public FragmentAnimator getFragmentAnimator() {
//        return mDelegate.getFragmentAnimator();
//    }
//
//    /**
//     * 设置Fragment内的全局动画
//     */
//    @Override
//    public void setFragmentAnimator(FragmentAnimator fragmentAnimator) {
//        mDelegate.setFragmentAnimator(fragmentAnimator);
//    }
//
//    /**
//     * 按返回键触发,前提是SupportActivity的onBackPressed()方法能被调用
//     *
//     * @return false则继续向上传递, true则消费掉该事件
//     */
//    @Override
//    public boolean onBackPressedSupport() {
//        return mDelegate.onBackPressedSupport();
//    }
//
//    /**
//     * 类似 {@link Activity#setResult(int, Intent)}
//     * <p>
//     * Similar to {@link Activity#setResult(int, Intent)}
//     *
//     * @see #startForResult(ISupportFragment, int)
//     */
//    @Override
//    public void setFragmentResult(int resultCode, Bundle bundle) {
//        mDelegate.setFragmentResult(resultCode, bundle);
//    }
//
//    /**
//     * 类似  {@link Activity onActivityResult(int, int, Intent)}
//     * <p>
//     * Similar to {@link Activity onActivityResult(int, int, Intent)}
//     *
//     * @see #startForResult(ISupportFragment, int)
//     */
//    @Override
//    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
//        mDelegate.onFragmentResult(requestCode, resultCode, data);
//    }
//
//    /**
//     * 在start(TargetFragment,LaunchMode)时,启动模式为SingleTask/SingleTop, 回调TargetFragment的该方法
//     * 类似 {@link Activity onNewIntent(Intent)}
//     * <p>
//     * Similar to {@link Activity onNewIntent(Intent)}
//     *
//     * @param args putNewBundle(Bundle newBundle)
//     * @see #start(ISupportFragment, int)
//     */
//    @Override
//    public void onNewBundle(Bundle args) {
//        mDelegate.onNewBundle(args);
//    }
//
//    /**
//     * 添加NewBundle,用于启动模式为SingleTask/SingleTop时
//     *
//     * @see #start(ISupportFragment, int)
//     */
//    @Override
//    public void putNewBundle(Bundle newBundle) {
//        mDelegate.putNewBundle(newBundle);
//    }
//
//
//    /****************************************以下为可选方法(Optional methods)******************************************************/
//    // 自定制Support时，可移除不必要的方法
//
//    /**
//     * 隐藏软键盘
//     */
//    protected void hideSoftInput() {
//        mDelegate.hideSoftInput();
//    }
//
//    /**
//     * 显示软键盘,调用该方法后,会在onPause时自动隐藏软键盘
//     */
//    protected void showSoftInput(final View view) {
//        mDelegate.showSoftInput(view);
//    }
//
//    /**
//     * 加载根Fragment, 即Activity内的第一个Fragment 或 Fragment内的第一个子Fragment
//     *
//     * @param containerId 容器id
//     * @param toFragment  目标Fragment
//     */
//    public void loadRootFragment(int containerId, ISupportFragment toFragment) {
//        mDelegate.loadRootFragment(containerId, toFragment);
//    }
//
//    public void loadRootFragment(int containerId, ISupportFragment toFragment, boolean addToBackStack, boolean allowAnim) {
//        mDelegate.loadRootFragment(containerId, toFragment, addToBackStack, allowAnim);
//    }
//
//    public void start(ISupportFragment toFragment) {
//        mDelegate.start(toFragment);
//    }
//
//    /**
//     * @param launchMode Similar to Activity's LaunchMode.
//     */
//    public void start(final ISupportFragment toFragment, @LaunchMode int launchMode) {
//        mDelegate.start(toFragment, launchMode);
//    }
//
//    /**
//     * Launch an fragment for which you would like a result when it poped.
//     */
//    public void startForResult(ISupportFragment toFragment, int requestCode) {
//        mDelegate.startForResult(toFragment, requestCode);
//    }
//
//    /**
//     * Start the target Fragment and pop itself
//     */
//    public void startWithPop(ISupportFragment toFragment) {
//        mDelegate.startWithPop(toFragment);
//    }
//
//    /**
//     * @see #popTo(Class, boolean)
//     * +
//     * @see #start(ISupportFragment)
//     */
//    public void startWithPopTo(ISupportFragment toFragment, Class<?> targetFragmentClass, boolean includeTargetFragment) {
//        mDelegate.startWithPopTo(toFragment, targetFragmentClass, includeTargetFragment);
//    }
//
//    public void replaceFragment(ISupportFragment toFragment, boolean addToBackStack) {
//        mDelegate.replaceFragment(toFragment, addToBackStack);
//    }
//
//    public void pop() {
//        mDelegate.pop();
//    }
//
//    /**
//     * Pop the last fragment transition from the manager's fragment
//     * back stack.
//     * <p>
//     * 出栈到目标fragment
//     *
//     * @param targetFragmentClass   目标fragment
//     * @param includeTargetFragment 是否包含该fragment
//     */
//    public void popTo(Class<?> targetFragmentClass, boolean includeTargetFragment) {
//        mDelegate.popTo(targetFragmentClass, includeTargetFragment);
//    }
//
//    /**
//     * 获取栈内的fragment对象
//     */
//    public <T extends ISupportFragment> T findChildFragment(Class<T> fragmentClass) {
//        return SupportHelper.findFragment(getChildFragmentManager(), fragmentClass);
//    }
//}
