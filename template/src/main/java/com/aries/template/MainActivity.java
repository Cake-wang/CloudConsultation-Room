package com.aries.template;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.aries.library.fast.entity.FastTabEntity;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.module.activity.FastMainActivity;
import com.aries.library.fast.retrofit.FastLoadingObserver;
import com.aries.library.fast.retrofit.FastObserver;
import com.aries.library.fast.util.SPUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.template.entity.CancelregisterResultEntity;
import com.aries.template.entity.FindUserResultEntity;
import com.aries.template.entity.GetConsultsAndRecipesResultEntity;
import com.aries.template.module.base.BaseEventFragment;
import com.aries.template.module.main.HomeFragment;
import com.aries.template.module.mine.DepartmentFragment;
import com.aries.template.module.mine.MineCardFragment;
import com.aries.template.module.mine.OrderConsultFragment;
import com.aries.template.module.mine.OrderRecipesListFragment;
import com.aries.template.module.mine.PhoneRegisterFragment;
import com.aries.template.module.mine.VideoConsultFragment;
import com.aries.template.retrofit.repository.ApiRepository;
import com.aries.template.thridapp.JTJKSSDCard;
import com.aries.template.thridapp.JTJKThirdAppUtil;
import com.aries.template.utils.ActivityUtils;
import com.aries.template.utils.DateUtils;
import com.aries.template.xiaoyu.dapinsocket.DapinSocketProxy;
import com.aries.ui.view.tab.CommonTabLayout;
import com.decard.NDKMethod.BasicOper;
import com.decard.NDKMethod.EGovernment;
import com.decard.NDKMethod.SSCardDriver;
import com.decard.entitys.SSCard;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import io.reactivex.Observable;
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
 * 界面承载容器
 * 医保卡循环读取任务
 * 获取 findUser 任务，确定用户是否已经登录 #requestFindUser();
 * 检查用户是否有未支付挂号单或处方单 #requestConsultsAndRecipes();
 *
 * 规则：
 * 进入体检后，立刻跳回到主页
 * 进入读卡页面后，立刻读卡，并启动3秒读卡任务。读卡成功后，就停止读卡。
 *
 * 主要关注的页面
 * HomeFragment 首页
 *
 *
 * @author louisluo
 * @Author: AriesHoo on 2018/7/23 10:00
 * @E-Mail: AriesHoo@126.com
 * Function: 示例主页面
 * Description:
 */
public class MainActivity extends FastMainActivity implements ISupportActivity {

    /** mDelegate 控制器 FastMainActivity 的机制 */
    final SupportActivityDelegate mDelegate = new SupportActivityDelegate(this);

    @BindView(R.id.tabLayout_commonFastLib) CommonTabLayout mTabLayout;
    private ArrayList<FastTabEntity> mTabEntities;

    /** 检测当前读卡请求用户状态是否进行中 */
    private boolean isReadCardProcessing;

    @Override
    public boolean isSwipeEnable() {
        return false;
    }

    @Override
    public List<FastTabEntity> getTabList() {
        mTabEntities = new ArrayList<>();
//        mTabEntities.add(new FastTabEntity(R.string.home, R.drawable.ic_home_normal, R.drawable.ic_home_selected, HomeFragment.newInstance()));
//        mTabEntities.add(new FastTabEntity(R.string.mine, R.drawable.ic_mine_normal, R.drawable.ic_mine_selected, MineCardFragment.newInstance("")));
        return mTabEntities;
    }

    @Override
    public void setTabLayout(CommonTabLayout tabLayout) {
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mTabLayout.setVisibility(View.GONE);

    }

