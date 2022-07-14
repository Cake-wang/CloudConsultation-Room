package com.aries.template.entity;

/**
 * 查询复诊单的小鱼视频会议室房间号和密码
 * 完成支付之后，执行库存推送接口
 */
public class RoomIdInsAuthEntity {

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

        public void setCaErrorMsg(Object caErrorMsg) {
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
            private String msg;
            private Object body;
            private Object properties;

            public int getCode() {
                return code;
            }

            public void setCode(int code) {
                this.code = code;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }

            public Object getBody() {
                return body;
            }

            public void setBody(Object body) {
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
