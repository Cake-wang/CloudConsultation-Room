package com.aries.template.entity;

import java.util.List;

/**
 * @Author: AriesHoo on 2018/11/19 14:17
 * @E-Mail: AriesHoo@126.com
 * @Function: 检查新版本实体
 * @Description:
 */
public class FindValidOrganProfessionForRevisitResultEntity {

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

            public String msg;
            public String properties;

            public List<OrganProfessionDTO> body;

            public Integer getCode() {
                return code;
            }

            public void setCode(Integer code) {
                this.code = code;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }

            public String getProperties() {
                return properties;
            }

            public void setProperties(String properties) {
                this.properties = properties;
            }

            public List<OrganProfessionDTO> getBody() {
                return body;
            }

            public void setBody(List<OrganProfessionDTO> body) {
                this.body = body;
            }

            public class OrganProfessionDTO {

                public Integer   id;
                public String       code;
                public String         name;
                public String         mCode;
                public Integer        organId;
                public String        professionCode;
                public Integer       orderNum;
                public boolean  leaf;
                public Integer        parent;
                public String         createDt;
                public String         lastModify;
                public Integer        uploadRegulationIf;
                public Integer       clientDisplaysStatus;
                public String          organIdText;
                public String          professionCodeText;

                public Integer getId() {
                    return id;
                }

                public void setId(Integer id) {
                    this.id = id;
                }

                public String getCode() {
                    return code;
                }

                public void setCode(String code) {
                    this.code = code;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getmCode() {
                    return mCode;
                }

                public void setmCode(String mCode) {
                    this.mCode = mCode;
                }

                public Integer getOrganId() {
                    return organId;
                }

                public void setOrganId(Integer organId) {
                    this.organId = organId;
                }

                public String getProfessionCode() {
                    return professionCode;
                }

                public void setProfessionCode(String professionCode) {
                    this.professionCode = professionCode;
                }

                public Integer getOrderNum() {
                    return orderNum;
                }

                public void setOrderNum(Integer orderNum) {
                    this.orderNum = orderNum;
                }

                public boolean isLeaf() {
                    return leaf;
                }

                public void setLeaf(boolean leaf) {
                    this.leaf = leaf;
                }

                public Integer getParent() {
                    return parent;
                }

                public void setParent(Integer parent) {
                    this.parent = parent;
                }

                public String getCreateDt() {
                    return createDt;
                }

                public void setCreateDt(String createDt) {
                    this.createDt = createDt;
                }

                public String getLastModify() {
                    return lastModify;
                }

                public void setLastModify(String lastModify) {
                    this.lastModify = lastModify;
                }

                public Integer getUploadRegulationIf() {
                    return uploadRegulationIf;
                }

                public void setUploadRegulationIf(Integer uploadRegulationIf) {
                    this.uploadRegulationIf = uploadRegulationIf;
                }

                public Integer getClientDisplaysStatus() {
                    return clientDisplaysStatus;
                }

                public void setClientDisplaysStatus(Integer clientDisplaysStatus) {
                    this.clientDisplaysStatus = clientDisplaysStatus;
                }

                public String getOrganIdText() {
                    return organIdText;
                }

                public void setOrganIdText(String organIdText) {
                    this.organIdText = organIdText;
                }

                public String getProfessionCodeText() {
                    return professionCodeText;
                }

                public void setProfessionCodeText(String professionCodeText) {
                    this.professionCodeText = professionCodeText;
                }
            }

        }


    }

}
