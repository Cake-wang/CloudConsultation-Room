package com.aries.template.retrofit.service;


import com.aries.library.fast.retrofit.FastRetrofit;
import com.aries.template.constant.ApiConstant;
import com.aries.template.entity.BaseMovieEntity;
import com.aries.template.entity.CanRequestOnlineConsultResultEntity;
import com.aries.template.entity.CancelregisterResultEntity;
import com.aries.template.entity.CreateOrderResultEntity;
import com.aries.template.entity.FindUserResultEntity;
import com.aries.template.entity.FindValidDepartmentForRevisitResultEntity;
import com.aries.template.entity.FindValidOrganProfessionForRevisitResultEntity;
import com.aries.template.entity.GetConfigurationToThirdForPatientResultEntity;
import com.aries.template.entity.GetConsultsAndRecipesResultEntity;
import com.aries.template.entity.IsRegisterResultEntity;
import com.aries.template.entity.RegisterResultEntity;
import com.aries.template.entity.RequestConsultAndCdrOtherdocResultEntity;
import com.aries.template.entity.SearchDoctorListByBusTypeV2ResultEntity;
import com.aries.template.entity.UpdateEntity;
import com.aries.template.widget.mgson.MFastRetrofit;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
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

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.doBaseNgariRequest)
    Observable<GetConfigurationToThirdForPatientResultEntity> getConfigurationToThirdForPatient(@Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.doBaseNgariRequest)
    Observable<CreateOrderResultEntity> createOrder(@Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.getConsultsAndRecipes)
    Observable<GetConsultsAndRecipesResultEntity> getConsultsAndRecipes(@Body RequestBody body);

}
