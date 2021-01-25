package com.pinalopes.informationspositives.categories.viewmodel;

import androidx.lifecycle.ViewModel;

import com.pinalopes.informationspositives.categories.model.Category;

public class CategoryViewModel extends ViewModel {

    private Category category;
    private String desc;

    public CategoryViewModel(Category category, String desc) {
        this.category = category;
        this.desc = desc;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
