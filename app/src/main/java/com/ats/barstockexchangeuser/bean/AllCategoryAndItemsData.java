package com.ats.barstockexchangeuser.bean;

import java.util.List;

/**
 * Created by maxadmin on 25/11/17.
 */

public class AllCategoryAndItemsData {

    private List<CategoryItemList> categoryItemList ;
    private ErrorMessage errorMessage;
    private Settings settings;

    public List<CategoryItemList> getCategoryItemList() {
        return categoryItemList;
    }

    public void setCategoryItemList(List<CategoryItemList> categoryItemList) {
        this.categoryItemList = categoryItemList;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }


}
