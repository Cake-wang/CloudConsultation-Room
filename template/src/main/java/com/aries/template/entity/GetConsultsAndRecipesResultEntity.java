package com.aries.template.entity;

import java.util.Date;
import java.util.List;

/**
 * @Author: AriesHoo on 2018/11/19 14:17
 * @E-Mail: AriesHoo@126.com
 * @Function: 检查新版本实体
 * @Description:
 */
public class GetConsultsAndRecipesResultEntity {

    /**
     * versionCode : 230
     * versionName : 2.2.10-beta1
     * url : https://raw.githubusercontent.com/AriesHoo/FastLib/master/apk/sample.apk
     * force : true
     * message : 优化:调整ActivityFragmentControl 将状态栏及导航栏控制增加
     * 优化:多状态管理StatusLayoutManager调整完成
     * 优化:滑动返回控制swipeBack功能新增各种回调功能
     * 优化:将原默认配置方法调整到最终实现类功能
     * 优化:其它细节优化
     */

    public String code;
    public String message;
    public String sign;

    public boolean success;

    public QueryArrearsSummary data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public QueryArrearsSummary getData() {
        return data;
    }

    public void setData(QueryArrearsSummary data) {
        this.data = data;
    }

    public class QueryArrearsSummary {

        public Integer statusCode;
        public String requestId;
        public String caErrorMsg;
        public String errorMessage;

        public boolean success;

        public JsonResponseBean jsonResponseBean;
        public Consults consults;
        public Recipes recipes;

        public Integer getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(Integer statusCode) {
            this.statusCode = statusCode;
        }

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public String getCaErrorMsg() {
            return caErrorMsg;
        }

