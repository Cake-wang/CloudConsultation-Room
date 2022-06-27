package com.aries.template.entity;

/**
 * 获取手机注册验证码
 * @Author: AriesHoo on 2018/11/19 14:17
 * @E-Mail: AriesHoo@126.com
 * @Function: 检查新版本实体
 * @Description:
 */
public class AuthCodeResultEntity {

    public Boolean success;
    public String code;
    public String message;
    public DataBean data;
    public String sign;

    public static class DataBean {
        public String authCodeId;
    }
}
