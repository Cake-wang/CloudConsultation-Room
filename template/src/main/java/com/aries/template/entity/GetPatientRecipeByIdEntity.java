package com.aries.template.entity;

import java.util.List;

/**
 * 3.12.	支付请求接口
 * 根据
 */
public class GetPatientRecipeByIdEntity {

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
                private boolean isDownload;
                private String tipsType;
                private boolean childRecipeFlag;
                private RecipeDTO recipe;
                private int showRefund;
                private String recipeGetModeTip;
                private boolean isHiddenRecipeDetail;
                private String tips;
                private int showSendToEnterprises;
                private PatientDTO patient;
                private int recipeChooseChronicDisease;
                private boolean showChecker;
                private boolean showReferencePrice;
                private boolean checkEnterprise;
                private boolean mergeRecipeFlag;
                private String unSendTitle;
                private boolean optional;
                private List<RecipedetailsDTO> recipedetails;
                private int showSendToHos;
                private GiveModeShowButtonVODTO giveModeShowButtonVO;
                private String qrName;
                private boolean showButton;
                private String doctorSignImg;
                private String doctorSignImgToken;
                private RecipeExtendDTO recipeExtend;

                public boolean isIsDownload() {
                    return isDownload;
                }

                public void setIsDownload(boolean isDownload) {
                    this.isDownload = isDownload;
                }

                public String getTipsType() {
                    return tipsType;
                }

                public void setTipsType(String tipsType) {
                    this.tipsType = tipsType;
                }

                public boolean isChildRecipeFlag() {
                    return childRecipeFlag;
                }

                public void setChildRecipeFlag(boolean childRecipeFlag) {
                    this.childRecipeFlag = childRecipeFlag;
                }

                public RecipeDTO getRecipe() {
                    return recipe;
                }

                public void setRecipe(RecipeDTO recipe) {
                    this.recipe = recipe;
                }

                public int getShowRefund() {
                    return showRefund;
                }

                public void setShowRefund(int showRefund) {
                    this.showRefund = showRefund;
                }

                public String getRecipeGetModeTip() {
                    return recipeGetModeTip;
                }

                public void setRecipeGetModeTip(String recipeGetModeTip) {
                    this.recipeGetModeTip = recipeGetModeTip;
                }

                public boolean isIsHiddenRecipeDetail() {
                    return isHiddenRecipeDetail;
                }

                public void setIsHiddenRecipeDetail(boolean isHiddenRecipeDetail) {
                    this.isHiddenRecipeDetail = isHiddenRecipeDetail;
                }

                public String getTips() {
                    return tips;
                }

                public void setTips(String tips) {
                    this.tips = tips;
                }

                public int getShowSendToEnterprises() {
                    return showSendToEnterprises;
                }

                public void setShowSendToEnterprises(int showSendToEnterprises) {
                    this.showSendToEnterprises = showSendToEnterprises;
                }

                public PatientDTO getPatient() {
                    return patient;
                }

                public void setPatient(PatientDTO patient) {
                    this.patient = patient;
                }

                public int getRecipeChooseChronicDisease() {
                    return recipeChooseChronicDisease;
                }

                public void setRecipeChooseChronicDisease(int recipeChooseChronicDisease) {
                    this.recipeChooseChronicDisease = recipeChooseChronicDisease;
                }

                public boolean isShowChecker() {
                    return showChecker;
                }

                public void setShowChecker(boolean showChecker) {
                    this.showChecker = showChecker;
                }

                public boolean isShowReferencePrice() {
                    return showReferencePrice;
                }

                public void setShowReferencePrice(boolean showReferencePrice) {
                    this.showReferencePrice = showReferencePrice;
                }

                public boolean isCheckEnterprise() {
                    return checkEnterprise;
                }

                public void setCheckEnterprise(boolean checkEnterprise) {
                    this.checkEnterprise = checkEnterprise;
                }

                public boolean isMergeRecipeFlag() {
                    return mergeRecipeFlag;
                }

                public void setMergeRecipeFlag(boolean mergeRecipeFlag) {
                    this.mergeRecipeFlag = mergeRecipeFlag;
                }

                public String getUnSendTitle() {
                    return unSendTitle;
                }

                public void setUnSendTitle(String unSendTitle) {
                    this.unSendTitle = unSendTitle;
                }

                public boolean isOptional() {
                    return optional;
                }

                public void setOptional(boolean optional) {
                    this.optional = optional;
                }

                public List<RecipedetailsDTO> getRecipedetails() {
                    return recipedetails;
                }

                public void setRecipedetails(List<RecipedetailsDTO> recipedetails) {
                    this.recipedetails = recipedetails;
                }

                public int getShowSendToHos() {
                    return showSendToHos;
                }

                public void setShowSendToHos(int showSendToHos) {
                    this.showSendToHos = showSendToHos;
                }

                public GiveModeShowButtonVODTO getGiveModeShowButtonVO() {
                    return giveModeShowButtonVO;
                }

                public void setGiveModeShowButtonVO(GiveModeShowButtonVODTO giveModeShowButtonVO) {
                    this.giveModeShowButtonVO = giveModeShowButtonVO;
                }

                public String getQrName() {
                    return qrName;
                }

                public void setQrName(String qrName) {
                    this.qrName = qrName;
                }

                public boolean isShowButton() {
                    return showButton;
                }

                public void setShowButton(boolean showButton) {
                    this.showButton = showButton;
                }

                public String getDoctorSignImg() {
                    return doctorSignImg;
                }

                public void setDoctorSignImg(String doctorSignImg) {
                    this.doctorSignImg = doctorSignImg;
                }

                public String getDoctorSignImgToken() {
                    return doctorSignImgToken;
                }

                public void setDoctorSignImgToken(String doctorSignImgToken) {
                    this.doctorSignImgToken = doctorSignImgToken;
                }

                public RecipeExtendDTO getRecipeExtend() {
                    return recipeExtend;
                }

                public void setRecipeExtend(RecipeExtendDTO recipeExtend) {
                    this.recipeExtend = recipeExtend;
                }

