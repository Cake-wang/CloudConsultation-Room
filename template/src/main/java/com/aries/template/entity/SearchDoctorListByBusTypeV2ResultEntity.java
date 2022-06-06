package com.aries.template.entity;

import java.util.List;

/**
 * @Author: AriesHoo on 2018/11/19 14:17
 * @E-Mail: AriesHoo@126.com
 * @Function: 检查新版本实体
 * @Description:
 */
public class SearchDoctorListByBusTypeV2ResultEntity {

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

                private int start;
                private List<DocList> docList;
                public void setStart(int start) {
                    this.start = start;
                }
                public int getStart() {
                    return start;
                }

                public void setDocList(List<DocList> docList) {
                    this.docList = docList;
                }
                public List<DocList> getDocList() {
                    return docList;
                }
/**
 * Copyright 2022 bejson.com
 */


                /**
                 * Auto-generated: 2022-05-26 14:2:49
                 *
                 * @author bejson.com (i@bejson.com)
                 * @website http://www.bejson.com/java2pojo/
                 */
                public class DocList {

                    private String departmentName;
                    private Doctor doctor;

                    public void setDepartmentName(String departmentName) {
                        this.departmentName = departmentName;
                    }

                    public String getDepartmentName() {
                        return departmentName;
                    }

                    public void setDoctor(Doctor doctor) {
                        this.doctor = doctor;
                    }

                    public Doctor getDoctor() {
                        return doctor;
                    }

                    public class Doctor {

                        private String loginId;
                        private long doctorId;
                        private String name;
                        private String gender;
                        private String profession;
                        private long organProfession;
                        private String proTitle;
                        private boolean teams;
                        private long organ;
                        private int status;
                        private boolean expert;
                        private int userType;
                        private int haveAppoint;
                        private int testPersonnel;
                        private int chief;
                        private boolean virtualDoctor;
                        private int source;
                        private int groupType;
                        private int generalDoctor;
                        private ExtendParam extendParam;
                        private int easemobStatus;
                        private int doctorSortNum;
                        private long currentOrgan;
                        private int cloudAppointSourceFlag;
                        private boolean canReport;
                        private boolean hasReport;
                        private String genderText;
                        private String professionText;
                        private String organProfessionText;
                        private String proTitleText;
                        private String organText;
                        private String departmentText;
                        private String sourceText;
                        private String leaderText;
                        private String groupTypeText;
                        private String appointSourceFlagText;
                        private String currentOrganText;
                        private String cloudAppointSourceFlagText;
                        private String gradeText;
                        private String groupModeText;
                        private String userTypeText;
                        private String statusText;

                        public void setLoginId(String loginId) {
                            this.loginId = loginId;
                        }

                        public String getLoginId() {
                            return loginId;
                        }

                        public void setDoctorId(long doctorId) {
                            this.doctorId = doctorId;
                        }

                        public long getDoctorId() {
                            return doctorId;
                        }

                        public void setName(String name) {
                            this.name = name;
                        }

                        public String getName() {
                            return name;
                        }

                        public void setGender(String gender) {
                            this.gender = gender;
                        }

                        public String getGender() {
                            return gender;
                        }

                        public void setProfession(String profession) {
                            this.profession = profession;
                        }

                        public String getProfession() {
                            return profession;
                        }

                        public void setOrganProfession(long organProfession) {
                            this.organProfession = organProfession;
                        }

                        public long getOrganProfession() {
                            return organProfession;
                        }

                        public void setProTitle(String proTitle) {
                            this.proTitle = proTitle;
                        }

                        public String getProTitle() {
                            return proTitle;
                        }

                        public void setTeams(boolean teams) {
                            this.teams = teams;
                        }

                        public boolean getTeams() {
                            return teams;
                        }

                        public void setOrgan(long organ) {
                            this.organ = organ;
                        }

                        public long getOrgan() {
                            return organ;
                        }

                        public void setStatus(int status) {
                            this.status = status;
                        }

                        public int getStatus() {
                            return status;
                        }

                        public void setExpert(boolean expert) {
                            this.expert = expert;
                        }

                        public boolean getExpert() {
                            return expert;
                        }

                        public void setUserType(int userType) {
                            this.userType = userType;
                        }

                        public int getUserType() {
                            return userType;
                        }

                        public void setHaveAppoint(int haveAppoint) {
                            this.haveAppoint = haveAppoint;
                        }

                        public int getHaveAppoint() {
                            return haveAppoint;
                        }

                        public void setTestPersonnel(int testPersonnel) {
                            this.testPersonnel = testPersonnel;
                        }

                        public int getTestPersonnel() {
                            return testPersonnel;
                        }

                        public void setChief(int chief) {
                            this.chief = chief;
                        }

                        public int getChief() {
                            return chief;
                        }

                        public void setVirtualDoctor(boolean virtualDoctor) {
                            this.virtualDoctor = virtualDoctor;
                        }

