package com.aries.template.entity;

import java.util.List;

/**
 * 3.12.	支付请求接口
 * 根据
 */
public class GetConsultAndPatientAndDoctorByIdEntity {

    private boolean success;
    private String code;
    private String message;
    private DataDTO data;
    private String sign;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

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

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public static class DataDTO {
        private int statusCode;
        private String requestId;
        private Object caErrorMsg;
        private Object errorMessage;
        private JsonResponseBeanDTO jsonResponseBean;
        private boolean success;

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public Object getCaErrorMsg() {
            return caErrorMsg;
        }

        public void setCaErrorMsg(Object caErrorMsg) {
            this.caErrorMsg = caErrorMsg;
        }

        public Object getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(Object errorMessage) {
            this.errorMessage = errorMessage;
        }

        public JsonResponseBeanDTO getJsonResponseBean() {
            return jsonResponseBean;
        }

        public void setJsonResponseBean(JsonResponseBeanDTO jsonResponseBean) {
            this.jsonResponseBean = jsonResponseBean;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public static class JsonResponseBeanDTO {
            private int code;
            private Object msg;
            private BodyDTO body;
            private Object properties;

            public int getCode() {
                return code;
            }

            public void setCode(int code) {
                this.code = code;
            }

            public Object getMsg() {
                return msg;
            }

            public void setMsg(Object msg) {
                this.msg = msg;
            }

            public BodyDTO getBody() {
                return body;
            }

            public void setBody(BodyDTO body) {
                this.body = body;
            }

            public Object getProperties() {
                return properties;
            }

            public void setProperties(Object properties) {
                this.properties = properties;
            }

            public static class BodyDTO {
                private ConsultDTO consult;
                private DoctorDTO doctor;
                private PatientDTO patient;
                private List<CdrOtherdocsDTO> cdrOtherdocs;

                public ConsultDTO getConsult() {
                    return consult;
                }

                public void setConsult(ConsultDTO consult) {
                    this.consult = consult;
                }

                public DoctorDTO getDoctor() {
                    return doctor;
                }

                public void setDoctor(DoctorDTO doctor) {
                    this.doctor = doctor;
                }

                public PatientDTO getPatient() {
                    return patient;
                }

                public void setPatient(PatientDTO patient) {
                    this.patient = patient;
                }

                public List<CdrOtherdocsDTO> getCdrOtherdocs() {
                    return cdrOtherdocs;
                }

                public void setCdrOtherdocs(List<CdrOtherdocsDTO> cdrOtherdocs) {
                    this.cdrOtherdocs = cdrOtherdocs;
                }

                public static class ConsultDTO {
                    private int consultId;
                    private String mpiid;
                    private String mpiName;
                    private double weight;
                    private int requestMode;
                    private String requestMpi;
                    private String requestTime;
                    private int consultOrgan;
                    private int consultDepart;
                    private int consultDoctor;
                    private String answerTel;
                    private double consultCost;
                    private int payflag;
                    private String endDate;
                    private String cancelTime;
                    private String cancelCause;
                    private int consultStatus;
                    private String paymentDate;
                    private double consultPrice;
                    private String sessionID;
                    private String sessionStartTime;
                    private boolean feedBack;
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
                    private double actualPrice;
                    private boolean hasAdditionMessage;
                    private int questionnaireId;
                    private QuestionnaireDTO questionnaire;
                    private int status;
                    private String statusDescribe;
                    private boolean recipeReminderFlag;
                    private String lastModified;
                    private int expert;
                    private String clientType;
                    private String clientName;
                    private int userType;
                    private int medicalFlag;
                    private double fundAmount;
                    private double cashAmount;
                    private int busSource;
                    private int patientRefundStatus;
                    private int passNumber;
                    private String signInFlag;
                    private int sbType;
                    private double sbReduceAmount;
                    private int reserveDoctorId;
                    private int lastConsultId;
                    private int revisitBussType;
                    private String statusText;
                    private String revisitDetailStatusText;
                    private boolean showRefundProcess;
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

                    public int getConsultId() {
                        return consultId;
                    }

                    public void setConsultId(int consultId) {
                        this.consultId = consultId;
                    }

                    public String getMpiid() {
                        return mpiid;
                    }

                    public void setMpiid(String mpiid) {
                        this.mpiid = mpiid;
                    }

                    public String getMpiName() {
                        return mpiName;
                    }

                    public void setMpiName(String mpiName) {
                        this.mpiName = mpiName;
                    }

                    public double getWeight() {
                        return weight;
                    }

                    public void setWeight(double weight) {
                        this.weight = weight;
                    }

                    public int getRequestMode() {
                        return requestMode;
                    }

                    public void setRequestMode(int requestMode) {
                        this.requestMode = requestMode;
                    }

                    public String getRequestMpi() {
                        return requestMpi;
                    }

                    public void setRequestMpi(String requestMpi) {
                        this.requestMpi = requestMpi;
                    }

                    public String getRequestTime() {
                        return requestTime;
                    }

                    public void setRequestTime(String requestTime) {
                        this.requestTime = requestTime;
                    }

                    public int getConsultOrgan() {
                        return consultOrgan;
                    }

                    public void setConsultOrgan(int consultOrgan) {
                        this.consultOrgan = consultOrgan;
                    }

                    public int getConsultDepart() {
                        return consultDepart;
                    }

                    public void setConsultDepart(int consultDepart) {
                        this.consultDepart = consultDepart;
                    }

                    public int getConsultDoctor() {
                        return consultDoctor;
                    }

                    public void setConsultDoctor(int consultDoctor) {
                        this.consultDoctor = consultDoctor;
                    }

                    public String getAnswerTel() {
                        return answerTel;
                    }

                    public void setAnswerTel(String answerTel) {
                        this.answerTel = answerTel;
                    }

                    public double getConsultCost() {
                        return consultCost;
                    }

                    public void setConsultCost(double consultCost) {
                        this.consultCost = consultCost;
                    }

                    public int getPayflag() {
                        return payflag;
                    }

                    public void setPayflag(int payflag) {
                        this.payflag = payflag;
                    }

                    public String getEndDate() {
                        return endDate;
                    }

                    public void setEndDate(String endDate) {
                        this.endDate = endDate;
                    }

                    public String getCancelTime() {
                        return cancelTime;
                    }

                    public void setCancelTime(String cancelTime) {
                        this.cancelTime = cancelTime;
                    }

                    public String getCancelCause() {
                        return cancelCause;
                    }

                    public void setCancelCause(String cancelCause) {
                        this.cancelCause = cancelCause;
                    }

                    public int getConsultStatus() {
                        return consultStatus;
                    }

                    public void setConsultStatus(int consultStatus) {
                        this.consultStatus = consultStatus;
                    }

                    public String getPaymentDate() {
                        return paymentDate;
                    }

                    public void setPaymentDate(String paymentDate) {
                        this.paymentDate = paymentDate;
                    }

                    public double getConsultPrice() {
                        return consultPrice;
                    }

                    public void setConsultPrice(double consultPrice) {
                        this.consultPrice = consultPrice;
                    }

                    public String getSessionID() {
                        return sessionID;
                    }

                    public void setSessionID(String sessionID) {
                        this.sessionID = sessionID;
                    }

                    public String getSessionStartTime() {
                        return sessionStartTime;
                    }

                    public void setSessionStartTime(String sessionStartTime) {
                        this.sessionStartTime = sessionStartTime;
                    }

                    public boolean isFeedBack() {
                        return feedBack;
                    }

                    public void setFeedBack(boolean feedBack) {
                        this.feedBack = feedBack;
                    }

                    public boolean isTeams() {
                        return teams;
                    }

                    public void setTeams(boolean teams) {
                        this.teams = teams;
                    }

                    public int getRequestMpiUrt() {
                        return requestMpiUrt;
                    }

                    public void setRequestMpiUrt(int requestMpiUrt) {
                        this.requestMpiUrt = requestMpiUrt;
                    }

                    public int getConsultDoctorUrt() {
                        return consultDoctorUrt;
                    }

                    public void setConsultDoctorUrt(int consultDoctorUrt) {
                        this.consultDoctorUrt = consultDoctorUrt;
                    }

                    public String getAppId() {
                        return appId;
                    }

                    public void setAppId(String appId) {
                        this.appId = appId;
                    }

                    public String getOpenId() {
                        return openId;
                    }

                    public void setOpenId(String openId) {
                        this.openId = openId;
                    }

                    public boolean isHasChat() {
                        return hasChat;
                    }

                    public void setHasChat(boolean hasChat) {
                        this.hasChat = hasChat;
                    }

                    public boolean isRemindFlag() {
                        return remindFlag;
                    }

                    public void setRemindFlag(boolean remindFlag) {
                        this.remindFlag = remindFlag;
                    }

                    public boolean isSignInRemindFlag() {
                        return signInRemindFlag;
                    }

                    public void setSignInRemindFlag(boolean signInRemindFlag) {
                        this.signInRemindFlag = signInRemindFlag;
                    }

                    public int getCancelRole() {
                        return cancelRole;
                    }

                    public void setCancelRole(int cancelRole) {
                        this.cancelRole = cancelRole;
                    }

                    public int getDeviceId() {
                        return deviceId;
                    }

                    public void setDeviceId(int deviceId) {
                        this.deviceId = deviceId;
                    }

                    public int getGroupMode() {
                        return groupMode;
                    }

                    public void setGroupMode(int groupMode) {
                        this.groupMode = groupMode;
                    }

                    public double getActualPrice() {
                        return actualPrice;
                    }

                    public void setActualPrice(double actualPrice) {
                        this.actualPrice = actualPrice;
                    }

                    public boolean isHasAdditionMessage() {
                        return hasAdditionMessage;
                    }

                    public void setHasAdditionMessage(boolean hasAdditionMessage) {
                        this.hasAdditionMessage = hasAdditionMessage;
                    }

                    public int getQuestionnaireId() {
                        return questionnaireId;
                    }

                    public void setQuestionnaireId(int questionnaireId) {
                        this.questionnaireId = questionnaireId;
                    }

                    public QuestionnaireDTO getQuestionnaire() {
                        return questionnaire;
                    }

                    public void setQuestionnaire(QuestionnaireDTO questionnaire) {
                        this.questionnaire = questionnaire;
                    }

                    public int getStatus() {
                        return status;
                    }

                    public void setStatus(int status) {
                        this.status = status;
                    }

                    public String getStatusDescribe() {
                        return statusDescribe;
                    }

                    public void setStatusDescribe(String statusDescribe) {
                        this.statusDescribe = statusDescribe;
                    }

                    public boolean isRecipeReminderFlag() {
                        return recipeReminderFlag;
                    }

                    public void setRecipeReminderFlag(boolean recipeReminderFlag) {
                        this.recipeReminderFlag = recipeReminderFlag;
                    }

                    public String getLastModified() {
                        return lastModified;
                    }

                    public void setLastModified(String lastModified) {
                        this.lastModified = lastModified;
                    }

                    public int getExpert() {
                        return expert;
                    }

                    public void setExpert(int expert) {
                        this.expert = expert;
                    }

                    public String getClientType() {
                        return clientType;
                    }

                    public void setClientType(String clientType) {
                        this.clientType = clientType;
                    }

                    public String getClientName() {
                        return clientName;
                    }

                    public void setClientName(String clientName) {
                        this.clientName = clientName;
                    }

                    public int getUserType() {
                        return userType;
                    }

                    public void setUserType(int userType) {
                        this.userType = userType;
                    }

                    public int getMedicalFlag() {
                        return medicalFlag;
                    }

                    public void setMedicalFlag(int medicalFlag) {
                        this.medicalFlag = medicalFlag;
                    }

                    public double getFundAmount() {
                        return fundAmount;
                    }

                    public void setFundAmount(double fundAmount) {
                        this.fundAmount = fundAmount;
                    }

                    public double getCashAmount() {
                        return cashAmount;
                    }

                    public void setCashAmount(double cashAmount) {
                        this.cashAmount = cashAmount;
                    }

                    public int getBusSource() {
                        return busSource;
                    }

                    public void setBusSource(int busSource) {
                        this.busSource = busSource;
                    }

                    public int getPatientRefundStatus() {
                        return patientRefundStatus;
                    }

                    public void setPatientRefundStatus(int patientRefundStatus) {
                        this.patientRefundStatus = patientRefundStatus;
                    }

                    public int getPassNumber() {
                        return passNumber;
                    }

                    public void setPassNumber(int passNumber) {
                        this.passNumber = passNumber;
                    }

                    public String getSignInFlag() {
                        return signInFlag;
                    }

                    public void setSignInFlag(String signInFlag) {
                        this.signInFlag = signInFlag;
                    }

                    public int getSbType() {
                        return sbType;
                    }

                    public void setSbType(int sbType) {
                        this.sbType = sbType;
                    }

                    public double getSbReduceAmount() {
                        return sbReduceAmount;
                    }

                    public void setSbReduceAmount(double sbReduceAmount) {
                        this.sbReduceAmount = sbReduceAmount;
                    }

                    public int getReserveDoctorId() {
                        return reserveDoctorId;
                    }

                    public void setReserveDoctorId(int reserveDoctorId) {
                        this.reserveDoctorId = reserveDoctorId;
                    }

                    public int getLastConsultId() {
                        return lastConsultId;
                    }

                    public void setLastConsultId(int lastConsultId) {
                        this.lastConsultId = lastConsultId;
                    }

                    public int getRevisitBussType() {
                        return revisitBussType;
                    }

                    public void setRevisitBussType(int revisitBussType) {
                        this.revisitBussType = revisitBussType;
                    }

                    public String getStatusText() {
                        return statusText;
                    }

                    public void setStatusText(String statusText) {
                        this.statusText = statusText;
                    }

                    public String getRevisitDetailStatusText() {
                        return revisitDetailStatusText;
                    }

                    public void setRevisitDetailStatusText(String revisitDetailStatusText) {
                        this.revisitDetailStatusText = revisitDetailStatusText;
                    }

                    public boolean isShowRefundProcess() {
                        return showRefundProcess;
                    }

                    public void setShowRefundProcess(boolean showRefundProcess) {
                        this.showRefundProcess = showRefundProcess;
                    }

                    public String getRequestModeText() {
                        return requestModeText;
                    }

                    public void setRequestModeText(String requestModeText) {
                        this.requestModeText = requestModeText;
                    }

                    public String getConsultOrganText() {
                        return consultOrganText;
                    }

                    public void setConsultOrganText(String consultOrganText) {
                        this.consultOrganText = consultOrganText;
                    }

                    public String getConsultDepartText() {
                        return consultDepartText;
                    }

                    public void setConsultDepartText(String consultDepartText) {
                        this.consultDepartText = consultDepartText;
                    }

                    public String getConsultDoctorText() {
                        return consultDoctorText;
                    }

                    public void setConsultDoctorText(String consultDoctorText) {
                        this.consultDoctorText = consultDoctorText;
                    }

                    public String getPayflagText() {
                        return payflagText;
                    }

                    public void setPayflagText(String payflagText) {
                        this.payflagText = payflagText;
                    }

                    public String getExeDoctorText() {
                        return exeDoctorText;
                    }

                    public void setExeDoctorText(String exeDoctorText) {
                        this.exeDoctorText = exeDoctorText;
                    }

                    public String getExeDepartText() {
                        return exeDepartText;
                    }

                    public void setExeDepartText(String exeDepartText) {
                        this.exeDepartText = exeDepartText;
                    }

                    public String getExeOrganText() {
                        return exeOrganText;
                    }

                    public void setExeOrganText(String exeOrganText) {
                        this.exeOrganText = exeOrganText;
                    }

                    public String getRefuseFlagText() {
                        return refuseFlagText;
                    }

                    public void setRefuseFlagText(String refuseFlagText) {
                        this.refuseFlagText = refuseFlagText;
                    }

                    public String getCancelRoleText() {
                        return cancelRoleText;
                    }

                    public void setCancelRoleText(String cancelRoleText) {
                        this.cancelRoleText = cancelRoleText;
                    }

                    public String getGroupModeText() {
                        return groupModeText;
                    }

                    public void setGroupModeText(String groupModeText) {
                        this.groupModeText = groupModeText;
                    }

                    public String getUserTypeText() {
                        return userTypeText;
                    }

                    public void setUserTypeText(String userTypeText) {
                        this.userTypeText = userTypeText;
                    }

                    public String getWorkTypeText() {
                        return workTypeText;
                    }

                    public void setWorkTypeText(String workTypeText) {
                        this.workTypeText = workTypeText;
                    }

                    public static class QuestionnaireDTO {
                        private int questionnaireId;
                        private int pregnent;
                        private int alleric;
                        private int diseaseStatus;
                        private String proposedDrugs;
                        private int haveTake;
                        private int haveReaction;
                        private String questionDesc;
                        private int returnVisitStatus;

                        public int getQuestionnaireId() {
                            return questionnaireId;
                        }

                        public void setQuestionnaireId(int questionnaireId) {
                            this.questionnaireId = questionnaireId;
                        }

                        public int getPregnent() {
                            return pregnent;
                        }

                        public void setPregnent(int pregnent) {
                            this.pregnent = pregnent;
                        }

                        public int getAlleric() {
                            return alleric;
                        }

                        public void setAlleric(int alleric) {
                            this.alleric = alleric;
                        }

                        public int getDiseaseStatus() {
                            return diseaseStatus;
                        }

                        public void setDiseaseStatus(int diseaseStatus) {
                            this.diseaseStatus = diseaseStatus;
                        }

                        public String getProposedDrugs() {
                            return proposedDrugs;
                        }

                        public void setProposedDrugs(String proposedDrugs) {
                            this.proposedDrugs = proposedDrugs;
                        }

                        public int getHaveTake() {
                            return haveTake;
                        }

                        public void setHaveTake(int haveTake) {
                            this.haveTake = haveTake;
                        }

                        public int getHaveReaction() {
                            return haveReaction;
                        }

                        public void setHaveReaction(int haveReaction) {
                            this.haveReaction = haveReaction;
                        }

                        public String getQuestionDesc() {
                            return questionDesc;
                        }

                        public void setQuestionDesc(String questionDesc) {
                            this.questionDesc = questionDesc;
                        }

                        public int getReturnVisitStatus() {
                            return returnVisitStatus;
                        }

                        public void setReturnVisitStatus(int returnVisitStatus) {
                            this.returnVisitStatus = returnVisitStatus;
                        }
                    }
                }

                public static class DoctorDTO {
                    private String loginId;
                    private int doctorId;
                    private String name;
                    private String gender;
                    private String profession;
                    private int organProfession;
                    private String proTitle;
                    private boolean teams;
                    private int organ;
                    private int status;
                    private boolean expert;
                    private int userType;
                    private int haveAppoint;
                    private int testPersonnel;
                    private int chief;
                    private boolean virtualDoctor;
                    private int source;
                    private int groupType;
                    private int generalDoctor;
                    private ExtendParamDTO extendParam;
                    private int easemobStatus;
                    private int doctorSortNum;
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

                    public String getLoginId() {
                        return loginId;
                    }

                    public void setLoginId(String loginId) {
                        this.loginId = loginId;
                    }

                    public int getDoctorId() {
                        return doctorId;
                    }

                    public void setDoctorId(int doctorId) {
                        this.doctorId = doctorId;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getGender() {
                        return gender;
                    }

                    public void setGender(String gender) {
                        this.gender = gender;
                    }

                    public String getProfession() {
                        return profession;
                    }

                    public void setProfession(String profession) {
                        this.profession = profession;
                    }

                    public int getOrganProfession() {
                        return organProfession;
                    }

                    public void setOrganProfession(int organProfession) {
                        this.organProfession = organProfession;
                    }

                    public String getProTitle() {
                        return proTitle;
                    }

                    public void setProTitle(String proTitle) {
                        this.proTitle = proTitle;
                    }

                    public boolean isTeams() {
                        return teams;
                    }

                    public void setTeams(boolean teams) {
                        this.teams = teams;
                    }

                    public int getOrgan() {
                        return organ;
                    }

                    public void setOrgan(int organ) {
                        this.organ = organ;
                    }

                    public int getStatus() {
                        return status;
                    }

                    public void setStatus(int status) {
                        this.status = status;
                    }

                    public boolean isExpert() {
                        return expert;
                    }

                    public void setExpert(boolean expert) {
                        this.expert = expert;
                    }

                    public int getUserType() {
                        return userType;
                    }

                    public void setUserType(int userType) {
                        this.userType = userType;
                    }

                    public int getHaveAppoint() {
                        return haveAppoint;
                    }

                    public void setHaveAppoint(int haveAppoint) {
                        this.haveAppoint = haveAppoint;
                    }

                    public int getTestPersonnel() {
                        return testPersonnel;
                    }

                    public void setTestPersonnel(int testPersonnel) {
                        this.testPersonnel = testPersonnel;
                    }

                    public int getChief() {
                        return chief;
                    }

                    public void setChief(int chief) {
                        this.chief = chief;
                    }

                    public boolean isVirtualDoctor() {
                        return virtualDoctor;
                    }

                    public void setVirtualDoctor(boolean virtualDoctor) {
                        this.virtualDoctor = virtualDoctor;
                    }

                    public int getSource() {
                        return source;
                    }

                    public void setSource(int source) {
                        this.source = source;
                    }

                    public int getGroupType() {
                        return groupType;
                    }

                    public void setGroupType(int groupType) {
                        this.groupType = groupType;
                    }

                    public int getGeneralDoctor() {
                        return generalDoctor;
                    }

                    public void setGeneralDoctor(int generalDoctor) {
                        this.generalDoctor = generalDoctor;
                    }

                    public ExtendParamDTO getExtendParam() {
                        return extendParam;
                    }

                    public void setExtendParam(ExtendParamDTO extendParam) {
                        this.extendParam = extendParam;
                    }

                    public int getEasemobStatus() {
                        return easemobStatus;
                    }

                    public void setEasemobStatus(int easemobStatus) {
                        this.easemobStatus = easemobStatus;
                    }

                    public int getDoctorSortNum() {
                        return doctorSortNum;
                    }

                    public void setDoctorSortNum(int doctorSortNum) {
                        this.doctorSortNum = doctorSortNum;
                    }

                    public int getCurrentOrgan() {
                        return currentOrgan;
                    }

                    public void setCurrentOrgan(int currentOrgan) {
                        this.currentOrgan = currentOrgan;
                    }

                    public int getCloudAppointSourceFlag() {
                        return cloudAppointSourceFlag;
                    }

                    public void setCloudAppointSourceFlag(int cloudAppointSourceFlag) {
                        this.cloudAppointSourceFlag = cloudAppointSourceFlag;
                    }

                    public boolean isCanReport() {
                        return canReport;
                    }

                    public void setCanReport(boolean canReport) {
                        this.canReport = canReport;
                    }

                    public boolean isHasReport() {
                        return hasReport;
                    }

                    public void setHasReport(boolean hasReport) {
                        this.hasReport = hasReport;
                    }

                    public String getGenderText() {
                        return genderText;
                    }

                    public void setGenderText(String genderText) {
                        this.genderText = genderText;
                    }

                    public String getProfessionText() {
                        return professionText;
                    }

                    public void setProfessionText(String professionText) {
                        this.professionText = professionText;
                    }

                    public String getOrganProfessionText() {
                        return organProfessionText;
                    }

                    public void setOrganProfessionText(String organProfessionText) {
                        this.organProfessionText = organProfessionText;
                    }

                    public String getProTitleText() {
                        return proTitleText;
                    }

                    public void setProTitleText(String proTitleText) {
                        this.proTitleText = proTitleText;
                    }

                    public String getOrganText() {
                        return organText;
                    }

                    public void setOrganText(String organText) {
                        this.organText = organText;
                    }

                    public String getDepartmentText() {
                        return departmentText;
                    }

                    public void setDepartmentText(String departmentText) {
                        this.departmentText = departmentText;
                    }

                    public String getSourceText() {
                        return sourceText;
                    }

                    public void setSourceText(String sourceText) {
                        this.sourceText = sourceText;
                    }

                    public String getLeaderText() {
                        return leaderText;
                    }

                    public void setLeaderText(String leaderText) {
                        this.leaderText = leaderText;
                    }

                    public String getGroupTypeText() {
                        return groupTypeText;
                    }

                    public void setGroupTypeText(String groupTypeText) {
                        this.groupTypeText = groupTypeText;
                    }

                    public String getAppointSourceFlagText() {
                        return appointSourceFlagText;
                    }

                    public void setAppointSourceFlagText(String appointSourceFlagText) {
                        this.appointSourceFlagText = appointSourceFlagText;
                    }

                    public String getCurrentOrganText() {
                        return currentOrganText;
                    }

                    public void setCurrentOrganText(String currentOrganText) {
                        this.currentOrganText = currentOrganText;
                    }

                    public String getCloudAppointSourceFlagText() {
                        return cloudAppointSourceFlagText;
                    }

                    public void setCloudAppointSourceFlagText(String cloudAppointSourceFlagText) {
                        this.cloudAppointSourceFlagText = cloudAppointSourceFlagText;
                    }

                    public String getGradeText() {
                        return gradeText;
                    }

                    public void setGradeText(String gradeText) {
                        this.gradeText = gradeText;
                    }

                    public String getStatusText() {
                        return statusText;
                    }

                    public void setStatusText(String statusText) {
                        this.statusText = statusText;
                    }

                    public String getGroupModeText() {
                        return groupModeText;
                    }

                    public void setGroupModeText(String groupModeText) {
                        this.groupModeText = groupModeText;
                    }

                    public String getUserTypeText() {
                        return userTypeText;
                    }

                    public void setUserTypeText(String userTypeText) {
                        this.userTypeText = userTypeText;
                    }

                    public static class ExtendParamDTO {
                    }
                }

                public static class PatientDTO {
                    private String mpiId;
                    private String patientName;
                    private String patientSex;
                    private int age;
                    private String certificate;
                    private int certificateType;
                    private String mobile;
                    private String certificateTypeText;
                    private String patientSexText;

                    public String getMpiId() {
                        return mpiId;
                    }

                    public void setMpiId(String mpiId) {
                        this.mpiId = mpiId;
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

                    public int getAge() {
                        return age;
                    }

                    public void setAge(int age) {
                        this.age = age;
                    }

                    public String getCertificate() {
                        return certificate;
                    }

                    public void setCertificate(String certificate) {
                        this.certificate = certificate;
                    }

                    public int getCertificateType() {
                        return certificateType;
                    }

                    public void setCertificateType(int certificateType) {
                        this.certificateType = certificateType;
                    }

                    public String getMobile() {
                        return mobile;
                    }

                    public void setMobile(String mobile) {
                        this.mobile = mobile;
                    }

                    public String getCertificateTypeText() {
                        return certificateTypeText;
                    }

                    public void setCertificateTypeText(String certificateTypeText) {
                        this.certificateTypeText = certificateTypeText;
                    }

                    public String getPatientSexText() {
                        return patientSexText;
                    }

                    public void setPatientSexText(String patientSexText) {
                        this.patientSexText = patientSexText;
                    }
                }

                public static class CdrOtherdocsDTO {
                    private int otherDocId;
                    private int clinicType;
                    private int clinicId;
                    private String mpiid;
                    private String createDate;
                    private String docType;
                    private String docName;
                    private String docFormat;
                    private String docContent;
                    private String medicalDate;
                    private String clinicTypeText;
                    private String docTypeText;
                    private String docFormatText;
                    private String docContentToken;

                    public int getOtherDocId() {
                        return otherDocId;
                    }

                    public void setOtherDocId(int otherDocId) {
                        this.otherDocId = otherDocId;
                    }

                    public int getClinicType() {
                        return clinicType;
                    }

                    public void setClinicType(int clinicType) {
                        this.clinicType = clinicType;
                    }

                    public int getClinicId() {
                        return clinicId;
                    }

                    public void setClinicId(int clinicId) {
                        this.clinicId = clinicId;
                    }

                    public String getMpiid() {
                        return mpiid;
                    }

                    public void setMpiid(String mpiid) {
                        this.mpiid = mpiid;
                    }

                    public String getCreateDate() {
                        return createDate;
                    }

                    public void setCreateDate(String createDate) {
                        this.createDate = createDate;
                    }

                    public String getDocType() {
                        return docType;
                    }

                    public void setDocType(String docType) {
                        this.docType = docType;
                    }

                    public String getDocName() {
                        return docName;
                    }

                    public void setDocName(String docName) {
                        this.docName = docName;
                    }

                    public String getDocFormat() {
                        return docFormat;
                    }

                    public void setDocFormat(String docFormat) {
                        this.docFormat = docFormat;
                    }

                    public String getDocContent() {
                        return docContent;
                    }

                    public void setDocContent(String docContent) {
                        this.docContent = docContent;
                    }

                    public String getMedicalDate() {
                        return medicalDate;
                    }

                    public void setMedicalDate(String medicalDate) {
                        this.medicalDate = medicalDate;
                    }

                    public String getClinicTypeText() {
                        return clinicTypeText;
                    }

                    public void setClinicTypeText(String clinicTypeText) {
                        this.clinicTypeText = clinicTypeText;
                    }

                    public String getDocTypeText() {
                        return docTypeText;
                    }

                    public void setDocTypeText(String docTypeText) {
                        this.docTypeText = docTypeText;
                    }

                    public String getDocFormatText() {
                        return docFormatText;
                    }

                    public void setDocFormatText(String docFormatText) {
                        this.docFormatText = docFormatText;
                    }

                    public String getDocContentToken() {
                        return docContentToken;
                    }

                    public void setDocContentToken(String docContentToken) {
                        this.docContentToken = docContentToken;
                    }
                }
            }
        }
    }
}
