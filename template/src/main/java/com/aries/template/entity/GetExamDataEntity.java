package com.aries.template.entity;

/**
 * @Author: AriesHoo on 2018/11/19 14:17
 * @E-Mail: AriesHoo@126.com
 * @Function: 检查新版本实体
 * @Description:
 */
public class GetExamDataEntity {
    public boolean success;
    public String code;
    public String message;
    public DataDTO data;
    public String sign;

    public static class DataDTO {
        public String code;
        public String status;
        public Object message;
        public String data;
        public boolean success;
    }
}
