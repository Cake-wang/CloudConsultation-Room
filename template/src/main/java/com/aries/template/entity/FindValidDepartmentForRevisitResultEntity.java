package com.aries.template.entity;

import java.util.List;

/**
 * @Author: AriesHoo on 2018/11/19 14:17
 * @E-Mail: AriesHoo@126.com
 * @Function: 检查新版本实体
 * @Description:
 */
public class FindValidDepartmentForRevisitResultEntity {

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

                /**
                 * Copyright 2022 bejson.com
                 */


                /**
                 * Auto-generated: 2022-05-26 10:2:49
                 *
                 * @author bejson.com (i@bejson.com)
                 * @website http://www.bejson.com/java2pojo/
                 */


                    private int deptId;
                    private int organId;
                    private String code;
                    private String name;
                    private String professionCode;
                    private int parentDept;
                    private int orderNum;
                    private int clinicEnable;
                    private int inpatientEnable;
                    private String remarks;
                    private int status;
                    private int organProfession;
                    private int frontShowStatus;
                    private boolean intClinicFlag;
                    private String deptTips;
                    private int autoFollowUp;
                    private String precisionForm;
                    private int diagnoseEnable;
                    private int doctorShowLogic;
                    private int uploadRegulationIf;
                    private long directorDoctor;
                    private String doctorShowLogicText;
                    private String directorDoctorText;
                    private String statusText;
                    private String organProfessionText;
                    private String organIdText;
                    private String professionCodeText;
                    private String parentOrganProfessionText;

                    public void setDeptId(int deptId) {
                        this.deptId = deptId;
                    }

                    public int getDeptId() {
                        return deptId;
                    }

                    public void setOrganId(int organId) {
                        this.organId = organId;
                    }

                    public int getOrganId() {
                        return organId;
                    }

                    public void setCode(String code) {
                        this.code = code;
                    }

                    public String getCode() {
                        return code;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setProfessionCode(String professionCode) {
                        this.professionCode = professionCode;
                    }

                    public String getProfessionCode() {
                        return professionCode;
                    }

                    public void setParentDept(int parentDept) {
                        this.parentDept = parentDept;
                    }

                    public int getParentDept() {
                        return parentDept;
                    }

                    public void setOrderNum(int orderNum) {
                        this.orderNum = orderNum;
                    }

                    public int getOrderNum() {
                        return orderNum;
                    }

                    public void setClinicEnable(int clinicEnable) {
                        this.clinicEnable = clinicEnable;
                    }

                    public int getClinicEnable() {
                        return clinicEnable;
                    }

                    public void setInpatientEnable(int inpatientEnable) {
                        this.inpatientEnable = inpatientEnable;
                    }

                    public int getInpatientEnable() {
                        return inpatientEnable;
                    }

                    public void setRemarks(String remarks) {
                        this.remarks = remarks;
                    }

                    public String getRemarks() {
                        return remarks;
                    }

                    public void setStatus(int status) {
                        this.status = status;
                    }

                    public int getStatus() {
                        return status;
                    }

                    public void setOrganProfession(int organProfession) {
                        this.organProfession = organProfession;
                    }

                    public int getOrganProfession() {
                        return organProfession;
                    }

                    public void setFrontShowStatus(int frontShowStatus) {
                        this.frontShowStatus = frontShowStatus;
                    }

                    public int getFrontShowStatus() {
                        return frontShowStatus;
                    }

                    public void setIntClinicFlag(boolean intClinicFlag) {
                        this.intClinicFlag = intClinicFlag;
                    }

                    public boolean getIntClinicFlag() {
                        return intClinicFlag;
                    }

                    public void setDeptTips(String deptTips) {
                        this.deptTips = deptTips;
                    }

                    public String getDeptTips() {
                        return deptTips;
                    }

                    public void setAutoFollowUp(int autoFollowUp) {
                        this.autoFollowUp = autoFollowUp;
                    }

                    public int getAutoFollowUp() {
                        return autoFollowUp;
                    }

                    public void setPrecisionForm(String precisionForm) {
                        this.precisionForm = precisionForm;
                    }

                    public String getPrecisionForm() {
                        return precisionForm;
                    }

                    public void setDiagnoseEnable(int diagnoseEnable) {
                        this.diagnoseEnable = diagnoseEnable;
                    }

                    public int getDiagnoseEnable() {
                        return diagnoseEnable;
                    }

                    public void setDoctorShowLogic(int doctorShowLogic) {
                        this.doctorShowLogic = doctorShowLogic;
                    }

                    public int getDoctorShowLogic() {
                        return doctorShowLogic;
                    }

                    public void setUploadRegulationIf(int uploadRegulationIf) {
                        this.uploadRegulationIf = uploadRegulationIf;
                    }

                    public int getUploadRegulationIf() {
                        return uploadRegulationIf;
                    }

                    public void setDirectorDoctor(long directorDoctor) {
                        this.directorDoctor = directorDoctor;
                    }

                    public long getDirectorDoctor() {
                        return directorDoctor;
                    }

                    public void setDoctorShowLogicText(String doctorShowLogicText) {
                        this.doctorShowLogicText = doctorShowLogicText;
                    }

                    public String getDoctorShowLogicText() {
                        return doctorShowLogicText;
                    }

                    public void setDirectorDoctorText(String directorDoctorText) {
                        this.directorDoctorText = directorDoctorText;
                    }

                    public String getDirectorDoctorText() {
                        return directorDoctorText;
                    }

                    public void setStatusText(String statusText) {
                        this.statusText = statusText;
                    }

                    public String getStatusText() {
                        return statusText;
                    }

                    public void setOrganProfessionText(String organProfessionText) {
                        this.organProfessionText = organProfessionText;
                    }

                    public String getOrganProfessionText() {
                        return organProfessionText;
                    }

                    public void setOrganIdText(String organIdText) {
                        this.organIdText = organIdText;
                    }

                    public String getOrganIdText() {
                        return organIdText;
                    }

                    public void setProfessionCodeText(String professionCodeText) {
                        this.professionCodeText = professionCodeText;
                    }

                    public String getProfessionCodeText() {
                        return professionCodeText;
                    }

                    public void setParentOrganProfessionText(String parentOrganProfessionText) {
                        this.parentOrganProfessionText = parentOrganProfessionText;
                    }

                    public String getParentOrganProfessionText() {
                        return parentOrganProfessionText;
                    }

                }




        }

    }

}