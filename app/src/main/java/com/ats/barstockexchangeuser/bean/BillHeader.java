package com.ats.barstockexchangeuser.bean;

import java.util.List;

/**
 * Created by maxadmin on 28/11/17.
 */

public class BillHeader {

    private Bill bill;
    private List<BillDetail> billDetails;

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public List<BillDetail> getBillDetails() {
        return billDetails;
    }

    public void setBillDetails(List<BillDetail> billDetails) {
        this.billDetails = billDetails;
    }

    @Override
    public String toString() {
        return "BillHeader{" +
                "bill=" + bill +
                ", billDetails=" + billDetails +
                '}';
    }
}
