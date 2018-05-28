package com.ats.barstockexchangeuser.bean;

import java.util.List;

/**
 * Created by maxadmin on 25/11/17.
 */

public class ItemListData {

    private List<Item> item ;
    private ErrorMessage errorMessage;

    public List<Item> getItem() {
        return item;
    }

    public void setItem(List<Item> item) {
        this.item = item;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "ItemListData{" +
                "item=" + item +
                ", errorMessage=" + errorMessage +
                '}';
    }
}
