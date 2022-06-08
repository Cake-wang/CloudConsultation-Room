package com.aries.template;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.aries.library.fast.entity.FastTabEntity;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.module.activity.FastMainActivity;
import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.retrofit.FastObserver;
import com.aries.library.fast.util.SPUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.template.entity.FindUserResultEntity;
import com.aries.template.entity.GetConsultsAndRecipesResultEntity;
import com.aries.template.module.main.HomeFragment;
import com.aries.template.module.mine.DepartmentFragment;
import com.aries.template.module.mine.MineFragment;
import com.aries.template.module.mine.OrderFragment;
import com.aries.template.module.mine.PutRecordFragment;
import com.aries.template.retrofit.repository.ApiRepository;
import com.aries.ui.view.tab.CommonTabLayout;
import com.decard.NDKMethod.BasicOper;
import com.decard.NDKMethod.EGovernment;
import com.decard.entitys.SSCard;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.yokeyword.fragmentation.ExtraTransaction;
import me.yokeyword.fragmentation.ISupportActivity;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportActivityDelegate;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.SupportHelper;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * 主页显示界面
 * @Author: AriesHoo on 2018/7/23 10:00
 * @E-Mail: AriesHoo@126.com
 * Function: 示例主页面
 * Description:
 */
public class MainActivity extends FastMainActivity implements ISupportActivity {

    final SupportActivityDelegate mDelegate = new SupportActivityDelegate(this);

