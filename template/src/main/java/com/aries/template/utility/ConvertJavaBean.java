package com.aries.template.utility;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by wgl on 2018/9/20.
 */

public class ConvertJavaBean {
    public static Gson gson;
    //将JSON字符串转换成javabean
    public static <T> T parsr(String json ,Class<T> tClass){
        //判读字符串是否为空
        if(TextUtils.isEmpty(json)){
            return null;
        }

        if(gson==null){
            gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create();
        }
        return gson.fromJson(json,tClass);
    }
    //将javabean转换成JSON字符串
    public static String converJavaBeanToJson(Object obj){
        if(obj == null){
            return "";
        }
        if(gson == null){
            gson  = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create();
        }
        String beanstr = gson.toJson(obj);
        if(!TextUtils.isEmpty(beanstr)){
            return beanstr;
        }
        return "";
    }

    public static String converJavaBeanToJsonNew(Object obj){
        if(obj == null){
            return "";
        }
//        if(gson == null){
            gson  = new GsonBuilder().disableHtmlEscaping().create();
//        }
        String beanstr = gson.toJson(obj);
        if(!TextUtils.isEmpty(beanstr)){
            return beanstr;
        }
        return "";
    }


//    public static String converJavaBeanToJson_old(VerifyData obj){
//        if(obj == null){
//            return "";
//        }
//        String beanstr ="";
//        JSONObject jsonObject = new JSONObject();
//
//        try {
//            jsonObject.put("idCard",obj.getIdCard());
//            jsonObject.put("photoStr",obj.getPhotoStr());
//            jsonObject.put("phoneNo",obj.getPhoneNo());
//             beanstr = jsonObject.toString();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        if(!TextUtils.isEmpty(beanstr)){
//            return beanstr;
//        }
//        return "";
//    }

}
