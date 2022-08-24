package com.aries.template.entity;

public class OrderPreSettleEntity {
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
                public String preSettleTotalAmount;
                public String fundAmount;
                public String cashAmount;
            }
        }
    }
}
