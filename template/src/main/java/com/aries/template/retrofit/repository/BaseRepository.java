package com.aries.template.retrofit.repository;

import android.accounts.NetworkErrorException;

import com.aries.library.fast.retrofit.FastNullException;
import com.aries.library.fast.retrofit.FastRetryWhen;
import com.aries.library.fast.retrofit.FastTransformer;
import com.aries.template.FakeDataExample;
import com.aries.template.GlobalConfig;
import com.aries.template.utility.ConvertJavaBean;
import com.aries.template.utility.JTJKRsaUtil;
import com.aries.template.utility.JTJSONUtils;
import com.aries.template.utility.RSASignature;
import com.aries.template.utility.RSAUtil;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import okhttp3.RequestBody;

/**
 * @Author: AriesHoo on 2018/10/10 17:24
 * @E-Mail: AriesHoo@126.com
 * @Function: retrofit使用基类封装
 * @Description:
 */
public abstract class BaseRepository {

    /**
     * @param observable 用于解析 统一返回实体统一做相应的错误码--如登录失效
     * @param <T>
     * @return
     */
    protected <T> Observable<T> transform(Observable<BaseEntity<T>> observable) {
        return FastTransformer.switchSchedulers(
                observable.retryWhen(new FastRetryWhen())
                        .flatMap((Function<BaseEntity<T>, ObservableSource<T>>) result -> {
                            if (result == null) {
                                return Observable.error(new NetworkErrorException());
                            } else {
                                if (result.success) {
                                    return result.data != null ? Observable.just(result.data)
                                            : Observable.error(new FastNullException());
                                } else {
                                    return Observable.error(new NetworkErrorException());
                                }
                            }
                        }));
    }


    /**
     * 输入到通信的body创造工程
     * 由于是一个统一的网络请求，通过methodcode来调用不同的方法，所以需要输入methodCode
     * @param map 输入进body的数据
     * @param methodCode 调用目标方法的code
     */
    protected RequestBody BodyCreate(Map map, String methodCode){
        return BodyCreate(map,methodCode,true);
    }

    /**
     * 输入到通信的body创造工程
     * 由于是一个统一的网络请求，通过methodcode来调用不同的方法，所以需要输入methodCode
     * @param isBizArray bizcontent 是否为 array。纳里的请求bizcontent是个数组,
     */
    protected RequestBody BodyCreate(Map map, String methodCode, boolean isBizArray){
        // bizContent 结构数据信息补全
        Map<String,String> bizContent = new HashMap<>();
        bizContent.put("appKey", GlobalConfig.NALI_APPKEY);
        bizContent.put("appkey", GlobalConfig.NALI_APPKEY);//todo cc
        bizContent.put("tid",GlobalConfig.NALI_TID);
        bizContent.putAll(map);

        ArrayList<Map> maps = new ArrayList<>();
        maps.add(bizContent);

        // 创建body
        ApiRepository.common.getInstance().machineId = GlobalConfig.machineId;
        ApiRepository.common.getInstance().userId = GlobalConfig.NALI_TID;

        //  由于加密要求，必须要按照字母顺序排列
        final Map<String, Object> params = new HashMap<>(4);
        if (isBizArray)
            params.put("bizContent", maps);
        else
            params.put("bizContent", maps.get(0));// 不是取第一个值，而是取bizContent数组的第一位，maps是多个bizContent
        params.put("common", ApiRepository.common.getInstance());
        params.put("logTraceId", ApiRepository.getUUID());//getUUID 请求日志ID唯一识别流水ID，32位，推荐使用UUID
        params.put("merchantId",GlobalConfig.merchantId);
        params.put("methodCode",methodCode);

//        // 签名加密
//        final String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());//时间戳
//        final String signSource = "bizContent=idCard"+""+"&hosiptalNo="+ SPUtil.get(mContext,"hosiptalNo","")+"&mchntId="+SPUtil.get(mContext,"mchntId","")+"&posId="+SPUtil.get(mContext,"posId","")+"&terminal="+SPUtil.get(mContext,"termial","")+"&timestamp="+timeStamp+"";
//        String signTarget = null;
//        try {
//            signTarget = RSASignature.sign(signSource, ApiConstant.NALI_PRIVATE_KEY);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            //将最外层参数中除sign字段外的所有字段按照按字母序排序，键和值用=号连接，键值对之间用&符号分隔
            try {
                String noSgin = JTJSONUtils.translateSign2(params);
                String sgin = RSAUtil.sign(noSgin,GlobalConfig.PRIVATE_KEY);
//                String tryStr = "bizContent={appKey=app_web, tid=tid_2, startPage=0, requestMode=4, tabStatus=ongoing, recipeIndex=0, recipeLimit=10}&common={\"machineId\":\"SY0001\",\"userId\":\"2fcd34d6dde742098737b10ff0fddd9a\"}&logTraceId=1657847389340&merchantId=123456&methodCode=patientList";
//                String tryStrSgin = RSAUtil.sign(tryStr,GlobalConfig.PRIVATE_KEY);
                if (sgin!=null)
                    params.put("sign",sgin);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String strEntity = ConvertJavaBean.converJavaBeanToJsonNew(params);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("Content-Type:application/json;charset=UTF-8"),strEntity);
        return body;
    }



}
