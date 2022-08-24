package com.aries.template.entity;

public class VisitMedicalPreSettleEntity {
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
            public Object body;
            public Object properties;
        }
    }
}
