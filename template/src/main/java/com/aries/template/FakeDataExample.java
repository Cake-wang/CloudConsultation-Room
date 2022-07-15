package com.aries.template;

import com.aries.template.retrofit.repository.ApiRepository;
import com.aries.template.retrofit.repository.BaseRepository;
import com.decard.entitys.SSCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

/******
 * 自动化测试用的假数据填充
 * 网络自动化测试数据填充
 *
 * @author  ::: louis luo
 * Date ::: 2022/6/10 9:57 AM
 *
 */
public class FakeDataExample extends BaseRepository {

    public static final String phoneNumber = "18667115892";
//    public static final String idCard = "33052219861229693X";
    public static final String idCard = "330522198612296933";
    public static final String name = "飞飞";
    public static final String consultId = "815423874";
    public static final String departmentId = "18804";//行政科室编码
    public static final String organId = "2000300";//机构Id
    public static final String profession = "444986";//专科编码

    public static final String payway = "111";//支付类型代码
    public static final String decoctionFlag = "0";//是否代煎
    public static final String payMode = "1";//支付方式代码
    public static final String recipeId = "2257979";//电子处方ID

    public static final String clinicSn = "0017";//诊亭编号
    public static final String hospitalName = "test医院";//
    public static final String deptName = "test";//
    public static final String patientIdCard = "320511199704230754";//
    public static final String patientGender = "M";//
    public static final String doctorName = "gogo";//
    public static final String patientName = "gogo";//
    public static final String patientMobile = "13913884146";//
    public static final String patientDateOfBirth = "1990-04-13";//
    public static final String complaint = "感冒发烧";//
    public static final String diseaseName = "细菌感染";//
    public static final String outerOrderNo = "112131212131321320";//
    public static final String prescriptionType = "";//
    public static final String totalAmount = "100";//
    public static final String billNo = "";//
    public static final String paymentSeqNo = "";//
    public static final ArrayList<Map> drugs = new ArrayList(){{add(drugs());}};//
    public static final ArrayList<String> skus = new ArrayList(){{
        add("OTC-G-0012");
        add("OTC-G-0013");
    }};//
    // 复诊单的配置
    public static final String nickname = "eric"; //复诊人姓名 复诊单拿

    // 支付
    public static final String recipeFee = "0.01";//
    public static final ArrayList<String> recipeIds = new ArrayList(){{
        add("2258510");
    }};//
     public static final ArrayList<String> recipeCode = new ArrayList(){{
        add("2258594ngari999");
    }};//

    //------------环信的配置
    public static final String easemobUserName = "dev_patient_5494620"; //环信用户id
    public static final String password = "patient123"; //固定值，不用改

    //信令的配置
    public static final String patientUserId = "627dd085cc2f202b1d2146f3"; //用户userId
    public static final String doctorUserId = "627a861baa36e516a612dc80"; //医生userId


    //------------小鱼的配置
    public static final String xyAppId = "5886885697deb9f4760b3a5e1ab912b9a3b7dfd3"; //小鱼appid
    public static final String account = "8827"; //患者小鱼id，实际上是从信令获取到的
    //    private static final String meetingRoomNumber = "910007543093"; //会议室房间号,从接口获取到的
    public static final String meetingRoomNumber = "9038284649"; //会议室房间号,从接口获取到的
    //    private static final String meetingPassword = "383164"; //会议室密码，从接口获取到的
    public static final String meetingPassword = "348642"; //会议室密码，从接口获取到的

    public static Map<String,Object> drugs(){
        Map<String, Object> drugs = new HashMap<>();
        drugs.put("direction","口服");
        drugs.put("dosageUnit","粒");
        drugs.put("drugCommonName","牛黄蛇胆川贝液");
        drugs.put("drugTradeName","牛黄蛇胆川贝液");
        drugs.put("eachDosage","1");
        drugs.put("itemDays","3");
        drugs.put("price","1");
        drugs.put("quantity","1");
        drugs.put("quantityUnit","盒");
        drugs.put("sku","packing-bag");
        drugs.put("spec","12支/盒");
        return drugs;
    }


    public static Map<String,Object> getSignExampleMap(){
        Map<String,Object> tempMap = new HashMap<>();
        tempMap.put("bizContent","{appKey=app_web, tid=tid_2, startPage=0, requestMode=4, tabStatus=ongoing, recipeIndex=0, recipeLimit=10}");
        tempMap.put("common", ApiRepository.common.getInstance());
        tempMap.put("logTraceId","1657847389340");
        tempMap.put("merchantId","123456");
        tempMap.put("methodCode","patientList");
        return tempMap;
    }

    /**
     * 注入全局数据
     */
    public static void GlobalInject(){
        GlobalConfig.organId = 1;
        GlobalConfig.ssCard = fakeSSCard();
    }


    /**
     * 社保卡假数据
     */
    public static SSCard fakeSSCard(){
        SSCard ssCard = new SSCard();
        ssCard.setName(name);
        ssCard.setSSNum(idCard);
        ssCard.setCardNum(phoneNumber);
        return ssCard;
    }

