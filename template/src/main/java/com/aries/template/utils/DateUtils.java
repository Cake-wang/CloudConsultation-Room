package com.aries.template.utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * Create by danbao@hengtiansoft.com on 2019-09-06 10:04
 */
public class DateUtils {
    /**
     * 获取今天
     * 今天的格式是 "yyyy年MM月dd日 星期？ HH:mm:ss"
     * @return String 格式化的今天
     */
    public static String getToday() {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 %%% HH:mm:ss");
        Date dt = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        //weekDays[w] //星期几
        //simpleDateFormat.format(dt) //yyyy年MM月dd日 %d HH:mm:ss
        String returnStr = simpleDateFormat.format(dt).replace("%%%",weekDays[w]);
        return returnStr;
    }

}
