package com.aries.template.entity;

public class GetTakeCodeEntity {

    public boolean success;
    public String code;
    public String message;
    public DataDTO data;
    public String sign;

    public static class DataDTO {
        public int busId;
        public String createTime;
        public String updateTime;
        public String takeCode;
    }
}
