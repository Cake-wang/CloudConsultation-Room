package com.aries.template.utils;

import java.util.HashMap;
import java.util.Map;

/******
 * defence coding
 *
 * 防御 用户 恶意的操作。
 */
//todo 虽然使用了 static ，但是这个容器，始终会占用一部分资源，不能面向static编程
public class DefenceUtil {
    private DefenceUtil(){};

    /** 重定向容器*/
    private static Map<String,String> directionMap = new HashMap<>();

    /** 向重定向容器查询数据*/
    private static String getValue(String key, String initValue){
        if (null==directionMap.get(key)){
            directionMap.put(key,initValue);
        }
        return directionMap.get(key);
    }

    /** 向重定向容器添加数据 */
    private static void setValue(String key, String value){
        directionMap.put(key,value);
    }

    /**  默认 1000毫秒后才可以重新点击 */
    private static final int DEFAULT_TIME = 1000; // 1000 ms 内才可以点击
    /** 上一次点击时间记录点 */
    private static long lastTime;

    /**
     * 判断当前方法是否重复提交
     * @param name 方法的名称，必须是唯一的,命名规则可以是 Class.method 这样的形式
     * @return true 可以提交，false 不能提交
     */
    public static boolean checkReSubmit(String name){
        long currentTime = System.currentTimeMillis();
        long targetTime = Long.parseLong(getValue(name,"0"));
        boolean backBoolean = currentTime - targetTime>DEFAULT_TIME;
        if (backBoolean){
            setValue(name,String.valueOf(currentTime));
        }
        return backBoolean;
//
//        long currentTime = System.currentTimeMillis();
//        boolean backBoolean = currentTime - lastTime>DEFAULT_TIME;
//        lastTime = currentTime;
//        return backBoolean;
    }
}
