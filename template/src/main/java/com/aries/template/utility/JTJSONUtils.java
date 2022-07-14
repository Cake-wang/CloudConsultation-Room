package com.aries.template.utility;

import com.google.gson.JsonArray;

import java.util.Collection;
import java.util.Map;

public class JTJSONUtils {

    /**
     * 处理将一个数组转换为JSONObject
     * 一般情况下 数组是不能被转换为 JSONObject
     * 所以在这里将所有 Collection 对象全部转成JSON对象
     */
    public static JsonArray pressJsonArray(Collection<String> array){
        JsonArray jsonArray = new JsonArray();
        for (String item : array) {
            jsonArray.add(item);
        }
        return jsonArray;
    }

    /**
     * 将MAP的数据转换为签名需要的数据格式
     * 将最外层参数中除sign字段外的所有字段按照按字母序排序，键和值用=号连接，键值对之间用&符号分隔
     */
    public static String translateSign(Map<String, Object> params){
        String sign = "";
        for (String key : params.keySet()) {
            String strEntity = ConvertJavaBean.converJavaBeanToJsonNew(params.get(key));
            sign+=key+"="+strEntity;
            sign+="&";
        }
        sign = sign.substring(0,sign.length()-1);
        return sign;
    }
}