                public static class RecipeDTO {
                    private int recipeId;
                    private int bussSource;
                    private int clinicId;
                    private String mpiid;
                    private String patientID;
                    private int patientStatus;
                    private int clinicOrgan;
                    private String organName;
                    private String recipeCode;
                    private int recipeType;
                    private String recipeMode;
                    private int depart;
                    private String appointDepart;
                    private String appointDepartName;
                    private int doctor;
                    private String createDate;
                    private int copyNum;
                    private double totalMoney;
                    private String organDiseaseName;
                    private String organDiseaseId;
                    private int payFlag;
                    private int giveFlag;
                    private int valueDays;
                    private int giveMode;
                    private String giveModeText;
                    private int status;
                    private int fromflag;
                    private String lastModify;
                    private String signDate;
                    private int chooseFlag;
                    private int remindFlag;
                    private int pushFlag;
                    private double actualPrice;
                    private double orderAmount;
                    private String discountAmount;
                    private int medicalPayFlag;
                    private int distributionFlag;
                    private String doctorName;
                    private String patientName;
                    private int takeMedicine;
                    private String requestMpiId;
                    private int requestUrt;
                    private int currentClient;
                    private int notation;
                    private int reviewType;
                    private int checkMode;
                    private int checkStatus;
                    private int recipeSourceType;
                    private int recipePayType;
                    private int grabOrderStatus;
                    private String recipeSupportGiveMode;
                    private int processState;
                    private int subState;
                    private String subStateText;
                    private int auditState;
                    private int medicalFlag;
                    private String offlineRecipeName;
                    private int offlineRecipeTotalPrice;
                    private int doctorSignState;
                    private int checkerSignState;
                    private String statusText;
                    private String address1Text;
                    private String address2Text;
                    private String address3Text;
                    private String payModeText;
                    private String organProfessionText;
                    private String recipeTypeText;
                    private String processStateText;
                    private String clinicOrganText;
                    private String originClinicOrganText;
                    private String departText;
                    private String doctorText;
                    private String checkOrganText;
                    private String fromflagText;

                    public int getRecipeId() {
                        return recipeId;
                    }

                    public void setRecipeId(int recipeId) {
                        this.recipeId = recipeId;
                    }

                    public int getBussSource() {
                        return bussSource;
                    }

