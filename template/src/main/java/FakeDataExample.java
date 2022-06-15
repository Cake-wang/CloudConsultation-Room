import android.content.Context;

import com.aries.library.fast.retrofit.FastRetryWhen;
import com.aries.library.fast.retrofit.FastTransformer;
import com.aries.template.entity.CanRequestOnlineConsultResultEntity;
import com.aries.template.entity.FindValidDepartmentForRevisitResultEntity;
import com.aries.template.entity.FindValidOrganProfessionForRevisitResultEntity;
import com.aries.template.entity.SearchDoctorListByBusTypeV2ResultEntity;
import com.aries.template.retrofit.repository.BaseRepository;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
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

    /**
     * 获得复诊按机构查找一级科室
     */
    public RequestBody findValidOrganProfessionForRevisit() {
        // 除了公共的数据之外，还有其他的数据请求
        Map<String,String> bizContent = new HashMap<>();
        bizContent.put("organId",String.valueOf(1));
        // 请求的类型 findValidOrganProfessionForRevisit
        RequestBody body = BodyCreate(bizContent,"findValidOrganProfessionForRevisit");
        return body;
    }


    /**
     * 按照机构和科室，查找二级科室
     */
    public RequestBody findValidDepartmentForRevisit() {
        // 除了公共的数据之外，还有其他的数据请求
        Map<String,String> bizContent = new HashMap<>();
        bizContent.put("organId",String.valueOf(1));
        bizContent.put("organProfessionId",String.valueOf(2));
        // 请求的类型 findValidDepartmentForRevisit
        RequestBody body = BodyCreate(bizContent,"findValidDepartmentForRevisit");
        return body;
    }



    /**
     * 请求医生列表接口
     */
    public RequestBody searchDoctorListByBusTypeV2(){
        // 除了公共的数据之外，还有其他的数据请求
        Map<String,String> bizContent = new HashMap<>();
        bizContent.put("departmentId",String.valueOf(18804));
        bizContent.put("profession",String.valueOf(444986));
        bizContent.put("organId",String.valueOf(2000300));
        bizContent.put("sortKey",String.valueOf(1));//排序类型(1:综合排序，2:复诊价格排序，3:复诊量排序，4: 好评排序)
        bizContent.put("recipeConsultSourceFlag",String.valueOf(2));//医生类型(1：平台排班，2：无排班，3：his排班，4：医生自主排班)
        bizContent.put("start",String.valueOf(0));//是否快速返回，快速返回没有职业点和扩展信息//todo cc
        bizContent.put("limit",String.valueOf(10));//是否快速返回，快速返回没有职业点和扩展信息//todo cc
        bizContent.put("packageFlag",String.valueOf(1));//是否快速返回，快速返回没有职业点和扩展信息
        bizContent.put("search","");//是否快速返回，快速返回没有职业点和扩展信息
        // 请求的类型 searchDoctorListByBusTypeV2
        RequestBody body = BodyCreate(bizContent,"searchDoctorListByBusTypeV2");
        return body;
    }

    /**
     * 能否发起复诊
     */
    public RequestBody canRequestOnlineConsult() {
        // 除了公共的数据之外，还有其他的数据请求
        Map<String,String> bizContent = new HashMap<>();
        bizContent.put("doctorId",String.valueOf(111739));
        // 请求的类型 canRequestOnlineConsult
        RequestBody body = BodyCreate(bizContent,"canRequestOnlineConsult");
        return body;
    }


    /**
     * 确认并发起复诊 的数据
     */
    public RequestBody requestConsultAndCdrOtherdocBody(){
        Map<String,String> questionnaire =new HashMap<>();
        questionnaire.put("pregnent",String.valueOf(0));
        questionnaire.put("pregnentMemo","");
        questionnaire.put("alleric",String.valueOf(0));
        questionnaire.put("allericMemo","");
        questionnaire.put("proposedDrugs","鲜铁皮石斛");
        questionnaire.put("haveReaction",String.valueOf(0));
        questionnaire.put("haveReactionMemo","");
        questionnaire.put("disease","");
        questionnaire.put("confirmedDate","");
        questionnaire.put("returnVisitStatus",String.valueOf(1));

        Map<String,String> cdrOtherdocs =new HashMap<>();
        cdrOtherdocs.put("docType",String.valueOf(9));
        cdrOtherdocs.put("docName","20210522135342oz0d.jpg");
        cdrOtherdocs.put("docFormat",String.valueOf(13));
        cdrOtherdocs.put("docContent","60a89c66f0f97817f591851c");

        Map<String,Object> bizContent = new HashMap<>();
        bizContent.put("mpiid","2c95818f80b0ab390180b0db16ea0000");
        bizContent.put("appClientType","APP_WEB");
        bizContent.put("appType","ngari-health");
        bizContent.put("requestMode",String.valueOf(4));
        bizContent.put("consultOrgan",String.valueOf(2000300));
        bizContent.put("consultDepart",String.valueOf(18804));
        bizContent.put("consultDoctor",String.valueOf(111733));
        bizContent.put("consultCost",String.valueOf(0));
        bizContent.put("consultPrice",String.valueOf(0));
        bizContent.put("leaveMess","");
        bizContent.put("questionnaire",questionnaire);
        bizContent.put("cdrOtherdocs",cdrOtherdocs);

        RequestBody body = BodyCreate(bizContent,"requestConsultAndCdrOtherdoc");
        return body;
    }
}
