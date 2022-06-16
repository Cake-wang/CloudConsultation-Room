package com.aries.template.entity;

import java.util.List;

/**
 * @Author: AriesHoo on 2018/11/19 14:17
 * @E-Mail: AriesHoo@126.com
 * @Function: 检查新版本实体
 * @Description:
 */
public class RequestConsultAndCdrOtherdocResultEntity {

    public Boolean success;
    public String code;
    public String message;
    public DataBean data;
    public String sign;

    public static class DataBean {
        public Integer statusCode;
        public String requestId;
        public String caErrorMsg;
        public String errorMessage;
        public JsonResponseBeanBean jsonResponseBean;
        public Boolean success;

        public static class JsonResponseBeanBean {
            public Integer code;
            public String msg;
            public BodyBean body;
            public String properties;

            public static class BodyBean {
                public List<CdrOtherdocsBean> cdrOtherdocs;
                public PatientBean patient;
                public DoctorBean doctor;
                public ConsultBean consult;

                public static class PatientBean {
                    public String mpiId;
                    public String patientName;
                    public String patientSex;
                    public Integer age;
                    public String certificate;
                    public Integer certificateType;
                    public String mobile;
                    public String patientSexText;
                    public String certificateTypeText;
                }

                public static class DoctorBean {
                    public String loginId;
                    public Integer urt;
                    public Integer doctorId;
                    public String name;
                    public String gender;
                    public Integer userType;
                    public String birthDay;
                    public String profession;
                    public Integer organProfession;
                    public String mobile;
                    public String proTitle;
                    public Boolean teams;
                    public Boolean expert;
                    public Integer status;
                    public String createDt;
                    public String lastModify;
                    public Integer organ;
                    public Integer haveAppoint;
                    public Integer testPersonnel;
                    public Integer chief;
                    public Boolean virtualDoctor;
                    public Integer source;
                    public Boolean rewardFlag;
                    public String qrCode;
                    public String qrUrl;
                    public Integer groupType;
                    public Integer generalDoctor;
                    public Double searchRating;
                    public ExtendParamBean extendParam;
                    public Integer easemobStatus;
                    public Integer doctorSortNum;
                    public Integer currentOrgan;
                    public Integer cloudAppointSourceFlag;
                    public Boolean canReport;
                    public Boolean hasReport;
                    public Integer infoStatus;
                    public Integer verifyStatus;
                    public String revisitGroupModeText;
                    public String infoStatusText;
                    public String verifyStatusText;
                    public String genderText;
                    public String professionText;
                    public String organProfessionText;
                    public String proTitleText;
                    public String jobTitleText;
                    public String educationText;
                    public String onlineText;
                    public String busyFlagText;
                    public String organText;
                    public String departmentText;
                    public String sourceText;
                    public String leaderText;
                    public String groupTypeText;
                    public String appointSourceFlagText;
                    public String currentOrganText;
                    public String cloudAppointSourceFlagText;
                    public String groupModeText;
                    public String userTypeText;
                    public String statusText;

                    public static class ExtendParamBean {
                    }
                }

                public static class ConsultBean {
                    public Long consultId;
                    public String mpiid;
                    public String mpiName;
                    public Double weight;
                    public Integer requestMode;
                    public String requestMpi;
                    public String requestTime;
                    public Integer consultOrgan;
                    public Integer consultDepart;
                    public Integer consultDoctor;
                    public String answerTel;
                    public Double consultCost;
                    public Integer payflag;
                    public Integer consultStatus;
                    public String paymentDate;
                    public Double consultPrice;
                    public String sessionID;
                    public String sessionStartTime;
                    public Boolean feedBack;
                    public Boolean teams;
                    public Integer requestMpiUrt;
                    public Integer consultDoctorUrt;
                    public String appId;
                    public String openId;
                    public Boolean hasChat;
                    public Boolean remindFlag;
                    public Boolean signInRemindFlag;
                    public Long deviceId;
                    public Integer groupMode;
                    public Double actualPrice;
                    public Boolean hasAdditionMessage;
                    public Long questionnaireId;
                    public QuestionnaireBean questionnaire;
                    public Integer status;
                    public String statusDescribe;
                    public Boolean recipeReminderFlag;
                    public String lastModified;
                    public Integer expert;
                    public String clientType;
                    public String clientName;
                    public Integer userType;
                    public Integer medicalFlag;
                    public Double fundAmount;
                    public Double cashAmount;
                    public Integer busSource;
                    public Integer patientRefundStatus;
                    public Integer passNumber;
                    public String signInFlag;
                    public Integer sbType;
                    public Double sbReduceAmount;
                    public Long reserveDoctorId;
                    public Long lastConsultId;
                    public Integer revisitBussType;
                    public String requestModeText;
                    public String consultOrganText;
                    public String consultDepartText;
                    public String consultDoctorText;
                    public String payflagText;
                    public String exeDoctorText;
                    public String exeDepartText;
                    public String exeOrganText;
                    public String refuseFlagText;
                    public String cancelRoleText;
                    public String groupModeText;
                    public String userTypeText;
                    public String workTypeText;

                    public static class QuestionnaireBean {
                        public Long questionnaireId;
                        public Integer pregnent;
                        public Integer alleric;
                        public Integer diseaseStatus;
                        public String proposedDrugs;
                        public Integer haveTake;
                        public Integer haveReaction;
                        public String questionDesc;
                        public Integer returnVisitStatus;
                    }
                }

                public static class CdrOtherdocsBean {
                    public Long otherDocId;
                    public Integer clinicType;
                    public Long clinicId;
                    public String mpiid;
                    public String createDate;
                    public String docType;
                    public String docName;
                    public String docFormat;
                    public String docContent;
                    public String medicalDate;
                    public String clinicTypeText;
                    public String docTypeText;
                    public String docFormatText;
                    public String docContentToken;
                }
            }
        }
    }
}