                    public void setBussSource(int bussSource) {
                        this.bussSource = bussSource;
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

                    public String getPatientID() {
                        return patientID;
                    }

                    public void setPatientID(String patientID) {
                        this.patientID = patientID;
                    }

                    public int getPatientStatus() {
                        return patientStatus;
                    }

                    public void setPatientStatus(int patientStatus) {
                        this.patientStatus = patientStatus;
                    }

                    public int getClinicOrgan() {
                        return clinicOrgan;
                    }

                    public void setClinicOrgan(int clinicOrgan) {
                        this.clinicOrgan = clinicOrgan;
                    }

                    public String getOrganName() {
                        return organName;
                    }

                    public void setOrganName(String organName) {
                        this.organName = organName;
                    }

                    public String getRecipeCode() {
                        return recipeCode;
                    }

                    public void setRecipeCode(String recipeCode) {
                        this.recipeCode = recipeCode;
                    }

                    public int getRecipeType() {
                        return recipeType;
                    }

                    public void setRecipeType(int recipeType) {
                        this.recipeType = recipeType;
                    }

                    public String getRecipeMode() {
                        return recipeMode;
                    }

                    public void setRecipeMode(String recipeMode) {
                        this.recipeMode = recipeMode;
                    }

                    public int getDepart() {
                        return depart;
                    }

                    public void setDepart(int depart) {
                        this.depart = depart;
                    }

                    public String getAppointDepart() {
                        return appointDepart;
                    }

                    public void setAppointDepart(String appointDepart) {
                        this.appointDepart = appointDepart;
                    }

                    public String getAppointDepartName() {
                        return appointDepartName;
                    }

                    public void setAppointDepartName(String appointDepartName) {
                        this.appointDepartName = appointDepartName;
                    }

                    public int getDoctor() {
                        return doctor;
                    }

                    public void setDoctor(int doctor) {
                        this.doctor = doctor;
                    }

                    public String getCreateDate() {
                        return createDate;
                    }

                    public void setCreateDate(String createDate) {
                        this.createDate = createDate;
                    }

                    public int getCopyNum() {
                        return copyNum;
                    }

                    public void setCopyNum(int copyNum) {
                        this.copyNum = copyNum;
                    }

                    public double getTotalMoney() {
                        return totalMoney;
                    }

                    public void setTotalMoney(double totalMoney) {
                        this.totalMoney = totalMoney;
                    }

                    public String getOrganDiseaseName() {
                        return organDiseaseName;
                    }

                    public void setOrganDiseaseName(String organDiseaseName) {
                        this.organDiseaseName = organDiseaseName;
                    }

                    public String getOrganDiseaseId() {
                        return organDiseaseId;
                    }

                    public void setOrganDiseaseId(String organDiseaseId) {
                        this.organDiseaseId = organDiseaseId;
                    }

                    public int getPayFlag() {
                        return payFlag;
                    }

                    public void setPayFlag(int payFlag) {
                        this.payFlag = payFlag;
                    }

                    public int getGiveFlag() {
                        return giveFlag;
                    }

                    public void setGiveFlag(int giveFlag) {
                        this.giveFlag = giveFlag;
                    }

                    public int getValueDays() {
                        return valueDays;
                    }

                    public void setValueDays(int valueDays) {
                        this.valueDays = valueDays;
                    }

                    public int getGiveMode() {
                        return giveMode;
                    }

                    public void setGiveMode(int giveMode) {
                        this.giveMode = giveMode;
                    }

                    public String getGiveModeText() {
                        return giveModeText;
                    }

                    public void setGiveModeText(String giveModeText) {
                        this.giveModeText = giveModeText;
                    }

                    public int getStatus() {
                        return status;
                    }

                    public void setStatus(int status) {
                        this.status = status;
                    }

                    public int getFromflag() {
                        return fromflag;
                    }

                    public void setFromflag(int fromflag) {
                        this.fromflag = fromflag;
                    }

                    public String getLastModify() {
                        return lastModify;
                    }

                    public void setLastModify(String lastModify) {
                        this.lastModify = lastModify;
                    }

                    public String getSignDate() {
                        return signDate;
                    }

                    public void setSignDate(String signDate) {
                        this.signDate = signDate;
                    }

                    public int getChooseFlag() {
                        return chooseFlag;
                    }

                    public void setChooseFlag(int chooseFlag) {
                        this.chooseFlag = chooseFlag;
                    }

                    public int getRemindFlag() {
                        return remindFlag;
                    }

                    public void setRemindFlag(int remindFlag) {
                        this.remindFlag = remindFlag;
                    }

                    public int getPushFlag() {
                        return pushFlag;
                    }

                    public void setPushFlag(int pushFlag) {
                        this.pushFlag = pushFlag;
                    }

                    public double getActualPrice() {
                        return actualPrice;
                    }

                    public void setActualPrice(double actualPrice) {
                        this.actualPrice = actualPrice;
                    }

                    public double getOrderAmount() {
                        return orderAmount;
                    }

                    public void setOrderAmount(double orderAmount) {
                        this.orderAmount = orderAmount;
                    }

                    public String getDiscountAmount() {
                        return discountAmount;
                    }

                    public void setDiscountAmount(String discountAmount) {
                        this.discountAmount = discountAmount;
                    }

                    public int getMedicalPayFlag() {
                        return medicalPayFlag;
                    }

                    public void setMedicalPayFlag(int medicalPayFlag) {
                        this.medicalPayFlag = medicalPayFlag;
                    }

                    public int getDistributionFlag() {
                        return distributionFlag;
                    }

                    public void setDistributionFlag(int distributionFlag) {
                        this.distributionFlag = distributionFlag;
                    }

                    public String getDoctorName() {
                        return doctorName;
                    }

                    public void setDoctorName(String doctorName) {
                        this.doctorName = doctorName;
                    }

                    public String getPatientName() {
                        return patientName;
                    }

                    public void setPatientName(String patientName) {
                        this.patientName = patientName;
                    }

                    public int getTakeMedicine() {
                        return takeMedicine;
                    }

                    public void setTakeMedicine(int takeMedicine) {
                        this.takeMedicine = takeMedicine;
                    }

                    public String getRequestMpiId() {
                        return requestMpiId;
                    }

                    public void setRequestMpiId(String requestMpiId) {
                        this.requestMpiId = requestMpiId;
                    }

                    public int getRequestUrt() {
                        return requestUrt;
                    }

                    public void setRequestUrt(int requestUrt) {
                        this.requestUrt = requestUrt;
                    }

                    public int getCurrentClient() {
                        return currentClient;
                    }

                    public void setCurrentClient(int currentClient) {
                        this.currentClient = currentClient;
                    }

                    public int getNotation() {
                        return notation;
                    }

                    public void setNotation(int notation) {
                        this.notation = notation;
                    }

                    public int getReviewType() {
                        return reviewType;
                    }

                    public void setReviewType(int reviewType) {
                        this.reviewType = reviewType;
                    }

                    public int getCheckMode() {
                        return checkMode;
                    }

                    public void setCheckMode(int checkMode) {
                        this.checkMode = checkMode;
                    }

                    public int getCheckStatus() {
                        return checkStatus;
                    }

                    public void setCheckStatus(int checkStatus) {
                        this.checkStatus = checkStatus;
                    }

                    public int getRecipeSourceType() {
                        return recipeSourceType;
                    }

                    public void setRecipeSourceType(int recipeSourceType) {
                        this.recipeSourceType = recipeSourceType;
                    }

                    public int getRecipePayType() {
                        return recipePayType;
                    }

                    public void setRecipePayType(int recipePayType) {
                        this.recipePayType = recipePayType;
                    }

                    public int getGrabOrderStatus() {
                        return grabOrderStatus;
                    }

                    public void setGrabOrderStatus(int grabOrderStatus) {
                        this.grabOrderStatus = grabOrderStatus;
                    }

                    public String getRecipeSupportGiveMode() {
                        return recipeSupportGiveMode;
                    }

                    public void setRecipeSupportGiveMode(String recipeSupportGiveMode) {
                        this.recipeSupportGiveMode = recipeSupportGiveMode;
                    }

                    public int getProcessState() {
                        return processState;
                    }

                    public void setProcessState(int processState) {
                        this.processState = processState;
                    }

                    public int getSubState() {
                        return subState;
                    }

                    public void setSubState(int subState) {
                        this.subState = subState;
                    }

                    public String getSubStateText() {
                        return subStateText;
                    }

                    public void setSubStateText(String subStateText) {
                        this.subStateText = subStateText;
                    }

                    public int getAuditState() {
                        return auditState;
                    }

                    public void setAuditState(int auditState) {
                        this.auditState = auditState;
                    }

                    public int getMedicalFlag() {
                        return medicalFlag;
                    }

                    public void setMedicalFlag(int medicalFlag) {
                        this.medicalFlag = medicalFlag;
                    }

                    public String getOfflineRecipeName() {
                        return offlineRecipeName;
                    }

                    public void setOfflineRecipeName(String offlineRecipeName) {
                        this.offlineRecipeName = offlineRecipeName;
                    }

                    public int getOfflineRecipeTotalPrice() {
                        return offlineRecipeTotalPrice;
                    }

                    public void setOfflineRecipeTotalPrice(int offlineRecipeTotalPrice) {
                        this.offlineRecipeTotalPrice = offlineRecipeTotalPrice;
                    }

                    public int getDoctorSignState() {
                        return doctorSignState;
                    }

                    public void setDoctorSignState(int doctorSignState) {
                        this.doctorSignState = doctorSignState;
                    }

                    public int getCheckerSignState() {
                        return checkerSignState;
                    }

                    public void setCheckerSignState(int checkerSignState) {
                        this.checkerSignState = checkerSignState;
                    }

                    public String getStatusText() {
                        return statusText;
                    }

                    public void setStatusText(String statusText) {
                        this.statusText = statusText;
                    }

                    public String getAddress1Text() {
                        return address1Text;
                    }

                    public void setAddress1Text(String address1Text) {
                        this.address1Text = address1Text;
                    }

                    public String getAddress2Text() {
                        return address2Text;
                    }

                    public void setAddress2Text(String address2Text) {
                        this.address2Text = address2Text;
                    }

                    public String getAddress3Text() {
                        return address3Text;
                    }

                    public void setAddress3Text(String address3Text) {
                        this.address3Text = address3Text;
                    }

                    public String getPayModeText() {
                        return payModeText;
                    }

                    public void setPayModeText(String payModeText) {
                        this.payModeText = payModeText;
                    }

                    public String getOrganProfessionText() {
                        return organProfessionText;
                    }

                    public void setOrganProfessionText(String organProfessionText) {
                        this.organProfessionText = organProfessionText;
                    }

                    public String getRecipeTypeText() {
                        return recipeTypeText;
                    }

                    public void setRecipeTypeText(String recipeTypeText) {
                        this.recipeTypeText = recipeTypeText;
                    }

                    public String getProcessStateText() {
                        return processStateText;
                    }

                    public void setProcessStateText(String processStateText) {
                        this.processStateText = processStateText;
                    }

                    public String getClinicOrganText() {
                        return clinicOrganText;
                    }

                    public void setClinicOrganText(String clinicOrganText) {
                        this.clinicOrganText = clinicOrganText;
                    }

                    public String getOriginClinicOrganText() {
                        return originClinicOrganText;
                    }

                    public void setOriginClinicOrganText(String originClinicOrganText) {
                        this.originClinicOrganText = originClinicOrganText;
                    }

                    public String getDepartText() {
                        return departText;
                    }

                    public void setDepartText(String departText) {
                        this.departText = departText;
                    }

                    public String getDoctorText() {
                        return doctorText;
                    }

                    public void setDoctorText(String doctorText) {
                        this.doctorText = doctorText;
                    }

                    public String getCheckOrganText() {
                        return checkOrganText;
                    }

                    public void setCheckOrganText(String checkOrganText) {
                        this.checkOrganText = checkOrganText;
                    }

                    public String getFromflagText() {
                        return fromflagText;
                    }

                    public void setFromflagText(String fromflagText) {
                        this.fromflagText = fromflagText;
                    }
                }

