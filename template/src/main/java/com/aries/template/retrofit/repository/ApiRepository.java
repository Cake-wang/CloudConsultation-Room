package com.aries.template.retrofit.repository;


import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.aries.library.fast.retrofit.FastRetryWhen;
import com.aries.library.fast.retrofit.FastTransformer;
import com.aries.library.fast.util.SPUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.template.entity.AuthCodeResultEntity;
import com.aries.template.entity.BaseMovieEntity;
import com.aries.template.entity.CanRequestOnlineConsultResultEntity;
import com.aries.template.entity.CancelregisterResultEntity;
import com.aries.template.entity.CreateOrderResultEntity;
import com.aries.template.entity.FindUserResultEntity;
import com.aries.template.entity.FindValidDepartmentForRevisitResultEntity;
import com.aries.template.entity.FindValidOrganProfessionForRevisitResultEntity;
import com.aries.template.entity.GetConfigurationToThirdForPatientRequestEntity;
import com.aries.template.entity.GetConfigurationToThirdForPatientResultEntity;
import com.aries.template.entity.GetConsultsAndRecipesResultEntity;
import com.aries.template.entity.IsRegisterRequestEntity;
import com.aries.template.entity.IsRegisterResultEntity;
import com.aries.template.entity.MachineEntity;
import com.aries.template.entity.RegisterResultEntity;
import com.aries.template.entity.RequestConsultAndCdrOtherdocResultEntity;
import com.aries.template.entity.SearchDoctorListByBusTypeV2ResultEntity;
import com.aries.template.entity.UpdateEntity;
import com.aries.template.retrofit.service.ApiService;
import com.aries.template.utility.ConvertJavaBean;
import com.aries.template.utility.RSASignature;
import com.aries.template.widget.mgson.MFastRetrofit;
import com.decard.NDKMethod.BasicOper;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * @Author: AriesHoo on 2018/11/19 14:25
 * @E-Mail: AriesHoo@126.com
 * @Function: Retrofit api调用示例
 * @Description:
 */
public class ApiRepository extends BaseRepository {

    private static volatile ApiRepository instance;
    private ApiService mApiService;

    private ApiRepository() {
        mApiService = getApiService();
    }

    public static ApiRepository getInstance() {
        if (instance == null) {
            synchronized (ApiRepository.class) {
                if (instance == null) {
                    instance = new ApiRepository();
                }
            }
        }
        return instance;
    }

    private ApiService getApiService() {
        mApiService = MFastRetrofit.getInstance().createService(ApiService.class);
        return mApiService;
    }

