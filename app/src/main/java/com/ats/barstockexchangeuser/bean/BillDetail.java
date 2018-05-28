package com.ats.barstockexchangeuser.bean;

/**
 * Created by maxadmin on 28/11/17.
 */

public class BillDetail {

    private Integer billDetailsId;
    private Integer billId;
    private Integer orderId;
    private Integer delStatus;
    private Integer itemId;
    private String itemName;
    private Integer quantity;
    private float rate;
    private float sgst;
    private float cgst;
    private float total;
    private float taxableAmt;
    private float totalTax;

    public Integer getBillDetailsId() {
        return billDetailsId;
    }

    public void setBillDetailsId(Integer billDetailsId) {
        this.billDetailsId = billDetailsId;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public float getSgst() {
        return sgst;
    }

    public void setSgst(float sgst) {
        this.sgst = sgst;
    }

    public float getCgst() {
        return cgst;
    }

    public void setCgst(float cgst) {
        this.cgst = cgst;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getTaxableAmt() {
        return taxableAmt;
    }

    public void setTaxableAmt(float taxableAmt) {
        this.taxableAmt = taxableAmt;
    }

    public float getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(float totalTax) {
        this.totalTax = totalTax;
    }

    @Override
    public String toString() {
        return "BillDetail{" +
                "billDetailsId=" + billDetailsId +
                ", billId=" + billId +
                ", orderId=" + orderId +
                ", delStatus=" + delStatus +
                ", itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", quantity=" + quantity +
                ", rate=" + rate +
                ", sgst=" + sgst +
                ", cgst=" + cgst +
                ", total=" + total +
                ", taxableAmt=" + taxableAmt +
                ", totalTax=" + totalTax +
                '}';
    }
}
