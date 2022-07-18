package com.aries.template.xiaoyu.dapinsocket;

public class MessageType {
    //身高体重
    public static final int HW = 1;
    //人体成分
    public static final int BC = 2;
    //体温
    public static final int TEMP = 3;
    //血压
    public static final int BP = 4;
    //血氧
    public static final int SPO2 = 5;
    //心电
    public static final int EGC = 6;
    //骨密度
    public static final int BD = 7;
    //用户信息
    public static final int USERINFO = 8;
    //跳转
    public static final int SKIP = 9;
    //动脉硬化
    public static final int ARTERY = 10;
    //回到初始页面  收不到PC端发过来的10
    public static final int INIT = 12;
    //生成报告
    public static final int REPORT = 11;
    //未做身高体重，通知PC播放语音
    public static final int FORBIDDEN = 13;
    //开始测量
    public static final int START = 14;

}
