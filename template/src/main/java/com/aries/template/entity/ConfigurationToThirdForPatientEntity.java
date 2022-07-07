package com.aries.template.entity;

/**
 *获取第三方配置信息
 * getConfigurationToThirdForPatient
 * @author louisluo
 * @Author: AriesHoo on 2018/11/19 14:17
 * @E-Mail: AriesHoo@126.com
 * @Function: 检查新版本实体
 * @Description:
 */
public class ConfigurationToThirdForPatientEntity {


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
                private String userId;
                private String picturDownloadUrl;
                private String pictureUploadUrl;
                private String apiURL;
                private String appkey;
                private String username;
                private String userpwd;
                private String xmppURL;
                private String wsurl;

                public String getUserId() {
                    return userId;
                }

                public void setUserId(String userId) {
                    this.userId = userId;
                }

                public String getPicturDownloadUrl() {
                    return picturDownloadUrl;
                }

                public void setPicturDownloadUrl(String picturDownloadUrl) {
                    this.picturDownloadUrl = picturDownloadUrl;
                }

                public String getPictureUploadUrl() {
                    return pictureUploadUrl;
                }

                public void setPictureUploadUrl(String pictureUploadUrl) {
                    this.pictureUploadUrl = pictureUploadUrl;
                }

                public String getApiURL() {
                    return apiURL;
                }

                public void setApiURL(String apiURL) {
                    this.apiURL = apiURL;
                }

                public String getAppkey() {
                    return appkey;
                }

                public void setAppkey(String appkey) {
                    this.appkey = appkey;
                }

                public String getUsername() {
                    return username;
                }

                public void setUsername(String username) {
                    this.username = username;
                }

                public String getUserpwd() {
                    return userpwd;
                }

                public void setUserpwd(String userpwd) {
                    this.userpwd = userpwd;
                }

                public String getXmppURL() {
                    return xmppURL;
                }

                public void setXmppURL(String xmppURL) {
                    this.xmppURL = xmppURL;
                }

                public String getWsurl() {
                    return wsurl;
                }

                public void setWsurl(String wsurl) {
                    this.wsurl = wsurl;
                }
            }
        }
    }
}
