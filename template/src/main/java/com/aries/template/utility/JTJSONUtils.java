package com.aries.template.utility;

import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
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

    /**
     * 将MAP的数据转换为签名需要的数据格式
     * 将最外层参数中除sign字段外的所有字段按照按字母序排序，键和值用=号连接，键值对之间用&符号分隔
     */
    public static String translateSign2(Map<String, Object> map){
        String sign = "";
        List<Map.Entry<String, Object>> list = sortMap(map);
        for (Map.Entry<String, Object> entry : list) {
            if (entry.getKey().equals("common")){
                String strEntity = ConvertJavaBean.converJavaBeanToJsonNew(entry.getValue());
                strEntity.replaceAll("\"","\\\"");
                sign+=entry.getKey()+"="+strEntity;
                sign+="&";
            }else{
                String strEntity = entry.getValue().toString();
                sign+=entry.getKey()+"="+strEntity;
                sign+="&";
            }
        }
        sign = sign.substring(0,sign.length()-1);
        return sign;
    }

    /**
     * 对map进行字母排序
     */
    public static List<Map.Entry<String, Object>> sortMap(Map<String, Object> map) {
        List<Map.Entry<String, Object>> infos = new ArrayList<Map.Entry<String, Object>>(map.entrySet());

        // 重写集合的排序方法：按字母顺序
        Collections.sort(infos, (o1, o2) -> (o1.getKey().compareTo(o2.getKey())));
        return infos;
    }
}