                public static class PatientDTO {
                    private String loginId;
                    private String mpiId;
                    private String patientName;
                    private String patientSex;
                    private String birthday;
                    private String patientType;
                    private String idcard;
                    private String mobile;
                    private String homeArea;
                    private String createDate;
                    private String lastModify;
                    private int status;
                    private boolean guardianFlag;
                    private int certificateType;
                    private String fullHomeArea;
                    private int patientUserType;
                    private int authStatus;
                    private boolean isOwn;
                    private List<?> healthCards;
                    private int age;
                    private boolean signFlag;
                    private boolean relationFlag;
                    private List<?> labelNames;
                    private int urt;
                    private String ageString;
                    private String statusText;
                    private String patientTypeText;
                    private String patientSexText;
                    private String educationText;
                    private String expectClinicPeriodTypeText;
                    private String patientUserTypeText;
                    private String certificateTypeText;
                    private String authStatusText;
                    private String homeAreaText;
                    private String marryText;
                    private String jobText;
                    private String nationText;
                    private String countryText;
                    private String stateText;
                    private String birthPlaceText;
                    private String houseHoldText;
                    private String residentText;
                    private String guardianCertificateTypeText;
                    private boolean defaultPatient;

                    public String getLoginId() {
                        return loginId;
                    }

                    public void setLoginId(String loginId) {
                        this.loginId = loginId;
                    }

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

                    public String getBirthday() {
                        return birthday;
                    }

                    public void setBirthday(String birthday) {
                        this.birthday = birthday;
                    }

                    public String getPatientType() {
                        return patientType;
                    }

                    public void setPatientType(String patientType) {
                        this.patientType = patientType;
                    }

                    public String getIdcard() {
                        return idcard;
                    }

                    public void setIdcard(String idcard) {
                        this.idcard = idcard;
                    }

                    public String getMobile() {
                        return mobile;
                    }

                    public void setMobile(String mobile) {
                        this.mobile = mobile;
                    }

                    public String getHomeArea() {
                        return homeArea;
                    }

                    public void setHomeArea(String homeArea) {
                        this.homeArea = homeArea;
                    }

                    public String getCreateDate() {
                        return createDate;
                    }

                    public void setCreateDate(String createDate) {
                        this.createDate = createDate;
                    }

                    public String getLastModify() {
                        return lastModify;
                    }

                    public void setLastModify(String lastModify) {
                        this.lastModify = lastModify;
                    }

                    public int getStatus() {
                        return status;
                    }

                    public void setStatus(int status) {
                        this.status = status;
                    }

                    public boolean isGuardianFlag() {
                        return guardianFlag;
                    }

                    public void setGuardianFlag(boolean guardianFlag) {
                        this.guardianFlag = guardianFlag;
                    }

                    public int getCertificateType() {
                        return certificateType;
                    }

                    public void setCertificateType(int certificateType) {
                        this.certificateType = certificateType;
                    }

                    public String getFullHomeArea() {
                        return fullHomeArea;
                    }

                    public void setFullHomeArea(String fullHomeArea) {
                        this.fullHomeArea = fullHomeArea;
                    }

                    public int getPatientUserType() {
                        return patientUserType;
                    }

                    public void setPatientUserType(int patientUserType) {
                        this.patientUserType = patientUserType;
                    }

                    public int getAuthStatus() {
                        return authStatus;
                    }

                    public void setAuthStatus(int authStatus) {
                        this.authStatus = authStatus;
                    }

                    public boolean isIsOwn() {
                        return isOwn;
                    }

                    public void setIsOwn(boolean isOwn) {
                        this.isOwn = isOwn;
                    }

                    public List<?> getHealthCards() {
                        return healthCards;
                    }

                    public void setHealthCards(List<?> healthCards) {
                        this.healthCards = healthCards;
                    }

                    public int getAge() {
                        return age;
                    }

                    public void setAge(int age) {
                        this.age = age;
                    }

                    public boolean isSignFlag() {
                        return signFlag;
                    }

                    public void setSignFlag(boolean signFlag) {
                        this.signFlag = signFlag;
                    }

                    public boolean isRelationFlag() {
                        return relationFlag;
                    }

                    public void setRelationFlag(boolean relationFlag) {
                        this.relationFlag = relationFlag;
                    }

                    public List<?> getLabelNames() {
                        return labelNames;
                    }

                    public void setLabelNames(List<?> labelNames) {
                        this.labelNames = labelNames;
                    }

                    public int getUrt() {
                        return urt;
                    }

                    public void setUrt(int urt) {
                        this.urt = urt;
                    }

                    public String getAgeString() {
                        return ageString;
                    }

                    public void setAgeString(String ageString) {
                        this.ageString = ageString;
                    }

                    public String getStatusText() {
                        return statusText;
                    }

                    public void setStatusText(String statusText) {
                        this.statusText = statusText;
                    }

                    public String getPatientTypeText() {
                        return patientTypeText;
                    }

