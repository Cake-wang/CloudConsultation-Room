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
     */
    public static int organId = 1;

    /**
     * 用户个人信息
     * 姓名
     * todo 是否要使用个人信息数据bean？
     */
    public static  String name = "";

    /**
     * 科室ID，当前选择的医生所在的科室
     */
    public static String departmentID="";

    /**
     * 复诊医生全信息
     */
    public static SearchDoctorListByBusTypeV2ResultEntity.QueryArrearsSummary.JsonResponseBean.OrganProfessionDTO.DocList.Doctor doc;

    /**
     * 用户医保卡信息，全局数据
     */
    public static SSCard ssCard;

    /**
     * 清空所有缓存数据
     * 比如使用在返回主页中
     */
    public static void clear(){
        token="";
        name="";
        doc=null;
        departmentID="";
        ssCard=null;
    }

}
