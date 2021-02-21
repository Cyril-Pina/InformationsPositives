package com.pinalopes.informationspositives.settings.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import com.pinalopes.informationspositives.databinding.CategoryFilterRowBinding;
import com.pinalopes.informationspositives.search.model.FilterCategoriesAdapter;
import com.pinalopes.informationspositives.search.viewmodel.FilterCategoriesViewModel;

import java.util.List;

import static com.pinalopes.informationspositives.Constants.INDEX_OF_FAILURE;
import static com.pinalopes.informationspositives.Constants.MAX_CATEGORIES_SELECTED;

public class TopCategoriesAdapter extends FilterCategoriesAdapter {

    private final List<Integer> categoriesSelected;
    private final OnCategoriesClickListener listener;

    public TopCategoriesAdapter(Context context, List<Integer> categoriesSelected, List<FilterCategoriesViewModel> categories, OnCategoriesClickListener listener) {
        super(context, categories, null);
        this.categoriesSelected = categoriesSelected;
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder")
        CategoryFilterRowBinding binding = CategoryFilterRowBinding.inflate(((Activity) context).getLayoutInflater());
        FilterCategoriesViewModel category = categories.get(position);
        updateDataRow(binding, categories.get(position));

        if (isCategorySelected(position + 1)) {
            category.changeSelection(true);
            updateCategorySelectionState(binding, category, position);
        }
        View categoryFilterRow = binding.getRoot();
        categoryFilterRow.setOnClickListener(view -> {
            if (categoriesSelected.size() < MAX_CATEGORIES_SELECTED ||
                    categoriesSelected.size() == MAX_CATEGORIES_SELECTED && category.isSelected()) {
                category.changeSelection();
                updateCategorySelectionState(binding, category, position);
            }
        });
        return categoryFilterRow;
    }

    private boolean isCategorySelected(int idCategory) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return categoriesSelected.stream()
                    .anyMatch(t -> t.equals(idCategory));
        } else {
            return categoriesSelected.contains(idCategory);
        }
    }

    private void updateCategorySelectionState(CategoryFilterRowBinding binding, FilterCategoriesViewModel category, int position) {
        updateNbCategoriesSelected(position + 1, category.isSelected());
        updateDataRow(binding, category);
        listener.onCategoriesSelected(position + 1, category.isSelected(), categoriesSelected.size());
    }

    private void updateNbCategoriesSelected(int idCategory, boolean isSelected) {
        if (isSelected && !categoriesSelected.contains(idCategory)) {
            categoriesSelected.add(idCategory);
        } else if (!isSelected && categoriesSelected.contains(idCategory)) {
            int index = this.categoriesSelected.indexOf(idCategory);
            if (index != INDEX_OF_FAILURE) {
                categoriesSelected.remove(index);
            }
        }
    }
}