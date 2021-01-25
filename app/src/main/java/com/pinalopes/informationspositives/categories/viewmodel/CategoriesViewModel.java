package com.pinalopes.informationspositives.categories.viewmodel;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.ViewModel;

import com.pinalopes.informationspositives.categories.model.Category;

public class CategoriesViewModel extends ViewModel {

    protected Category category;
    private int categoryStartColor;
    private int categoryEndColor;

    public CategoriesViewModel(Category category, int categoryStartColor, int categoryEndColor) {
        this.category = category;
        this.categoryStartColor = categoryStartColor;
        this.categoryEndColor = categoryEndColor;
    }

    public Category getCategory() {
        return category;
    }
    
    public String getCategoryName() {
        return this.category.getCategoryName();
    }

    public void setCategoryName(String categoryName) {
        this.category.setCategoryName(categoryName);
    }

    public int getCategoryRes() {
        return this.category.getCategoryRes();
    }

    public void setCategoryRes(int categoryRes) {
        this.category.setCategoryRes(categoryRes);
    }

    public int getCategoryIcon() {
        return this.category.getCategoryIcon();
    }

    public void setCategoryIcon(int categoryIcon) {
        this.category.setCategoryIcon(categoryIcon);
    }

    public int getCategoryStartColor() {
        return categoryStartColor;
    }

    public void setCategoryStartColor(int categoryStartColor) {
        this.categoryStartColor = categoryStartColor;
    }

    public int getCategoryEndColor() {
        return categoryEndColor;
    }

    public void setCategoryEndColor(int categoryEndColor) {
        this.categoryEndColor = categoryEndColor;
    }

    @BindingAdapter({"android:src"})
    public static void setImageViewResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }

    @BindingAdapter({"app:srcCompat"})
    public static void setImageViewResourceApp(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }

    @BindingAdapter({"tools:srcCompat"})
    public static void setImageViewResourceTools(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }
}
