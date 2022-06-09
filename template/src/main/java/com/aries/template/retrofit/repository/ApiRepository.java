package com.aries.template.retrofit.repository;


import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.aries.library.fast.retrofit.FastRetrofit;
import com.aries.library.fast.retrofit.FastRetryWhen;
import com.aries.library.fast.retrofit.FastTransformer;
import com.aries.library.fast.util.SPUtil;
import com.aries.library.fast.util.ToastUtil;
import com.aries.template.entity.BaseMovieEntity;
import com.aries.template.entity.CanRequestOnlineConsultRequestEntity;
import com.aries.template.entity.CanRequestOnlineConsultResultEntity;
import com.aries.template.entity.CancelregisterRequestEntity;
import com.aries.template.entity.CancelregisterResultEntity;
import com.aries.template.entity.CreateOrderRequestEntity;
import com.aries.template.entity.CreateOrderResultEntity;
import com.aries.template.entity.FindUserResultEntity;
import com.aries.template.entity.FindValidDepartmentForRevisitRequestEntity;
import com.aries.template.entity.FindValidDepartmentForRevisitResultEntity;
import com.aries.template.entity.FindValidOrganProfessionForRevisitRequestEntity;
import com.aries.template.entity.FindValidOrganProfessionForRevisitResultEntity;
import com.aries.template.entity.GetConfigurationToThirdForPatientRequestEntity;
import com.aries.template.entity.GetConfigurationToThirdForPatientResultEntity;
import com.aries.template.entity.GetConsultsAndRecipesRequestEntity;
import com.aries.template.entity.GetConsultsAndRecipesResultEntity;
import com.aries.template.entity.IsRegisterRequestEntity;
import com.aries.template.entity.IsRegisterResultEntity;
import com.aries.template.entity.RegisterRequestEntity;
import com.aries.template.entity.RegisterResultEntity;
import com.aries.template.entity.SearchDoctorListByBusTypeV2RequestEntity;
import com.aries.template.entity.SearchDoctorListByBusTypeV2ResultEntity;
import com.aries.template.entity.UpdateEntity;
import com.aries.template.retrofit.service.ApiService;
import com.aries.template.utility.ConvertJavaBean;
import com.aries.template.utility.RSASignature;
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
        mApiService = FastRetrofit.getInstance().createService(ApiService.class);
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

    public Observable<FindUserResultEntity> findUser(String idCard, Context mContext) {

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
        return FastTransformer.switchSchedulers(getApiService().findUser(body).retryWhen(new FastRetryWhen()));
    }

    public Observable<RegisterResultEntity> register(String idCard,String name,String phoneNumber, Context mContext) {

//        idCard = "33052219861229693X";
//        SPUtil.put(mContext, "termial","YTJ1001");
//        SPUtil.put(mContext,"hosiptalNo", "A0005");
//        SPUtil.put(mContext, "mchntId", "330160400279");
//
//        SPUtil.put(mContext, "posId","1001");

        RegisterRequestEntity checkActivationStateReferenceEntity = new RegisterRequestEntity();
        checkActivationStateReferenceEntity.setIdCard(idCard);
        checkActivationStateReferenceEntity.setName(name);
        checkActivationStateReferenceEntity.setMobile(phoneNumber);
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
        return FastTransformer.switchSchedulers(getApiService().register(body).retryWhen(new FastRetryWhen()));
    }

    public Observable<CancelregisterResultEntity> patientCancelGraphicTextConsult(String appKey, String tid, Integer consultId, Context mContext) {

//        idCard = "33052219861229693X";
//        SPUtil.put(mContext, "termial","YTJ1001");
//        SPUtil.put(mContext,"hosiptalNo", "A0005");
//        SPUtil.put(mContext, "mchntId", "330160400279");
//
//        SPUtil.put(mContext, "posId","1001");

        CancelregisterRequestEntity checkActivationStateReferenceEntity = new CancelregisterRequestEntity();
        checkActivationStateReferenceEntity.setAppKey(appKey);
        checkActivationStateReferenceEntity.setConsultId(consultId);
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
        params.put("methodCode","patientCancelGraphicTextConsult");
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
        return FastTransformer.switchSchedulers(getApiService().patientCancelGraphicTextConsult(body).retryWhen(new FastRetryWhen()));
    }

    public Observable<FindValidOrganProfessionForRevisitResultEntity> findValidOrganProfessionForRevisit(String appKey, String tid, Integer organId, Context mContext) {

//        idCard = "33052219861229693X";
//        SPUtil.put(mContext, "termial","YTJ1001");
//        SPUtil.put(mContext,"hosiptalNo", "A0005");
//        SPUtil.put(mContext, "mchntId", "330160400279");
//
//        SPUtil.put(mContext, "posId","1001");

        FindValidOrganProfessionForRevisitRequestEntity checkActivationStateReferenceEntity = new FindValidOrganProfessionForRevisitRequestEntity();
        checkActivationStateReferenceEntity.setAppKey(appKey);
        checkActivationStateReferenceEntity.setOrganId(organId);
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
        params.put("methodCode","findValidOrganProfessionForRevisit");
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
        return FastTransformer.switchSchedulers(getApiService().findValidOrganProfessionForRevisit(body).retryWhen(new FastRetryWhen()));
    }

    public Observable<FindValidDepartmentForRevisitResultEntity> findValidDepartmentForRevisit(String appKey, String tid, Integer organId, Integer organProfessionId, Context mContext) {

//        idCard = "33052219861229693X";
//        SPUtil.put(mContext, "termial","YTJ1001");
//        SPUtil.put(mContext,"hosiptalNo", "A0005");
//        SPUtil.put(mContext, "mchntId", "330160400279");
//
//        SPUtil.put(mContext, "posId","1001");

        FindValidDepartmentForRevisitRequestEntity checkActivationStateReferenceEntity = new FindValidDepartmentForRevisitRequestEntity();
        checkActivationStateReferenceEntity.setAppKey(appKey);
        checkActivationStateReferenceEntity.setOrganId(organId);
        checkActivationStateReferenceEntity.setOrganProfessionId(organProfessionId);
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
        params.put("methodCode","findValidOrganProfessionForRevisit");
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
        return FastTransformer.switchSchedulers(getApiService().findValidDepartmentForRevisit(body).retryWhen(new FastRetryWhen()));
    }

    public Observable<SearchDoctorListByBusTypeV2ResultEntity> searchDoctorListByBusTypeV2(String appKey, String tid,
                                                                                           String profession,
                                                                                           int sortKey,
                                                                                           int departmentId,
                                                                                           int organId,
                                                                                           int start,
                                                                                           int limit,
                                                                                           int recipeConsultSourceFlag,
                                                                                           int packageFlag,
                                                                                           String search,
                                                                                           Context mContext) {

//        idCard = "33052219861229693X";
//        SPUtil.put(mContext, "termial","YTJ1001");
//        SPUtil.put(mContext,"hosiptalNo", "A0005");
//        SPUtil.put(mContext, "mchntId", "330160400279");
//
//        SPUtil.put(mContext, "posId","1001");

        SearchDoctorListByBusTypeV2RequestEntity checkActivationStateReferenceEntity = new SearchDoctorListByBusTypeV2RequestEntity();
        checkActivationStateReferenceEntity.setAppKey(appKey);
        checkActivationStateReferenceEntity.setOrganId(organId);
        checkActivationStateReferenceEntity.setSortKey(sortKey);
        checkActivationStateReferenceEntity.setTid(tid);
        checkActivationStateReferenceEntity.setProfession(profession);
        checkActivationStateReferenceEntity.setDepartmentId(departmentId);
        checkActivationStateReferenceEntity.setStart(start);
        checkActivationStateReferenceEntity.setLimit(limit);
        checkActivationStateReferenceEntity.setOrganId(organId);
        checkActivationStateReferenceEntity.setPackageFlag(packageFlag);
        checkActivationStateReferenceEntity.setSearch(search);
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
        params.put("methodCode","findValidOrganProfessionForRevisit");
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
        return FastTransformer.switchSchedulers(getApiService().searchDoctorListByBusTypeV2(body).retryWhen(new FastRetryWhen()));
    }

    public Observable<CanRequestOnlineConsultResultEntity> canRequestOnlineConsult(String appKey, String tid, Integer doctorId, Context mContext) {

//        idCard = "33052219861229693X";
//        SPUtil.put(mContext, "termial","YTJ1001");
//        SPUtil.put(mContext,"hosiptalNo", "A0005");
//        SPUtil.put(mContext, "mchntId", "330160400279");
//
//        SPUtil.put(mContext, "posId","1001");

        CanRequestOnlineConsultRequestEntity checkActivationStateReferenceEntity = new CanRequestOnlineConsultRequestEntity();
        checkActivationStateReferenceEntity.setAppKey(appKey);
        checkActivationStateReferenceEntity.setDoctorId(doctorId);
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
        params.put("methodCode","findValidOrganProfessionForRevisit");
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
        return FastTransformer.switchSchedulers(getApiService().canRequestOnlineConsult(body).retryWhen(new FastRetryWhen()));
    }

    public Observable<CanRequestOnlineConsultResultEntity> requestConsultAndCdrOtherdoc(String appKey, String tid, Integer doctorId, Context mContext) {

//        idCard = "33052219861229693X";
//        SPUtil.put(mContext, "termial","YTJ1001");
//        SPUtil.put(mContext,"hosiptalNo", "A0005");
//        SPUtil.put(mContext, "mchntId", "330160400279");
//
//        SPUtil.put(mContext, "posId","1001");

        CanRequestOnlineConsultRequestEntity checkActivationStateReferenceEntity = new CanRequestOnlineConsultRequestEntity();
        checkActivationStateReferenceEntity.setAppKey(appKey);
        checkActivationStateReferenceEntity.setDoctorId(doctorId);
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
        params.put("methodCode","findValidOrganProfessionForRevisit");
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
        return FastTransformer.switchSchedulers(getApiService().canRequestOnlineConsult(body).retryWhen(new FastRetryWhen()));
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

    public Observable<CreateOrderResultEntity> createOrder(String appKey, String tid, Integer doctorId, Context mContext) {

//        idCard = "33052219861229693X";
//        SPUtil.put(mContext, "termial","YTJ1001");
//        SPUtil.put(mContext,"hosiptalNo", "A0005");
//        SPUtil.put(mContext, "mchntId", "330160400279");
//
//        SPUtil.put(mContext, "posId","1001");

        CreateOrderRequestEntity checkActivationStateReferenceEntity = new CreateOrderRequestEntity();
        checkActivationStateReferenceEntity.setAppKey(appKey);

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
        params.put("methodCode","createOrder");
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
        return FastTransformer.switchSchedulers(getApiService().createOrder(body).retryWhen(new FastRetryWhen()));
    }

    public Observable<GetConsultsAndRecipesResultEntity> getConsultsAndRecipes(String appKey, String tid, Integer doctorId, Context mContext) {

//        idCard = "33052219861229693X";
//        SPUtil.put(mContext, "termial","YTJ1001");
//        SPUtil.put(mContext,"hosiptalNo", "A0005");
//        SPUtil.put(mContext, "mchntId", "330160400279");
//
//        SPUtil.put(mContext, "posId","1001");

        GetConsultsAndRecipesRequestEntity checkActivationStateReferenceEntity = new GetConsultsAndRecipesRequestEntity();
        checkActivationStateReferenceEntity.setAppKey(appKey);

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
//        params.put("methodCode","createOrder");
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
        return FastTransformer.switchSchedulers(getApiService().getConsultsAndRecipes(body).retryWhen(new FastRetryWhen()));
    }

}
