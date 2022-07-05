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
    public String code;
    public String message;
    public String sign;

    public boolean success;

    public QueryArrearsSummary data;

    public class QueryArrearsSummary {
        public String wsurl;
        public String pictureUploadUrl;
        public String picturDownloadUrl;
        public String xmppURL;
        public String apiURL;
        public String appkey;
        public String username;
        public String userpwd;
        public String userId;
        public String organId;
    }

}