    //===================
    // netWorkFake
    //===================


    /**
     * 按照机构和科室，查找二级科室
     */
    public RequestBody findValidDepartmentForRevisit() {
        // 除了公共的数据之外，还有其他的数据请求
        Map<String, String> bizContent = new HashMap<>();
        bizContent.put("organId", String.valueOf(1));
        bizContent.put("organProfessionId", String.valueOf(2));
        // 请求的类型 findValidDepartmentForRevisit
        RequestBody body = BodyCreate(bizContent, "findValidDepartmentForRevisit");
        return body;
    }

    /**
     * 获得复诊按机构查找一级科室
     */
    public RequestBody findValidOrganProfessionForRevisit() {
        // 除了公共的数据之外，还有其他的数据请求
        Map<String, String> bizContent = new HashMap<>();
        bizContent.put("organId", String.valueOf(1));
        // 请求的类型 findValidOrganProfessionForRevisit
        RequestBody body = BodyCreate(bizContent, "findValidOrganProfessionForRevisit");
        return body;
    }


    /**
     * 请求医生列表接口
     */
    public RequestBody searchDoctorListByBusTypeV2() {
        // 除了公共的数据之外，还有其他的数据请求
        Map<String, String> bizContent = new HashMap<>();
        bizContent.put("departmentId", String.valueOf(18804));
        bizContent.put("profession", String.valueOf(444986));
        bizContent.put("organId", String.valueOf(2000300));
        bizContent.put("sortKey", String.valueOf(1));//排序类型(1:综合排序，2:复诊价格排序，3:复诊量排序，4: 好评排序)
        bizContent.put("recipeConsultSourceFlag", String.valueOf(2));//医生类型(1：平台排班，2：无排班，3：his排班，4：医生自主排班)
        bizContent.put("start", String.valueOf(0));//是否快速返回，快速返回没有职业点和扩展信息//todo cc
        bizContent.put("limit", String.valueOf(10));//是否快速返回，快速返回没有职业点和扩展信息//todo cc
        bizContent.put("packageFlag", String.valueOf(1));//是否快速返回，快速返回没有职业点和扩展信息
        bizContent.put("search", "");//是否快速返回，快速返回没有职业点和扩展信息
        // 请求的类型 searchDoctorListByBusTypeV2
        RequestBody body = BodyCreate(bizContent, "searchDoctorListByBusTypeV2");
        return body;
    }

    /**
     * 能否发起复诊
     */
    public RequestBody canRequestOnlineConsult() {
        // 除了公共的数据之外，还有其他的数据请求
        Map<String, String> bizContent = new HashMap<>();
        bizContent.put("doctorId", String.valueOf(111739));
        // 请求的类型 canRequestOnlineConsult
        RequestBody body = BodyCreate(bizContent, "canRequestOnlineConsult");
        return body;
    }


    /**
     * 确认并发起复诊 的数据
     */
    public RequestBody requestConsultAndCdrOtherdocBody() {
        Map<String, String> questionnaire = new HashMap<>();
        questionnaire.put("pregnent", String.valueOf(0));
        questionnaire.put("pregnentMemo", "");
        questionnaire.put("alleric", String.valueOf(0));
        questionnaire.put("allericMemo", "");
        questionnaire.put("proposedDrugs", "鲜铁皮石斛");
        questionnaire.put("haveReaction", String.valueOf(0));
        questionnaire.put("haveReactionMemo", "");
        questionnaire.put("disease", "");
        questionnaire.put("confirmedDate", "");
        questionnaire.put("returnVisitStatus", String.valueOf(1));

        Map<String, String> cdrOtherdocs = new HashMap<>();
        cdrOtherdocs.put("docType", String.valueOf(9));
        cdrOtherdocs.put("docName", "20210522135342oz0d.jpg");
        cdrOtherdocs.put("docFormat", String.valueOf(13));
        cdrOtherdocs.put("docContent", "60a89c66f0f97817f591851c");

        Map<String, Object> bizContent = new HashMap<>();
        bizContent.put("mpiid", "2c95818f80b0ab390180b0db16ea0000");
        bizContent.put("appClientType", "APP_WEB");
        bizContent.put("appType", "ngari-health");
        bizContent.put("requestMode", String.valueOf(4));
        bizContent.put("consultOrgan", String.valueOf(2000300));
        bizContent.put("consultDepart", String.valueOf(18804));
        bizContent.put("consultDoctor", String.valueOf(111733));
        bizContent.put("consultCost", String.valueOf(0));
        bizContent.put("consultPrice", String.valueOf(0));
        bizContent.put("leaveMess", "");
        bizContent.put("questionnaire", questionnaire);
        bizContent.put("cdrOtherdocs", cdrOtherdocs);

        RequestBody body = BodyCreate(bizContent, "requestConsultAndCdrOtherdoc");
        return body;
    }
}
