/**
 * Copyright (c) 2020.year. Shanghai Zhenhui Information Technology Co,. ltd. All rights are reserved.
 *
 * @project Helios
 * @author pan.jiang@huilianyi.com
 * @Date 2021-01-07 - 20:00
 * @description: eleme 报备接口
 */

package com.hand.basicObject.supplierObject.eleme;

public class ElemeOrderReportRequest {


    /**
     * orderNo : 1202048151994295705        第三方业务编号
     * orderNo98 : 781653488299932848135546 用户编码
     * reportInfo :                         报备信息
     * {
     * "mealNumber":1,                      消费人数
     * "mealReason":"123123123",            消费原因
     * "mealReceipt":"https://alta1-oss-1.oss-cn-shanghai.aliyuncs.com/a/f1/f04508827f39ce9125033d0224e99gif.gif",消费小票
     * "mealDistance":                      消费距离(单位厘米，用户进入的地点和餐厅之间的距离)
     * }
     * bno :                                第三方业务编号
     * uno : 232731066730761835             用户编码
     */

    private String orderNo;
    private String orderNo98;
    private ReportInfo reportInfo;
    private String bno;
    private String uno;
    private Integer orderType;

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderNo98() {
        return orderNo98;
    }

    public void setOrderNo98(String orderNo98) {
        this.orderNo98 = orderNo98;
    }

    public ReportInfo getReportInfo() {
        return reportInfo;
    }

    public void setReportInfo(ReportInfo reportInfo) {
        this.reportInfo = reportInfo;
    }

    public String getBno() {
        return bno;
    }

    public void setBno(String bno) {
        this.bno = bno;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public static class ReportInfo {
        /**
         * mealNumber : 1
         * mealReason : 123123123
         * mealReceipt : https://alta1-oss-1.oss-cn-shanghai.aliyuncs.com/a/f1/f04508827f39ce9125033d0224e99gif.gif
         * mealDistance : null
         */

        private int mealNumber;
        private String mealReason;
        private String mealReceipt;
        private Long mealDistance;
        private String invoice;
        private String photograph;

        public String getInvoice() {
            return invoice;
        }

        public void setInvoice(String invoice) {
            this.invoice = invoice;
        }

        public String getPhotograph() {
            return photograph;
        }

        public void setPhotograph(String photograph) {
            this.photograph = photograph;
        }

        public int getMealNumber() {
            return mealNumber;
        }

        public void setMealNumber(int mealNumber) {
            this.mealNumber = mealNumber;
        }

        public String getMealReason() {
            return mealReason;
        }

        public void setMealReason(String mealReason) {
            this.mealReason = mealReason;
        }

        public String getMealReceipt() {
            return mealReceipt;
        }

        public void setMealReceipt(String mealReceipt) {
            this.mealReceipt = mealReceipt;
        }

        public Long getMealDistance() {
            return mealDistance;
        }

        public void setMealDistance(Long mealDistance) {
            this.mealDistance = mealDistance;
        }

    }
}
