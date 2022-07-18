package com.aries.template.entity;

/**
 * 根据机器ID获取机器信息
 * 调用地址：/cloudHospital/machineRelation/findByMachineId
 */
public class MachineEntity {
    public boolean success;
    public String code;
    public String message;
    public DataDTO data;
    public String sign;

    public static class DataDTO {
        public String machineId;
        public String cabinetId;
        public String hospitalNo;
        public String hospitalName;
        public int machineStatus;
        public String machineIp;
        public String createTime;
        public String updateTime;
    }
}
