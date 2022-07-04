package com.aries.template.entity;

/**
 * 根据机器ID获取机器信息
 * 调用地址：/cloudHospital/machineRelation/findByMachineId
 */
public class MachineEntity {

    public String code;
    public boolean success;
    public String message;
    public String sign;

    public QueryArrearsSummary data;
    public class QueryArrearsSummary {
        public String machineId;
        public String cabinetId;
        public String hospitalNo;
        public String hospitalName;
        public Integer machineStatus;
    }
}
