package com.aries.template.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
    public static String getCurrentTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 EEE HH:mm:ss");
        Date dt = new Date(System.currentTimeMillis());
        String returnStr = simpleDateFormat.format(dt);
        return returnStr;
    }

    /**
     * 获取今天
     * 今天的格式是 "yyyy年MM月dd日 星期？ HH:mm:ss"
     * @return String 格式化的今天
     */
    public static String getToday() {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 %%%");
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

    /**
     * 输入一个生日时间，计算出现在的年龄
     * @return
     */
    public static int getAge(String birthday){
        try {
            final Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            currentCalendar.setTimeInMillis(System.currentTimeMillis());

            SimpleDateFormat ageFormat = new SimpleDateFormat("yyyyMMdd");
            Calendar birthdayCalendar = Calendar.getInstance();
            birthdayCalendar.setTime(ageFormat.parse(birthday));
            birthdayCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

            if (currentCalendar.get(Calendar.MONTH) >= birthdayCalendar.get(Calendar.MONTH)) {//如果现在的月份大于生日的月份
                return currentCalendar.get(Calendar.YEAR) - birthdayCalendar.get(Calendar.YEAR);//那就直接减,因为现在的年月都大于生日的年月
            } else {
                return currentCalendar.get(Calendar.YEAR) - birthdayCalendar.get(Calendar.YEAR) - 1;//否则,减掉一年
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
//
//        Calendar currentCalendar = Calendar.getInstance();//实例化calendar
//        currentCalendar.setTimeInMillis(System.currentTimeMillis());//调用setTimeInMillis方法和System.currentTimeMillis()获取当前时间
//        Calendar targetCalendar = Calendar.getInstance();
//        targetCalendar.setTimeInMillis(birthday);//这个解析传进来的时间戳
//        if (currentCalendar.get(Calendar.MONTH) >= targetCalendar.get(Calendar.MONTH)) {//如果现在的月份大于生日的月份
//            return currentCalendar.get(Calendar.YEAR) - targetCalendar.get(Calendar.YEAR);//那就直接减,因为现在的年月都大于生日的年月
//        } else {
//            return currentCalendar.get(Calendar.YEAR) - targetCalendar.get(Calendar.YEAR) - 1;//否则,减掉一年
//        }
    }

}