        public void setCaErrorMsg(String caErrorMsg) {
            this.caErrorMsg = caErrorMsg;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public JsonResponseBean getJsonResponseBean() {
            return jsonResponseBean;
        }

        public void setJsonResponseBean(JsonResponseBean jsonResponseBean) {
            this.jsonResponseBean = jsonResponseBean;
        }

        public Consults getConsults() {
            return consults;
        }

        public void setConsults(Consults consults) {
            this.consults = consults;
        }

        public Recipes getRecipes() {
            return recipes;
        }

        public void setRecipes(Recipes recipes) {
            this.recipes = recipes;
        }

        public class JsonResponseBean {

            public Integer code;

            public String msg;
            public String properties;

            public boolean body;

            public Integer getCode() {
                return code;
            }

            public void setCode(Integer code) {
                this.code = code;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }

            public String getProperties() {
                return properties;
            }

            public void setProperties(String properties) {
                this.properties = properties;
            }

            public boolean isBody() {
                return body;
            }

            public void setBody(boolean body) {
                this.body = body;
            }

            public class Msg {

                public Integer status;
                public Integer cid;

                public Integer getStatus() {
                    return status;
                }

                public void setStatus(Integer status) {
                    this.status = status;
                }

                public Integer getCid() {
                    return cid;
                }

                public void setCid(Integer cid) {
                    this.cid = cid;
                }
            }


        }

        public class Consults {

            public Integer consultId;

            public Date requestTime;

            public Integer consultOrgan;
            public Integer consultDepart;
            public Integer consultDoctor;

            public String leaveMess;

            public Date appointTime;
            public Date appointEndTime;

            public Integer payflag;

            public String payflagText;

            public Date startDate;
            public Date endDate;

            public Integer exeDoctor;
            public Integer exeDepart;
            public Integer exeOrgan;

            public Date cancelTime;

            public String cancelCause;
            public String tradeNo;
            public String outTradeNo;

            public Date paymentDate;

            public Double consultPrice;

            public Integer groupMode;

            public String consultOrganText;
            public String consultDoctorText;
            public String groupModeText;

            public Integer status;

            public String statusText;

            public Integer getConsultId() {
                return consultId;
            }

            public void setConsultId(Integer consultId) {
                this.consultId = consultId;
            }

            public Date getRequestTime() {
                return requestTime;
            }

            public void setRequestTime(Date requestTime) {
                this.requestTime = requestTime;
            }

            public Integer getConsultOrgan() {
                return consultOrgan;
            }

            public void setConsultOrgan(Integer consultOrgan) {
                this.consultOrgan = consultOrgan;
            }

            public Integer getConsultDepart() {
                return consultDepart;
            }

            public void setConsultDepart(Integer consultDepart) {
                this.consultDepart = consultDepart;
            }

            public Integer getConsultDoctor() {
                return consultDoctor;
            }

            public void setConsultDoctor(Integer consultDoctor) {
                this.consultDoctor = consultDoctor;
            }

            public String getLeaveMess() {
                return leaveMess;
            }

            public void setLeaveMess(String leaveMess) {
                this.leaveMess = leaveMess;
            }

            public Date getAppointTime() {
                return appointTime;
            }

            public void setAppointTime(Date appointTime) {
                this.appointTime = appointTime;
            }

            public Date getAppointEndTime() {
                return appointEndTime;
            }

            public void setAppointEndTime(Date appointEndTime) {
                this.appointEndTime = appointEndTime;
            }

            public Integer getPayflag() {
                return payflag;
            }

            public void setPayflag(Integer payflag) {
                this.payflag = payflag;
            }

            public String getPayflagText() {
                return payflagText;
            }

            public void setPayflagText(String payflagText) {
                this.payflagText = payflagText;
            }

            public Date getStartDate() {
                return startDate;
            }

            public void setStartDate(Date startDate) {
                this.startDate = startDate;
            }

            public Date getEndDate() {
                return endDate;
            }

            public void setEndDate(Date endDate) {
                this.endDate = endDate;
            }

            public Integer getExeDoctor() {
                return exeDoctor;
            }

            public void setExeDoctor(Integer exeDoctor) {
                this.exeDoctor = exeDoctor;
            }

            public Integer getExeDepart() {
                return exeDepart;
            }

            public void setExeDepart(Integer exeDepart) {
                this.exeDepart = exeDepart;
            }

            public Integer getExeOrgan() {
                return exeOrgan;
            }

            public void setExeOrgan(Integer exeOrgan) {
                this.exeOrgan = exeOrgan;
            }

            public Date getCancelTime() {
                return cancelTime;
            }

            public void setCancelTime(Date cancelTime) {
                this.cancelTime = cancelTime;
            }

            public String getCancelCause() {
                return cancelCause;
            }

            public void setCancelCause(String cancelCause) {
                this.cancelCause = cancelCause;
            }

            public String getTradeNo() {
                return tradeNo;
            }

            public void setTradeNo(String tradeNo) {
                this.tradeNo = tradeNo;
            }

            public String getOutTradeNo() {
                return outTradeNo;
            }

            public void setOutTradeNo(String outTradeNo) {
                this.outTradeNo = outTradeNo;
            }

            public Date getPaymentDate() {
                return paymentDate;
            }

            public void setPaymentDate(Date paymentDate) {
                this.paymentDate = paymentDate;
            }

            public Double getConsultPrice() {
                return consultPrice;
            }

            public void setConsultPrice(Double consultPrice) {
                this.consultPrice = consultPrice;
            }

            public Integer getGroupMode() {
                return groupMode;
            }

            public void setGroupMode(Integer groupMode) {
                this.groupMode = groupMode;
            }

            public String getConsultOrganText() {
                return consultOrganText;
            }

            public void setConsultOrganText(String consultOrganText) {
                this.consultOrganText = consultOrganText;
            }

            public String getConsultDoctorText() {
                return consultDoctorText;
            }

            public void setConsultDoctorText(String consultDoctorText) {
                this.consultDoctorText = consultDoctorText;
            }

            public String getGroupModeText() {
                return groupModeText;
            }

            public void setGroupModeText(String groupModeText) {
                this.groupModeText = groupModeText;
            }

            public Integer getStatus() {
                return status;
            }

            public void setStatus(Integer status) {
                this.status = status;
            }

            public String getStatusText() {
                return statusText;
            }

            public void setStatusText(String statusText) {
                this.statusText = statusText;
            }
        }

        public class Recipes {

            public String recipeId;
            public String patientName;
            public String photo;
            public String patientSex;
            public String organDiseaseName;

            public Date signDate;
            public Double totalMoney;
            public String statusText;
            public Integer statusCode;
            public String recipeSurplusHours;

            public String logisticsCompany;
            public Integer recipeType;
            public String trackingNumber;

            public List<RecipeDetail> recipeDetail;

            public String getRecipeId() {
                return recipeId;
            }

            public void setRecipeId(String recipeId) {
                this.recipeId = recipeId;
            }

            public String getPatientName() {
                return patientName;
            }

            public void setPatientName(String patientName) {
                this.patientName = patientName;
            }

            public String getPhoto() {
                return photo;
            }

            public void setPhoto(String photo) {
                this.photo = photo;
            }

            public String getPatientSex() {
                return patientSex;
            }

            public void setPatientSex(String patientSex) {
                this.patientSex = patientSex;
            }

            public String getOrganDiseaseName() {
                return organDiseaseName;
            }

            public void setOrganDiseaseName(String organDiseaseName) {
                this.organDiseaseName = organDiseaseName;
            }

            public Date getSignDate() {
                return signDate;
            }

            public void setSignDate(Date signDate) {
                this.signDate = signDate;
            }

            public Double getTotalMoney() {
                return totalMoney;
            }

            public void setTotalMoney(Double totalMoney) {
                this.totalMoney = totalMoney;
            }

            public String getStatusText() {
                return statusText;
            }

            public void setStatusText(String statusText) {
                this.statusText = statusText;
            }

            public Integer getStatusCode() {
                return statusCode;
            }

            public void setStatusCode(Integer statusCode) {
                this.statusCode = statusCode;
            }

            public String getRecipeSurplusHours() {
                return recipeSurplusHours;
            }

            public void setRecipeSurplusHours(String recipeSurplusHours) {
                this.recipeSurplusHours = recipeSurplusHours;
            }

            public String getLogisticsCompany() {
                return logisticsCompany;
            }

            public void setLogisticsCompany(String logisticsCompany) {
                this.logisticsCompany = logisticsCompany;
            }

            public Integer getRecipeType() {
                return recipeType;
            }

            public void setRecipeType(Integer recipeType) {
                this.recipeType = recipeType;
            }

            public String getTrackingNumber() {
                return trackingNumber;
            }

            public void setTrackingNumber(String trackingNumber) {
                this.trackingNumber = trackingNumber;
            }

            public List<RecipeDetail> getRecipeDetail() {
                return recipeDetail;
            }

            public void setRecipeDetail(List<RecipeDetail> recipeDetail) {
                this.recipeDetail = recipeDetail;
            }

            public class RecipeDetail {

                private Integer recipeDetailId;
                private Integer recipeId;
                private String drugSpec;
                private Integer pack;
                private String drugUnit;
                private String drugName;
                private Double useDose;
                private Double defaultUseDose;
                private String useDoseUnit;
                private String dosageUnit;
                private String usingRate;
                private String usePathways;
                private Double useTotalDose;
                private Double sendNumber;
                private Integer useDays;
                private Double drugCost;
                private String memo;
                private String organDrugCode;

                public Integer getRecipeDetailId() {
                    return recipeDetailId;
                }

                public void setRecipeDetailId(Integer recipeDetailId) {
                    this.recipeDetailId = recipeDetailId;
                }

                public Integer getRecipeId() {
                    return recipeId;
                }

                public void setRecipeId(Integer recipeId) {
                    this.recipeId = recipeId;
                }

                public String getDrugSpec() {
                    return drugSpec;
                }

                public void setDrugSpec(String drugSpec) {
                    this.drugSpec = drugSpec;
                }

                public Integer getPack() {
                    return pack;
                }

                public void setPack(Integer pack) {
                    this.pack = pack;
                }

                public String getDrugUnit() {
                    return drugUnit;
                }

                public void setDrugUnit(String drugUnit) {
                    this.drugUnit = drugUnit;
                }

                public String getDrugName() {
                    return drugName;
                }

                public void setDrugName(String drugName) {
                    this.drugName = drugName;
                }

                public Double getUseDose() {
                    return useDose;
                }

                public void setUseDose(Double useDose) {
                    this.useDose = useDose;
                }

                public Double getDefaultUseDose() {
                    return defaultUseDose;
                }

                public void setDefaultUseDose(Double defaultUseDose) {
                    this.defaultUseDose = defaultUseDose;
                }

                public String getUseDoseUnit() {
                    return useDoseUnit;
                }

                public void setUseDoseUnit(String useDoseUnit) {
                    this.useDoseUnit = useDoseUnit;
                }

                public String getDosageUnit() {
                    return dosageUnit;
                }

                public void setDosageUnit(String dosageUnit) {
                    this.dosageUnit = dosageUnit;
                }

                public String getUsingRate() {
                    return usingRate;
                }

                public void setUsingRate(String usingRate) {
                    this.usingRate = usingRate;
                }

                public String getUsePathways() {
                    return usePathways;
                }

                public void setUsePathways(String usePathways) {
                    this.usePathways = usePathways;
                }

                public Double getUseTotalDose() {
                    return useTotalDose;
                }

                public void setUseTotalDose(Double useTotalDose) {
                    this.useTotalDose = useTotalDose;
                }

                public Double getSendNumber() {
                    return sendNumber;
                }

                public void setSendNumber(Double sendNumber) {
                    this.sendNumber = sendNumber;
                }

                public Integer getUseDays() {
                    return useDays;
                }

                public void setUseDays(Integer useDays) {
                    this.useDays = useDays;
                }

                public Double getDrugCost() {
                    return drugCost;
                }

                public void setDrugCost(Double drugCost) {
                    this.drugCost = drugCost;
                }

                public String getMemo() {
                    return memo;
                }

                public void setMemo(String memo) {
                    this.memo = memo;
                }

                public String getOrganDrugCode() {
                    return organDrugCode;
                }

                public void setOrganDrugCode(String organDrugCode) {
                    this.organDrugCode = organDrugCode;
                }
            }

        }

    }

}
