package com.aries.template.entity;

public class ReceiveMessageFromPatientWithRequestModeEntity {

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
            public String body;
            public Object properties;
        }
    }
}
