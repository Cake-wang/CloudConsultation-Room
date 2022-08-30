package com.aries.template.entity;

import java.util.List;

public class FindMedicineStockEntity {

    public boolean success;
    public String code;
    public String message;
    public DataDTO data;
    public String sign;

    public static class DataDTO {
        public List<DrugListDTO> drugList;

        public static class DrugListDTO {
            public Object drugId;
            public String drugCode;
            public Object gname;
            public Object name;
            public int total;
            public Object drugSpec;
            public Object unit;
            public Object pharmacyCode;
            public Object pharmacyName;
            public Object producerCode;
            public Object producer;
            public int stockAmount;
            public String stockAmountChin;
        }
    }
}