                    public void setPatientTypeText(String patientTypeText) {
                        this.patientTypeText = patientTypeText;
                    }

                    public String getPatientSexText() {
                        return patientSexText;
                    }

                    public void setPatientSexText(String patientSexText) {
                        this.patientSexText = patientSexText;
                    }

                    public String getEducationText() {
                        return educationText;
                    }

                    public void setEducationText(String educationText) {
                        this.educationText = educationText;
                    }

                    public String getExpectClinicPeriodTypeText() {
                        return expectClinicPeriodTypeText;
                    }

                    public void setExpectClinicPeriodTypeText(String expectClinicPeriodTypeText) {
                        this.expectClinicPeriodTypeText = expectClinicPeriodTypeText;
                    }

                    public String getPatientUserTypeText() {
                        return patientUserTypeText;
                    }

                    public void setPatientUserTypeText(String patientUserTypeText) {
                        this.patientUserTypeText = patientUserTypeText;
                    }

                    public String getCertificateTypeText() {
                        return certificateTypeText;
                    }

                    public void setCertificateTypeText(String certificateTypeText) {
                        this.certificateTypeText = certificateTypeText;
                    }

                    public String getAuthStatusText() {
                        return authStatusText;
                    }

                    public void setAuthStatusText(String authStatusText) {
                        this.authStatusText = authStatusText;
                    }

                    public String getHomeAreaText() {
                        return homeAreaText;
                    }

                    public void setHomeAreaText(String homeAreaText) {
                        this.homeAreaText = homeAreaText;
                    }

                    public String getMarryText() {
                        return marryText;
                    }

                    public void setMarryText(String marryText) {
                        this.marryText = marryText;
                    }

                    public String getJobText() {
                        return jobText;
                    }

                    public void setJobText(String jobText) {
                        this.jobText = jobText;
                    }

                    public String getNationText() {
                        return nationText;
                    }

                    public void setNationText(String nationText) {
                        this.nationText = nationText;
                    }

                    public String getCountryText() {
                        return countryText;
                    }

                    public void setCountryText(String countryText) {
                        this.countryText = countryText;
                    }

                    public String getStateText() {
                        return stateText;
                    }

                    public void setStateText(String stateText) {
                        this.stateText = stateText;
                    }

                    public String getBirthPlaceText() {
                        return birthPlaceText;
                    }

                    public void setBirthPlaceText(String birthPlaceText) {
                        this.birthPlaceText = birthPlaceText;
                    }

                    public String getHouseHoldText() {
                        return houseHoldText;
                    }

                    public void setHouseHoldText(String houseHoldText) {
                        this.houseHoldText = houseHoldText;
                    }

                    public String getResidentText() {
                        return residentText;
                    }

                    public void setResidentText(String residentText) {
                        this.residentText = residentText;
                    }

                    public String getGuardianCertificateTypeText() {
                        return guardianCertificateTypeText;
                    }

                    public void setGuardianCertificateTypeText(String guardianCertificateTypeText) {
                        this.guardianCertificateTypeText = guardianCertificateTypeText;
                    }

                    public boolean isDefaultPatient() {
                        return defaultPatient;
                    }

                    public void setDefaultPatient(boolean defaultPatient) {
                        this.defaultPatient = defaultPatient;
                    }
                }

                public static class GiveModeShowButtonVODTO {
                    private boolean optional;
                    private int buttonType;
                    private boolean showButton;
                    private ListItemDTO listItem;
                    private List<GiveModeButtonsDTO> giveModeButtons;

                    public boolean isOptional() {
                        return optional;
                    }

                    public void setOptional(boolean optional) {
                        this.optional = optional;
                    }

                    public int getButtonType() {
                        return buttonType;
                    }

                    public void setButtonType(int buttonType) {
                        this.buttonType = buttonType;
                    }

                    public boolean isShowButton() {
                        return showButton;
                    }

                    public void setShowButton(boolean showButton) {
                        this.showButton = showButton;
                    }

                    public ListItemDTO getListItem() {
                        return listItem;
                    }

                    public void setListItem(ListItemDTO listItem) {
                        this.listItem = listItem;
                    }

                    public List<GiveModeButtonsDTO> getGiveModeButtons() {
                        return giveModeButtons;
                    }

                    public void setGiveModeButtons(List<GiveModeButtonsDTO> giveModeButtons) {
                        this.giveModeButtons = giveModeButtons;
                    }

                    public static class ListItemDTO {
                        private String showButtonKey;
                        private String showButtonName;
                        private String buttonSkipType;

                        public String getShowButtonKey() {
                            return showButtonKey;
                        }

                        public void setShowButtonKey(String showButtonKey) {
                            this.showButtonKey = showButtonKey;
                        }

                        public String getShowButtonName() {
                            return showButtonName;
                        }

                        public void setShowButtonName(String showButtonName) {
                            this.showButtonName = showButtonName;
                        }

                        public String getButtonSkipType() {
                            return buttonSkipType;
                        }

                        public void setButtonSkipType(String buttonSkipType) {
                            this.buttonSkipType = buttonSkipType;
                        }
                    }

                    public static class GiveModeButtonsDTO {
                        private String showButtonKey;
                        private String showButtonName;
                        private String buttonSkipType;
                        private String skipUrl;

                        public String getShowButtonKey() {
                            return showButtonKey;
                        }

                        public void setShowButtonKey(String showButtonKey) {
                            this.showButtonKey = showButtonKey;
                        }

                        public String getShowButtonName() {
                            return showButtonName;
                        }

                        public void setShowButtonName(String showButtonName) {
                            this.showButtonName = showButtonName;
                        }

                        public String getButtonSkipType() {
                            return buttonSkipType;
                        }

                        public void setButtonSkipType(String buttonSkipType) {
                            this.buttonSkipType = buttonSkipType;
                        }

                        public String getSkipUrl() {
                            return skipUrl;
                        }

                        public void setSkipUrl(String skipUrl) {
                            this.skipUrl = skipUrl;
                        }
                    }
                }

