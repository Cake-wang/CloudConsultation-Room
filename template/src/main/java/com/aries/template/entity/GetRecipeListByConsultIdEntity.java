package com.aries.template.entity;

import java.util.List;

/**
 * 获取病人信息
 * 主要是 mpiId 这个值
 */
public class GetRecipeListByConsultIdEntity {
    public boolean success;
    public String code;
    public String message;
    public DataDTO data;
    public String sign;

    public static class DataDTO {
        public int statusCode;
        public String requestId;
        public Object caErrorMsg;
        public Object errorMessage;
        public JsonResponseBeanDTO jsonResponseBean;
        public boolean success;

        public static class JsonResponseBeanDTO {
            public int code;
            public Object msg;
            public List<BodyDTO> body;
            public Object properties;

            public static class BodyDTO {
                public int recipeId;
                public int bussSource;
                public int clinicId;
                public String mpiid;
                public String patientID;
                public int patientStatus;
                public int clinicOrgan;
                public String organName;
                public String recipeCode;
                public int recipeType;
                public String recipeMode;
                public int depart;
                public String appointDepart;
                public String appointDepartName;
                public int doctor;
                public String createDate;
                public int copyNum;
                public double totalMoney;
                public String organDiseaseName;
                public String organDiseaseId;
                public int payFlag;
                public int giveFlag;
                public int valueDays;
                public int giveMode;
                public int status;
                public int fromflag;
                public String lastModify;
                public String signDate;
                public int chooseFlag;
                public int remindFlag;
                public int pushFlag;
                public double actualPrice;
                public int medicalPayFlag;
                public int distributionFlag;
                public String doctorName;
                public String patientName;
                public int takeMedicine;
                public String requestMpiId;
                public int requestUrt;
                public int currentClient;
                public int notation;
                public int reviewType;
                public int checkMode;
                public int checkStatus;
                public int recipeSourceType;
                public int recipePayType;
                public int grabOrderStatus;
                public int checkFlag;
                public String recipeSupportGiveMode;
                public int processState;
                public int subState;
                public int auditState;
                public int targetedDrugType;
                public int medicalFlag;
                public int fastRecipeFlag;
                public int doctorSignState;
                public int checkerSignState;
            }
        }
    }
}
