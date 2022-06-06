package com.aries.template.entity;

/**
 * @Author: AriesHoo on 2018/11/19 14:17
 * @E-Mail: AriesHoo@126.com
 * @Function: 检查新版本实体
 * @Description:
 */
public class CreateOrderRequestEntity {

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

    public String appKey;
    public String tid;

    public String recipeId;
    public RecipeOrder recipeOrder;
//    public String mobile;
//    public String crdNo;
//    public String amount;
//    public String amountUsable;
//    public String rate;
//    public String amount2;
//    public String amount2Usable;
//    public String       rate2;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public RecipeOrder getRecipeOrder() {
        return recipeOrder;
    }

    public void setRecipeOrder(RecipeOrder recipeOrder) {
        this.recipeOrder = recipeOrder;
    }

    public class RecipeOrder {

        public String addressId;
        public String payway;
        public String payMode;
        public String decoctionFlag;
        public String decoctionId;
        public String gfFeeFlag;
        public String depId;
        public Double expressFee;
        public String gysCode;
        public String sendMethod;
        public Double calculateFee;
        public String pharmacyCode;

        public String getAddressId() {
            return addressId;
        }

        public void setAddressId(String addressId) {
            this.addressId = addressId;
        }

        public String getPayway() {
            return payway;
        }

        public void setPayway(String payway) {
            this.payway = payway;
        }

        public String getPayMode() {
            return payMode;
        }

        public void setPayMode(String payMode) {
            this.payMode = payMode;
        }

        public String getDecoctionFlag() {
            return decoctionFlag;
        }

        public void setDecoctionFlag(String decoctionFlag) {
            this.decoctionFlag = decoctionFlag;
        }

        public String getDecoctionId() {
            return decoctionId;
        }

        public void setDecoctionId(String decoctionId) {
            this.decoctionId = decoctionId;
        }

        public String getGfFeeFlag() {
            return gfFeeFlag;
        }

        public void setGfFeeFlag(String gfFeeFlag) {
            this.gfFeeFlag = gfFeeFlag;
        }

        public String getDepId() {
            return depId;
        }

        public void setDepId(String depId) {
            this.depId = depId;
        }

        public Double getExpressFee() {
            return expressFee;
        }

        public void setExpressFee(Double expressFee) {
            this.expressFee = expressFee;
        }

        public String getGysCode() {
            return gysCode;
        }

        public void setGysCode(String gysCode) {
            this.gysCode = gysCode;
        }

        public String getSendMethod() {
            return sendMethod;
        }

        public void setSendMethod(String sendMethod) {
            this.sendMethod = sendMethod;
        }

        public Double getCalculateFee() {
            return calculateFee;
        }

        public void setCalculateFee(Double calculateFee) {
            this.calculateFee = calculateFee;
        }

        public String getPharmacyCode() {
            return pharmacyCode;
        }

        public void setPharmacyCode(String pharmacyCode) {
            this.pharmacyCode = pharmacyCode;
        }
    }

//    public String getActiveStatus() {
//        return activeStatus;
//    }
//
//    public void setActiveStatus(String activeStatus) {
//        this.activeStatus = activeStatus;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getMobile() {
//        return mobile;
//    }
//
//    public void setMobile(String mobile) {
//        this.mobile = mobile;
//    }
//
//    public String getCrdNo() {
//        return crdNo;
//    }
//
//    public void setCrdNo(String crdNo) {
//        this.crdNo = crdNo;
//    }
//
//    public String getAmount() {
//        return amount;
//    }
//
//    public void setAmount(String amount) {
//        this.amount = amount;
//    }
//
//    public String getAmountUsable() {
//        return amountUsable;
//    }
//
//    public void setAmountUsable(String amountUsable) {
//        this.amountUsable = amountUsable;
//    }
//
//    public String getRate() {
//        return rate;
//    }
//
//    public void setRate(String rate) {
//        this.rate = rate;
//    }
//
//    public String getAmount2() {
//        return amount2;
//    }
//
//    public void setAmount2(String amount2) {
//        this.amount2 = amount2;
//    }
//
//    public String getAmount2Usable() {
//        return amount2Usable;
//    }
//
//    public void setAmount2Usable(String amount2Usable) {
//        this.amount2Usable = amount2Usable;
//    }
//
//    public String getRate2() {
//        return rate2;
//    }
//
//    public void setRate2(String rate2) {
//        this.rate2 = rate2;
//    }
}
