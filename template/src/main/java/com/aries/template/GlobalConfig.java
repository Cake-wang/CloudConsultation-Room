package com.aries.template;

import com.aries.template.entity.SearchDoctorListByBusTypeV2ResultEntity;
import com.aries.template.retrofit.repository.ApiRepository;
import com.decard.entitys.SSCard;

/******
 * 全世界存储对象
 * @author  ::: louis luo
 * Date ::: 2022/6/9 10:31 AM
 *
 */
public class GlobalConfig {

    /**
     * 私钥
     * 用于网络做签名
     */
    public static final String PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDVLlZe4AP+CgBV4CXL5DeWdGIUQn4nXKNItrZqLpaVYaQmVVv7CKrT6ixmGj7Z//ON+BeHqitVy18QV84yzoMyg7n5rZsjmP8VaFGnFSOCSvd9d7EEeVzEXNO5QDDFBzTNtxqxrfqVYW0EvQcao3it8lIUKXK17VNWmcNuhS6l3Movqy3H8r59glI0QFqwq1EqQQI8v39ZJTUQiq8viLLw67Kq7K61qI3rOE3Cs/1QSd2FFyPtAqbNxrjp3f5EclFaMVaVLmxFRWd7LsDXA2M0qgL0L3myIFxDd2c5LbCiyyHGpMlsxJ5MNzowS7KCCaw7BvqsMCBqUriKGgoV5lfZAgMBAAECggEAB9Re2bcyjlcBsiW8XaOxIvZ9T68tgPaXDKmhQ38Yir3+UGYcLbkgxQ25ubpHCqyq3lD5VEM8ujbw8+G1sgoBqY5K+0+T/he1bqzZKuDM4BEuy83kk3x9mryqDgi8gdAE8XVDJrl0FZ5xaZYjt6e/W+wldZYcH3Bq+ihFlD6R+wdywJNI2nDogGPTyBKHcOo0L01hi8qjvRKMGPFBSkehpGMtCRewdXlSY0EO65XfhC3wb5D379+TFGAfPKu5+m71vI/uZaT0OGq1l72YQHs03M1Q3xFkFnJemPHy1AKmXkyH350PJvs/bfASdFn7vSGovYcQT2ZO7B2/bPr0drX0cQKBgQDqFwWaLMaYI4lfvpDC1b3Ti2W1RalFcQTP+HgilURzrvpzMxk1TzzzLZb4sh2VsR8BMbs2SDEWiOczK6F7z5t7u3wT3cljmwR1IAk7jChA4x1vvyqyslLmoYLlGGUjT5bRRKAGQAl1ievcZItBAqJLwiZbOfXAdgRR9nz7r8ewFQKBgQDpIlJA/xEI9yHgSI7H4Y8/j5Zl1ZCWCsSyk2iRHnnb6yzDNxWRvQSPTKHYc/JJMr3i3a+Tyhhm5tUkBgfmhVS+D0S9Y/lPI9OceoB0zI/nSjEJb0YrhfnOMbDjxIWB2A3mxjND3JolH4QDPdNdfY2txgG3ijLlR7mM5NRQe1i1tQKBgQDS3oIxa/xJuFlbYjLND/W7xmqMbIAbCcAoB89Qd939x7XcaD9hAkwJUxwYU3rLCY7AaKgYMdfmUNTUB42kFlQdlbojuzpa+518VKt8dLkeGni93Rr9dh2vm/ZpoRwaPuvA/2yXtL/QnblWA0xd951zWSVsMD3sbWNe4gecQbBEMQKBgQCdUx3y4q3aQPvJYO2JkXubxwgVXJOfzVCDudo85DYT5JZmfou9t7KWCX7GlSgRoX5m1Hch4qWo+2kmUDOQqrVPNPqMXCTn9SNeW4TITStnR7fjyAWwZU74iKv4aKw3vVdUPrhluT8EgkoR7ezvEEVF2XNbKpXCCC79F4b3cOWiaQKBgQDFrgHvifj2fvR3U8HiknduY6aPDdhLhi0fRxNMcz5k9gPysRqJPUdNAYjxKnL3EGltt+syu20P7KXqOwNNKsOHHsjIeTvbwnW9c0qKoQIqR0EyPdkEVrPIQgdntyvcJ808iGReTnEXDZ5Y3WYvovuULg5tYmsJdJUF2RLZVwg8NQ==";

    /**
     * 公钥
     */
    public static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1S5WXuAD/goAVeAly+Q3lnRiFEJ+J1yjSLa2ai6WlWGkJlVb+wiq0+osZho+2f/zjfgXh6orVctfEFfOMs6DMoO5+a2bI5j/FWhRpxUjgkr3fXexBHlcxFzTuUAwxQc0zbcasa36lWFtBL0HGqN4rfJSFClyte1TVpnDboUupdzKL6stx/K+fYJSNEBasKtRKkECPL9/WSU1EIqvL4iy8OuyquyutaiN6zhNwrP9UEndhRcj7QKmzca46d3+RHJRWjFWlS5sRUVney7A1wNjNKoC9C95siBcQ3dnOS2wosshxqTJbMSeTDc6MEuyggmsOwb6rDAgalK4ihoKFeZX2QIDAQAB";

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
     *  机器编号
     *  从获取机构编号，组织代码，机器编号 的请求接口一并返回，请求时，该参数应该用唯一设备号填充
     *  如果机器编号没有从后台返回，则使用 唯一设备号，在MainActivity启动时赋值一次
     *  不可以被清空或重置
     *  */
    public static String machineId = "";

    /**
     * 第三方机器ID
     * 使用 唯一设备号，在MainActivity启动时赋值一次
     */
    public static String thirdMachineId;

    /**
     * 第三方厂家 ID
     * 1 盖瑞
     */
    public static String thirdFactory ="1";

    /**
     * 商户ID
     * 不可以被清空或重置
     */
    public static String merchantId = "123456";

    /**
     * 大屏幕链接的 socket的地址
     */
    public static String machineIp;

    /**
     * 大屏幕链接的 socket的地址 端口
     * 端口写死，是12333
     * 不可以被清空或重置
     */
    public static final String machinePort = "12333";

    /**
     * 最后一次执行大屏的命令是什么
     * 如果最后一次大屏的通信是启动身体检测，则回来不打开视频广告画面
     * 记录的是 DapinSocketProxy 常量，例如 FLAG_SCREENFLAG_BODYTESTING_OPEN
     */
    public static String lastDapinSocketStr = "";

    /**
     * todo 身体检测厂家包名
     * 在requestMachineInfo里面获取 找machineRelation/findByMachineId
     * 获取后跳转时通过这个包名，来启动第三方身体检测供应商。
     * 如果为空字符串，则提示用户 去检测设备上操作检测
     * 不可以被清空或重置
     */
    public static String factoryResource = "";

    /**
     * 身体检测，厂家的打开页面
     */
    public static String factoryMainPage = "";

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
     * 在其他地方也叫 诊亭编号clinicSn
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
     * 用户电话
     * 检测用户是否注册的时候，添加进来的
     */
    public static String mobile;

    /**
     * 是否从 未支付 复诊单进入问诊
     */
    public static boolean isIntoVideoFromOrder;

    /**
     * 清空所有缓存数据
     * 比如使用在返回主页中
     */
    public static void clear(){
        token="";
        age=0;
        doc=null;
        departmentID="";
//        ssCard=null;
        isFindUserDone = false;
    }
}
