package com.aries.template.entity;

/**
 * @Author: AriesHoo on 2018/11/19 14:17
 * @E-Mail: AriesHoo@126.com
 * @Function: 检查新版本实体
 * @Description:
 */
public class SearchDoctorListByBusTypeV2RequestEntity {

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

    private String appKey;
    private String tid;
    private String profession;
    private int sortKey;
    private int departmentId;
    private int organId;
    private int start;
    private int limit;
    private int recipeConsultSourceFlag;
    private int packageFlag;
    private String search;
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
    public String getAppKey() {
        return appKey;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }
    public String getTid() {
        return tid;
    }

    public void setSortKey(int sortKey) {
        this.sortKey = sortKey;
    }
    public int getSortKey() {
        return sortKey;
    }

    public void setOrganId(int organId) {
        this.organId = organId;
    }
    public int getOrganId() {
        return organId;
    }

    public void setStart(int start) {
        this.start = start;
    }
    public int getStart() {
        return start;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
    public int getLimit() {
        return limit;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public void setRecipeConsultSourceFlag(int recipeConsultSourceFlag) {
        this.recipeConsultSourceFlag = recipeConsultSourceFlag;
    }
    public int getRecipeConsultSourceFlag() {
        return recipeConsultSourceFlag;
    }

    public void setPackageFlag(int packageFlag) {
        this.packageFlag = packageFlag;
    }
    public int getPackageFlag() {
        return packageFlag;
    }

    public void setSearch(String search) {
        this.search = search;
    }
    public String getSearch() {
        return search;
    }



}
