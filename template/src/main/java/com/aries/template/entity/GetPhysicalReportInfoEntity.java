package com.aries.template.entity;

import java.util.List;

/**
 * @Author: AriesHoo on 2018/11/19 14:17
 * @E-Mail: AriesHoo@126.com
 * @Function: 检查新版本实体
 * @Description:
 */
public class GetPhysicalReportInfoEntity {

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

    public boolean success;

    public List<QueryArrearsSummary> data;

    public String sign;

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

    public List<QueryArrearsSummary> getData() {
        return data;
    }

    public void setData(List<QueryArrearsSummary> data) {
        this.data = data;
    }

    public class QueryArrearsSummary {
        public String reportNo;
        public String reportTime;
        public String name;
        public String idNo;
        public String mobile;
        public String reportUrl;
        public Double height; //身高
        public Double weight; //体重
        public Double temperature;//体温
        public Integer sys;//收缩压
        public Integer dia;//舒张压
        public Integer pr;//脉率
        public Integer mea;//平均压
        public String conclusion;//心电结论

        public String getReportNo() {
            return reportNo;
        }

        public void setReportNo(String reportNo) {
            this.reportNo = reportNo;
        }

        public String getReportTime() {
            return reportTime;
        }

        public void setReportTime(String reportTime) {
            this.reportTime = reportTime;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIdNo() {
            return idNo;
        }

        public void setIdNo(String idNo) {
            this.idNo = idNo;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getReportUrl() {
            return reportUrl;
        }

        public void setReportUrl(String reportUrl) {
            this.reportUrl = reportUrl;
        }

        public Double getHeight() {
            return height;
        }

        public void setHeight(Double height) {
            this.height = height;
        }

        public Double getWeight() {
            return weight;
        }

        public void setWeight(Double weight) {
            this.weight = weight;
        }

        public Double getTemperature() {
            return temperature;
        }

        public void setTemperature(Double temperature) {
            this.temperature = temperature;
        }

        public Integer getSys() {
            return sys;
        }

        public void setSys(Integer sys) {
            this.sys = sys;
        }

        public Integer getDia() {
            return dia;
        }

        public void setDia(Integer dia) {
            this.dia = dia;
        }

        public Integer getPr() {
            return pr;
        }

        public void setPr(Integer pr) {
            this.pr = pr;
        }

        public Integer getMea() {
            return mea;
        }

        public void setMea(Integer mea) {
            this.mea = mea;
        }

        public String getConclusion() {
            return conclusion;
        }

        public void setConclusion(String conclusion) {
            this.conclusion = conclusion;
        }
    }

}