                public static class RecipeExtendDTO {
                    private int recipeId;
                    private String mainDieaseDescribe;
                    private String currentMedical;
                    private String histroyMedical;
                    private String registerID;
                    private String physicalCheck;
                    private String isLongRecipe;
                    private String recipeJsonConfig;
                    private String everyTcmNumFre;
                    private String juice;
                    private String juiceUnit;
                    private String minor;
                    private String minorUnit;
                    private int docIndexId;
                    private String pharmNo;
                    private int canUrgentAuditRecipe;
                    private int forceCashType;
                    private int appointEnterpriseType;
                    private int recipeFlag;
                    private String medicalRecordNumber;
                    private String cancellation;
                    private int recipeChooseChronicDisease;
                    private String guardianMobile;
                    private String recipeCostNumber;
                    private double weight;
                    private int recipeBusinessType;
                    private String doctorIsDecoction;
                    private String illnessType;
                    private String illnessName;
                    private String handleType;
                    private String sideCourtYardType;
                    private String chronicDiseaseFlagText;
                    private String refundNodeStatusText;

                    public int getRecipeId() {
                        return recipeId;
                    }

                    public void setRecipeId(int recipeId) {
                        this.recipeId = recipeId;
                    }

                    public String getMainDieaseDescribe() {
                        return mainDieaseDescribe;
                    }

                    public void setMainDieaseDescribe(String mainDieaseDescribe) {
                        this.mainDieaseDescribe = mainDieaseDescribe;
                    }

                    public String getCurrentMedical() {
                        return currentMedical;
                    }

                    public void setCurrentMedical(String currentMedical) {
                        this.currentMedical = currentMedical;
                    }

                    public String getHistroyMedical() {
                        return histroyMedical;
                    }

                    public void setHistroyMedical(String histroyMedical) {
                        this.histroyMedical = histroyMedical;
                    }

                    public String getRegisterID() {
                        return registerID;
                    }

                    public void setRegisterID(String registerID) {
                        this.registerID = registerID;
                    }

                    public String getPhysicalCheck() {
                        return physicalCheck;
                    }

                    public void setPhysicalCheck(String physicalCheck) {
                        this.physicalCheck = physicalCheck;
                    }

                    public String getIsLongRecipe() {
                        return isLongRecipe;
                    }

                    public void setIsLongRecipe(String isLongRecipe) {
                        this.isLongRecipe = isLongRecipe;
                    }

                    public String getRecipeJsonConfig() {
                        return recipeJsonConfig;
                    }

                    public void setRecipeJsonConfig(String recipeJsonConfig) {
                        this.recipeJsonConfig = recipeJsonConfig;
                    }

                    public String getEveryTcmNumFre() {
                        return everyTcmNumFre;
                    }

                    public void setEveryTcmNumFre(String everyTcmNumFre) {
                        this.everyTcmNumFre = everyTcmNumFre;
                    }

                    public String getJuice() {
                        return juice;
                    }

                    public void setJuice(String juice) {
                        this.juice = juice;
                    }

                    public String getJuiceUnit() {
                        return juiceUnit;
                    }

                    public void setJuiceUnit(String juiceUnit) {
                        this.juiceUnit = juiceUnit;
                    }

                    public String getMinor() {
                        return minor;
                    }

                    public void setMinor(String minor) {
                        this.minor = minor;
                    }

                    public String getMinorUnit() {
                        return minorUnit;
                    }

                    public void setMinorUnit(String minorUnit) {
                        this.minorUnit = minorUnit;
                    }

                    public int getDocIndexId() {
                        return docIndexId;
                    }

                    public void setDocIndexId(int docIndexId) {
                        this.docIndexId = docIndexId;
                    }

                    public String getPharmNo() {
                        return pharmNo;
                    }

                    public void setPharmNo(String pharmNo) {
                        this.pharmNo = pharmNo;
                    }

                    public int getCanUrgentAuditRecipe() {
                        return canUrgentAuditRecipe;
                    }

                    public void setCanUrgentAuditRecipe(int canUrgentAuditRecipe) {
                        this.canUrgentAuditRecipe = canUrgentAuditRecipe;
                    }

                    public int getForceCashType() {
                        return forceCashType;
                    }

                    public void setForceCashType(int forceCashType) {
                        this.forceCashType = forceCashType;
                    }

                    public int getAppointEnterpriseType() {
                        return appointEnterpriseType;
                    }

                    public void setAppointEnterpriseType(int appointEnterpriseType) {
                        this.appointEnterpriseType = appointEnterpriseType;
                    }

                    public int getRecipeFlag() {
                        return recipeFlag;
                    }

                    public void setRecipeFlag(int recipeFlag) {
                        this.recipeFlag = recipeFlag;
                    }

                    public String getMedicalRecordNumber() {
                        return medicalRecordNumber;
                    }

                    public void setMedicalRecordNumber(String medicalRecordNumber) {
                        this.medicalRecordNumber = medicalRecordNumber;
                    }

                    public String getCancellation() {
                        return cancellation;
                    }

                    public void setCancellation(String cancellation) {
                        this.cancellation = cancellation;
                    }

                    public int getRecipeChooseChronicDisease() {
                        return recipeChooseChronicDisease;
                    }

                    public void setRecipeChooseChronicDisease(int recipeChooseChronicDisease) {
                        this.recipeChooseChronicDisease = recipeChooseChronicDisease;
                    }

                    public String getGuardianMobile() {
                        return guardianMobile;
                    }

                    public void setGuardianMobile(String guardianMobile) {
                        this.guardianMobile = guardianMobile;
                    }

                    public String getRecipeCostNumber() {
                        return recipeCostNumber;
                    }

                    public void setRecipeCostNumber(String recipeCostNumber) {
                        this.recipeCostNumber = recipeCostNumber;
                    }

                    public double getWeight() {
                        return weight;
                    }

                    public void setWeight(double weight) {
                        this.weight = weight;
                    }

                    public int getRecipeBusinessType() {
                        return recipeBusinessType;
                    }

                    public void setRecipeBusinessType(int recipeBusinessType) {
                        this.recipeBusinessType = recipeBusinessType;
                    }

                    public String getDoctorIsDecoction() {
                        return doctorIsDecoction;
                    }

                    public void setDoctorIsDecoction(String doctorIsDecoction) {
                        this.doctorIsDecoction = doctorIsDecoction;
                    }

                    public String getIllnessType() {
                        return illnessType;
                    }

                    public void setIllnessType(String illnessType) {
                        this.illnessType = illnessType;
                    }

                    public String getIllnessName() {
                        return illnessName;
                    }

                    public void setIllnessName(String illnessName) {
                        this.illnessName = illnessName;
                    }

