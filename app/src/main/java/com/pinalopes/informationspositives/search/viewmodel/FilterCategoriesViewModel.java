package com.pinalopes.informationspositives.search.viewmodel;

import com.pinalopes.informationspositives.categories.model.Category;
import com.pinalopes.informationspositives.categories.viewmodel.CategoriesViewModel;

public class FilterCategoriesViewModel extends CategoriesViewModel {

    private boolean isSelected;

    public FilterCategoriesViewModel(Category category, boolean isSelected) {
        super(category, 0, 0);
        this.category = category;
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void changeSelection() {
        this.isSelected = !this.isSelected;
    }
}
