package com.aries.template.entity;

import java.util.List;

/**
 * 获取病人信息
 * 主要是 mpiId 这个值
 */
public class PatientListEntity {

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
            public BodyDTO body;
            public Object properties;

            public static class BodyDTO {
                public PatientDTO patient;

                public static class PatientDTO {
                    public String loginId;
                    public String mpiId;
                    public String patientName;
                    public String patientSex;
                    public int age;
                    public String certificate;
                    public int certificateType;
                    public String mobile;
                    public List<?> healthCards;
                    public String patientSexText;
                    public String certificateTypeText;
                }
            }
        }
    }
}
