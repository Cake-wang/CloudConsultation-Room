package com.aries.template;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

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
import com.aries.template.module.mine.OrderConsultFragment;
import com.aries.template.module.mine.PutRecordFragment;
import com.aries.template.retrofit.repository.ApiRepository;
import com.aries.template.utils.ActivityUtils;
import com.aries.ui.view.tab.CommonTabLayout;
import com.decard.NDKMethod.BasicOper;
import com.decard.NDKMethod.EGovernment;
import com.decard.entitys.SSCard;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.util.ArrayList;
import java.util.Calendar;
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

        Log.d("111111MODEL", "111111MODEL");



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

        Log.d("111111MODEL", getTopFragment()+"");


        //打开端口，usb模式，打开之前必须确保已经获取到USB权限，返回值为设备句柄号。
        int devHandle = BasicOper.dc_open("AUSB",this,"",0);
//        int devHandle = BasicOper.dc_open("COM",null,"/dev/ttyS0",115200);
        Log.d("111111MODEL", devHandle+"");
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
        if (getTopFragment() instanceof HomeFragment){

            return;

        }

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

                    SPUtil.put(mContext,"smkCard",ssCard.getCardNum());
                    SPUtil.put(mContext,"age",getAge(Long.parseLong(ssCard.getBirthday())));
                    SPUtil.put(mContext,"userName",ssCard.getName());
                    SPUtil.put(mContext,"sex",ssCard.getSex());
                    SPUtil.put(mContext,"idCard",ssCard.getSSNum());

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


    public static int getAge(long birthday) {
        Calendar currentCalendar = Calendar.getInstance();//实例化calendar
        currentCalendar.setTimeInMillis(System.currentTimeMillis());//调用setTimeInMillis方法和System.currentTimeMillis()获取当前时间

        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.setTimeInMillis(birthday);//这个解析传进来的时间戳

        if (currentCalendar.get(Calendar.MONTH) >= targetCalendar.get(Calendar.MONTH)) {//如果现在的月份大于生日的月份
            return currentCalendar.get(Calendar.YEAR) - targetCalendar.get(Calendar.YEAR);//那就直接减,因为现在的年月都大于生日的年月
        } else {
            return currentCalendar.get(Calendar.YEAR) - targetCalendar.get(Calendar.YEAR) - 1;//否则,减掉一年
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
                                        SPUtil.put(mContext,"mobile",entity.getData().getMobile());
                                        if(tag.contains("stjc")){

                                            Intent intent = new Intent(Intent.ACTION_MAIN);
                                            /**知道要跳转应用的包命与目标Activity*/
                                            ComponentName componentName = new ComponentName("com.garea.launcher", "com.garea.launcher.login.LauncherLogin");
                                            intent.setComponent(componentName);
                                            intent.putExtra("userName", entity.getData().getName());//这里Intent传值
                                            intent.putExtra("idCard", entity.getData().getIdcard());
                                            intent.putExtra("mobile", entity.getData().getMobile());
                                            startActivity(intent);

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

    public void getConsultsAndRecipes() {
        ApiRepository.getInstance().getConsultsAndRecipes()
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastLoadingObserver<GetConsultsAndRecipesResultEntity>("请稍后...") {
                    @Override
                    public void _onNext(@io.reactivex.annotations.NonNull GetConsultsAndRecipesResultEntity entity) {
                        if (entity == null) {
                            ToastUtil.show("请检查网络");
                            return;
                        }
//                                checkVersion(entity);
                        if (entity.isSuccess()){
                            // 查看处方单是否多余1条
                            if(entity.getData().getConsults().size()>0){
                                for (GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Consults item : entity.getData().getConsults()) {
                                    int status = item.getConsults().getStatus();
                                   if ( item.getConsults().getPayflag()==1 &&
                                           (status==1 || status ==2 || status == 3)){
                                       // 挂号
                                       start(OrderConsultFragment.newInstance(item));
                                   }
                                }
                            }else if (entity.getData().getRecipes().size()>0){
                                // 处方
                                start(OrderFragment.newInstance(entity.getData()));
                            } else {
                                start(DepartmentFragment.newInstance(new Object()));
                            }
                        }else {
                            ToastUtil.show(entity.getMessage());
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
        mDelegate.onCreate(savedInstanceState);
        if (findFragment(HomeFragment.class) == null) {
            loadRootFragment(R.id.fLayout_containerFastMain, HomeFragment.newInstance());  // 加载根Fragment
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        ActivityUtils.fullScreen(getWindow(),false);
        ActivityUtils.lightOnScreen(getWindow());
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
}

