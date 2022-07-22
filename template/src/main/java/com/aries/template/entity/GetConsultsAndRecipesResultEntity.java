package com.aries.template.entity;

import java.io.Serializable;
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

    public class QueryArrearsSummary{

//        public Integer statusCode;
//        public String requestId;
//        public String caErrorMsg;
//        public String errorMessage;
//
//        public boolean success;
//
//        public JsonResponseBean jsonResponseBean;
        public List<Consults> consults;
        public List<Recipes> recipes;

//        public Integer getStatusCode() {
//            return statusCode;
//        }
//
//        public void setStatusCode(Integer statusCode) {
//            this.statusCode = statusCode;
//        }
//
//        public String getRequestId() {
//            return requestId;
//        }
//
//        public void setRequestId(String requestId) {
//            this.requestId = requestId;
//        }
//
//        public String getCaErrorMsg() {
//            return caErrorMsg;
//        }
//
//        public void setCaErrorMsg(String caErrorMsg) {
//            this.caErrorMsg = caErrorMsg;
//        }
//
//        public String getErrorMessage() {
//            return errorMessage;
//        }
//
//        public void setErrorMessage(String errorMessage) {
//            this.errorMessage = errorMessage;
//        }
//
//        public boolean isSuccess() {
//            return success;
//        }
//
//        public void setSuccess(boolean success) {
//            this.success = success;
//        }
//
//        public JsonResponseBean getJsonResponseBean() {
//            return jsonResponseBean;
//        }
//
//        public void setJsonResponseBean(JsonResponseBean jsonResponseBean) {
//            this.jsonResponseBean = jsonResponseBean;
//        }

        public List<Consults> getConsults() {
            return consults;
        }

        public void setConsults(List<Consults> consults) {
            this.consults = consults;
        }

        public List<Recipes> getRecipes() {
            return recipes;
        }

        public void setRecipes(List<Recipes> recipes) {
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

        public class Consults implements Serializable
        {
            private Consultss consults;

            private Doctor doctor;

            public void setConsults(Consultss consults){
                this.consults = consults;
            }
            public Consultss getConsults(){
                return this.consults;
            }
            public void setDoctor(Doctor doctor){
                this.doctor = doctor;
            }
            public Doctor getDoctor(){
                return this.doctor;
            }

            public class Doctor
            {
                private String name;

                private String gender;

                private String profession;

                private int organProfession;

                private String proTitle;

                private boolean teams;

                private int organ;

                private ExtendParam extendParam;

                private int currentOrgan;

                private int cloudAppointSourceFlag;

                private boolean canReport;

                private boolean hasReport;

                private String genderText;

                private String professionText;

                private String organProfessionText;

                private String proTitleText;

                private String organText;

                private String departmentText;

                private String sourceText;

                private String leaderText;

                private String groupTypeText;

                private String appointSourceFlagText;

                private String currentOrganText;

                private String cloudAppointSourceFlagText;

                private String gradeText;

                private String statusText;

                private String groupModeText;

                private String userTypeText;

                public void setName(String name){
                    this.name = name;
                }
                public String getName(){
                    return this.name;
                }
                public void setGender(String gender){
                    this.gender = gender;
                }
                public String getGender(){
                    return this.gender;
                }
                public void setProfession(String profession){
                    this.profession = profession;
                }
                public String getProfession(){
                    return this.profession;
                }
                public void setOrganProfession(int organProfession){
                    this.organProfession = organProfession;
                }
                public int getOrganProfession(){
                    return this.organProfession;
                }
                public void setProTitle(String proTitle){
                    this.proTitle = proTitle;
                }
                public String getProTitle(){
                    return this.proTitle;
                }
                public void setTeams(boolean teams){
                    this.teams = teams;
                }
                public boolean getTeams(){
                    return this.teams;
                }
                public void setOrgan(int organ){
                    this.organ = organ;
                }
                public int getOrgan(){
                    return this.organ;
                }
                public void setExtendParam(ExtendParam extendParam){
                    this.extendParam = extendParam;
                }
                public ExtendParam getExtendParam(){
                    return this.extendParam;
                }
                public void setCurrentOrgan(int currentOrgan){
                    this.currentOrgan = currentOrgan;
                }
                public int getCurrentOrgan(){
                    return this.currentOrgan;
                }
                public void setCloudAppointSourceFlag(int cloudAppointSourceFlag){
                    this.cloudAppointSourceFlag = cloudAppointSourceFlag;
                }
                public int getCloudAppointSourceFlag(){
                    return this.cloudAppointSourceFlag;
                }
                public void setCanReport(boolean canReport){
                    this.canReport = canReport;
                }
                public boolean getCanReport(){
                    return this.canReport;
                }
                public void setHasReport(boolean hasReport){
                    this.hasReport = hasReport;
                }
                public boolean getHasReport(){
                    return this.hasReport;
                }
                public void setGenderText(String genderText){
                    this.genderText = genderText;
                }
                public String getGenderText(){
                    return this.genderText;
                }
                public void setProfessionText(String professionText){
                    this.professionText = professionText;
                }
                public String getProfessionText(){
                    return this.professionText;
                }
                public void setOrganProfessionText(String organProfessionText){
                    this.organProfessionText = organProfessionText;
                }
                public String getOrganProfessionText(){
                    return this.organProfessionText;
                }
                public void setProTitleText(String proTitleText){
                    this.proTitleText = proTitleText;
                }
                public String getProTitleText(){
                    return this.proTitleText;
                }
                public void setOrganText(String organText){
                    this.organText = organText;
                }
                public String getOrganText(){
                    return this.organText;
                }
                public void setDepartmentText(String departmentText){
                    this.departmentText = departmentText;
                }
                public String getDepartmentText(){
                    return this.departmentText;
                }
                public void setSourceText(String sourceText){
                    this.sourceText = sourceText;
                }
                public String getSourceText(){
                    return this.sourceText;
                }
                public void setLeaderText(String leaderText){
                    this.leaderText = leaderText;
                }
                public String getLeaderText(){
                    return this.leaderText;
                }
                public void setGroupTypeText(String groupTypeText){
                    this.groupTypeText = groupTypeText;
                }
                public String getGroupTypeText(){
                    return this.groupTypeText;
                }
                public void setAppointSourceFlagText(String appointSourceFlagText){
                    this.appointSourceFlagText = appointSourceFlagText;
                }
                public String getAppointSourceFlagText(){
                    return this.appointSourceFlagText;
                }
                public void setCurrentOrganText(String currentOrganText){
                    this.currentOrganText = currentOrganText;
                }
                public String getCurrentOrganText(){
                    return this.currentOrganText;
                }
                public void setCloudAppointSourceFlagText(String cloudAppointSourceFlagText){
                    this.cloudAppointSourceFlagText = cloudAppointSourceFlagText;
                }
                public String getCloudAppointSourceFlagText(){
                    return this.cloudAppointSourceFlagText;
                }
                public void setGradeText(String gradeText){
                    this.gradeText = gradeText;
                }
                public String getGradeText(){
                    return this.gradeText;
                }
                public void setStatusText(String statusText){
                    this.statusText = statusText;
                }
                public String getStatusText(){
                    return this.statusText;
                }
                public void setGroupModeText(String groupModeText){
                    this.groupModeText = groupModeText;
                }
                public String getGroupModeText(){
                    return this.groupModeText;
                }
                public void setUserTypeText(String userTypeText){
                    this.userTypeText = userTypeText;
                }
                public String getUserTypeText(){
                    return this.userTypeText;
                }

                public class ExtendParam
                {
                }


            }

            public class Consultss
            {
                private int consultId;

                private String mpiid;

                private String mpiName;

                private int requestMode;

                private String requestMpi;

                private String requestTime;

                private int consultOrgan;

                private int consultDepart;

                private int consultDoctor;

                private int consultCost;

                private int payflag;

                private String endDate;

                private String cancelTime;

                private String cancelCause;

                private int consultStatus;

                private String paymentDate;

                private int consultPrice;

                private String sessionID;

                private String sessionStartTime;

                private String time;

                private boolean teams;

                private int requestMpiUrt;

                private int consultDoctorUrt;

                private String appId;

                private String openId;

                private boolean hasChat;

                private boolean remindFlag;

                private boolean signInRemindFlag;

                private int cancelRole;

                private int deviceId;

                private int groupMode;

                private int actualPrice;

                private boolean hasAdditionMessage;

                private int questionnaireId;

                private Questionnaire questionnaire;

                private int status;

                private boolean recipeReminderFlag;

                private String lastModified;

                private int expert;

                private String clientType;

                private String clientName;

                private int userType;

                private int medicalFlag;

                private int fundAmount;

                private int cashAmount;

                private int sbType;

                private int sbReduceAmount;

                private int reserveDoctorId;

                private int lastConsultId;

                private int revisitBussType;

                private String requestModeText;

                private String consultOrganText;

                private String consultDepartText;

                private String consultDoctorText;

                private String payflagText;

                private String exeDoctorText;

                private String exeDepartText;

                private String exeOrganText;

                private String refuseFlagText;

                private String cancelRoleText;

                private String groupModeText;

                private String userTypeText;

                private String workTypeText;

                public void setConsultId(int consultId){
                    this.consultId = consultId;
                }
                public int getConsultId(){
                    return this.consultId;
                }
                public void setMpiid(String mpiid){
                    this.mpiid = mpiid;
                }
                public String getMpiid(){
                    return this.mpiid;
                }
                public void setMpiName(String mpiName){
                    this.mpiName = mpiName;
                }
                public String getMpiName(){
                    return this.mpiName;
                }
                public void setRequestMode(int requestMode){
                    this.requestMode = requestMode;
                }
                public int getRequestMode(){
                    return this.requestMode;
                }
                public void setRequestMpi(String requestMpi){
                    this.requestMpi = requestMpi;
                }
                public String getRequestMpi(){
                    return this.requestMpi;
                }
                public void setRequestTime(String requestTime){
                    this.requestTime = requestTime;
                }
                public String getRequestTime(){
                    return this.requestTime;
                }
                public void setConsultOrgan(int consultOrgan){
                    this.consultOrgan = consultOrgan;
                }
                public int getConsultOrgan(){
                    return this.consultOrgan;
                }
                public void setConsultDepart(int consultDepart){
                    this.consultDepart = consultDepart;
                }
                public int getConsultDepart(){
                    return this.consultDepart;
                }
                public void setConsultDoctor(int consultDoctor){
                    this.consultDoctor = consultDoctor;
                }
                public int getConsultDoctor(){
                    return this.consultDoctor;
                }
                public void setConsultCost(int consultCost){
                    this.consultCost = consultCost;
                }
                public int getConsultCost(){
                    return this.consultCost;
                }
                public void setPayflag(int payflag){
                    this.payflag = payflag;
                }
                public int getPayflag(){
                    return this.payflag;
                }
                public void setEndDate(String endDate){
                    this.endDate = endDate;
                }
                public String getEndDate(){
                    return this.endDate;
                }
                public void setCancelTime(String cancelTime){
                    this.cancelTime = cancelTime;
                }
                public String getCancelTime(){
                    return this.cancelTime;
                }
                public void setCancelCause(String cancelCause){
                    this.cancelCause = cancelCause;
                }
                public String getCancelCause(){
                    return this.cancelCause;
                }
                public void setConsultStatus(int consultStatus){
                    this.consultStatus = consultStatus;
                }
                public int getConsultStatus(){
                    return this.consultStatus;
                }
                public void setPaymentDate(String paymentDate){
                    this.paymentDate = paymentDate;
                }
                public String getPaymentDate(){
                    return this.paymentDate;
                }
                public void setConsultPrice(int consultPrice){
                    this.consultPrice = consultPrice;
                }
                public int getConsultPrice(){
                    return this.consultPrice;
                }
                public void setSessionID(String sessionID){
                    this.sessionID = sessionID;
                }
                public String getSessionID(){
                    return this.sessionID;
                }
                public void setSessionStartTime(String sessionStartTime){
                    this.sessionStartTime = sessionStartTime;
                }
                public String getSessionStartTime(){
                    return this.sessionStartTime;
                }
                public void setTime(String time){
                    this.time = time;
                }
                public String getTime(){
                    return this.time;
                }
                public void setTeams(boolean teams){
                    this.teams = teams;
                }
                public boolean getTeams(){
                    return this.teams;
                }
                public void setRequestMpiUrt(int requestMpiUrt){
                    this.requestMpiUrt = requestMpiUrt;
                }
                public int getRequestMpiUrt(){
                    return this.requestMpiUrt;
                }
                public void setConsultDoctorUrt(int consultDoctorUrt){
                    this.consultDoctorUrt = consultDoctorUrt;
                }
                public int getConsultDoctorUrt(){
                    return this.consultDoctorUrt;
                }
                public void setAppId(String appId){
                    this.appId = appId;
                }
                public String getAppId(){
                    return this.appId;
                }
                public void setOpenId(String openId){
                    this.openId = openId;
                }
                public String getOpenId(){
                    return this.openId;
                }
                public void setHasChat(boolean hasChat){
                    this.hasChat = hasChat;
                }
                public boolean getHasChat(){
                    return this.hasChat;
                }
                public void setRemindFlag(boolean remindFlag){
                    this.remindFlag = remindFlag;
                }
                public boolean getRemindFlag(){
                    return this.remindFlag;
                }
                public void setSignInRemindFlag(boolean signInRemindFlag){
                    this.signInRemindFlag = signInRemindFlag;
                }
                public boolean getSignInRemindFlag(){
                    return this.signInRemindFlag;
                }
                public void setCancelRole(int cancelRole){
                    this.cancelRole = cancelRole;
                }
                public int getCancelRole(){
                    return this.cancelRole;
                }
                public void setDeviceId(int deviceId){
                    this.deviceId = deviceId;
                }
                public int getDeviceId(){
                    return this.deviceId;
                }
                public void setGroupMode(int groupMode){
                    this.groupMode = groupMode;
                }
                public int getGroupMode(){
                    return this.groupMode;
                }
                public void setActualPrice(int actualPrice){
                    this.actualPrice = actualPrice;
                }
                public int getActualPrice(){
                    return this.actualPrice;
                }
                public void setHasAdditionMessage(boolean hasAdditionMessage){
                    this.hasAdditionMessage = hasAdditionMessage;
                }
                public boolean getHasAdditionMessage(){
                    return this.hasAdditionMessage;
                }
                public void setQuestionnaireId(int questionnaireId){
                    this.questionnaireId = questionnaireId;
                }
                public int getQuestionnaireId(){
                    return this.questionnaireId;
                }
                public void setQuestionnaire(Questionnaire questionnaire){
                    this.questionnaire = questionnaire;
                }
                public Questionnaire getQuestionnaire(){
                    return this.questionnaire;
                }
                public void setStatus(int status){
                    this.status = status;
                }
                public int getStatus(){
                    return this.status;
                }
                public void setRecipeReminderFlag(boolean recipeReminderFlag){
                    this.recipeReminderFlag = recipeReminderFlag;
                }
                public boolean getRecipeReminderFlag(){
                    return this.recipeReminderFlag;
                }
                public void setLastModified(String lastModified){
                    this.lastModified = lastModified;
                }
                public String getLastModified(){
                    return this.lastModified;
                }
                public void setExpert(int expert){
                    this.expert = expert;
                }
                public int getExpert(){
                    return this.expert;
                }
                public void setClientType(String clientType){
                    this.clientType = clientType;
                }
                public String getClientType(){
                    return this.clientType;
                }
                public void setClientName(String clientName){
                    this.clientName = clientName;
                }
                public String getClientName(){
                    return this.clientName;
                }
                public void setUserType(int userType){
                    this.userType = userType;
                }
                public int getUserType(){
                    return this.userType;
                }
                public void setMedicalFlag(int medicalFlag){
                    this.medicalFlag = medicalFlag;
                }
                public int getMedicalFlag(){
                    return this.medicalFlag;
                }
                public void setFundAmount(int fundAmount){
                    this.fundAmount = fundAmount;
                }
                public int getFundAmount(){
                    return this.fundAmount;
                }
                public void setCashAmount(int cashAmount){
                    this.cashAmount = cashAmount;
                }
                public int getCashAmount(){
                    return this.cashAmount;
                }
                public void setSbType(int sbType){
                    this.sbType = sbType;
                }
                public int getSbType(){
                    return this.sbType;
                }
                public void setSbReduceAmount(int sbReduceAmount){
                    this.sbReduceAmount = sbReduceAmount;
                }
                public int getSbReduceAmount(){
                    return this.sbReduceAmount;
                }
                public void setReserveDoctorId(int reserveDoctorId){
                    this.reserveDoctorId = reserveDoctorId;
                }
                public int getReserveDoctorId(){
                    return this.reserveDoctorId;
                }
                public void setLastConsultId(int lastConsultId){
                    this.lastConsultId = lastConsultId;
                }
                public int getLastConsultId(){
                    return this.lastConsultId;
                }
                public void setRevisitBussType(int revisitBussType){
                    this.revisitBussType = revisitBussType;
                }
                public int getRevisitBussType(){
                    return this.revisitBussType;
                }
                public void setRequestModeText(String requestModeText){
                    this.requestModeText = requestModeText;
                }
                public String getRequestModeText(){
                    return this.requestModeText;
                }
                public void setConsultOrganText(String consultOrganText){
                    this.consultOrganText = consultOrganText;
                }
                public String getConsultOrganText(){
                    return this.consultOrganText;
                }
                public void setConsultDepartText(String consultDepartText){
                    this.consultDepartText = consultDepartText;
                }
                public String getConsultDepartText(){
                    return this.consultDepartText;
                }
                public void setConsultDoctorText(String consultDoctorText){
                    this.consultDoctorText = consultDoctorText;
                }
                public String getConsultDoctorText(){
                    return this.consultDoctorText;
                }
                public void setPayflagText(String payflagText){
                    this.payflagText = payflagText;
                }
                public String getPayflagText(){
                    return this.payflagText;
                }
                public void setExeDoctorText(String exeDoctorText){
                    this.exeDoctorText = exeDoctorText;
                }
                public String getExeDoctorText(){
                    return this.exeDoctorText;
                }
                public void setExeDepartText(String exeDepartText){
                    this.exeDepartText = exeDepartText;
                }
                public String getExeDepartText(){
                    return this.exeDepartText;
                }
                public void setExeOrganText(String exeOrganText){
                    this.exeOrganText = exeOrganText;
                }
                public String getExeOrganText(){
                    return this.exeOrganText;
                }
                public void setRefuseFlagText(String refuseFlagText){
                    this.refuseFlagText = refuseFlagText;
                }
                public String getRefuseFlagText(){
                    return this.refuseFlagText;
                }
                public void setCancelRoleText(String cancelRoleText){
                    this.cancelRoleText = cancelRoleText;
                }
                public String getCancelRoleText(){
                    return this.cancelRoleText;
                }
                public void setGroupModeText(String groupModeText){
                    this.groupModeText = groupModeText;
                }
                public String getGroupModeText(){
                    return this.groupModeText;
                }
                public void setUserTypeText(String userTypeText){
                    this.userTypeText = userTypeText;
                }
                public String getUserTypeText(){
                    return this.userTypeText;
                }
                public void setWorkTypeText(String workTypeText){
                    this.workTypeText = workTypeText;
                }
                public String getWorkTypeText(){
                    return this.workTypeText;
                }

                public class Questionnaire
                {
                    private int questionnaireId;

                    private int pregnent;

                    private int alleric;

                    private int diseaseStatus;

                    private String proposedDrugs;

                    private int haveTake;

                    private int haveReaction;

                    private int returnVisitStatus;

                    public void setQuestionnaireId(int questionnaireId){
                        this.questionnaireId = questionnaireId;
                    }
                    public int getQuestionnaireId(){
                        return this.questionnaireId;
                    }
                    public void setPregnent(int pregnent){
                        this.pregnent = pregnent;
                    }
                    public int getPregnent(){
                        return this.pregnent;
                    }
                    public void setAlleric(int alleric){
                        this.alleric = alleric;
                    }
                    public int getAlleric(){
                        return this.alleric;
                    }
                    public void setDiseaseStatus(int diseaseStatus){
                        this.diseaseStatus = diseaseStatus;
                    }
                    public int getDiseaseStatus(){
                        return this.diseaseStatus;
                    }
                    public void setProposedDrugs(String proposedDrugs){
                        this.proposedDrugs = proposedDrugs;
                    }
                    public String getProposedDrugs(){
                        return this.proposedDrugs;
                    }
                    public void setHaveTake(int haveTake){
                        this.haveTake = haveTake;
                    }
                    public int getHaveTake(){
                        return this.haveTake;
                    }
                    public void setHaveReaction(int haveReaction){
                        this.haveReaction = haveReaction;
                    }
                    public int getHaveReaction(){
                        return this.haveReaction;
                    }
                    public void setReturnVisitStatus(int returnVisitStatus){
                        this.returnVisitStatus = returnVisitStatus;
                    }
                    public int getReturnVisitStatus(){
                        return this.returnVisitStatus;
                    }
                }

            }

        }


        public class Recipes implements Serializable{
            public Integer recipeId;
            public String patientName;
            public String patientSex;
            public String organDiseaseName;
            public String signDate;
            public Double totalMoney;
            public String statusText;
            public Integer statusCode;
            public Integer status;
            public String recipeSurplusHours;
            public Integer recipeType;
            public List<RecipeDetail> recipeDetailBeans;

            public Integer getRecipeId() {
                return recipeId;
            }

            public void setRecipeId(Integer recipeId) {
                this.recipeId = recipeId;
            }

            public String getPatientName() {
                return patientName;
            }

            public void setPatientName(String patientName) {
                this.patientName = patientName;
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

            public String getSignDate() {
                return signDate;
            }

            public void setSignDate(String signDate) {
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

            public Integer getStatus() {
                return status;
            }

            public void setStatus(Integer status) {
                this.status = status;
            }

            public String getRecipeSurplusHours() {
                return recipeSurplusHours;
            }

            public void setRecipeSurplusHours(String recipeSurplusHours) {
                this.recipeSurplusHours = recipeSurplusHours;
            }

            public Integer getRecipeType() {
                return recipeType;
            }

            public void setRecipeType(Integer recipeType) {
                this.recipeType = recipeType;
            }

            public List<RecipeDetail> getRecipeDetailBeans() {
                return recipeDetailBeans;
            }

            public void setRecipeDetailBeans(List<RecipeDetail> recipeDetailBeans) {
                this.recipeDetailBeans = recipeDetailBeans;
            }

            public class RecipeDetail {
                public Integer recipeDetailId;
                public Integer recipeId;
                public String drugSpec;
                public Integer pack;
                public String drugUnit;
                public String drugName;
                public Double useDose;
                public Double drugCost;
                public Double defaultUseDose;
                public String useDoseUnit;
                public String dosageUnit;
                public String usingRate;
                public String usePathways;
                public Double useTotalDose;
                public Double sendNumber;
                public Integer useDays;
                public String memo;
                public String organDrugCode;

                public Double getDrugCost() {
                    return drugCost;
                }

                public void setDrugCost(Double drugCost) {
                    this.drugCost = drugCost;
                }

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
