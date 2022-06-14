package com.aries.template.entity;

/**
 * @Author: AriesHoo on 2018/11/19 14:17
 * @E-Mail: AriesHoo@126.com
 * @Function: 检查新版本实体
 * @Description:
 */
public class RequestConsultAndCdrOtherdocResultEntity {

    /**
     * versionCode : 230
     * versionName : 2.2.10-beta1
     * url : https://raw.githubusercontent.com/AriesHoo/FastLib/master/apk/sample.apk
     * force : true
     * message : 优化:调整ActivityFragmentControl 将状态栏及导航栏控制增加
     * 优化:多状态管理StatusLayoutManager调整完成
     * 优化:滑动返回控制swipeBack功能新增各种回调功能
     * 优化:将原默认配置方法调整到最终实现类功能
     * 优化:其它细节优化
     */

    public String code;
    public String message;
    public String sign;

    public boolean success;

    public QueryArrearsSummary data;

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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public QueryArrearsSummary getData() {
        return data;
    }

    public void setData(QueryArrearsSummary data) {
        this.data = data;
    }

    public class QueryArrearsSummary {

        public Integer statusCode;
        public String requestId;
        public String caErrorMsg;
        public String errorMessage;

        public boolean success;

        public JsonResponseBean jsonResponseBean;

        public Integer getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(Integer statusCode) {
            this.statusCode = statusCode;
        }

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public String getCaErrorMsg() {
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

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public JsonResponseBean getJsonResponseBean() {
            return jsonResponseBean;
        }

        public void setJsonResponseBean(JsonResponseBean jsonResponseBean) {
            this.jsonResponseBean = jsonResponseBean;
        }

        public class JsonResponseBean {

            public Integer code;

            public Msg msg;
            public String properties;

            public boolean body;

            public Integer getCode() {
                return code;
            }

            public void setCode(Integer code) {
                this.code = code;
            }

            public Msg getMsg() {
                return msg;
            }

            public void setMsg(Msg msg) {
                this.msg = msg;
            }

            public String getProperties() {
                return properties;
            }

            public void setProperties(String properties) {
                this.properties = properties;
            }

            public boolean isBody() {
                return body;
            }

            public void setBody(boolean body) {
                this.body = body;
            }

            public class Msg {

                public Integer status;
                public Integer cid;

                public Integer getStatus() {
                    return status;
                }

                public void setStatus(Integer status) {
                    this.status = status;
                }

                public Integer getCid() {
                    return cid;
                }

                public void setCid(Integer cid) {
                    this.cid = cid;
                }
            }


        }


    }

}
