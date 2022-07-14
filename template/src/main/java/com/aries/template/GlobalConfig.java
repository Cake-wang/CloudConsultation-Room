package com.aries.template;

import com.aries.template.entity.SearchDoctorListByBusTypeV2ResultEntity;
import com.decard.entitys.SSCard;

/******
 * 全世界存储对象
 * @author  ::: louis luo
 * Date ::: 2022/6/9 10:31 AM
 *
 */
public class GlobalConfig {

    // 私钥
    public static final String PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDVLlZe4AP+CgBV4CXL5DeWdGIUQn4nXKNItrZqLpaVYaQmVVv7CKrT6ixmGj7Z//ON+BeHqitVy18QV84yzoMyg7n5rZsjmP8VaFGnFSOCSvd9d7EEeVzEXNO5QDDFBzTNtxqxrfqVYW0EvQcao3it8lIUKXK17VNWmcNuhS6l3Movqy3H8r59glI0QFqwq1EqQQI8v39ZJTUQiq8viLLw67Kq7K61qI3rOE3Cs/1QSd2FFyPtAqbNxrjp3f5EclFaMVaVLmxFRWd7LsDXA2M0qgL0L3myIFxDd2c5LbCiyyHGpMlsxJ5MNzowS7KCCaw7BvqsMCBqUriKGgoV5lfZAgMBAAECggEAB9Re2bcyjlcBsiW8XaOxIvZ9T68tgPaXDKmhQ38Yir3+UGYcLbkgxQ25ubpHCqyq3lD5VEM8ujbw8+G1sgoBqY5K+0+T/he1bqzZKuDM4BEuy83kk3x9mryqDgi8gdAE8XVDJrl0FZ5xaZYjt6e/W+wldZYcH3Bq+ihFlD6R+wdywJNI2nDogGPTyBKHcOo0L01hi8qjvRKMGPFBSkehpGMtCRewdXlSY0EO65XfhC3wb5D379+TFGAfPKu5+m71vI/uZaT0OGq1l72YQHs03M1Q3xFkFnJemPHy1AKmXkyH350PJvs/bfASdFn7vSGovYcQT2ZO7B2/bPr0drX0cQKBgQDqFwWaLMaYI4lfvpDC1b3Ti2W1RalFcQTP+HgilURzrvpzMxk1TzzzLZb4sh2VsR8BMbs2SDEWiOczK6F7z5t7u3wT3cljmwR1IAk7jChA4x1vvyqyslLmoYLlGGUjT5bRRKAGQAl1ievcZItBAqJLwiZbOfXAdgRR9nz7r8ewFQKBgQDpIlJA/xEI9yHgSI7H4Y8/j5Zl1ZCWCsSyk2iRHnnb6yzDNxWRvQSPTKHYc/JJMr3i3a+Tyhhm5tUkBgfmhVS+D0S9Y/lPI9OceoB0zI/nSjEJb0YrhfnOMbDjxIWB2A3mxjND3JolH4QDPdNdfY2txgG3ijLlR7mM5NRQe1i1tQKBgQDS3oIxa/xJuFlbYjLND/W7xmqMbIAbCcAoB89Qd939x7XcaD9hAkwJUxwYU3rLCY7AaKgYMdfmUNTUB42kFlQdlbojuzpa+518VKt8dLkeGni93Rr9dh2vm/ZpoRwaPuvA/2yXtL/QnblWA0xd951zWSVsMD3sbWNe4gecQbBEMQKBgQCdUx3y4q3aQPvJYO2JkXubxwgVXJOfzVCDudo85DYT5JZmfou9t7KWCX7GlSgRoX5m1Hch4qWo+2kmUDOQqrVPNPqMXCTn9SNeW4TITStnR7fjyAWwZU74iKv4aKw3vVdUPrhluT8EgkoR7ezvEEVF2XNbKpXCCC79F4b3cOWiaQKBgQDFrgHvifj2fvR3U8HiknduY6aPDdhLhi0fRxNMcz5k9gPysRqJPUdNAYjxKnL3EGltt+syu20P7KXqOwNNKsOHHsjIeTvbwnW9c0qKoQIqR0EyPdkEVrPIQgdntyvcJ808iGReTnEXDZ5Y3WYvovuULg5tYmsJdJUF2RLZVwg8NQ==";

    //公钥
    public static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1S5WXuAD/goAVeAly+" +
            "Q3lnRiFEJ+J1yjSLa2ai6WlWGkJlVb+wiq0+osZho+2f/zjfgXh6orVctfEFfOMs6DMoO5+a2bI5j/FWhRpxUjgkr" +
            "3fXexBHlcxFzTuUAwxQc0zbcasa36lWFtBL0HGqN4rfJSFClyte1TVpnDboUupdzKL6stx/K+fYJSNEBasKtRKk" +
            "ECPL9/WSU1EIqvL4iy8OuyquyutaiN6zhNwrP9UEndhRcj7QKmzca46d3+RHJRWjFWlS5sRUVney7A1wNjNKoC" +
            "9C95siBcQ3dnOS2wosshxqTJbMSeTDc6MEuyggmsOwb6rDAgalK4ihoKFeZX2QIDAQAB";

    /**
     * 由纳里平台分配的公司标识，
     * 固定写死
     * */
    public static final String NALI_APPKEY = "app_web";

    /**
     * 由纳里平台分配 第三方平台用户唯一主键，
     * 在findUser里面取的 在findUser 这个请求中获取, 这个值就是 userId
     * 不可以被清空或重置
     * */
    public static String NALI_TID = "tid_eric_1";

    /**
     *  机器编号 从获取机构编号，组织代码，机器编号的接口一并返回
     *  不可以被清空或重置
     *  */
    public static String machineId = "SY0001";

    /**
     * 组织代码
     * 用于确定医院组织的代码。1 浙大附属邵逸夫医院
     * 目前ID是根据机器的ID，发送到后端，后端返回组织代码给机器。
     * 一台机器的组织代码是固定的。
     * 不可以被清空或重置
     */
    public static int organId = 0;

    /**
     * 药柜号
     * 必须在启动的时候被赋值
     * 不可以被清空或重置
     */
    public static String cabinetId = "";

    /**
     * 医院名称
     * 必须在启动的时候被赋值
     * 不可以被清空或重置
     */
    public static String hospitalName = "";

    /**
     *  全世界网络通信凭据
     */
    public static String token = "";

    /**
     * 用户的年龄信息
     */
    public static  int age = 0;

    /**
     * 科室ID，当前选择的医生所在的科室
     */
    public static String departmentID="";

    /**
     * 复诊医生全信息
     * 这个数据在用户选择好复诊医生后被存储
     */
    public static SearchDoctorListByBusTypeV2ResultEntity.QueryArrearsSummary.JsonResponseBean.OrganProfessionDTO.DocList.Doctor doc;

    /**
     * 用户医保卡信息，全局数据
     * 医保卡上电，循环读3秒一次，读到数据后，数据存上来。
     */
    public static SSCard ssCard;

    /**
     * 是否已经获得用户数据
     * 这个数据通过身份证获取，用于验证是否注册
     * 只有在读卡界面才会验证是否注册，其他界面不会。
     */
    public static boolean isFindUserDone;

    /**
     * 清空所有缓存数据
     * 比如使用在返回主页中
     */
    public static void clear(){
        token="";
        age=0;
        doc=null;
        departmentID="";
        ssCard=null;
        isFindUserDone = false;
    }
}