    /**
     * 禁用首页的物理键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (getTopFragment() instanceof HomeFragment){
            if (keyCode == KeyEvent.KEYCODE_BACK) {
//                return true;
                return super.onKeyDown(keyCode, event);
            }
        }else if (getTopFragment() instanceof VideoConsultFragment){
            return true;
        }else if (getTopFragment() instanceof BaseEventFragment){
            ((BaseEventFragment) getTopFragment()).pop();
            ((BaseEventFragment) getTopFragment()).onDismiss();
            return true;
        }
       return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String tag = (String) SPUtil.get(mContext,"tag","");
        if (tag.contains("backMain")){
//            HomeFragment fragment = findFragment(HomeFragment.class);
//            // 在栈内的HomeFragment以SingleTask模式启动（即在其之上的Fragment会出栈）
//            start(fragment, SupportFragment.SINGLETASK);
//            start(HomeFragment.newInstance(), SupportFragment.SINGLETASK);
        }
    }

    /**
     * 医保卡读卡器 获取信息
     * devHandle -1 没有拿到 设备句柄号
     */
    public void openSerialport() {
        Log.d("111111MODEL", Build.MODEL);
        Log.d("111111MODEL", getTopFragment()+"");
        //打开端口，usb模式，打开之前必须确保已经获取到USB权限，返回值为设备句柄号。
        int devHandle = BasicOper.dc_open("AUSB",this,"",0);
//        int devHandle = BasicOper.dc_open("AUSB",this,"/dev/ttyHSL1",115200);
//        int devHandle = BasicOper.dc_open("COM",null,"/dev/ttyS0",115200);
        Log.d("111111MODEL", devHandle+"");
        if(devHandle>0){
            Log.d("open","dc_open success devHandle = "+devHandle);
            timeLoop();
        }else{
            ToastUtil.show("读卡失败，请重新插卡，并重试");
            start(HomeFragment.newInstance(), SupportFragment.SINGLETASK);
            BasicOper.dc_exit();
        }
    }

    private static final int PERIOD = 3* 1000;
    private static final int DELAY = 100;
    private Disposable mDisposable;

    /**
     * 定时循环任务
     * 每3秒检测一次
     */
    public void timeLoop() {

        mDisposable = null;
        mDisposable = Observable.interval(DELAY, PERIOD, TimeUnit.MILLISECONDS)
                .map((aLong -> aLong + 1))
//                    .delay(PERIOD, TimeUnit.MILLISECONDS, true)       // 设置delayError为true，表示出现错误的时候也需要延迟5s进行通知，达到无论是请求正常还是请求失败，都是5s后重新订阅，即重新请求。
                .subscribeOn(Schedulers.io())
                .repeat()   // repeat保证请求成功后能够重新订阅。
                .retry()    // retry保证请求失败后能重新订阅
                .observeOn(Schedulers.newThread())
                .subscribe(aLong ->readCardNew());//getUnreadCount()执行的任务

//        if (mDisposable == null){
//            mDisposable = Observable.interval(DELAY, PERIOD, TimeUnit.MILLISECONDS)
//                    .map((aLong -> aLong + 1))
////                    .delay(PERIOD, TimeUnit.MILLISECONDS, true)       // 设置delayError为true，表示出现错误的时候也需要延迟5s进行通知，达到无论是请求正常还是请求失败，都是5s后重新订阅，即重新请求。
//                    .subscribeOn(Schedulers.io())
//                    .repeat()   // repeat保证请求成功后能够重新订阅。
//                    .retry()    // retry保证请求失败后能重新订阅
//                    .observeOn(Schedulers.newThread())
//                    .subscribe(aLong ->readCardNew());//getUnreadCount()执行的任务
//        }
    }

