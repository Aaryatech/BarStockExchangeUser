package com.ats.barstockexchangeuser.bean;

/**
 * Created by maxadmin on 28/11/17.
 */

public class Bill {

    private Integer billId;
    private String billDate;
    private Integer delStatus;
    private Integer userId;
    private Integer enterBy;
    private Integer billClose;
    private Integer discount;
    private Double grandTotal;
    private Double payableAmt;
    private Integer tableNo;

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public Integer getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getEnterBy() {
        return enterBy;
    }

    public void setEnterBy(Integer enterBy) {
        this.enterBy = enterBy;
    }

    public Integer getBillClose() {
        return billClose;
    }

    public void setBillClose(Integer billClose) {
        this.billClose = billClose;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(Double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public Double getPayableAmt() {
        return payableAmt;
    }

    public void setPayableAmt(Double payableAmt) {
        this.payableAmt = payableAmt;
    }

    public Integer getTableNo() {
        return tableNo;
    }

    public void setTableNo(Integer tableNo) {
        this.tableNo = tableNo;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "billId=" + billId +
                ", billDate='" + billDate + '\'' +
                ", delStatus=" + delStatus +
                ", userId=" + userId +
                ", enterBy=" + enterBy +
                ", billClose=" + billClose +
                ", discount=" + discount +
                ", grandTotal=" + grandTotal +
                ", payableAmt=" + payableAmt +
                ", tableNo=" + tableNo +
                '}';
    }
}