                        public boolean getVirtualDoctor() {
                            return virtualDoctor;
                        }

                        public void setSource(int source) {
                            this.source = source;
                        }

                        public int getSource() {
                            return source;
                        }

                        public void setGroupType(int groupType) {
                            this.groupType = groupType;
                        }

                        public int getGroupType() {
                            return groupType;
                        }

                        public void setGeneralDoctor(int generalDoctor) {
                            this.generalDoctor = generalDoctor;
                        }

                        public int getGeneralDoctor() {
                            return generalDoctor;
                        }

                        public void setExtendParam(ExtendParam extendParam) {
                            this.extendParam = extendParam;
                        }

                        public ExtendParam getExtendParam() {
                            return extendParam;
                        }

                        public void setEasemobStatus(int easemobStatus) {
                            this.easemobStatus = easemobStatus;
                        }

                        public int getEasemobStatus() {
                            return easemobStatus;
                        }

                        public void setDoctorSortNum(int doctorSortNum) {
                            this.doctorSortNum = doctorSortNum;
                        }

                        public int getDoctorSortNum() {
                            return doctorSortNum;
                        }

                        public void setCurrentOrgan(long currentOrgan) {
                            this.currentOrgan = currentOrgan;
                        }

                        public long getCurrentOrgan() {
                            return currentOrgan;
                        }

                        public void setCloudAppointSourceFlag(int cloudAppointSourceFlag) {
                            this.cloudAppointSourceFlag = cloudAppointSourceFlag;
                        }

                        public int getCloudAppointSourceFlag() {
                            return cloudAppointSourceFlag;
                        }

                        public void setCanReport(boolean canReport) {
                            this.canReport = canReport;
                        }

                        public boolean getCanReport() {
                            return canReport;
                        }

                        public void setHasReport(boolean hasReport) {
                            this.hasReport = hasReport;
                        }

                        public boolean getHasReport() {
                            return hasReport;
                        }

                        public void setGenderText(String genderText) {
                            this.genderText = genderText;
                        }

                        public String getGenderText() {
                            return genderText;
                        }

                        public void setProfessionText(String professionText) {
                            this.professionText = professionText;
                        }

                        public String getProfessionText() {
                            return professionText;
                        }

                        public void setOrganProfessionText(String organProfessionText) {
                            this.organProfessionText = organProfessionText;
                        }

                        public String getOrganProfessionText() {
                            return organProfessionText;
                        }

                        public void setProTitleText(String proTitleText) {
                            this.proTitleText = proTitleText;
                        }

                        public String getProTitleText() {
                            return proTitleText;
                        }

                        public void setOrganText(String organText) {
                            this.organText = organText;
                        }

                        public String getOrganText() {
                            return organText;
                        }

                        public void setDepartmentText(String departmentText) {
                            this.departmentText = departmentText;
                        }

                        public String getDepartmentText() {
                            return departmentText;
                        }

                        public void setSourceText(String sourceText) {
                            this.sourceText = sourceText;
                        }

                        public String getSourceText() {
                            return sourceText;
                        }

                        public void setLeaderText(String leaderText) {
                            this.leaderText = leaderText;
                        }

                        public String getLeaderText() {
                            return leaderText;
                        }

                        public void setGroupTypeText(String groupTypeText) {
                            this.groupTypeText = groupTypeText;
                        }

                        public String getGroupTypeText() {
                            return groupTypeText;
                        }

                        public void setAppointSourceFlagText(String appointSourceFlagText) {
                            this.appointSourceFlagText = appointSourceFlagText;
                        }

                        public String getAppointSourceFlagText() {
                            return appointSourceFlagText;
                        }

                        public void setCurrentOrganText(String currentOrganText) {
                            this.currentOrganText = currentOrganText;
                        }

                        public String getCurrentOrganText() {
                            return currentOrganText;
                        }

                        public void setCloudAppointSourceFlagText(String cloudAppointSourceFlagText) {
                            this.cloudAppointSourceFlagText = cloudAppointSourceFlagText;
                        }

                        public String getCloudAppointSourceFlagText() {
                            return cloudAppointSourceFlagText;
                        }

                        public void setGradeText(String gradeText) {
                            this.gradeText = gradeText;
                        }

                        public String getGradeText() {
                            return gradeText;
                        }

                        public void setGroupModeText(String groupModeText) {
                            this.groupModeText = groupModeText;
                        }

                        public String getGroupModeText() {
                            return groupModeText;
                        }

                        public void setUserTypeText(String userTypeText) {
                            this.userTypeText = userTypeText;
                        }

                        public String getUserTypeText() {
                            return userTypeText;
                        }

                        public void setStatusText(String statusText) {
                            this.statusText = statusText;
                        }

                        public String getStatusText() {
                            return statusText;
                        }

                        public class ExtendParam {

                        }

                    }

                }

            }


        }

    }

}