    /**
     * 插卡需要读芯片
     * 如果没有芯片就需要非接设备
     */
    public void readCardNew() {
        Log.d("111111MODEL", getTopFragment()+"");
//        if (getTopFragment() instanceof HomeFragment){
//            if (mDisposable != null) {
//                mDisposable.dispose();
//                mDisposable=null;
//            }
//            BasicOper.dc_exit();
//            return;
//        }
        if (getTopFragment() instanceof MineCardFragment){
//            //社保卡上电
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
//            byte info[] = new byte[256];
//            long ret = SSCardDriver.iReadCardBas(1,info);
//            try {
//                ToastUtil.show("iReadCardBas: ret:"+ret+" info:"+new String(info,"gbk").trim());
//                Log.d("TAG", "iReadCardBas: ret:"+ret+" info:"+new String(info,"gbk").trim());
//            }
//            catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }

                // 新一代读卡
                // 格式化卡数据
//                330100|522725198711013517|AA0368350|330100D156000005456188401C06C0CE|陈东武|008100885086653301015DD82C|3.00|20210318|20310318|330100912171|00012600202203000183|
                byte info[] = new byte[256];
                long ret = SSCardDriver.iReadCardBas(1, info);
                try {
                    Log.d("TAG", "iReadCardBas: ret:" + ret + " info:" + new String(info, "gbk").trim());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                JTJKSSDCard ssCard = null;
                try {
                    ssCard = JTJKSSDCard.build2D(new String(info, "gbk").trim());
                    //                SSCard ssCard = EGovernment.EgAPP_SI_ReadSSCardInfo();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 注入数据
                if(ssCard!=null){

                    // 输入社保数据
                    setSSDCardData(ssCard);

//                    Log.d("EgAPP_SI_ReadSSCardInfo",ssCard.toString());
//                    // 向全局填写当前社保卡信息
//                    GlobalConfig.ssCard = ssCard;
//                    // 由于病人的名字是带空格的，必须去掉
//                    GlobalConfig.ssCard.setName(ssCard.getName().trim());
//                    GlobalConfig.age = getAge(ssCard.getSSNum());
//
//                    SPUtil.put(mContext,"smkCard",ssCard.getCardNum());
//                    SPUtil.put(mContext,"age",getAge(ssCard.getSSNum()));
//                    SPUtil.put(mContext,"userName",ssCard.getName().trim());
//                    SPUtil.put(mContext,"sex",ssCard.getSex());
//                    SPUtil.put(mContext,"idCard",ssCard.getSSNum());
//
//                    // 获取信息后，直接请求用户数据
//                    runOnUiThread(() -> requestFindUser(GlobalConfig.ssCard.getSSNum(),GlobalConfig.ssCard.getName(),GlobalConfig.ssCard.getCardNum()));

                    // 读取成功后，清除mDisposable不再进行验证
                    if (mDisposable != null) {
                        mDisposable.dispose();
                        mDisposable=null;
                    }
                    BasicOper.dc_exit();
                }else{
                    if (mDisposable != null) {mDisposable.dispose();}
                    BasicOper.dc_exit();
                    ToastUtil.show("读取社保卡信息失败，请重试");
                    Log.d("EgAPP_SI_ReadSSCardInfo","读取社保卡信息失败");
                }
            }else {
                ToastUtil.show("读卡失败，请重新插入卡片");
            }
            //社保卡下电
            if(bCardPowerOn){
                result = EGovernment.EgAPP_SI_CardPowerOff(1);
            }
        }else {
            if (mDisposable != null) {
                mDisposable.dispose();
                mDisposable=null;
            }
            BasicOper.dc_exit();
            return;
        }
//        else {
//            //设置卡座
//            String result = BasicOper.dc_setcpu(0);
//            String[] resultArr = result.split("\\|",-1);
//            if(resultArr[0].equals("0000")) {
//                String resultAfter = BasicOper.dc_setcpupara(0,0x00, 0x5C);
//                String[] resultArrAfter = resultAfter.split("\\|", -1);
//                if (resultArrAfter[0].equals("0000")) {
//                    ////设置接触式CPU卡参数
//                    Log.d("dc_setcpupara", "接触式CPU卡参数设置成功");
//                    // 设置卡座成功之后，才可以读取卡片存在状态
//                    Log.d("dc_setcpu","success");
//                    String result111 = BasicOper.dc_card_status();
//                    String[] resultArr111 = result111.split("\\|",-1);
//                    if(resultArr111[0].equals("0000")){
//                        Log.d("dc_card_status","卡片存在");
//                    } else{
//                        HomeFragment fragment = findFragment(HomeFragment.class);
////                Bundle newBundle = new Bundle();
////                fragment.putNewBundle(newBundle);
//                        // 在栈内的HomeFragment以SingleTask模式启动（即在其之上的Fragment会出栈）
//                        start(fragment, SupportFragment.SINGLETASK);
//                        Log.d("dc_card_status","error code = "+resultArr111[0] +" error msg = "+resultArr111[1] );
//                    }
//                } else {
//                    Log.d("dc_setcpupara", "error code = " + resultArrAfter[0] + " error msg = " + resultArrAfter[1]);
//                }
//            } else {
//                Log.d("dc_setcpu","error code = "+resultArr[0] +" error msg = "+resultArr[1] );
//            }
//        }
    }

    /**
     * 向全局输入SSDcard数据，并进行跳转
     * @param ssCard 社保卡数据
     */
    public void setSSDCardData(JTJKSSDCard ssCard){
        if(ssCard!=null){
            Log.d("EgAPP_SI_ReadSSCardInfo",ssCard.toString());
            // 向全局填写当前社保卡信息
            GlobalConfig.ssCard = ssCard;
            // 由于病人的名字是带空格的，必须去掉
            GlobalConfig.ssCard.setName(ssCard.getName().trim());
            GlobalConfig.age = getAge(ssCard.getSSNum());

            SPUtil.put(mContext,"smkCard",ssCard.getCardNum());
            SPUtil.put(mContext,"age",getAge(ssCard.getSSNum()));
            SPUtil.put(mContext,"userName",ssCard.getName().trim());
            SPUtil.put(mContext,"sex",ssCard.getSex());
            SPUtil.put(mContext,"idCard",ssCard.getSSNum());

            // 获取信息后，直接请求用户数据
            runOnUiThread(() -> requestFindUser(GlobalConfig.ssCard.getSSNum(),GlobalConfig.ssCard.getName(),GlobalConfig.ssCard.getCardNum()));
        }
    }

    /**
     * 通过生日返回年龄
     * @param cardNumber 身份证号
     */
    public static int getAge(String cardNumber) {
        String birthday = cardNumber.substring(6,14);
        return DateUtils.getAge(birthday);
    }

    /**
     * 用户信息查询
     * 验证用户是否注册
     * @param idCard 身份证
     */
    public void requestFindUser(String idCard, String name, String smkcard) {
        // 验证当前 用户信息是否已经获取，如果获取，则不再请求。
        if (GlobalConfig.isFindUserDone)
            return;
        else
            isReadCardProcessing = false;
        // 验证当前请求是否已经在执行中，如果是，则不再多次请求。
        if (isReadCardProcessing)
            return;
        Log.d("readCardSuccess","readCardSuccess");
        // 正在执行中。。。
        isReadCardProcessing = true;
        // 验证用户是否注册。
        ApiRepository.getInstance().findUser(idCard)
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastLoadingObserver<FindUserResultEntity>("请稍后...") {
                            @Override
                            public void _onNext(@io.reactivex.annotations.NonNull FindUserResultEntity entity) {
                                if (entity == null) {
                                    ToastUtil.show("请检查网络");
                                    return;
                                }
                               try {
                                   if (entity.isSuccess()){
                                       String tag = (String) SPUtil.get(mContext,"tag","fzpy");
                                       if (entity.getData()!=null){
                                           GlobalConfig.isFindUserDone = true;
                                           GlobalConfig.NALI_TID = entity.getData().getUserId();
                                           GlobalConfig.mobile = entity.getData().getMobile();
                                           SPUtil.put(mContext,"tid",entity.getData().getUserId());
                                           SPUtil.put(mContext,"mobile",entity.getData().getMobile());
                                           if(tag.contains("stjc")){
//                                            Intent intent = new Intent(Intent.ACTION_MAIN);
                                               /**知道要跳转应用的包命与目标Activity*/
                                               // 启动身体检查系统
                                               SPUtil.put(mContext,"tag","backMain");
                                               if (mDisposable != null) {
                                                   mDisposable.dispose();
                                                   mDisposable=null;
                                               }
                                               BasicOper.dc_exit();

                                               // 启动第三方跳转
                                               if (!TextUtils.isEmpty(GlobalConfig.factoryResource)){
                                                   new JTJKThirdAppUtil().gotoBodyTesting(MainActivity.this,
                                                           GlobalConfig.factoryResource,
                                                           GlobalConfig.factoryMainPage,
                                                           entity.getData().getName().trim(),
                                                           entity.getData().getIdcard(),
                                                           entity.getData().getMobile());
                                                   start(HomeFragment.newInstance(), SupportFragment.SINGLETASK);
                                               }else {
                                                   ToastUtil.show("没有第三方应用信息，无法跳转");
                                               }
//                                            ComponentName componentName = new ComponentName("com.garea.launcher", "com.garea.launcher.login.LauncherLogin");
//                                            intent.setComponent(componentName);
//                                            intent.putExtra("userName", entity.getData().getName());//这里Intent传值
//                                            intent.putExtra("idCard", entity.getData().getIdcard());
//                                            intent.putExtra("mobile", entity.getData().getMobile());
//                                            startActivity(intent);
                                           }else {
                                               //判断是否有挂号或处方，如果没有，跳转一级部门选择。
                                               requestConsultsAndRecipes();
                                           }
                                       }else {
                                           if(TextUtils.isEmpty(tag)){
                                               ToastUtil.show("参数缺失");
                                               isReadCardProcessing = false;
                                           }else {
                                               // 如果没有注册，跳转到手机注册页面
                                               start(PhoneRegisterFragment.newInstance( idCard, name.trim(), smkcard));
                                           }
                                       }
                                   }else {
                                       ToastUtil.show(entity.getMessage());
                                       if (Objects.equals(entity.code, "2100"))// 如果没有注册，跳转到手机注册页面
                                           start(PhoneRegisterFragment.newInstance( idCard, name.trim(), smkcard));
                                       isReadCardProcessing = false;
                                   }
                               }catch (Exception e){
                                   e.printStackTrace();
                               }
                            }

                    @Override
                    public void _onError(Throwable e) {
                        super._onError(e);
                        isReadCardProcessing = false;
                    }
                });
    }

