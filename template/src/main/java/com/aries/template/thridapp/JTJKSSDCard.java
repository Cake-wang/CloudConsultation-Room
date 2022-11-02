package com.aries.template.thridapp;

import com.aries.template.entity.SbkcardResultEntity;
import com.decard.entitys.IDCard;

/**
 * 金投健康 SSDCard 数据
 */
public class JTJKSSDCard {
    private String cardIdentificationCode;
    private String cardClass;
    private String cardCanonicalVersion;
    private String initOrganizationNum;
    private String issuingDate;
    private String expirationDate;
    private String cardNum; //卡号
    private String SSNum;//社会保障号码 身份证
    private String name;//姓名
    private String nameExtend;
    private String sex;
    private String nation;
    private String placeOfBirth;
    private String birthday;

    /**
     * 生成数据格式
     * todo 这里的数据没有填全
     * 第二代 数据生成器
     * 数据格式如下
     * 330100|522725198711013517|AA0368350|330100D156000005456188401C06C0CE|陈东武|008100885086653301015DD82C|3.00|20210318|20310318|330100912171|00012600202203000183|
     * 发卡地区行政区划代码(卡识别码前 6 位)、社会保障号码、卡号、卡识别码、 姓名、卡复位信息(仅取历史字节)、规范版本、发卡日期、卡有效期、终端机 编号、终端设备号。各数据项之间以“|”分割，且最后一个数据项以“|”结尾。
     */
    public static JTJKSSDCard build2D(String data){
        String[] splits = data.split("\\|");
        if (splits.length<=0)
            return null;

        // 创建对象
        JTJKSSDCard card = new JTJKSSDCard();
        if (splits.length>=11){
            // 卡号
            card.cardNum = splits[2];
            // 姓名
            card.name = splits[4].trim();
            // 社会保障号码 身份证
            card.SSNum = splits[1];
            // 卡片识别码
            card.cardIdentificationCode = splits[3];
            // 性别
            card.sex = Integer.valueOf(splits[1].substring(16,17))%2==0?"女":"男";
        }
        return card;
    }

    public static JTJKSSDCard buildiD(IDCard idCard){
//        String[] splits = data.split("\\|");
//        if (splits.length<=0)
//            return null;

        // 创建对象
        JTJKSSDCard card = new JTJKSSDCard();
//        if (splits.length>=11){
            // 卡号
            card.cardNum = "未读卡";
            // 姓名
            card.name = idCard.getName();
            // 社会保障号码 身份证
            card.SSNum = idCard.getId();
            // 卡片识别码
            card.cardIdentificationCode = "";
            // 性别
            card.sex = idCard.getSex();
//        }
        return card;
    }

    public static JTJKSSDCard buildsbkcard(SbkcardResultEntity.SbkcardResult idCard){
//        String[] splits = data.split("\\|");
//        if (splits.length<=0)
//            return null;

        // 创建对象
        JTJKSSDCard card = new JTJKSSDCard();
//        if (splits.length>=11){
        // 卡号
        card.cardNum = "未读卡";
        // 姓名
        card.name = idCard.getName();
        // 社会保障号码 身份证
        card.SSNum = idCard.getIdno();
        // 卡片识别码
        card.cardIdentificationCode = "";
        // 性别
        card.sex = idCard.getSex().equals("2")?"女":"男";

        card.birthday = idCard.getBirthday();
//        }
        return card;
    }

    public void setCardIdentificationCode(String cardIdentificationCode) {
        this.cardIdentificationCode = cardIdentificationCode;
    }

    public void setCardClass(String cardClass) {
        this.cardClass = cardClass;
    }

    public void setCardCanonicalVersion(String cardCanonicalVersion) {
        this.cardCanonicalVersion = cardCanonicalVersion;
    }

    public void setInitOrganizationNum(String initOrganizationNum) {
        this.initOrganizationNum = initOrganizationNum;
    }

    public void setIssuingDate(String issuingDate) {
        this.issuingDate = issuingDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public void setSSNum(String SSNum) {
        this.SSNum = SSNum;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNameExtend(String nameExtend) {
        this.nameExtend = nameExtend;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCardIdentificationCode() {
        return cardIdentificationCode;
    }

    public String getCardClass() {
        return cardClass;
    }

    public String getCardCanonicalVersion() {
        return cardCanonicalVersion;
    }

    public String getInitOrganizationNum() {
        return initOrganizationNum;
    }

    public String getIssuingDate() {
        return issuingDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public String getCardNum() {
        return cardNum;
    }

    public String getSSNum() {
        return SSNum;
    }

    public String getName() {
        return name;
    }

    public String getNameExtend() {
        return nameExtend;
    }

    public String getSex() {
        return sex;
    }

    public String getNation() {
        return nation;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public String getBirthday() {
        return birthday;
    }
}
