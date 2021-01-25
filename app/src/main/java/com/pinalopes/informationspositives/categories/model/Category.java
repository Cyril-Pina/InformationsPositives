package com.pinalopes.informationspositives.categories.model;

import android.content.Context;

import com.pinalopes.informationspositives.R;

import java.util.Arrays;

public class Category {
    private String categoryName;
    private int categoryRes;
    private int categoryIcon;

    public Category(String categoryName, int categoryRes, int categoryIcon) {
        this.categoryName = categoryName;
        this.categoryRes = categoryRes;
        this.categoryIcon = categoryIcon;
    }

    public String getDescFromName(Context context) {
        if (categoryName != null) {
            String[] categoriesName = context.getResources().getStringArray(R.array.categories_name);
            String[] categoriesDesc = context.getResources().getStringArray(R.array.categories_desc);

            int indexCategory = Arrays.asList(categoriesName).indexOf(categoryName);
            if (indexCategory != -1) {
                return categoriesDesc[indexCategory];
            }
        }
        return null;
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
