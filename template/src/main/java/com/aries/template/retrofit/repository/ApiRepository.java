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
import com.aries.template.entity.BatchCreateOrderEntity;
import com.aries.template.entity.CanRequestOnlineConsultResultEntity;
import com.aries.template.entity.CancelregisterResultEntity;
import com.aries.template.entity.ConfigurationToThirdForPatientEntity;
import com.aries.template.entity.CreateOrderResultEntity;
import com.aries.template.entity.FindRecipesForPatientAndTabStatusEntity;
import com.aries.template.entity.FindUserResultEntity;
import com.aries.template.entity.FindValidDepartmentForRevisitResultEntity;
import com.aries.template.entity.FindValidOrganProfessionForRevisitResultEntity;
import com.aries.template.entity.GetConfigurationToThirdForPatientRequestEntity;
import com.aries.template.entity.GetConfigurationToThirdForPatientResultEntity;
import com.aries.template.entity.GetConsultAndPatientAndDoctorByIdEntity;
import com.aries.template.entity.GetConsultsAndRecipesResultEntity;
import com.aries.template.entity.GetMedicalInfoEntity;
import com.aries.template.entity.GetPatientRecipeByIdEntity;
import com.aries.template.entity.GetRecipeListByConsultIdEntity;
import com.aries.template.entity.GetStockInfoEntity;
import com.aries.template.entity.IsRegisterRequestEntity;
import com.aries.template.entity.IsRegisterResultEntity;
import com.aries.template.entity.MachineEntity;
import com.aries.template.entity.PatientFinishGraphicTextConsultEntity;
import com.aries.template.entity.PatientListEntity;
import com.aries.template.entity.PayOrderEntity;
import com.aries.template.entity.PrescriptionPushEntity;
import com.aries.template.entity.RegisterResultEntity;
import com.aries.template.entity.RequestConsultAndCdrOtherdocResultEntity;
import com.aries.template.entity.RoomIdInsAuthEntity;
import com.aries.template.entity.SearchDoctorListByBusTypeV2ResultEntity;
import com.aries.template.entity.UpdateEntity;
import com.aries.template.retrofit.service.ApiService;
import com.aries.template.utility.ConvertJavaBean;
import com.aries.template.utility.JTJSONUtils;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
        public String thirdMachineId;
        public String thirdFactory;
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

        public String getThirdMachineId() {
            return thirdMachineId;
        }

        public void setThirdMachineId(String thirdMachineId) {
            this.thirdMachineId = thirdMachineId;
        }

        public String getThirdFactory() {
            return thirdFactory;
        }

        public void setThirdFactory(String thirdFactory) {
            this.thirdFactory = thirdFactory;
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
     * 3.1.16 患者取消复诊服务
     * @param consultId 挂号单单号
     */
    public Observable<CancelregisterResultEntity> patientCancelGraphicTextConsult(String consultId) {
        Map<String,String> bizContent = new HashMap<>();
        bizContent.put("consultId",consultId);
        RequestBody body = BodyCreate(bizContent,"patientCancelGraphicTextConsult");
        return FastTransformer.switchSchedulers(getApiService().patientCancelGraphicTextConsult(body).retryWhen(new FastRetryWhen()));
    }

//    public Observable<CancelregisterResultEntity> patientFinishGraphicTextConsult(Integer consultId) {
//        Map<String,String> bizContent = new HashMap<>();
////        bizContent.put("consultId",String.valueOf(consultId));
//        bizContent.put("consultId","815423835");
//        RequestBody body = BodyCreate(bizContent,"patientCancelGraphicTextConsult");
//        return FastTransformer.switchSchedulers(getApiService().patientCancelGraphicTextConsult(body).retryWhen(new FastRetryWhen()));
//    }

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
                                                                                             String alleric,
                                                                                             String haveReaction,
                                                                                             String confirmedDate,
                                                                                             String returnVisitStatus,
                                                                                             String consultCost,
                                                                                             String consultPrice,
                                                                                             Long consultDoctor) {
        Map<String,String> questionnaire =new HashMap<>(); //问卷单对象（详见questionnaire详细描述）
//        questionnaire.put("pregnent",pregnent); //是否怀孕 -1：男 0:无 1:有
//        questionnaire.put("pregnentMemo","");
        questionnaire.put("alleric",alleric);//有无过敏史 0:无 1:有
//        questionnaire.put("allericMemo","");
//        questionnaire.put("proposedDrugs","鲜铁皮石斛");//既往用药（多个药品用、隔开）
        questionnaire.put("haveReaction",haveReaction);//服药后不良反应 0:无 1:有
//        questionnaire.put("haveReactionMemo","");
//        questionnaire.put("disease","");//确诊疾病
        questionnaire.put("confirmedDate",confirmedDate);//确诊时间，如 2019-04-03
        questionnaire.put("returnVisitStatus",returnVisitStatus);// 复诊情况选择。-1:未选 0:本院同医生复诊 1:本院非同医生复诊 2:非本院复诊
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
        bizContent.put("consultOrgan",String.valueOf(consultOrgan));//复诊医生机构
        bizContent.put("consultDepart",String.valueOf(consultDepart));//复诊医生科室
        bizContent.put("consultDoctor",String.valueOf(consultDoctor));//复诊医生
        bizContent.put("consultCost",String.valueOf(consultCost));
        bizContent.put("consultPrice",String.valueOf(consultPrice));
//        bizContent.put("leaveMess","");
        bizContent.put("questionnaire",questionnaire);
//        bizContent.put("cdrOtherdocs",cdrOtherdocs);

        RequestBody body = BodyCreate(bizContent,"requestConsultAndCdrOtherdoc");
        return FastTransformer.switchSchedulers(getApiService().requestConsultAndCdrOtherdoc(body).retryWhen(new FastRetryWhen()));
    }

