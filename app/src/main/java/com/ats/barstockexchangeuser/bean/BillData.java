package com.ats.barstockexchangeuser.bean;

import java.util.List;

/**
 * Created by maxadmin on 28/11/17.
 */

public class BillData {

    private Integer userId;
    private List<BillHeader> billHeader;
    private ErrorMessage errorMessage;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<BillHeader> getBillHeader() {
        return billHeader;
    }

    public void setBillHeader(List<BillHeader> billHeader) {
        this.billHeader = billHeader;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "BillData{" +
                "userId=" + userId +
                ", billHeader=" + billHeader +
                ", errorMessage=" + errorMessage +
                '}';
    }
}
