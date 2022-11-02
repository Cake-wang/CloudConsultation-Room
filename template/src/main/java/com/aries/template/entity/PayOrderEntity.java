package com.aries.template.entity;

/**
 * 3.12.	支付请求接口
 */
public class PayOrderEntity {

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
                private int busId;
                private String code;
                private String payType;
                private double price;
                private int needPay;
                private boolean isStepOverMerchant;
                public String qr_code;
                public String formData;
                private String payWay;
                private String accessToken;
                private String prepayId;
                public String prepay_id;
                private int saFlag;

                public String getFormData() {
                    return formData;
                }

                public void setFormData(String formData) {
                    this.formData = formData;
                }

                public int getBusId() {
                    return busId;
                }

                public void setBusId(int busId) {
                    this.busId = busId;
                }

                public String getCode() {
                    return code;
                }

                public void setCode(String code) {
                    this.code = code;
                }

                public String getPayType() {
                    return payType;
                }

                public void setPayType(String payType) {
                    this.payType = payType;
                }

                public double getPrice() {
                    return price;
                }

                public void setPrice(double price) {
                    this.price = price;
                }

                public int getNeedPay() {
                    return needPay;
                }

                public void setNeedPay(int needPay) {
                    this.needPay = needPay;
                }

                public boolean isIsStepOverMerchant() {
                    return isStepOverMerchant;
                }

                public void setIsStepOverMerchant(boolean isStepOverMerchant) {
                    this.isStepOverMerchant = isStepOverMerchant;
                }

                public String getPayWay() {
                    return payWay;
                }

                public void setPayWay(String payWay) {
                    this.payWay = payWay;
                }

                public String getAccessToken() {
                    return accessToken;
                }

                public void setAccessToken(String accessToken) {
                    this.accessToken = accessToken;
                }

                public String getPrepayId() {
                    return prepayId;
                }

                public void setPrepayId(String prepayId) {
                    this.prepayId = prepayId;
                }

                public int getSaFlag() {
                    return saFlag;
                }

                public void setSaFlag(int saFlag) {
                    this.saFlag = saFlag;
                }
            }
        }
    }
}
