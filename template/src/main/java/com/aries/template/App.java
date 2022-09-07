package com.aries.template;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;

import com.aries.library.fast.FastManager;
import com.aries.library.fast.manager.LoggerManager;
import com.aries.library.fast.widget.FastLoadDialog;
import com.aries.template.constant.ApiConstant;
import com.aries.template.impl.ActivityControlImpl;
import com.aries.template.impl.AppImpl;
import com.aries.template.impl.HttpRequestControlImpl;
import com.aries.template.utils.SystemUtil;
import com.aries.template.view.FullScreenJTJKDialog;
import com.aries.template.widget.mgson.MFastRetrofit;
import com.aries.template.xiaoyu.EaseModeProxy;
import com.decard.NDKMethod.BasicOper;
import com.decard.dc_licensesdk.utils.AppInfoUtils;
import com.github.moduth.blockcanary.BlockCanary;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.xuexiang.xaop.XAOP;
import com.xuexiang.xaop.checker.IThrowableHandler;
import com.xuexiang.xaop.checker.Interceptor;
import com.xuexiang.xaop.logger.XLogger;
import com.xuexiang.xaop.util.PermissionUtils;

import org.aspectj.lang.JoinPoint;

import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.multidex.MultiDexApplication;
import me.yokeyword.fragmentation.Fragmentation;
import xcrash.ICrashCallback;
import xcrash.XCrash;

/**
 * 入口
 * @Author: AriesHoo on 2018/7/31 10:43
 * @E-Mail: AriesHoo@126.com
 * Function:基础配置Application
 * Description:
 */
public class App extends MultiDexApplication {

    private static Context mContext;
    private static String TAG = "FastTemplate";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        //初始化Logger日志打印
        LoggerManager.init(TAG, false,
                PrettyFormatStrategy.newBuilder()
                        .methodOffset(0)
                        .showThreadInfo(true)
                        .methodCount(3));

        //以下为更丰富自定义方法-可不设置即使用默认配置
        //全局UI配置参数-按需求设置
        AppImpl impl = new AppImpl(mContext);
        ActivityControlImpl activityControl = new ActivityControlImpl();
        FastManager.getInstance()
                //设置Adapter加载更多视图--默认设置了FastLoadMoreView
                .setLoadMoreFoot(impl)
                //全局设置RecyclerView
                .setFastRecyclerViewControl(impl)
                //设置RecyclerView加载过程多布局属性
                .setMultiStatusView(impl)
                //设置全局网络请求等待Loading提示框如登录等待loading--观察者必须为FastLoadingObserver及其子类
                .setLoadingDialog(impl)
                //设置SmartRefreshLayout刷新头-自定加载使用BaseRecyclerViewAdapterHelper
                .setDefaultRefreshHeader(impl)
                // 设置对话框，添加全局对话框，沉浸式
                .setLoadingDialog(activity -> new FastLoadDialog(activity,
                        new FullScreenJTJKDialog.JTJKBuilder(activity)
                                .setMessage(R.string.fast_loading)
                                .create()))
                //设置全局TitleBarView相关配置
                .setTitleBarViewControl(impl)
                //设置Activity滑动返回控制-默认开启滑动返回功能不需要设置透明主题
//                .setSwipeBackControl(new SwipeBackControlImpl())
                //设置Activity/Fragment相关配置(横竖屏+背景+虚拟导航栏+状态栏+生命周期)
                .setActivityFragmentControl(activityControl)
                //设置BasisActivity 子类按键监听
                .setActivityKeyEventControl(activityControl)
                //配置BasisActivity 子类事件派发相关
                .setActivityDispatchEventControl(activityControl)
                //设置http请求结果全局控制
                .setHttpRequestControl(new HttpRequestControlImpl())
                //配置{@link FastObserver#onError(Throwable)}全局处理
                .setFastObserverControl(impl)
                //设置主页返回键控制-默认效果为2000 毫秒时延退出程序
                .setQuitAppControl(impl)
                //设置ToastUtil全局控制
                .setToastControl(impl);

        //初始化Retrofit配置
//        FastRetrofit.getInstance()
        MFastRetrofit.getInstance()
                //配置全局网络请求BaseUrl
                .setBaseUrl(ApiConstant.BASEURLTest)
                //信任所有证书--也可设置setCertificates(单/双向验证)
                .setCertificates()
                //设置统一请求头
//                .addHeader(header)
//                .addHeader(key,value)
                //设置请求全局log-可设置tag及Level类型
                .setLogEnable(true)
//                .setLogEnable(BuildConfig.DEBUG, TAG, HttpLoggingInterceptor.Level.BODY)
                //设置统一超时--也可单独调用read/write/connect超时(可以设置时间单位TimeUnit)
                //默认20 s
                .setTimeout(30);

        //注意设置baseUrl要以/ 结尾 service 里的方法不要以/打头不然拦截到的url会有问题
        //以下为配置多BaseUrl--默认方式一优先级高 可通过FastRetrofit.getInstance().setHeaderPriorityEnable(true);设置方式二优先级
        //方式一 通过Service 里的method-(如:) 设置 推荐 使用该方式不需设置如方式二的额外Header
//        FastRetrofit.getInstance()
//                .putBaseUrl(ApiConstant.API_UPDATE_APP, BuildConfig.BASE__UPDATE_URL);
        MFastRetrofit.getInstance()
//                .addHeader(" Content-Type","application/json")
                .putBaseUrl(ApiConstant.isRegister, ApiConstant.BASEURLTest);

