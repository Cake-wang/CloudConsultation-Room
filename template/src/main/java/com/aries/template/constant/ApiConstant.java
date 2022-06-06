package com.aries.template.constant;

/**
 * @Author: AriesHoo on 2018/11/19 14:14
 * @E-Mail: AriesHoo@126.com
 * @Function: 接口地址常量
 * @Description:
 */
public class ApiConstant {

    public static final String API_MOVIE_IN_THEATERS = "v2/movie/in_theaters";
    public static final String API_MOVIE_COMING_SOON = "v2/movie/coming_soon";
    public static final String API_MOVIE_TOP = "v2/movie/top250";

    public static final String API_UPDATE_APP = "update";
    public static final String API_UPDATE_APP_KEY = "update";

    // 获取设备信息接口
    public static final String QueryTermialInfo  =  "api/aio/queryTermialInfo";

    public static  String QueryTermialInfoBaseurl  =  "http://10.100.10.45:18000/api/aio/";

   // 舒心测试接口地址：http://10.100.10.45:18033

    public static  String BASEURL = "http://10.100.10.45:18033/";

    public static  String DownURL = "http://10.100.10.45:18033/";

//查询激活状态
    public static final String isRegister  =  "cloudHospital/ngariUserRelation/isRegister";

    public static final String register  =  "cloudHospital/ngariUserRelation/register";

    public static final String cancelregister  =  "ngari/revisit/doBaseNgariRequest";

    public static final String getConsultsAndRecipes  =  "ngari/revisit/getConsultsAndRecipes";

//    http://XXXXX:port/api/activation/queryActive



}