                    public String getHandleType() {
                        return handleType;
                    }

                    public void setHandleType(String handleType) {
                        this.handleType = handleType;
                    }

                    public String getSideCourtYardType() {
                        return sideCourtYardType;
                    }

                    public void setSideCourtYardType(String sideCourtYardType) {
                        this.sideCourtYardType = sideCourtYardType;
                    }

                    public String getChronicDiseaseFlagText() {
                        return chronicDiseaseFlagText;
                    }

                    public void setChronicDiseaseFlagText(String chronicDiseaseFlagText) {
                        this.chronicDiseaseFlagText = chronicDiseaseFlagText;
                    }

                    public String getRefundNodeStatusText() {
                        return refundNodeStatusText;
                    }

                    public void setRefundNodeStatusText(String refundNodeStatusText) {
                        this.refundNodeStatusText = refundNodeStatusText;
                    }
                }

                public static class RecipedetailsDTO {
                    private int recipeDetailId;
                    private int recipeId;
                    private String drugGroup;
                    private int drugId;
                    private String organDrugCode;
                    private String drugName;
                    private String saleName;
                    private String drugSpec;
                    private int pack;
                    private String drugUnit;
                    private double defaultUseDose;
                    private String useDoseStr;
                    private String useDoseUnit;
                    private String dosageUnit;
                    private String usingRate;
                    private String usePathways;
                    private String organUsingRate;
                    private String organUsePathways;
                    private String usingRateTextFromHis;
                    private String usePathwaysTextFromHis;
                    private double useTotalDose;
                    private double sendNumber;
                    private int useDays;
                    private double drugCost;
                    private String entrustmentId;
                    private String memo;
                    private String createDt;
                    private String lastModify;
                    private double salePrice;
                    private double price;
                    private String orderNo;
                    private int status;
                    private String drugForm;
                    private String producer;
                    private String licenseNumber;
                    private String producerCode;
                    private String useDaysB;
                    private String usingRateId;
                    private String usePathwaysId;
                    private List<UseDoseAndUnitRelationDTO> useDoseAndUnitRelation;
                    private String drugUnitdoseAndUnit;
                    private int pharmacyId;
                    private String pharmacyName;
                    private int drugType;
                    private String drugDisplaySplicedName;
                    private String drugDisplaySplicedSaleName;
                    private String superScalarCode;
                    private String superScalarName;
                    private int type;
                    private String drugPic;
                    private double saleDrugPrice;
                    private String usingRateText;
                    private String usePathwaysText;
                    private String usingRateIdText;
                    private String usePathwaysIdText;
                    private String drugTypeText;

                    public int getRecipeDetailId() {
                        return recipeDetailId;
                    }

                    public void setRecipeDetailId(int recipeDetailId) {
                        this.recipeDetailId = recipeDetailId;
                    }

                    public int getRecipeId() {
                        return recipeId;
                    }

                    public void setRecipeId(int recipeId) {
                        this.recipeId = recipeId;
                    }

                    public String getDrugGroup() {
                        return drugGroup;
                    }

                    public void setDrugGroup(String drugGroup) {
                        this.drugGroup = drugGroup;
                    }

                    public int getDrugId() {
                        return drugId;
                    }

                    public void setDrugId(int drugId) {
                        this.drugId = drugId;
                    }

                    public String getOrganDrugCode() {
                        return organDrugCode;
                    }

                    public void setOrganDrugCode(String organDrugCode) {
                        this.organDrugCode = organDrugCode;
                    }

                    public String getDrugName() {
                        return drugName;
                    }

                    public void setDrugName(String drugName) {
                        this.drugName = drugName;
                    }

                    public String getSaleName() {
                        return saleName;
                    }

                    public void setSaleName(String saleName) {
                        this.saleName = saleName;
                    }

                    public String getDrugSpec() {
                        return drugSpec;
                    }

                    public void setDrugSpec(String drugSpec) {
                        this.drugSpec = drugSpec;
                    }

                    public int getPack() {
                        return pack;
                    }

                    public void setPack(int pack) {
                        this.pack = pack;
                    }

                    public String getDrugUnit() {
                        return drugUnit;
                    }

                    public void setDrugUnit(String drugUnit) {
                        this.drugUnit = drugUnit;
                    }

                    public double getDefaultUseDose() {
                        return defaultUseDose;
                    }

                    public void setDefaultUseDose(double defaultUseDose) {
                        this.defaultUseDose = defaultUseDose;
                    }

                    public String getUseDoseStr() {
                        return useDoseStr;
                    }