//    public Observable<RequestConsultAndCdrOtherdocResultEntity> presettlement(String appKey, String tid, String appClientType, Context mContext) {
////        idCard = "33052219861229693X";
////        SPUtil.put(mContext, "termial","YTJ1001");
////        SPUtil.put(mContext,"hosiptalNo", "A0005");
////        SPUtil.put(mContext, "mchntId", "330160400279");
////
////        SPUtil.put(mContext, "posId","1001");
//
////        CanRequestOnlineConsultRequestEntity checkActivationStateReferenceEntity = new CanRequestOnlineConsultRequestEntity();
////        checkActivationStateReferenceEntity.setAppKey(appKey);
////        checkActivationStateReferenceEntity.setDoctorId(doctorId);
////        checkActivationStateReferenceEntity.setTid(tid);
//        Map<String,String> bizContent = new HashMap<>();
//        bizContent.put("appClientType",appClientType);
//        RequestBody body = BodyCreate(bizContent,"requestConsultAndCdrOtherdoc");
//        return FastTransformer.switchSchedulers(getApiService().presettlement(body).retryWhen(new FastRetryWhen()));
//    }

//    /**
//     * 发起复诊接口
//     */
//    public Observable<RequestConsultAndCdrOtherdocResultEntity> paySuccess(String appKey, String tid, String appClientType, Context mContext) {
////        idCard = "33052219861229693X";
////        SPUtil.put(mContext, "termial","YTJ1001");
////        SPUtil.put(mContext,"hosiptalNo", "A0005");
////        SPUtil.put(mContext, "mchntId", "330160400279");
////
////        SPUtil.put(mContext, "posId","1001");
//
////        CanRequestOnlineConsultRequestEntity checkActivationStateReferenceEntity = new CanRequestOnlineConsultRequestEntity();
////        checkActivationStateReferenceEntity.setAppKey(appKey);
////        checkActivationStateReferenceEntity.setDoctorId(doctorId);
////        checkActivationStateReferenceEntity.setTid(tid);
//        Map<String,String> bizContent = new HashMap<>();
//        bizContent.put("appClientType",appClientType);
//        RequestBody body = BodyCreate(bizContent,"requestConsultAndCdrOtherdoc");
//        return FastTransformer.switchSchedulers(getApiService().paySuccess(body).retryWhen(new FastRetryWhen()));
//    }

    /**
     * 获取第三方配置信息
     * 比如 信
     * @param tid 就是tid
     * @param thirdParty 就是第三方的APP_KEY
     */
    public Observable<ConfigurationToThirdForPatientEntity> getConfigurationToThirdForPatient(String tid, String thirdParty) {
        Map<String, Object> bizContent = new HashMap<>();
        bizContent.put("tid", tid);
        bizContent.put("thirdParty", thirdParty);
        // 请求的类型 findValidOrganProfessionForRevisit
        RequestBody body = BodyCreate(bizContent, "getConfigurationToThirdForPatient");
        return FastTransformer.switchSchedulers(getApiService().getConfigurationToThirdForPatient(body).retryWhen(new FastRetryWhen()));
    }

    /**
     * 确认处方单信息并结算
     * 处方单支付永远支持 32 支付宝
     */
    public Observable<CreateOrderResultEntity> createOrder(String recipeId) {
        Map<String,String> recipeOrder =new HashMap<>(); //病历数据
        recipeOrder.put("payway","32");//支付类型代码 支付宝 32，微信：40 卫宁付：111
//        recipeOrder.put("decoctionFlag",decoctionFlag);//是否代煎 1：代煎，0：不代煎
//        recipeOrder.put("gfFeeFlag",String.valueOf(0));//是否收取制作费 1：表示需要制作费，0：不需要
        recipeOrder.put("payMode","1");//支付方式代码
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
     * @param tabStatus 状态标志位 ,ongoing 进行中，isover 已完成tab，onready 待处理。
     *                  目前未支付需要查询2次，一次ongoing，一次onready，然后合并结果。
     */
    public Observable<GetConsultsAndRecipesResultEntity> getConsultsAndRecipes(String tabStatus) {
        // 除了公共的数据之外，还有其他的数据请求
        Map<String,String> bizContent = new HashMap<>();
        bizContent.put("startPage","0");//复诊分页开始标记(默认每页十条，从0开始)
        bizContent.put("requestMode","4");//复诊类型,固定为4
        bizContent.put("tabStatus",tabStatus);//状态标志位,ongoing进行中tab，isover已完成tab
//        bizContent.put("tabStatus","onready");//状态标志位,ongoing进行中tab，isover已完成tab，onready待处理
        bizContent.put("recipeIndex","0");//处方分页起始位置
        bizContent.put("recipeLimit","20");//处方每页查询量(最大不超过20)
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

    /**
     * 查询复诊单的小鱼视频会议室房间号和密码 v
     * @param orderId 复诊单ID
     * @param thirdParty 就是第三方的APP_KEY
     */
    public Observable<RoomIdInsAuthEntity> getRoomIdInsAuth(String orderId, String thirdParty) {
        Map<String, Object> bizContent = new HashMap<>();
        bizContent.put("orderId", orderId); //	复诊单ID
        bizContent.put("thirdParty", thirdParty); //	复诊单ID
        bizContent.put("appType", "xyLink"); //视频的SDK名字 固定 xyLink
        bizContent.put("type", "add");//暂时小鱼SDK用不上 固定add
        // 请求的类型 findValidOrganProfessionForRevisit
        RequestBody body = BodyCreate(bizContent, "roomIdInsAuth");
        return FastTransformer.switchSchedulers(getApiService().roomIdInsAuth(body).retryWhen(new FastRetryWhen()));
    }

    /**
     * 4.1.4 处方药品推送接口
     * todo 处方单里面没有医生和科室信息？
     * @param totalAmount 这里的价格单位是分，所以输入的时候，要乘以100
     */
    public Observable<PrescriptionPushEntity> prescriptionPush(String  clinicSN,
                                                               String  hospitalName,
                                                               String  patientIdCard,
                                                               String  patientGender,
                                                               String  patientName,
                                                               String  diseaseName,
                                                               String  outerOrderNo,
                                                               String  totalAmount,
                                                               ArrayList<Map> drugs
                                                               ) {
        // 外部引入的 arrayList 必须在这里再重新组装一遍，
        // 有可能会出现数组不能被Gson格式话的问题
        ArrayList<Map> maps = new ArrayList<>();
        for (Map item : drugs) {
            maps.add(item);
        }

        // 除了公共的数据之外，还有其他的数据请求
        Map<String,Object> bizContent = new HashMap<>();
        bizContent.put("clinicSN",clinicSN);//诊亭编号
        bizContent.put("hospitalName",hospitalName);//医院名称
//        bizContent.put("doctorName",doctorName);//医生名称
//        bizContent.put("deptName",deptName);//挂号科室
        bizContent.put("patientIdCard",patientIdCard);//
        bizContent.put("patientGender",patientGender);//
        bizContent.put("patientName",patientName);//
//        bizContent.put("patientMobile",patientMobile);//
//        bizContent.put("patientDateOfBirth",patientDateOfBirth);//
//        bizContent.put("complaint",complaint);//主诉 比如感冒发烧
        bizContent.put("diseaseName",diseaseName);//诊断名称
        bizContent.put("outerOrderNo",outerOrderNo);//处方流水号
        bizContent.put("prescriptionType","");//可以为空
        bizContent.put("totalAmount",totalAmount);//
        bizContent.put("billNo","");//可以为空
        bizContent.put("paymentSeqNo","");//可以为空
        bizContent.put("paymentType","SELF");//
        bizContent.put("timeout","1440");//
        bizContent.put("drugs",maps);//
        // 请求的类型
        RequestBody body = BodyCreate(bizContent,"prescriptionPush",false);
        return FastTransformer.switchSchedulers(getApiService().prescriptionPush(body).retryWhen(new FastRetryWhen()));
    }

    /**
     * 4.1.5 查询设备所有库存接口
     * todo 返回的 GetMedicalInfoEntity.data.data 是一个json的字符串，不能转换为对象
     * @param clinicSn 诊亭编号
     */
    public Observable<GetMedicalInfoEntity> getMedicalInfo(String clinicSn) {
        // 除了公共的数据之外，还有其他的数据请求
        Map<String,String> bizContent = new HashMap<>();
        bizContent.put("clinicSn",clinicSn);//复诊分页开始标记(默认每页十条，从0开始)
        // 请求的类型
        RequestBody body = BodyCreate(bizContent,"getMedicalInfo",false);
        return FastTransformer.switchSchedulers(getApiService().getMedicalInfo(body).retryWhen(new FastRetryWhen()));
    }

    /**
     * 4.1.6 查询部分库存信息
     * todo 返回的 GetStockInfoEntity.data.data 是一个json的字符串，不能转换为对象
     * @param clinicSn 诊亭编号
     * @param skus 药品编码 列表
     */
    public Observable<GetStockInfoEntity> getStockInfo(String clinicSn, ArrayList<String> skus) {
        // 除了公共的数据之外，还有其他的数据请求
        Map<String,Object> bizContent = new HashMap<>();
        bizContent.put("clinicSn",clinicSn);//诊亭编号
        bizContent.put("skus", JTJSONUtils.pressJsonArray(skus));//药品编码
        // 请求的类型
        RequestBody body = BodyCreate(bizContent,"getStockInfo",false);
        return FastTransformer.switchSchedulers(getApiService().getStockInfo(body).retryWhen(new FastRetryWhen()));
    }


    /**
     * 3.1.19 处方合并生成订单接口
     * 合并处方并生成处方订单号，供用户支付。
     * 处方订单号，是支付的凭据。
     */
    public Observable<BatchCreateOrderEntity> batchCreateOrder(String recipeFee, ArrayList<String> recipeIds,ArrayList<String> recipeCode) {
        // 除了公共的数据之外，还有其他的数据请求
        Map<String,Object> bizContent = new HashMap<>();
        bizContent.put("giveMode","2");//2 暂定
        bizContent.put("payway", "32");//32 支付宝
        bizContent.put("recipeFee", recipeFee);//挂号费
        bizContent.put("busType", "recipe");//业务类型字符串 处方:recipe,复诊：revisit
        bizContent.put("recipeIds", JTJSONUtils.pressJsonArray(recipeIds));//处方ID集合
        bizContent.put("recipeCode", JTJSONUtils.pressJsonArray(recipeCode));//HIS处方编码集合，可以从处方详情中获取

        // 请求的类型
        RequestBody body = BodyCreate(bizContent,"batchCreateOrder");
        return FastTransformer.switchSchedulers(getApiService().batchCreateOrder(body).retryWhen(new FastRetryWhen()));
    }

    /**
     * 3.1.20 支付请求接口
     * 获取支付的二维码和详细数据
     * 从3.11节获取的订单id
     * 轮询处方单详情，读取payFlag字段，如果为1表示已经支付成功。
     * @param busId 交易订单，如果是复诊单，则是复诊单id
     * @param busType 类型只有2种 处方:recipe,复诊：onlinerecipe
     */
    public Observable<PayOrderEntity> payOrder(String busId,String busType) {
        // 除了公共的数据之外，还有其他的数据请求
        Map<String,Object> bizContent = new HashMap<>();
        bizContent.put("payWay", "32");//32 支付宝
        bizContent.put("busId", busId);//业务订单id 从3.11节获取的订单id
        bizContent.put("busType", busType);//处方:recipe,复诊：onlinerecipe

        // 请求的类型
        RequestBody body = BodyCreate(bizContent,"order");
        return FastTransformer.switchSchedulers(getApiService().payOrder(body).retryWhen(new FastRetryWhen()));
    }

    /**
     * 复诊单 详情
     */
    public Observable<GetConsultAndPatientAndDoctorByIdEntity> getConsultAndPatientAndDoctorById(String consultId) {
        // 除了公共的数据之外，还有其他的数据请求
        Map<String,Object> bizContent = new HashMap<>();
        bizContent.put("consultId", consultId);//复诊单 ID

        // 请求的类型
        RequestBody body = BodyCreate(bizContent,"getConsultAndPatientAndDoctorById");
        return FastTransformer.switchSchedulers(getApiService().getConsultAndPatientAndDoctorById(body).retryWhen(new FastRetryWhen()));
    }

    /**
     * 3.1.3 患者最新待处理处方
     * 轮询处方单详情，视频可以获得医生最新的轮训处方单
     */
    public Observable<FindRecipesForPatientAndTabStatusEntity> findRecipesForPatientAndTabStatus() {
        // 除了公共的数据之外，还有其他的数据请求
        Map<String,Object> bizContent = new HashMap<>();
        bizContent.put("tabStatus", "ongoing");//状态标志位
        bizContent.put("index", "0");//分页起始位置
        bizContent.put("limit", "20");//每页查询量

        // 请求的类型
        RequestBody body = BodyCreate(bizContent,"findRecipesForPatientAndTabStatus");
        return FastTransformer.switchSchedulers(getApiService().findRecipesForPatientAndTabStatus(body).retryWhen(new FastRetryWhen()));
    }


    /**
     * 处方单  详情
     * 轮询处方单详情，读取payFlag字段，如果为1表示已经支付成功。
     */
    public Observable<GetPatientRecipeByIdEntity> getPatientRecipeById(String recipeId) {
        // 除了公共的数据之外，还有其他的数据请求
        Map<String,Object> bizContent = new HashMap<>();
        bizContent.put("recipeId", recipeId);//处方单 ID

        // 请求的类型
        RequestBody body = BodyCreate(bizContent,"getPatientRecipeById");
        return FastTransformer.switchSchedulers(getApiService().getPatientRecipeById(body).retryWhen(new FastRetryWhen()));
    }


    /**
     * 获取病人详细数据 根据 TID
     * 唯一获得 mpiId 的地方
     */
    public Observable<PatientListEntity> getPatientList() {
        // 除了公共的数据之外，还有其他的数据请求
        Map<String,Object> bizContent = new HashMap<>();

        // 请求的类型
        RequestBody body = BodyCreate(bizContent,"patientList");
        return FastTransformer.switchSchedulers(getApiService().getPatientList(body).retryWhen(new FastRetryWhen()));
    }

    /**
     * 获取处方列表，根据复诊单
     * 支付中，通过复诊单id来查询处方单id
     */
    public Observable<GetRecipeListByConsultIdEntity> getRecipeListByConsultId(String consultId) {
        // 除了公共的数据之外，还有其他的数据请求
        Map<String,Object> bizContent = new HashMap<>();
        bizContent.put("consultId", consultId);//复诊单 ID

        // 请求的类型
        RequestBody body = BodyCreate(bizContent,"getRecipeListByConsultId");
        return FastTransformer.switchSchedulers(getApiService().getRecipeListByConsultId(body).retryWhen(new FastRetryWhen()));
    }

    /**
     * 结束问诊
     * 纳里结束问诊的接口
     */
    public Observable<PatientFinishGraphicTextConsultEntity> patientFinishGraphicTextConsult(String consultId) {
        // 除了公共的数据之外，还有其他的数据请求
        Map<String,Object> bizContent = new HashMap<>();
        bizContent.put("consultId", consultId);//复诊单 ID

        // 请求的类型
        RequestBody body = BodyCreate(bizContent,"patientFinishGraphicTextConsult");
        return FastTransformer.switchSchedulers(getApiService().patientFinishGraphicTextConsult(body).retryWhen(new FastRetryWhen()));
    }


}
