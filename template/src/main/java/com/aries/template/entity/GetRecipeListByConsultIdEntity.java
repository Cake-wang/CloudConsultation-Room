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
//                public String appointDepartName;
                public String departText;
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
                public List<RecipeDetailBeanListDTO> recipeDetailBeanList;
                public int grabOrderStatus;
                public int checkFlag;
                public String recipeSupportGiveMode;
                public int processState;
                public int subState;
                public int auditState;
                public int supportMode;
                public int targetedDrugType;
                public int medicalFlag;
                public int fastRecipeFlag;
                public int doctorSignState;
                public int checkerSignState;

                public static class RecipeDetailBeanListDTO {
                    public int recipeDetailId;
                    public int recipeId;
                    public String drugGroup;
                    public int drugId;
                    public String organDrugCode;
                    public String drugName;
                    public String saleName;
                    public String drugSpec;
                    public int pack;
                    public String drugUnit;
                    public double useDose;
                    public double defaultUseDose;
                    public String useDoseStr;
                    public String useDoseUnit;
                    public String dosageUnit;
                    public String usingRate;
                    public String usePathways;
                    public String organUsingRate;
                    public String organUsePathways;
                    public String usingRateTextFromHis;
                    public String usePathwaysTextFromHis;
                    public double useTotalDose;
                    public String usingRateText;
                    public double sendNumber;
                    public int useDays;
                    public double drugCost;
                    public String entrustmentId;
                    public String memo;
                    public String createDt;
                    public String lastModify;
                    public double salePrice;
                    public double price;
                    public String orderNo;
                    public int status;
                    public String drugForm;
                    public String producer;
                    public String licenseNumber;
                    public String producerCode;
                    public String useDaysB;
                    public String usingRateId;
                    public String usePathwaysId;
                    public List<UseDoseAndUnitRelationDTO> useDoseAndUnitRelation;
                    public String drugUnitdoseAndUnit;
                    public int pharmacyId;
                    public String pharmacyName;
                    public int drugType;
                    public String drugDisplaySplicedName;
                    public String drugDisplaySplicedSaleName;
                    public String superScalarCode;
                    public String superScalarName;
                    public int type;
                    public String drugPic;
                    public double saleDrugPrice;

                    public static class UseDoseAndUnitRelationDTO {
                        public String useDoseUnit;
                        public double realUseDose;
                    }
                }
            }
        }
    }
}