                    public void setUseDoseStr(String useDoseStr) {
                        this.useDoseStr = useDoseStr;
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

                    public String getOrganUsingRate() {
                        return organUsingRate;
                    }

                    public void setOrganUsingRate(String organUsingRate) {
                        this.organUsingRate = organUsingRate;
                    }

                    public String getOrganUsePathways() {
                        return organUsePathways;
                    }

                    public void setOrganUsePathways(String organUsePathways) {
                        this.organUsePathways = organUsePathways;
                    }

                    public String getUsingRateTextFromHis() {
                        return usingRateTextFromHis;
                    }

                    public void setUsingRateTextFromHis(String usingRateTextFromHis) {
                        this.usingRateTextFromHis = usingRateTextFromHis;
                    }

                    public String getUsePathwaysTextFromHis() {
                        return usePathwaysTextFromHis;
                    }

                    public void setUsePathwaysTextFromHis(String usePathwaysTextFromHis) {
                        this.usePathwaysTextFromHis = usePathwaysTextFromHis;
                    }

                    public double getUseTotalDose() {
                        return useTotalDose;
                    }

                    public void setUseTotalDose(double useTotalDose) {
                        this.useTotalDose = useTotalDose;
                    }

                    public double getSendNumber() {
                        return sendNumber;
                    }

                    public void setSendNumber(double sendNumber) {
                        this.sendNumber = sendNumber;
                    }

                    public int getUseDays() {
                        return useDays;
                    }

                    public void setUseDays(int useDays) {
                        this.useDays = useDays;
                    }

                    public double getDrugCost() {
                        return drugCost;
                    }

                    public void setDrugCost(double drugCost) {
                        this.drugCost = drugCost;
                    }

                    public String getEntrustmentId() {
                        return entrustmentId;
                    }

                    public void setEntrustmentId(String entrustmentId) {
                        this.entrustmentId = entrustmentId;
                    }

                    public String getMemo() {
                        return memo;
                    }

                    public void setMemo(String memo) {
                        this.memo = memo;
                    }

                    public String getCreateDt() {
                        return createDt;
                    }

                    public void setCreateDt(String createDt) {
                        this.createDt = createDt;
                    }

                    public String getLastModify() {
                        return lastModify;
                    }

                    public void setLastModify(String lastModify) {
                        this.lastModify = lastModify;
                    }

                    public double getSalePrice() {
                        return salePrice;
                    }

                    public void setSalePrice(double salePrice) {
                        this.salePrice = salePrice;
                    }

                    public double getPrice() {
                        return price;
                    }

                    public void setPrice(double price) {
                        this.price = price;
                    }

                    public String getOrderNo() {
                        return orderNo;
                    }

                    public void setOrderNo(String orderNo) {
                        this.orderNo = orderNo;
                    }

                    public int getStatus() {
                        return status;
                    }

                    public void setStatus(int status) {
                        this.status = status;
                    }

                    public String getDrugForm() {
                        return drugForm;
                    }

                    public void setDrugForm(String drugForm) {
                        this.drugForm = drugForm;
                    }

                    public String getProducer() {
                        return producer;
                    }

                    public void setProducer(String producer) {
                        this.producer = producer;
                    }

                    public String getLicenseNumber() {
                        return licenseNumber;
                    }

                    public void setLicenseNumber(String licenseNumber) {
                        this.licenseNumber = licenseNumber;
                    }

                    public String getProducerCode() {
                        return producerCode;
                    }

                    public void setProducerCode(String producerCode) {
                        this.producerCode = producerCode;
                    }

                    public String getUseDaysB() {
                        return useDaysB;
                    }

                    public void setUseDaysB(String useDaysB) {
                        this.useDaysB = useDaysB;
                    }

                    public String getUsingRateId() {
                        return usingRateId;
                    }

                    public void setUsingRateId(String usingRateId) {
                        this.usingRateId = usingRateId;
                    }

                    public String getUsePathwaysId() {
                        return usePathwaysId;
                    }

                    public void setUsePathwaysId(String usePathwaysId) {
                        this.usePathwaysId = usePathwaysId;
                    }

                    public List<UseDoseAndUnitRelationDTO> getUseDoseAndUnitRelation() {
                        return useDoseAndUnitRelation;
                    }

                    public void setUseDoseAndUnitRelation(List<UseDoseAndUnitRelationDTO> useDoseAndUnitRelation) {
                        this.useDoseAndUnitRelation = useDoseAndUnitRelation;
                    }

                    public String getDrugUnitdoseAndUnit() {
                        return drugUnitdoseAndUnit;
                    }

                    public void setDrugUnitdoseAndUnit(String drugUnitdoseAndUnit) {
                        this.drugUnitdoseAndUnit = drugUnitdoseAndUnit;
                    }

                    public int getPharmacyId() {
                        return pharmacyId;
                    }

                    public void setPharmacyId(int pharmacyId) {
                        this.pharmacyId = pharmacyId;
                    }

                    public String getPharmacyName() {
                        return pharmacyName;
                    }

                    public void setPharmacyName(String pharmacyName) {
                        this.pharmacyName = pharmacyName;
                    }

                    public int getDrugType() {
                        return drugType;
                    }

                    public void setDrugType(int drugType) {
                        this.drugType = drugType;
                    }

                    public String getDrugDisplaySplicedName() {
                        return drugDisplaySplicedName;
                    }

                    public void setDrugDisplaySplicedName(String drugDisplaySplicedName) {
                        this.drugDisplaySplicedName = drugDisplaySplicedName;
                    }

                    public String getDrugDisplaySplicedSaleName() {
                        return drugDisplaySplicedSaleName;
                    }

                    public void setDrugDisplaySplicedSaleName(String drugDisplaySplicedSaleName) {
                        this.drugDisplaySplicedSaleName = drugDisplaySplicedSaleName;
                    }

                    public String getSuperScalarCode() {
                        return superScalarCode;
                    }

                    public void setSuperScalarCode(String superScalarCode) {
                        this.superScalarCode = superScalarCode;
                    }

                    public String getSuperScalarName() {
                        return superScalarName;
                    }

                    public void setSuperScalarName(String superScalarName) {
                        this.superScalarName = superScalarName;
                    }

                    public int getType() {
                        return type;
                    }

                    public void setType(int type) {
                        this.type = type;
                    }

                    public String getDrugPic() {
                        return drugPic;
                    }

                    public void setDrugPic(String drugPic) {
                        this.drugPic = drugPic;
                    }

                    public double getSaleDrugPrice() {
                        return saleDrugPrice;
                    }

                    public void setSaleDrugPrice(double saleDrugPrice) {
                        this.saleDrugPrice = saleDrugPrice;
                    }

                    public String getUsingRateText() {
                        return usingRateText;
                    }

                    public void setUsingRateText(String usingRateText) {
                        this.usingRateText = usingRateText;
                    }

                    public String getUsePathwaysText() {
                        return usePathwaysText;
                    }

                    public void setUsePathwaysText(String usePathwaysText) {
                        this.usePathwaysText = usePathwaysText;
                    }

                    public String getUsingRateIdText() {
                        return usingRateIdText;
                    }

                    public void setUsingRateIdText(String usingRateIdText) {
                        this.usingRateIdText = usingRateIdText;
                    }

                    public String getUsePathwaysIdText() {
                        return usePathwaysIdText;
                    }

                    public void setUsePathwaysIdText(String usePathwaysIdText) {
                        this.usePathwaysIdText = usePathwaysIdText;
                    }

                    public String getDrugTypeText() {
                        return drugTypeText;
                    }

                    public void setDrugTypeText(String drugTypeText) {
                        this.drugTypeText = drugTypeText;
                    }

                    public static class UseDoseAndUnitRelationDTO {
                        private String useDoseUnit;
                        private double realUseDose;

                        public String getUseDoseUnit() {
                            return useDoseUnit;
                        }

                        public void setUseDoseUnit(String useDoseUnit) {
                            this.useDoseUnit = useDoseUnit;
                        }

                        public double getRealUseDose() {
                            return realUseDose;
                        }

                        public void setRealUseDose(double realUseDose) {
                            this.realUseDose = realUseDose;
                        }
                    }
                }
            }
        }
    }
}