    @BindView(R.id.tabLayout_commonFastLib) CommonTabLayout mTabLayout;
    private ArrayList<FastTabEntity> mTabEntities;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }

    @Override
    public boolean isSwipeEnable() {
        return false;
    }

    @Override
    public List<FastTabEntity> getTabList() {
        mTabEntities = new ArrayList<>();
        mTabEntities.add(new FastTabEntity(R.string.home, R.drawable.ic_home_normal, R.drawable.ic_home_selected, HomeFragment.newInstance()));
        mTabEntities.add(new FastTabEntity(R.string.mine, R.drawable.ic_mine_normal, R.drawable.ic_mine_selected, MineFragment.newInstance("")));
        return mTabEntities;
    }

    @Override
    public void setTabLayout(CommonTabLayout tabLayout) {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mTabLayout.setVisibility(View.GONE);
    }

    @Override
    public void loadData() {



        if (getTopFragment() instanceof HomeFragment){

            return;

        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /**
                 *要执行的操作
                 */
                openSerialport();
            }
        }, 500);//3秒后执行Runnable中的run方法

    }

    private void openSerialport() {

        Log.d("111111MODEL", Build.MODEL);


        //打开端口，usb模式，打开之前必须确保已经获取到USB权限，返回值为设备句柄号。
        int devHandle = BasicOper.dc_open("AUSB",this,"",0);
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


        if (getTopFragment() instanceof MineFragment){

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

        }else {
            String result111 = BasicOper.dc_card_status();
            String[] resultArr111 = result111.split("\\|",-1);
            if(resultArr111[0].equals("0000")){
                Log.d("dc_card_status","卡片存在");
            }
            else{
                HomeFragment fragment = findFragment(HomeFragment.class);
//                Bundle newBundle = new Bundle();
//
//                fragment.putNewBundle(newBundle);
                // 在栈内的HomeFragment以SingleTask模式启动（即在其之上的Fragment会出栈）
                start(fragment, SupportFragment.SINGLETASK);
                Log.d("dc_card_status","error code = "+resultArr111[0] +" error msg = "+resultArr111[1] );
            }
        }





    }

    public void readCardSuccess(String idCard,String name,String smkcard) {

        ApiRepository.getInstance().findUser(idCard,mContext)
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(true ?
                        new FastLoadingObserver<FindUserResultEntity>("请稍后...") {
                            @Override
                            public void _onNext(@io.reactivex.annotations.NonNull FindUserResultEntity entity) {
                                if (entity == null) {
                                    ToastUtil.show("请检查网络");
                                    return;
                                }
//                                checkVersion(entity);
                                if (entity.isSuccess()){

                                    String tag = (String) SPUtil.get(mContext,"tag","fzpy");

                                    if (entity.getData()!=null){
                                        SPUtil.put(mContext,"tid",entity.getData().getUserId());
                                        if(tag.contains("stjc")){

                                        }else {

                                            //判断有挂号或处方

                                            getConsultsAndRecipes();

                                        }
                                    }else {
                                        if(TextUtils.isEmpty(tag)){
                                            ToastUtil.show("参数缺失");
                                        }else {
                                            start(PutRecordFragment.newInstance( idCard, name, smkcard));
                                        }
                                    }



                                }else {



                                    ToastUtil.show(entity.getMessage());
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
                        new FastObserver<FindUserResultEntity>() {
                            @Override
                            public void _onNext(@io.reactivex.annotations.NonNull FindUserResultEntity entity) {
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

    private void getConsultsAndRecipes() {


        ApiRepository.getInstance().getConsultsAndRecipes("","",0,mContext)
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(true ?
                        new FastLoadingObserver<GetConsultsAndRecipesResultEntity>("请稍后...") {
                            @Override
                            public void _onNext(@io.reactivex.annotations.NonNull GetConsultsAndRecipesResultEntity entity) {
                                if (entity == null) {
                                    ToastUtil.show("请检查网络");
                                    return;
                                }
//                                checkVersion(entity);
                                if (entity.isSuccess()){
                                    if(entity.getData().isSuccess()){
                                        if(entity.getData().getConsults().size()>0||entity.getData().getRecipes().size()>0){
                                            start(OrderFragment.newInstance(entity.getData()));
                                        }else {
                                            start(DepartmentFragment.newInstance(new Object()));
                                        }
                                    }else {
                                        ToastUtil.show(entity.getData().getErrorMessage());
                                    }





                                }else {



                                    ToastUtil.show(entity.getMessage());
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
                        new FastObserver<GetConsultsAndRecipesResultEntity>() {
                            @Override
                            public void _onNext(@io.reactivex.annotations.NonNull GetConsultsAndRecipesResultEntity entity) {
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
    public void onTabSelect(int position) {
        LoggerManager.d("OnTabSelectListener:onTabSelect:" + position);
    }

    @Override
    public void onTabReselect(int position) {
        LoggerManager.d("OnTabSelectListener:onTabReselect:" + position);
    }

    //    @Override
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        LoggerManager.i(TAG, "onDestroy");
//    }



    @Override
    public SupportActivityDelegate getSupportDelegate() {
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewControlSettings();
        mDelegate.onCreate(savedInstanceState);

        if (findFragment(HomeFragment.class) == null) {
            loadRootFragment(R.id.fLayout_containerFastMain, HomeFragment.newInstance());  // 加载根Fragment
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDelegate.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        mDelegate.onDestroy();
        super.onDestroy();
    }

    /**
     * Note： return mDelegate.dispatchTouchEvent(ev) || super.dispatchTouchEvent(ev);
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mDelegate.dispatchTouchEvent(ev) || super.dispatchTouchEvent(ev);
    }

    /**
     * 不建议复写该方法,请使用 {@link #onBackPressedSupport} 代替
     */
    @Override
    final public void onBackPressed() {
        mDelegate.onBackPressed();
    }

    /**
     * 该方法回调时机为,Activity回退栈内Fragment的数量 小于等于1 时,默认finish Activity
     * 请尽量复写该方法,避免复写onBackPress(),以保证SupportFragment内的onBackPressedSupport()回退事件正常执行
     */
    @Override
    public void onBackPressedSupport() {
        mDelegate.onBackPressedSupport();
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
     * Set all fragments animation.
     * 设置Fragment内的全局动画
     */
    @Override
    public void setFragmentAnimator(FragmentAnimator fragmentAnimator) {
        mDelegate.setFragmentAnimator(fragmentAnimator);
    }

    /**
     * Set all fragments animation.
     * 构建Fragment转场动画
     * <p/>
     * 如果是在Activity内实现,则构建的是Activity内所有Fragment的转场动画,
     * 如果是在Fragment内实现,则构建的是该Fragment的转场动画,此时优先级 > Activity的onCreateFragmentAnimator()
     *
     * @return FragmentAnimator对象
     */
    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return mDelegate.onCreateFragmentAnimator();
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

    /****************************************以下为可选方法(Optional methods)******************************************************/

    // 选择性拓展其他方法

    public void loadRootFragment(int containerId, @NonNull ISupportFragment toFragment) {
        mDelegate.loadRootFragment(containerId, toFragment);
    }

    public void start(ISupportFragment toFragment) {
        mDelegate.start(toFragment);
    }

    /**
     * @param launchMode Same as Activity's LaunchMode.
     */
    public void start(ISupportFragment toFragment, @ISupportFragment.LaunchMode int launchMode) {
        mDelegate.start(toFragment, launchMode);
    }

    /**
     * It is recommended to use {@link SupportFragment#startWithPopTo(ISupportFragment, Class, boolean)}.
     *
     * @see #popTo(Class, boolean)
     * +
     * @see #start(ISupportFragment)
     */
    public void startWithPopTo(ISupportFragment toFragment, Class<?> targetFragmentClass, boolean includeTargetFragment) {
        mDelegate.startWithPopTo(toFragment, targetFragmentClass, includeTargetFragment);
    }

    /**
     * Pop the fragment.
     */
    public void pop() {
        mDelegate.pop();
    }

    /**
     * Pop the last fragment transition from the manager's fragment
     * back stack.
     */
    public void popTo(Class<?> targetFragmentClass, boolean includeTargetFragment) {
        mDelegate.popTo(targetFragmentClass, includeTargetFragment);
    }

    /**
     * If you want to begin another FragmentTransaction immediately after popTo(), use this method.
     * 如果你想在出栈后, 立刻进行FragmentTransaction操作，请使用该方法
     */
    public void popTo(Class<?> targetFragmentClass, boolean includeTargetFragment, Runnable afterPopTransactionRunnable) {
        mDelegate.popTo(targetFragmentClass, includeTargetFragment, afterPopTransactionRunnable);
    }

    public void popTo(Class<?> targetFragmentClass, boolean includeTargetFragment, Runnable afterPopTransactionRunnable, int popAnim) {
        mDelegate.popTo(targetFragmentClass, includeTargetFragment, afterPopTransactionRunnable, popAnim);
    }

    /**
     * 得到位于栈顶Fragment
     */
    public ISupportFragment getTopFragment() {
        return SupportHelper.getTopFragment(getSupportFragmentManager());
    }

    /**
     * 获取栈内的fragment对象
     */
    public <T extends ISupportFragment> T findFragment(Class<T> fragmentClass) {
        return SupportHelper.findFragment(getSupportFragmentManager(), fragmentClass);
    }

    /**
     * 页面属性配置
     * - 沉浸式
     * - 长期点亮
     */
    public void ViewControlSettings(){
        boolean keepScreenOn = true;//长亮屏幕
        boolean isRequestFocus = true;//焦点

        if (getWindow() == null) return;
        // 是否保持屏幕点亮
        // 这个判定会让屏幕一直保持点亮状态
        if(keepScreenOn){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        // 将父类的边框设定强制为0，一般这个父类都是最底层的显示对象
        getWindow().getDecorView().setPadding(0, 0, 0, 0);

        // FLAG_HARDWARE_ACCELERATED 表示这个window是否启动硬件加速,请求硬件加速但不能保证硬件加速生效。
        // 如果仅是用来启动硬件加速,可以在代码中控制,使用下面的代码给指定window启动硬件加速:
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        // 全屏的窗口下面 会忽略 SOFT_INPUT_ADJUST_RESIZE  ，不会进行调整
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // 设置全屏
        // SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN : Activity全屏显示，但是状态栏不会被覆盖掉，而是正常显示，只是Activity顶端布局会被覆盖住
        // SYSTEM_UI_FLAG_FULLSCREEN : Activity全屏显示，且状态栏被覆盖掉
        // SYSTEM_UI_FLAG_VISIBLE : 状态栏和Activity共存，Activity不全屏显示。也就是应用平常的显示画面
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        getWindow().getDecorView().setSystemUiVisibility(option);

        // 去掉状态栏的阴影
        if(Build.VERSION.SDK_INT == 19){
            //解决4.4上状态栏闪烁的问题
            // 设置状态栏为透明并且为全屏模式。
            // 然后通过添加一个与 StatusBar 一样大小的 View，将 View 的 backgroud 设置为我们想要的颜色，从而实现沉浸式。
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }else if (Build.VERSION.SDK_INT == 20) {
            // 由于透明主题不能在4.4以前的版本里面使用，
            // 所以系统样式跟以前没有区别，也就是看不到任何变化，
            // 这是一个兼容模式，这个模式可以兼容 20
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);
        }else if (Build.VERSION.SDK_INT >= 21) {
            // 尝试去掉状态栏
//            int navigationBarColor = 0xffffff;//显示导航栏颜色
            int navigationBarColor = Color.TRANSPARENT; //去掉导航栏
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            // 如果状态栏背景已经被调整颜色，则不需要再镂空
            getWindow().setNavigationBarColor(navigationBarColor);
            //尝试兼容部分手机上的状态栏空白问题
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        // 在show之前必须要关闭焦点，这个代码是很有必要的不能删除
        if(!isRequestFocus){
            //不获取焦点
            int flag = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            getWindow().setFlags(flag,flag);
        }

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    /**
     * 设置 window的FLAG的通用方法
     * 工具方法
     * 直接向window的LayoutParams设置FLAG
     */
    public void setWindowFlag(final int bits, boolean on) {
        WindowManager.LayoutParams winParams = getWindow().getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        getWindow().setAttributes(winParams);
    }

}

