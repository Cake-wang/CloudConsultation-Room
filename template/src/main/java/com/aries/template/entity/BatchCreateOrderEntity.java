package com.aries.template.entity;

/**
 * 3.11.	处方合并生成订单接口
 */
public class BatchCreateOrderEntity {


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
        private String caErrorMsg;
        private String errorMessage;
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

        public void setCaErrorMsg(String caErrorMsg) {
            this.caErrorMsg = caErrorMsg;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
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
            private int body;
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

            public int getBody() {
                return body;
            }

            public void setBody(int body) {
                this.body = body;
            }

            public Object getProperties() {
                return properties;
            }

            public void setProperties(Object properties) {
                this.properties = properties;
            }
        }
    }
}
