package com.pinalopes.informationspositives.search.model;

public class Filters {

    private String beginningDate;
    private String endingDate;
    private Boolean[] categories;

    public Filters() {
        this.beginningDate = null;
        this.endingDate = null;
        this.categories = new Boolean[] { false, false, false, false, false, false, false };
    }

    public String getBeginningDate() {
        return beginningDate;
    }

    public void setBeginningDate(String beginningDate) {
        this.beginningDate = beginningDate;
    }

    public String getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(String endingDate) {
        this.endingDate = endingDate;
    }

    public Boolean[] getCategories() {
        return categories;
    }

    public void setCategoriesValue(Boolean[] categories) {
        this.categories = categories;
    }
}