        MFastRetrofit.getInstance()
//                .addHeader(" Content-Type","application/json")
                .putBaseUrl(ApiConstant.register, ApiConstant.BASEURLTest);

        MFastRetrofit.getInstance()
//                .addHeader(" Content-Type","application/json")
                .putBaseUrl(ApiConstant.doBaseNgariRequest, ApiConstant.BASEURLTest);

        MFastRetrofit.getInstance()
//                .addHeader(" Content-Type","application/json")
                .putBaseUrl(ApiConstant.getConsultsAndRecipes, ApiConstant.BASEURLTest);

        //方式二 通过 Service 里添加特定header设置
        //step1
//        FastRetrofit.getInstance()
//                //设置Header模式优先-默认Method方式优先
//                .setHeaderPriorityEnable(true)
//                .putHeaderBaseUrl(ApiConstant.API_UPDATE_APP_KEY, BuildConfig.BASE__UPDATE_URL);
        //step2
        // 需要step1中baseUrl的方法需要在对应service里增加
        // @Headers({FastRetrofit.BASE_URL_NAME_HEADER + ApiConstant.API_UPDATE_APP_KEY})
        //增加一个Header配置注意FastRetrofit.BASE_URL_NAME_HEADER是必须为step1调用putHeaderBaseUrl方法设置的key
        // 参考com.aries.template.retrofit.service.ApiService#updateApp

        //其它初始化
        Fragmentation.builder()
                // 显示悬浮球 ; 其他Mode:SHAKE: 摇一摇唤出   NONE：隐藏
                .stackViewMode(Fragmentation.NONE)
                .debug(false)

             .install();

        //向系统申请使用USB权限,此过程为异步,建议放在程序启动时调用。

        BasicOper.dc_AUSB_ReqPermission(this);


        XAOP.init(this); //初始化插件
        XAOP.debug(true); //日志打印切片开启
        XAOP.setPriority(Log.INFO); //设置日志打印的等级,默认为0

//设置动态申请权限切片 申请权限被拒绝的事件响应监听
        XAOP.setOnPermissionDeniedListener(new PermissionUtils.OnPermissionDeniedListener() {
            @Override
            public void onDenied(List<String> permissionsDenied) {
                //申请权限被拒绝的处理
            }

        });

//设置自定义拦截切片的处理拦截器
        XAOP.setInterceptor(new Interceptor() {
            @Override
            public boolean intercept(int type, JoinPoint joinPoint) throws Throwable {
                XLogger.d("正在进行拦截，拦截类型:" + type);
                switch(type) {
                    case 1:
                        //做你想要的拦截
                        break;
                    case 2:
                        return true; //return true，直接拦截切片的执行
                    default:
                        break;
                }
                return false;
            }
        });

//设置自动捕获异常的处理者
        XAOP.setIThrowableHandler(new IThrowableHandler() {
            @Override
            public Object handleThrowable(String flag, Throwable throwable) {
                XLogger.d("捕获到异常，异常的flag:" + flag);
//                if (flag.equals(TRY_CATCH_KEY)) {
//                    return 100;
//                }
                return null;
            }
        });

        if(!isIgnoringBatteryOptimizations()){
            requestIgnoreBatteryOptimizations();
        }

        // 初始化并启动 easeMode
        EaseModeProxy.with().initInAPP(getContext());

        // 初始化 xCrash 崩溃提示，会导致 崩溃 不会被打印
        initXCrash();

        //blockcanary 初始化 卡顿
        // 不需要用只需要这里不让他们初始化
//        BlockCanary.install(this, new AppBlockCanaryContext()).start();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isIgnoringBatteryOptimizations() {
        Log.d("111111MODEL", "22222MODEL");
        boolean isIgnoring = false;
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            isIgnoring = powerManager.isIgnoringBatteryOptimizations(getPackageName());
        }
        Log.d("111111MODEL", isIgnoring+"");
        return isIgnoring;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestIgnoreBatteryOptimizations() {
        try {
            Log.d("111111MODEL", "33333MODEL");
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            Log.d("111111MODEL", "44444MODEL");
            e.printStackTrace();
        }
    }

    /**
     * 初始化爱奇艺 XCrash
     * 崩溃恢复系统
     *当APP出现Java异常、native异常和ANR时需要重启当前APP。
     */
    public void initXCrash(){
        XCrash.InitParameters initParameters =new XCrash.InitParameters();
        initParameters.setLogDir("xcrash/temp/");
        initParameters.setAppVersion(AppInfoUtils.getAppName(this));
        ICrashCallback crashCallback = (logPath, emergency) -> {
            Log.d("JTJK", "initXCrash: done");
//            ToastUtil.show("系统出了点小问题，请重新读卡操作");
            SystemUtil.reStart(getContext());

            // 如果崩溃了，就启动视频释放机制
            EaseModeProxy.with().releaseProxy();
        };
        initParameters.setJavaCallback(crashCallback);
        initParameters.setAnrCallback(crashCallback);
        initParameters.setNativeCallback(crashCallback);
        XCrash.init(this,initParameters);

        //Tombstone 文件默认将被写入到 Context#getFilesDir() + "/tombstones" 目录。
        // （通常在： /data/data/PACKAGE_NAME/files/tombstones）
//        XCrash.init(this);
    }

    public static Context getContext() {
        return mContext;
    }



}