    /**
     * 检测是否有未支付
     * 获取未支付复诊单挂号
     * 获取未支付处方单
     * 如果2个都没有，则跳转一级部门选择界面
     *
     * 如果复诊单挂号有多余的无效单，批量进行取消。
     */
    public void requestConsultsAndRecipes() {
        // 需要将 ongoing 和 onready 合并
        // 所有复诊单集合
        final List<GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Consults> allConsult = new ArrayList<>();
        // 所有处方单
        final ArrayList<GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes> allRecipes = new ArrayList<>();

        ApiRepository.getInstance().getConsultsAndRecipes("ongoing")
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastLoadingObserver<GetConsultsAndRecipesResultEntity>("请稍后...") {
                    @Override
                    public void _onNext(@io.reactivex.annotations.NonNull GetConsultsAndRecipesResultEntity entity) {
                        if (entity == null) {
                            ToastUtil.show("请检查网络，返回首页后重试");
                            return;
                        }
                        try {
                            if (entity.isSuccess()){
                                // 这里只收集数据，并不做处理
                                allConsult.addAll(entity.data.getConsults());
                                allRecipes.addAll(entity.data.recipes);

                                // 请求 onready
                                ApiRepository.getInstance().getConsultsAndRecipes("onready")
                                        .compose(MainActivity.this.bindUntilEvent(ActivityEvent.DESTROY))
                                        .subscribe(new FastLoadingObserver<GetConsultsAndRecipesResultEntity>("请稍后...") {
                                            @Override
                                            public void _onNext(@io.reactivex.annotations.NonNull GetConsultsAndRecipesResultEntity entity) {
                                                if (entity == null) {
                                                    ToastUtil.show("请检查网络，返回首页后重试");
                                                    return;
                                                }
                                                try {
                                                    if (entity.isSuccess()){
                                                        // 这里只收集数据，并不做处理
                                                        allConsult.addAll(entity.data.getConsults());
                                                        allRecipes.addAll(entity.data.recipes);

                                                        // 如果2个都不满足则跳转科室
                                                        boolean isDepartTag = true;
                                                        // 复诊单挂号和处方单不会同时出现，如果同时出现，则需要调整逻辑
                                                        // 查看挂号是否多余1条
                                                        if(allConsult.size()>0){
                                                            for (GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Consults item : allConsult) {
                                                                int status = item.getConsults().getStatus();
                                                                if (item.getConsults().getConsultOrgan() != GlobalConfig.organId)
                                                                    // 如果不是同一家机构，则跳过不处理
                                                                    break;
                                                                if ( item.getConsults().getPayflag()==1 &&
                                                                        (status==1 || status ==2 || status == 3 || status == 4)){
                                                                    //status=4 问诊中
                                                                    isDepartTag = false;
                                                                    // 去往复诊单挂号
                                                                    start(OrderConsultFragment.newInstance(item));
                                                                    return;
                                                                }
                                                                //  如果复诊单挂单有多余的无效单，批量进行取消。
                                                                if (item.getConsults().getPayflag()==0 && status!=8){
                                                                    // 取消复诊单 status = 8 已经取消
                                                                    ApiRepository.getInstance().patientCancelGraphicTextConsult(String.valueOf(item.getConsults().getConsultId()))
                                                                            .compose(MainActivity.this.bindUntilEvent(ActivityEvent.DESTROY))
                                                                            .subscribe(new FastObserver<CancelregisterResultEntity>() {
                                                                                @Override
                                                                                public void _onNext(CancelregisterResultEntity entity) {
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        }
                                                        // 查看处方单是否多余1条处方
                                                        if (allRecipes.size()>0){
                                                            // 每一个处方单中，都有一个处方信息，这个处方信息是需要合并的
                                                            ArrayList<GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes> recipes = new ArrayList();
                                                            for (GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes item : allRecipes) {
                                                                if (item.getOrganId() != GlobalConfig.organId)
                                                                    // 如果不是同一家机构，则跳过不处理
                                                                    break;
                                                                // 1 待审核, 2 待处理, 3 待取药
                                                                if (item.status==2){
                                                                    recipes.add(item);
                                                                }
                                                            }
                                                            // 遍历未支付处方单，如果有orderid 一样的，则合并处方
                                                            // 每合并一次，就会减少 recipes
                                                            // 每次结束，把0位置去掉，并添加到新
                                                            ArrayList<GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes> recipeGroup = new ArrayList();
                                                            if (recipes.size()>0){
                                                                while (recipes.size()>0) {
                                                                    // 被移除的对象
                                                                    final ArrayList<GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes> removeItems = new ArrayList();
                                                                    // 合并对象
                                                                    final GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes currentRecipes = recipes.get(0);
                                                                    for (int j = 1; j < recipes.size(); j++) {
                                                                        // orderId = null 表示没有合并支付过的，那么就不需要合并
                                                                        if ( recipes.get(j).orderId!=null && recipes.get(j).orderId.equals(currentRecipes.orderId)){
                                                                            currentRecipes.getRecipeDetailBeans().addAll(recipes.get(j).getRecipeDetailBeans());
                                                                            removeItems.add(recipes.get(j));
                                                                        }
                                                                    }
                                                                    // 移除合并单位
                                                                    if (removeItems.size()>0){
                                                                        for (GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes removeItem : removeItems) {
                                                                            recipes.remove(removeItem);
                                                                        }
                                                                    }
                                                                    // 获得数据后，将合并后的集合单位移除
                                                                    recipes.remove(0);
                                                                    recipeGroup.add(currentRecipes);
                                                                }
                                                            }


                                                            // 如果未支付处方单有，则进入批量处理界面
                                                            if (recipeGroup.size()>0){
                                                                isDepartTag = false;
                                                                start(OrderRecipesListFragment.newInstance(recipeGroup));
                                                                return;
                                                            }
                                                        }
                                                        // 如果即没有未支付处方单，也没有未支付复诊单
                                                        if (isDepartTag){
                                                            // 如果2个都不满足则跳转科室
                                                            start(DepartmentFragment.newInstance());
                                                        }
                                                    }else {
                                                        ToastUtil.show("获取未支付失败，请稍后重试");
                                                        start(HomeFragment.newInstance(), SupportFragment.SINGLETASK);
                                                    }
                                                    isReadCardProcessing = false;
                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void _onError(Throwable e) {
                                                super._onError(e);
                                                ToastUtil.show("网络问题，返回首页后重试");
                                                start(HomeFragment.newInstance(), SupportFragment.SINGLETASK);
                                                isReadCardProcessing = false;
                                            }
                                        });
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                       @Override
                       public void _onError(Throwable e) {
                           super._onError(e);
                           ToastUtil.show("网络问题，返回首页后重试");
                           start(HomeFragment.newInstance(), SupportFragment.SINGLETASK);
                           isReadCardProcessing = false;
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
        // 保持设备界面常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // 优先将设备号输入进来
        GlobalConfig.machineId = ApiRepository.getDeviceId();
        GlobalConfig.thirdMachineId = ApiRepository.getDeviceId();
        mDelegate.onCreate(savedInstanceState);
        if (findFragment(HomeFragment.class) == null) {
            loadRootFragment(R.id.fLayout_containerFastMain, HomeFragment.newInstance());  // 加载根Fragment
        }

        // 初始化社保卡设备
        BasicOper.setInitParameter(2, null, 0);
        BasicOper.CreateAndroidContext(this);
    }

    /**
     * 第一次启动 或 从其他应用返回
     */
    @Override
    protected void onStart() {
        super.onStart();
//        ActivityUtils.fullScreen(getWindow(),false);
//        ActivityUtils.lightOnScreen(getWindow());
        // 从身体检测返回测试，如果返回了，则启动返回socket
        new JTJKThirdAppUtil().backFromBodyTesting(MainActivity.this);
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

