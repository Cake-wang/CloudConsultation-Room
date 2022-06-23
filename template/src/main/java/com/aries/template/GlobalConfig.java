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

    /**
     *  全世界网络通信凭据
     */
    public static String token = "";

    /**
     * 组织代码
     * 用于确定医院组织的代码。1 浙大附属邵逸夫医院
     * 目前ID是根据机器的ID，发送到后端，后端返回组织代码给机器。
     * 一台机器的组织代码是固定的。
     */
    public static int organId = 1;

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
    }

}