    public static String getDeviceId() {
        String deviceId = null;
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream("/misc/dn")), 1000);
            deviceId = reader.readLine();
            reader.close();
            return deviceId;
        } catch (IOException var3) {
            var3.printStackTrace();
            return Build.SERIAL;
        }
    }

    /**
     * 获取电影列表
     *
     * @param url   拼接URL
     * @param start 起始 下标
     * @param count 请求总数量
     * @return
     */
    public Observable<BaseMovieEntity> getMovie(String url, int start, int count) {
        Map<String, Object> params = new HashMap<>(3);
        params.put("apikey","0b2bdeda43b5688921839c8ecb20399b");
        params.put("start", start);
        params.put("count", count);
        return FastTransformer.switchSchedulers(getApiService().getMovie(url, params).retryWhen(new FastRetryWhen()));
    }

    /**
     * 检查版本--是否传递本地App 版本相关信息根据具体接口而定(demo这里是可以不需要传的,所有判断逻辑放在app端--不推荐)
     *
     * @return
     */
    public Observable<UpdateEntity> updateApp() {
//        Map<String, Object> params = new HashMap<>(2);
//        params.put("versionCode", FastUtil.getVersionCode(App.getContext()));
//        params.put("versionName", FastUtil.getVersionName(App.getContext()));
        return FastTransformer.switchSchedulers(getApiService().updateApp().retryWhen(new FastRetryWhen()));
    }

    public static String getDeviceSN(Context context) {
        Log.d("open","dc_open success devHandle = "+Build.MODEL);

        String serialNumber = android.os.Build.SERIAL;

        String serialNumber_new = "";

        if (Build.MODEL.equals("f11_x1")){

            //向系统申请使用USB权限,此过程为异步,建议放在程序启动时调用。

//            BasicOper.dc_AUSB_ReqPermission(this);

//打开端口，usb模式，打开之前必须确保已经获取到USB权限，返回值为设备句柄号。

            int devHandle = BasicOper.dc_open("COM",null,"/dev/ttyUSB0",115200);
            Log.d("open","dc_open success devHandle = "+devHandle);
            if(devHandle>0){
                Log.d("open","dc_open success devHandle = "+devHandle);

//        if(devHandle>0){
                serialNumber_new =BasicOper.dc_GetDeviceUid().substring(5);
//        }
            }

            //串口使用之前确保设备支持串口，并且已知设备串口路径。

        }else if (Build.MODEL.equals("Z90N")){

            int devHandle1 = BasicOper.dc_open("COM",null,"/dev/ttyHSL1",115200);//返回值为设备句柄号。
            if(devHandle1>0){
                Log.d("openhhhhhh","dc_open success devHandle = "+devHandle1);


//        if(devHandle>0){
                serialNumber_new =BasicOper.dc_GetDeviceUid().substring(5);
//        }
//                        readCard();

//                  mTimeCounterRunnable = new Runnable() {
//                    @Override
//                    public void run() {//在此添加需轮寻的接口
//
//                        mHandler.postDelayed(this, 3* 1000);
//                    }
//                };




            }

        }

        if (TextUtils.isEmpty(serialNumber_new)){
            ToastUtil.showWarning("未获取到设备号");
        }else {



                BasicOper.dc_exit();


        }

        
        return serialNumber_new;
    }

    public static String getUUID() {
//        System.out.println(UUID.randomUUID());
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        return uuid;
    }

    public static class common {

        public String machineId;
        public String userId;

        private static volatile common instance;
        private ApiService mApiService;

//        private common() {
//            mApiService = getApiService();
//        }

        public static common getInstance() {
            if (instance == null) {
                synchronized (common.class) {
                    if (instance == null) {
                        instance = new common();
                    }
                }
            }
            return instance;
        }

        public String getMachineId() {
            return machineId;
        }

        public void setMachineId(String machineId) {
            this.machineId = machineId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }



    public Observable<IsRegisterResultEntity> isRegister(String idCard, Context mContext) {

//        idCard = "33052219861229693X";
//        SPUtil.put(mContext, "termial","YTJ1001");
//        SPUtil.put(mContext,"hosiptalNo", "A0005");
//        SPUtil.put(mContext, "mchntId", "330160400279");
//
//        SPUtil.put(mContext, "posId","1001");

        IsRegisterRequestEntity checkActivationStateReferenceEntity = new IsRegisterRequestEntity();
        checkActivationStateReferenceEntity.setIdCard(idCard);
        String beanstr = ConvertJavaBean.converJavaBeanToJsonNew(checkActivationStateReferenceEntity);
        String beanstrNew = "";
        try {
            beanstrNew = URLEncoder.encode(beanstr,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

//        String privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAOVpyOq/CpPhns2q7S9R83u1kaGXK628rzJT7xDOzPom2CMQvnlEUrmR4K1rLgrPHJpNsE9Y1CffdlM57u8XluPqYiPI3jnSEXwg2EO9/xZpYqMlx/vAWU8E+NJ9wgTouB9cTM1BRtf/gl5MC6D6pm9hWiW3EkOUPEilMUxVpjsdAgMBAAECgYEApQc76OIUkdyzh+91P50MMemjIULFwO0Ceom2Z7jb6vcWc+MPodxsReGSZi27qTrjsHxCYFHeC/DMBYKfRo8Jkv1vznYDkmscKiezNmVJOAUyeGs1yUFRa7PABcNOdPz48zwlwGNcmMXnYOPTUAZGvSDdzS7bRWTObZo6jFgnZmECQQD+P3+DjQ0NDb/43U+pfNkQrk23od12dGP7/VqqtG8unou+JdpUZdGAGJsR4bPd2cSDG+9+6eVn3DIiR2NyDBBHAkEA5v56MBtOI8SWCX+tWteo9ZnkD5Y3W/OH1JJgCz5H8sI3BDvoDxVvyTgdnH4B/8mQi3imqe2cr1+vEuvJE0fPewJBAOuBofoXE7m9vM8nIP1cGi0rZ+3rT5rD4UGvwuZQ0JQcZFEz+vMmhVLkd++uY8/iQfc4TzX+LnNwMjgybFld900CQQCN/ztfnTlwtWVCG5GWeWIs8FgI2N/ZD6CdJQoTf0q9SXSGjsj9lMDuIifZzoAuHFtV0WoqpZ2fFSPLbtRhnNBHAkB/dzzHOH/PFRxHzecR9P/mikDkCGWYSCQXmbOJkrEgLj+I2+QP72zHCGgbsGr8LgtDsxkDxfP9gnnPXpS6tQvt";
//        String privateKey ="MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBAKYSkP/RnkvdQXnG2vUjh3Isrh4Lw/wJu+cfAHZaAxKcqgi3vxr8i5VZvNf3XJupXB5utMrJw3o3Q0jJaTftQzkXW8jS9R1Fi6DhUpvvY9iAKM+wuuFPuTGt6NJLddLr4IV6JU840xPyG4HmfyyKt6ZDHjrzW3tuRvUCbPG/UXEZAgMBAAECgYEApL/wdsiF6C70AJIEr4HyoZKvnQEBCSzC6vK3a7G6MKGYkRNwE56L/rrKe9laVtY5m7spwa9NLBZcun2M0z2eHBDs/FPhutBVNXDw62w/Czl/EGCIjc1+dO+N5PVIlrByne9vCjRsq3o6No6DoFMj56aCgf97kvhF8YbkA5Z85L0CQQDLreS73gdFD34DR+/src/QWWJfiIVapgP5FEP/lFbR33wc0un723HpnbK7bEqNgxMc/vB+3Ijs7q/SYVN+QZ7bAkEA0LufyTaJUBkjnmvVYsAFit72DuN7F2PBQx5N35eNqrc9y/qf+QmCg3JaEQawVWHMZTaIP0MJH4dJEh0peugQGwJBAK2UaRbiRXfJmC7bo9Ek37JEOt4Hzv5N9ZzUdI7fsI3z1CYMaXRGtHHP/35S78ZiKmvPqM20zMx/LkAwyaMT8XkCQQC4ghGFYJLSXdZxdCqEhu6fSeVBVe35x/EO7lux2JqenCmhBGKiVbirjsp0oHAAmVl+4kxfd1c6KnD59RM0lKufAkEAg8KgGdB+cJGPwIImTsPVqdMNTy8ShrCCqIdsG1+pdO1aGRG8ackhDRe0jMCDtKfkDuHzLhxf3CnQVDoFEKoE/A==";


        //正式环境签名
        String privateKey ="MIICXQIBAAKBgQDeppiicqPIfjLjAzW1VKXjP2BsRGBjwh4nYV0C5tD8z+R0NvnLJo7de5icjhhsNnCDn6NFKtLF4WIL97x38nRgKueAD+LYjCPefJ6tZT513tMen9N8BYiUP8+9EyxSKVsVWdBCZnPEWp0GTSpN1sjv6dhk8PYjndfIHalYSY8YXQIDAQABAoGBAJ55p9SgknEnWiL46uaJPJX2SzRkqtL2nS3cgC6LiZ8Yffw2ETAG3tNIoMR1425KhWU6YCTgKSvNk1L/Xzdk7G0easRzCNRQ6EWm3H+c/UBHcDBVfSKq45SwlJvaVlU8A8c8YCmttEBFrKS2YEWln/U8DUSNkrbAs0ni/dCyOGnlAkEA+21tSJYxW2yY939yVHtsrIttCokBR3hp7sZPHEyAcXuslZ/O62k3k3PMZUWEqaXnhiAnIJWr8yrGpcTobwHfwwJBAOKzMYdtoPOswki5temLj7yyTitY9L27hTEgt+Y7y5/oCmB/P3XZlJOtpnFDu+xdJAnEpu1RuCYUUIs00l/kxV8CQGBLTKucOlMViJBh01vf2YNL8vsx9bd1urykXvArrJXKFBNKHWmz5oEmvIWc1m5TCBUqg1HLgQukumgKviqlwRkCQQCCVR9OivqT3Wi9QveQ04nJpIFIbpYWVq7WdccEeLAyuMbuf3nOmU7QMG+WgqiR1WKYsxR9MBQ84EUGI1Ini3DlAkBHyidb8c92GDPKxHG1NS2lMvAKpEOlWhPS18vtEPA1R0oQnRB942l6gekxSRZPUVpNrFXPheVApbZJLpbUaHpS";


//        String privateKey ="MIICXwIBAAKBgQCmEpD/0Z5L3UF5xtr1I4dyLK4eC8P8CbvnHwB2WgMSnKoIt78a/IuVWbzX91ybqVwebrTKycN6N0NIyWk37UM5F1vI0vUdRYug4VKb72PYgCjPsLrhT7kxrejSS3XS6+CFeiVPONMT8huB5n8siremQx4681t7bkb1Amzxv1FxGQIDAQABAoGBAKS/8HbIhegu9ACSBK+B8qGSr50BAQkswuryt2uxujChmJETcBOei/66ynvZWlbWOZu7KcGvTSwWXLp9jNM9nhwQ7PxT4brQVTVw8OtsPws5fxBgiI3NfnTvjeT1SJawcp3vbwo0bKt6OjaOg6BTI+emgoH/e5L4RfGG5AOWfOS9AkEAy63ku94HRQ9+A0fv7K3P0FliX4iFWqYD+RRD/5RW0d98HNLp+9tx6Z2yu2xKjYMTHP7wftyI7O6v0mFTfkGe2wJBANC7n8k2iVAZI55r1WLABYre9g7jexdjwUMeTd+Xjaq3Pcv6n/kJgoNyWhEGsFVhzGU2iD9DCR+HSRIdKXroEBsCQQCtlGkW4kV3yZgu26PRJN+yRDreB87+TfWc1HSO37CN89QmDGl0RrRxz/9+Uu/GYiprz6jNtMzMfy5AMMmjE/F5AkEAuIIRhWCS0l3WcXQqhIbun0nlQVXt+cfxDu5bsdianpwpoQRiolW4q47KdKBwAJlZfuJMX3dXOipw+fUTNJSrnwJBAIPCoBnQfnCRj8CCJk7D1anTDU8vEoawgqiHbBtfqXTtWhkRvGnJIQ0XtIzAg7Sn5A7h8y4cX9wp0FQ6BRCqBPw=";

        Date date = new Date();
        String reqSeq = new SimpleDateFormat("yyyyMMddHHmmss").format(date);

        String signSource = "bizContent=idCard"+idCard+"&hosiptalNo="+SPUtil.get(mContext,"hosiptalNo","")+"&mchntId="+SPUtil.get(mContext,"mchntId","")+"&posId="+SPUtil.get(mContext,"posId","")+"&terminal="+SPUtil.get(mContext,"termial","")+"&timestamp="+reqSeq+"";
        Log.d("timestamp",signSource);
        String signTarget = null;
        try {
            signTarget = RSASignature.sign(signSource, privateKey);

        } catch (Exception e) {
            e.printStackTrace();
        }




        Map<String, Object> params = new HashMap<>(4);
        params.put("logTraceId", getUUID());
//        params.put("methodCode","");
//        params.put("mchntId", SPUtil.get(mContext,"mchntId",""));
//        params.put("hosiptalNo", SPUtil.get(mContext,"hosiptalNo",""));
//        params.put("terminal", SPUtil.get(mContext,"termial","")+"_5");
        params.put("common", common.getInstance());
        params.put("bizContent", ""+beanstr+"");
        params.put("sign", signTarget);

//        Log.d("timestamp",reqSeq);
//        Log.d("timestamp", (String) SPUtil.get(mContext,"posId",""));
//        Log.d("timestamp",(String) SPUtil.get(mContext,"mchntId",""));
//        Log.d("timestamp",(String) SPUtil.get(mContext,"hosiptalNo",""));
//        Log.d("timestamp",(String) SPUtil.get(mContext,"termial",""));
//        Log.d("timestamp",idCard);
//        Log.d("timestamp",""+beanstr+"");
//        Log.d("timestamp",signTarget);

        String strEntity = ConvertJavaBean.converJavaBeanToJsonNew(params);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("Content-Type:application/json;charset=UTF-8"),strEntity);

//        params.put("clientVerison", FastUtil.getVersionName(App.getContext()));
        return FastTransformer.switchSchedulers(getApiService().isRegister(body).retryWhen(new FastRetryWhen()));
    }

    /**
     * 用户信息查询
     * 用于验证用户是否注册。
     * @param idCard 身份证 可能对应 SSNUM
     */
    public Observable<FindUserResultEntity> findUser(String idCard) {
        // 除了公共的数据之外，还有其他的数据请求
        Map<String,String> bizContent = new HashMap<>();
        bizContent.put("idCard",idCard);
        // 请求的类型 findValidOrganProfessionForRevisit
        RequestBody body = BodyCreate(bizContent,"",false);
        return FastTransformer.switchSchedulers(getApiService().findUser(body).retryWhen(new FastRetryWhen()));
    }

    /**
     * 通过手机号注册
     * @param idCard 证件号码
     * @param name 姓名
     * @param mobile 手机号
     * @param authCode 验证码
     * @param authCodeId 验证码ID
     */
    public Observable<RegisterResultEntity> register(String idCard,String name,String mobile,String authCode,String authCodeId) {
        // 除了公共的数据之外，还有其他的数据请求
        Map<String,String> bizContent = new HashMap<>();
        bizContent.put("idCard",idCard);
        bizContent.put("name",name);
        bizContent.put("mobile",mobile);
        bizContent.put("authCode",authCode);
        bizContent.put("authCodeId",authCodeId);
        // 请求的类型 findValidOrganProfessionForRevisit
        RequestBody body = BodyCreate(bizContent,"",false);
        return FastTransformer.switchSchedulers(getApiService().register(body).retryWhen(new FastRetryWhen()));
    }

    /**
     * 手机号验证码
     * 获取手机号注册的验证码
     * 这个接口只在申请注册的时候，使用
     * 这个验证码会以手机短信的形式注册在
     */
    public Observable<AuthCodeResultEntity> authCode(String mobile) {
        // 除了公共的数据之外，还有其他的数据请求
        Map<String,String> bizContent = new HashMap<>();
        bizContent.put("phoneNo",mobile);
        // 请求的类型 findValidOrganProfessionForRevisit
        RequestBody body = BodyCreate(bizContent,"",false);
        return FastTransformer.switchSchedulers(getApiService().getAuthCode(body).retryWhen(new FastRetryWhen()));
    }

    /**
     * 取消待支付挂号单
     * @param consultId 挂号单单号
     */
    public Observable<CancelregisterResultEntity> patientCancelGraphicTextConsult(Integer consultId) {
        Map<String,String> bizContent = new HashMap<>();
        bizContent.put("consultId",String.valueOf(consultId));
        RequestBody body = BodyCreate(bizContent,"patientCancelGraphicTextConsult");
        return FastTransformer.switchSchedulers(getApiService().patientCancelGraphicTextConsult(body).retryWhen(new FastRetryWhen()));
    }

    public Observable<CancelregisterResultEntity> patientFinishGraphicTextConsult(Integer consultId) {
        Map<String,String> bizContent = new HashMap<>();
//        bizContent.put("consultId",String.valueOf(consultId));
        bizContent.put("consultId","815423835");
        RequestBody body = BodyCreate(bizContent,"patientCancelGraphicTextConsult");
        return FastTransformer.switchSchedulers(getApiService().patientCancelGraphicTextConsult(body).retryWhen(new FastRetryWhen()));
    }

    /**
     * 获得复诊按机构查找一级科室
     */
    public Observable<FindValidOrganProfessionForRevisitResultEntity> findValidOrganProfessionForRevisit(Integer organId) {
        // 除了公共的数据之外，还有其他的数据请求
        Map<String,String> bizContent = new HashMap<>();
        bizContent.put("organId",String.valueOf(organId));
        // 请求的类型 findValidOrganProfessionForRevisit
        RequestBody body = BodyCreate(bizContent,"findValidOrganProfessionForRevisit");
        return FastTransformer.switchSchedulers(getApiService().findValidOrganProfessionForRevisit(body).retryWhen(new FastRetryWhen()));
    }


    /**
     * 按照机构和科室，查找二级科室
     */
    public Observable<FindValidDepartmentForRevisitResultEntity> findValidDepartmentForRevisit(Integer organId, String organProfessionId, Context mContext) {
        // 除了公共的数据之外，还有其他的数据请求
        Map<String,String> bizContent = new HashMap<>();
        bizContent.put("organId",String.valueOf(organId));
        bizContent.put("organProfessionId",String.valueOf(organProfessionId));
        // 请求的类型 findValidDepartmentForRevisit
        RequestBody body = BodyCreate(bizContent,"findValidDepartmentForRevisit");
        return FastTransformer.switchSchedulers(getApiService().findValidDepartmentForRevisit(body).retryWhen(new FastRetryWhen()));
    }



    /**
     * 请求医生列表接口
     * @param departmentId 行政科室编码
     * @param profession 专科编码
     * @param organId  机构Id
     */
    public Observable<SearchDoctorListByBusTypeV2ResultEntity> searchDoctorListByBusTypeV2(
            String departmentId,
            String profession,
            int organId){
        // 除了公共的数据之外，还有其他的数据请求
        Map<String,String> bizContent = new HashMap<>();
        bizContent.put("departmentId",String.valueOf(departmentId));
        bizContent.put("profession",String.valueOf(profession));
        bizContent.put("organId",String.valueOf(organId));
        bizContent.put("sortKey",String.valueOf(1));//排序类型(1:综合排序，2:复诊价格排序，3:复诊量排序，4: 好评排序)
        bizContent.put("recipeConsultSourceFlag",String.valueOf(2));//医生类型(1：平台排班，2：无排班，3：his排班，4：医生自主排班)
        bizContent.put("start",String.valueOf(0));//开始index点//todo 这个是否需要，还是一次性拿出来？
        bizContent.put("limit",String.valueOf(10));//结束index点//todo cc
        bizContent.put("packageFlag",String.valueOf(1));//是否快速返回，快速返回没有职业点和扩展信息
        bizContent.put("search","");//是否快速返回，快速返回没有职业点和扩展信息
        // 请求的类型 searchDoctorListByBusTypeV2
        RequestBody body = BodyCreate(bizContent,"searchDoctorListByBusTypeV2");
        return FastTransformer.switchSchedulers(getApiService().searchDoctorListByBusTypeV2(body).retryWhen(new FastRetryWhen()));
    }

    /**
     * 能否发起复诊
     * @param doctorId 医生ID
     */
    public Observable<CanRequestOnlineConsultResultEntity> canRequestOnlineConsult(Long doctorId) {
        // 除了公共的数据之外，还有其他的数据请求
        Map<String,String> bizContent = new HashMap<>();
        bizContent.put("doctorId",String.valueOf(doctorId));
        // 请求的类型 canRequestOnlineConsult
        RequestBody body = BodyCreate(bizContent,"canRequestOnlineConsult");
        return FastTransformer.switchSchedulers(getApiService().canRequestOnlineConsult(body).retryWhen(new FastRetryWhen()));
    }

    /**
     * 确认并发起复诊
     * @param consultOrgan 复诊医生机构
     * @param consultDepart 复诊医生科室
     * @param consultDoctor 复诊医生
     * @param mpiid userId
     */
    public Observable<RequestConsultAndCdrOtherdocResultEntity> requestConsultAndCdrOtherdoc(Long consultOrgan,
                                                                                             String mpiid,
                                                                                             String consultDepart,
                                                                                             Long consultDoctor) {
//        Map<String,String> questionnaire =new HashMap<>(); //问卷单对象（详见questionnaire详细描述）
//        questionnaire.put("pregnent",String.valueOf(0)); //是否怀孕 -1：男 0:无 1:有
////        questionnaire.put("pregnentMemo","");
//        questionnaire.put("alleric",String.valueOf(0));//有无过敏史 0:无 1:有
////        questionnaire.put("allericMemo","");
//        questionnaire.put("proposedDrugs","鲜铁皮石斛");//既往用药（多个药品用、隔开）
//        questionnaire.put("haveReaction",String.valueOf(0));//服药后不良反应 0:无 1:有
////        questionnaire.put("haveReactionMemo","");
//        questionnaire.put("disease","");//确诊疾病
//        questionnaire.put("confirmedDate","");//确诊时间，如 2019-04-03
////        questionnaire.put("returnVisitStatus",String.valueOf(1));
//
//        Map<String,String> cdrOtherdocs =new HashMap<>(); //病历数据
//        cdrOtherdocs.put("docType",String.valueOf(9));//文档类型，默认填9 0门诊病历 1检验报告 2检查报告 10体检报告 3处方 4治疗记录 5住院病历 6医嘱 7医学影像 8病患部位 9其他
//        cdrOtherdocs.put("docName","20210522135342oz0d.jpg");//文件名，包含后缀
//        cdrOtherdocs.put("docFormat",String.valueOf(13));//文档格式 01 CDA； 02 BSXML； 11 HTMLX；12 PDF； 13 JPG
//        cdrOtherdocs.put("docContent","60a89c66f0f97817f591851c");//文件上传或返回的文件id

        Map<String,Object> bizContent = new HashMap<>();
//        bizContent.put("appClientType",String.valueOf(appClientType));
//        bizContent.put("mpiid","2c95818f80b0ab390180b0db16ea0000");//就诊人索引
        bizContent.put("mpiid",mpiid);//就诊人索引
        bizContent.put("appClientType","APP_WEB");//由纳里平台分配的公司标识，固定写死
        bizContent.put("appType","ngari-health");//由纳里平台分配的公司标识，固定写死
        bizContent.put("requestMode",String.valueOf(4));//类型，复诊固定为4
//        bizContent.put("consultOrgan",String.valueOf(2000300));//复诊医生机构
        bizContent.put("consultOrgan",String.valueOf(consultOrgan));//复诊医生机构
//        bizContent.put("consultDepart",String.valueOf(18804));//复诊医生科室
        bizContent.put("consultDepart",String.valueOf(consultDepart));//复诊医生科室
//        bizContent.put("consultDoctor",String.valueOf(111733));//复诊医生
        bizContent.put("consultDoctor",String.valueOf(consultDoctor));//复诊医生
//        bizContent.put("consultCost",String.valueOf(0));
//        bizContent.put("consultPrice",String.valueOf(0));
//        bizContent.put("leaveMess","");
//        bizContent.put("questionnaire",questionnaire);
//        bizContent.put("cdrOtherdocs",cdrOtherdocs);

        RequestBody body = BodyCreate(bizContent,"requestConsultAndCdrOtherdoc");
        return FastTransformer.switchSchedulers(getApiService().requestConsultAndCdrOtherdoc(body).retryWhen(new FastRetryWhen()));
    }

    public Observable<RequestConsultAndCdrOtherdocResultEntity> presettlement(String appKey, String tid, String appClientType, Context mContext) {

//        idCard = "33052219861229693X";
//        SPUtil.put(mContext, "termial","YTJ1001");
//        SPUtil.put(mContext,"hosiptalNo", "A0005");
//        SPUtil.put(mContext, "mchntId", "330160400279");
//
//        SPUtil.put(mContext, "posId","1001");

//        CanRequestOnlineConsultRequestEntity checkActivationStateReferenceEntity = new CanRequestOnlineConsultRequestEntity();
//        checkActivationStateReferenceEntity.setAppKey(appKey);
//        checkActivationStateReferenceEntity.setDoctorId(doctorId);
//        checkActivationStateReferenceEntity.setTid(tid);
        Map<String,String> bizContent = new HashMap<>();
        bizContent.put("appClientType",appClientType);
        RequestBody body = BodyCreate(bizContent,"requestConsultAndCdrOtherdoc");
        return FastTransformer.switchSchedulers(getApiService().presettlement(body).retryWhen(new FastRetryWhen()));
    }

    public Observable<RequestConsultAndCdrOtherdocResultEntity> paySuccess(String appKey, String tid, String appClientType, Context mContext) {

//        idCard = "33052219861229693X";
//        SPUtil.put(mContext, "termial","YTJ1001");
//        SPUtil.put(mContext,"hosiptalNo", "A0005");
//        SPUtil.put(mContext, "mchntId", "330160400279");
//
//        SPUtil.put(mContext, "posId","1001");

//        CanRequestOnlineConsultRequestEntity checkActivationStateReferenceEntity = new CanRequestOnlineConsultRequestEntity();
//        checkActivationStateReferenceEntity.setAppKey(appKey);
//        checkActivationStateReferenceEntity.setDoctorId(doctorId);
//        checkActivationStateReferenceEntity.setTid(tid);
        Map<String,String> bizContent = new HashMap<>();
        bizContent.put("appClientType",appClientType);
        RequestBody body = BodyCreate(bizContent,"requestConsultAndCdrOtherdoc");
        return FastTransformer.switchSchedulers(getApiService().paySuccess(body).retryWhen(new FastRetryWhen()));
    }

    public Observable<GetConfigurationToThirdForPatientResultEntity> getConfigurationToThirdForPatient(String thirdParty, String tid, Context mContext) {

//        idCard = "33052219861229693X";
//        SPUtil.put(mContext, "termial","YTJ1001");
//        SPUtil.put(mContext,"hosiptalNo", "A0005");
//        SPUtil.put(mContext, "mchntId", "330160400279");
//
//        SPUtil.put(mContext, "posId","1001");

        GetConfigurationToThirdForPatientRequestEntity checkActivationStateReferenceEntity = new GetConfigurationToThirdForPatientRequestEntity();
        checkActivationStateReferenceEntity.setThirdParty(thirdParty);

        checkActivationStateReferenceEntity.setTid(tid);
        String beanstr = ConvertJavaBean.converJavaBeanToJsonNew(checkActivationStateReferenceEntity);
        String beanstrNew = "";
        try {
            beanstrNew = URLEncoder.encode(beanstr,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

//        String privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAOVpyOq/CpPhns2q7S9R83u1kaGXK628rzJT7xDOzPom2CMQvnlEUrmR4K1rLgrPHJpNsE9Y1CffdlM57u8XluPqYiPI3jnSEXwg2EO9/xZpYqMlx/vAWU8E+NJ9wgTouB9cTM1BRtf/gl5MC6D6pm9hWiW3EkOUPEilMUxVpjsdAgMBAAECgYEApQc76OIUkdyzh+91P50MMemjIULFwO0Ceom2Z7jb6vcWc+MPodxsReGSZi27qTrjsHxCYFHeC/DMBYKfRo8Jkv1vznYDkmscKiezNmVJOAUyeGs1yUFRa7PABcNOdPz48zwlwGNcmMXnYOPTUAZGvSDdzS7bRWTObZo6jFgnZmECQQD+P3+DjQ0NDb/43U+pfNkQrk23od12dGP7/VqqtG8unou+JdpUZdGAGJsR4bPd2cSDG+9+6eVn3DIiR2NyDBBHAkEA5v56MBtOI8SWCX+tWteo9ZnkD5Y3W/OH1JJgCz5H8sI3BDvoDxVvyTgdnH4B/8mQi3imqe2cr1+vEuvJE0fPewJBAOuBofoXE7m9vM8nIP1cGi0rZ+3rT5rD4UGvwuZQ0JQcZFEz+vMmhVLkd++uY8/iQfc4TzX+LnNwMjgybFld900CQQCN/ztfnTlwtWVCG5GWeWIs8FgI2N/ZD6CdJQoTf0q9SXSGjsj9lMDuIifZzoAuHFtV0WoqpZ2fFSPLbtRhnNBHAkB/dzzHOH/PFRxHzecR9P/mikDkCGWYSCQXmbOJkrEgLj+I2+QP72zHCGgbsGr8LgtDsxkDxfP9gnnPXpS6tQvt";
//        String privateKey ="MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBAKYSkP/RnkvdQXnG2vUjh3Isrh4Lw/wJu+cfAHZaAxKcqgi3vxr8i5VZvNf3XJupXB5utMrJw3o3Q0jJaTftQzkXW8jS9R1Fi6DhUpvvY9iAKM+wuuFPuTGt6NJLddLr4IV6JU840xPyG4HmfyyKt6ZDHjrzW3tuRvUCbPG/UXEZAgMBAAECgYEApL/wdsiF6C70AJIEr4HyoZKvnQEBCSzC6vK3a7G6MKGYkRNwE56L/rrKe9laVtY5m7spwa9NLBZcun2M0z2eHBDs/FPhutBVNXDw62w/Czl/EGCIjc1+dO+N5PVIlrByne9vCjRsq3o6No6DoFMj56aCgf97kvhF8YbkA5Z85L0CQQDLreS73gdFD34DR+/src/QWWJfiIVapgP5FEP/lFbR33wc0un723HpnbK7bEqNgxMc/vB+3Ijs7q/SYVN+QZ7bAkEA0LufyTaJUBkjnmvVYsAFit72DuN7F2PBQx5N35eNqrc9y/qf+QmCg3JaEQawVWHMZTaIP0MJH4dJEh0peugQGwJBAK2UaRbiRXfJmC7bo9Ek37JEOt4Hzv5N9ZzUdI7fsI3z1CYMaXRGtHHP/35S78ZiKmvPqM20zMx/LkAwyaMT8XkCQQC4ghGFYJLSXdZxdCqEhu6fSeVBVe35x/EO7lux2JqenCmhBGKiVbirjsp0oHAAmVl+4kxfd1c6KnD59RM0lKufAkEAg8KgGdB+cJGPwIImTsPVqdMNTy8ShrCCqIdsG1+pdO1aGRG8ackhDRe0jMCDtKfkDuHzLhxf3CnQVDoFEKoE/A==";


        //正式环境签名
        String privateKey ="MIICXQIBAAKBgQDeppiicqPIfjLjAzW1VKXjP2BsRGBjwh4nYV0C5tD8z+R0NvnLJo7de5icjhhsNnCDn6NFKtLF4WIL97x38nRgKueAD+LYjCPefJ6tZT513tMen9N8BYiUP8+9EyxSKVsVWdBCZnPEWp0GTSpN1sjv6dhk8PYjndfIHalYSY8YXQIDAQABAoGBAJ55p9SgknEnWiL46uaJPJX2SzRkqtL2nS3cgC6LiZ8Yffw2ETAG3tNIoMR1425KhWU6YCTgKSvNk1L/Xzdk7G0easRzCNRQ6EWm3H+c/UBHcDBVfSKq45SwlJvaVlU8A8c8YCmttEBFrKS2YEWln/U8DUSNkrbAs0ni/dCyOGnlAkEA+21tSJYxW2yY939yVHtsrIttCokBR3hp7sZPHEyAcXuslZ/O62k3k3PMZUWEqaXnhiAnIJWr8yrGpcTobwHfwwJBAOKzMYdtoPOswki5temLj7yyTitY9L27hTEgt+Y7y5/oCmB/P3XZlJOtpnFDu+xdJAnEpu1RuCYUUIs00l/kxV8CQGBLTKucOlMViJBh01vf2YNL8vsx9bd1urykXvArrJXKFBNKHWmz5oEmvIWc1m5TCBUqg1HLgQukumgKviqlwRkCQQCCVR9OivqT3Wi9QveQ04nJpIFIbpYWVq7WdccEeLAyuMbuf3nOmU7QMG+WgqiR1WKYsxR9MBQ84EUGI1Ini3DlAkBHyidb8c92GDPKxHG1NS2lMvAKpEOlWhPS18vtEPA1R0oQnRB942l6gekxSRZPUVpNrFXPheVApbZJLpbUaHpS";


//        String privateKey ="MIICXwIBAAKBgQCmEpD/0Z5L3UF5xtr1I4dyLK4eC8P8CbvnHwB2WgMSnKoIt78a/IuVWbzX91ybqVwebrTKycN6N0NIyWk37UM5F1vI0vUdRYug4VKb72PYgCjPsLrhT7kxrejSS3XS6+CFeiVPONMT8huB5n8siremQx4681t7bkb1Amzxv1FxGQIDAQABAoGBAKS/8HbIhegu9ACSBK+B8qGSr50BAQkswuryt2uxujChmJETcBOei/66ynvZWlbWOZu7KcGvTSwWXLp9jNM9nhwQ7PxT4brQVTVw8OtsPws5fxBgiI3NfnTvjeT1SJawcp3vbwo0bKt6OjaOg6BTI+emgoH/e5L4RfGG5AOWfOS9AkEAy63ku94HRQ9+A0fv7K3P0FliX4iFWqYD+RRD/5RW0d98HNLp+9tx6Z2yu2xKjYMTHP7wftyI7O6v0mFTfkGe2wJBANC7n8k2iVAZI55r1WLABYre9g7jexdjwUMeTd+Xjaq3Pcv6n/kJgoNyWhEGsFVhzGU2iD9DCR+HSRIdKXroEBsCQQCtlGkW4kV3yZgu26PRJN+yRDreB87+TfWc1HSO37CN89QmDGl0RrRxz/9+Uu/GYiprz6jNtMzMfy5AMMmjE/F5AkEAuIIRhWCS0l3WcXQqhIbun0nlQVXt+cfxDu5bsdianpwpoQRiolW4q47KdKBwAJlZfuJMX3dXOipw+fUTNJSrnwJBAIPCoBnQfnCRj8CCJk7D1anTDU8vEoawgqiHbBtfqXTtWhkRvGnJIQ0XtIzAg7Sn5A7h8y4cX9wp0FQ6BRCqBPw=";

        Date date = new Date();
        String reqSeq = new SimpleDateFormat("yyyyMMddHHmmss").format(date);

        String signSource = "bizContent=idCard"+""+"&hosiptalNo="+SPUtil.get(mContext,"hosiptalNo","")+"&mchntId="+SPUtil.get(mContext,"mchntId","")+"&posId="+SPUtil.get(mContext,"posId","")+"&terminal="+SPUtil.get(mContext,"termial","")+"&timestamp="+reqSeq+"";
        Log.d("timestamp",signSource);
        String signTarget = null;
        try {
            signTarget = RSASignature.sign(signSource, privateKey);

        } catch (Exception e) {
            e.printStackTrace();
        }




        Map<String, Object> params = new HashMap<>(4);
        params.put("logTraceId", getUUID());
        params.put("methodCode","getConfigurationToThirdForPatient");
//        params.put("mchntId", SPUtil.get(mContext,"mchntId",""));
//        params.put("hosiptalNo", SPUtil.get(mContext,"hosiptalNo",""));
//        params.put("terminal", SPUtil.get(mContext,"termial","")+"_5");
        params.put("common", common.getInstance());
        params.put("bizContent", ""+beanstr+"");
        params.put("sign", signTarget);

//        Log.d("timestamp",reqSeq);
//        Log.d("timestamp", (String) SPUtil.get(mContext,"posId",""));
//        Log.d("timestamp",(String) SPUtil.get(mContext,"mchntId",""));
//        Log.d("timestamp",(String) SPUtil.get(mContext,"hosiptalNo",""));
//        Log.d("timestamp",(String) SPUtil.get(mContext,"termial",""));
//        Log.d("timestamp",idCard);
//        Log.d("timestamp",""+beanstr+"");
//        Log.d("timestamp",signTarget);

        String strEntity = ConvertJavaBean.converJavaBeanToJsonNew(params);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("Content-Type:application/json;charset=UTF-8"),strEntity);

//        params.put("clientVerison", FastUtil.getVersionName(App.getContext()));
        return FastTransformer.switchSchedulers(getApiService().getConfigurationToThirdForPatient(body).retryWhen(new FastRetryWhen()));
    }

    /**
     * 确认处方单信息并结算
     */
    public Observable<CreateOrderResultEntity> createOrder(String recipeId, String payway, String decoctionFlag, String payMode) {
        Map<String,String> recipeOrder =new HashMap<>(); //病历数据
        recipeOrder.put("payway",payway);//支付类型代码 微信：40 卫宁付：111
        recipeOrder.put("decoctionFlag",decoctionFlag);//是否代煎 1：代煎，0：不代煎
//        recipeOrder.put("gfFeeFlag",String.valueOf(0));//是否收取制作费 1：表示需要制作费，0：不需要

        recipeOrder.put("payMode",payMode);//支付方式代码
        recipeOrder.put("addressId","");
        recipeOrder.put("decoctionId","");
        recipeOrder.put("depId","");
        recipeOrder.put("expressFee","");

        // 除了公共的数据之外，还有其他的数据请求
        Map<String,Object> bizContent = new HashMap<>();
//        bizContent.put("recipeId",String.valueOf(2257979));//电子处方ID
        bizContent.put("recipeId",recipeId);//电子处方ID
        bizContent.put("recipeOrder",recipeOrder);//处方订单信息
        // 请求的类型 findValidOrganProfessionForRevisit
        RequestBody body = BodyCreate(bizContent,"createOrder");
        return FastTransformer.switchSchedulers(getApiService().createOrder(body).retryWhen(new FastRetryWhen()));
    }

    /**
     * 获取复诊和处方列表
     * 获取处方单 10 页
     * 获取挂号单 10 页
     * todo 会不会获取的单号过小，找不到待支付。
     */
    public Observable<GetConsultsAndRecipesResultEntity> getConsultsAndRecipes() {
        // 除了公共的数据之外，还有其他的数据请求
        Map<String,String> bizContent = new HashMap<>();
        bizContent.put("startPage","0");//复诊分页开始标记(默认每页十条，从0开始)
        bizContent.put("requestMode","4");//复诊类型,固定为4
        bizContent.put("tabStatus","ongoing");//状态标志位,ongoing进行中tab，isover已完成tab
        bizContent.put("recipeIndex","0");//处方分页起始位置
        bizContent.put("recipeLimit","10");//处方每页查询量(最大不超过20)
        // 请求的类型
        RequestBody body = BodyCreate(bizContent,"",false);
        return FastTransformer.switchSchedulers(getApiService().getConsultsAndRecipes(body).retryWhen(new FastRetryWhen()));
    }

    /**
     * 根据机器ID获取机器信息
     * @param machineId 机器ID
     */
    public Observable<MachineEntity> findByMachineId(String machineId) {
        // 除了公共的数据之外，还有其他的数据请求
        Map<String,String> bizContent = new HashMap<>();
        bizContent.put("machineId",machineId);//复诊分页开始标记(默认每页十条，从0开始)
        // 请求的类型
        RequestBody body = BodyCreate(bizContent,"",false);
        return FastTransformer.switchSchedulers(getApiService().findByMachineId(body).retryWhen(new FastRetryWhen()));
    }

}
