package com.aries.template.retrofit.service;


import com.aries.library.fast.retrofit.FastRetrofit;
import com.aries.template.constant.ApiConstant;
import com.aries.template.entity.AuthCodeResultEntity;
import com.aries.template.entity.BaseMovieEntity;
import com.aries.template.entity.BatchCreateOrderEntity;
import com.aries.template.entity.CanRequestOnlineConsultResultEntity;
import com.aries.template.entity.CancelregisterResultEntity;
import com.aries.template.entity.ConfigurationToThirdForPatientEntity;
import com.aries.template.entity.CreateOrderResultEntity;
import com.aries.template.entity.FindRecipesForPatientAndTabStatusEntity;
import com.aries.template.entity.FindUserResultEntity;
import com.aries.template.entity.FindValidDepartmentForRevisitResultEntity;
import com.aries.template.entity.FindValidOrganProfessionForRevisitResultEntity;
import com.aries.template.entity.GetConfigurationToThirdForPatientResultEntity;
import com.aries.template.entity.GetConsultAndPatientAndDoctorByIdEntity;
import com.aries.template.entity.GetConsultsAndRecipesResultEntity;
import com.aries.template.entity.GetMedicalInfoEntity;
import com.aries.template.entity.GetPatientRecipeByIdEntity;
import com.aries.template.entity.GetRecipeListByConsultIdEntity;
import com.aries.template.entity.GetStockInfoEntity;
import com.aries.template.entity.IsRegisterResultEntity;
import com.aries.template.entity.MachineEntity;
import com.aries.template.entity.PatientListEntity;
import com.aries.template.entity.PayOrderEntity;
import com.aries.template.entity.PrescriptionPushEntity;
import com.aries.template.entity.RegisterResultEntity;
import com.aries.template.entity.RequestConsultAndCdrOtherdocResultEntity;
import com.aries.template.entity.RoomIdInsAuthEntity;
import com.aries.template.entity.SearchDoctorListByBusTypeV2ResultEntity;
import com.aries.template.entity.UpdateEntity;
import com.aries.template.widget.mgson.MFastRetrofit;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * @Author: AriesHoo on 2018/7/30 14:01
 * @E-Mail: AriesHoo@126.com
 * Function: 接口定义
 * Description:
 */
public interface ApiService {

    /**
     * 获取电影数据
     *
     * @param url
     * @param map
     * @return
     */
    @GET("{url}")
    Observable<BaseMovieEntity> getMovie(@Path("url") String url, @QueryMap Map<String, Object> map);

    /**
     * 检查应用更新--同时设置了Method及Header模式重定向请求Url,默认Method优先;
     * 可通过{@link FastRetrofit#setHeaderPriorityEnable(boolean)}设置Header模式优先
     *
     * @param
     * @return
     */
    @GET(ApiConstant.API_UPDATE_APP)
    @Headers({MFastRetrofit.BASE_URL_NAME_HEADER + ApiConstant.API_UPDATE_APP_KEY})
    Observable<UpdateEntity> updateApp();

//    @Headers("Content-Type: application/json")
//    @POST(ApiConstant.QueryTermialInfo)
//    Observable<UserInfoEntityAll> checkUserInfo(@Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.isRegister)
    Observable<IsRegisterResultEntity> isRegister(@Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.findUser)
    Observable<FindUserResultEntity> findUser(@Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.register)
    Observable<RegisterResultEntity> register(@Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.getAuthCode)
    Observable<AuthCodeResultEntity> getAuthCode(@Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.doBaseNgariRequest)
    Observable<CancelregisterResultEntity> patientCancelGraphicTextConsult(@Body RequestBody body);

    /**
     * 复诊按机构查找一级科室
     */
    @Headers("Content-Type: application/json")
    @POST(ApiConstant.doBaseNgariRequest)
    Observable<FindValidOrganProfessionForRevisitResultEntity> findValidOrganProfessionForRevisit(@Body RequestBody body);

    /**
     * 复诊按机构查找一级科室
     */
    @Headers("Content-Type: application/json")
    @POST(ApiConstant.doBaseNgariRequest)
    Observable<FindValidDepartmentForRevisitResultEntity> findValidDepartmentForRevisit(@Body RequestBody body);

    /**
     * 查找医生列表
     */
    @Headers("Content-Type: application/json")
    @POST(ApiConstant.doBaseNgariRequest)
    Observable<SearchDoctorListByBusTypeV2ResultEntity> searchDoctorListByBusTypeV2(@Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.doBaseNgariRequest)
    Observable<CanRequestOnlineConsultResultEntity> canRequestOnlineConsult(@Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.doBaseNgariRequest)
    Observable<RequestConsultAndCdrOtherdocResultEntity> requestConsultAndCdrOtherdoc(@Body RequestBody body);

//    @Headers("Content-Type: application/json")
//    @POST(ApiConstant.doBaseNgariRequest)
//    Observable<GetConfigurationToThirdForPatientResultEntity> getConfigurationToThirdForPatient(@Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.doBaseNgariRequest)
    Observable<CreateOrderResultEntity> createOrder(@Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.getConsultsAndRecipes)
    Observable<GetConsultsAndRecipesResultEntity> getConsultsAndRecipes(@Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.doBaseNgariRequest)
    Observable<RequestConsultAndCdrOtherdocResultEntity> presettlement(@Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.doBaseNgariRequest)
    Observable<RequestConsultAndCdrOtherdocResultEntity> paySuccess(@Body RequestBody body);

