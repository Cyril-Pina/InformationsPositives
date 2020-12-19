package com.pinalopes.informationspositives.categories.model;

public class Category {
    private String categoryName;
    private int categoryRes;
    private int categoryIcon;

    public Category(String categoryName, int categoryRes, int categoryIcon) {
        this.categoryName = categoryName;
        this.categoryRes = categoryRes;
        this.categoryIcon = categoryIcon;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryRes() {
        return categoryRes;
    }

    public void setCategoryRes(int categoryRes) {
        this.categoryRes = categoryRes;
    }

    public int getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(int categoryIcon) {
        this.categoryIcon = categoryIcon;
    }
}
