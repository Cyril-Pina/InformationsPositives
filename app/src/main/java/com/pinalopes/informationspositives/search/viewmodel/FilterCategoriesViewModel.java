package com.pinalopes.informationspositives.search.viewmodel;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.ViewModel;

public class FilterCategoriesViewModel extends ViewModel {

    private String categoryName;
    private int categoryRes;
    private boolean isSelected;

    public FilterCategoriesViewModel(String categoryName, int categoryRes, boolean isSelected) {
        this.categoryName = categoryName;
        this.categoryRes = categoryRes;
        this.isSelected = isSelected;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getCategoryRes() {
        return categoryRes;
    }

    public void setCategoryRes(int categoryRes) {
        this.categoryRes = categoryRes;
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
