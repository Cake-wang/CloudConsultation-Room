package com.aries.template.entity;

import java.util.List;

/******
 *
 *
 * @author  ::: louis luo
 * Date ::: 2022/6/9 4:42 PM
 *
 */
public class FindValidOrganProfessionForRevisitBean {

    public Integer code;
    public List<BodyBean> body;

    public static class BodyBean {
        public Integer id;
        public String code;
        public String name;
        public String mCode;
        public Integer organId;
        public String professionCode;
        public Integer orderNum;
        public Boolean leaf;
        public Integer parent;
        public String createDt;
        public String lastModify;
        public String organIdText;
        public String professionCodeText;
    }
}