    /**
     * 2.1.7 根据机器ID获取机器信息
     */
    @Headers("Content-Type: application/json")
    @POST(ApiConstant.machineRelationByMachineId)
    Observable<MachineEntity> findByMachineId(@Body RequestBody body);

    /**
     * 3.1.11 获取第三方配置信息
     */
    @Headers("Content-Type: application/json")
    @POST(ApiConstant.doBaseNgariRequest)
    Observable<ConfigurationToThirdForPatientEntity> getConfigurationToThirdForPatient(@Body RequestBody body);


    /**
     * 3.1.18 查询复诊单的小鱼视频会议室房间号和密码 v
     * 通过这2个参数，来打开小鱼视频
     */
    @Headers("Content-Type: application/json")
    @POST(ApiConstant.doBaseNgariRequest)
    Observable<RoomIdInsAuthEntity> roomIdInsAuth(@Body RequestBody body);

    /**
     * 4.1.4 处方药品推送接口 v
     * 完成支付之后，执行库存推送接口
     * 注意这是个 doBaseGareaRequest 盖瑞接口
     */
    @Headers("Content-Type: application/json")
    @POST(ApiConstant.doBaseGareaRequest)
    Observable<PrescriptionPushEntity> prescriptionPush(@Body RequestBody body);

    /**
     * 4.1.5 查询设备所有库存接口 v
     * 注意这是个 doBaseGareaRequest 盖瑞接口
     */
    @Headers("Content-Type: application/json")
    @POST(ApiConstant.doBaseGareaRequest)
    Observable<GetMedicalInfoEntity> getMedicalInfo(@Body RequestBody body);

    /**
     * 4.1.6 查询部分库存信息 v
     * 注意这是个 doBaseGareaRequest 盖瑞接口
     */
    @Headers("Content-Type: application/json")
    @POST(ApiConstant.doBaseGareaRequest)
    Observable<GetStockInfoEntity> getStockInfo(@Body RequestBody body);

    /**
     * 3.11.	处方合并生成订单接口
     * 历史原因，处方下单的时候，可以让医院传递具体的费用（有些药品可能医院那边的价格更准确）
     */
    @Headers("Content-Type: application/json")
    @POST(ApiConstant.doBaseNgariRequest)
    Observable<BatchCreateOrderEntity> batchCreateOrder(@Body RequestBody body);

    /**
     * 3.12.	处方合并生成订单接口
     * 历史原因，处方下单的时候，可以让医院传递具体的费用（有些药品可能医院那边的价格更准确）
     */
    @Headers("Content-Type: application/json")
    @POST(ApiConstant.doBaseNgariRequest)
    Observable<PayOrderEntity> payOrder(@Body RequestBody body);

    /**
     * 复诊单详细信息
     * 可以被用来轮询是否已经支付
     */
    @Headers("Content-Type: application/json")
    @POST(ApiConstant.doBaseNgariRequest)
    Observable<GetConsultAndPatientAndDoctorByIdEntity> getConsultAndPatientAndDoctorById(@Body RequestBody body);

    /**
     * 3.1.3 患者最新待处理处方
     *
     */
    @Headers("Content-Type: application/json")
    @POST(ApiConstant.doBaseNgariRequest)
    Observable<FindRecipesForPatientAndTabStatusEntity> findRecipesForPatientAndTabStatus(@Body RequestBody body);


    /**
     * 处方单详细信息
     * 可以被用来轮询是否已经支付
     */
    @Headers("Content-Type: application/json")
    @POST(ApiConstant.doBaseNgariRequest)
    Observable<GetPatientRecipeByIdEntity> getPatientRecipeById(@Body RequestBody body);

    /**
     * 获取病人详细数据 根据 TID
     * 唯一获得 mpiId 的地方
     */
    @Headers("Content-Type: application/json")
    @POST(ApiConstant.doBaseNgariRequest)
    Observable<PatientListEntity> getPatientList(@Body RequestBody body);


    /**
     * 获取处方列表
     * 支付中，通过复诊单id来查询处方单id
     */
    @Headers("Content-Type: application/json")
    @POST(ApiConstant.doBaseNgariRequest)
    Observable<GetRecipeListByConsultIdEntity> getRecipeListByConsultId(@Body RequestBody body);
}
