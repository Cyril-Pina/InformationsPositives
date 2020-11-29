package com.pinalopes.informationspositives.search.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.lifecycle.MutableLiveData;

import com.pinalopes.informationspositives.databinding.CategoryFilterRowBinding;
import com.pinalopes.informationspositives.search.viewmodel.FilterCategoriesViewModel;

import java.util.List;

import static com.pinalopes.informationspositives.Constants.DRAWABLE;
import static com.pinalopes.informationspositives.Constants.PACKAGE_NAME;

public class FilterCategoriesAdapter extends BaseAdapter {

    private final Context context;
    private final List<CategoryFilter> categories;
    private final MutableLiveData<Integer> clickOnCategoryMutable;

    public FilterCategoriesAdapter(Context context, List<CategoryFilter> categories, MutableLiveData<Integer> clickOnCategoryMutable) {
        this.context = context;
        this.categories = categories;
        this.clickOnCategoryMutable = clickOnCategoryMutable;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder")
        CategoryFilterRowBinding binding = CategoryFilterRowBinding.inflate(((Activity) context).getLayoutInflater());

        int resId = context.getResources().getIdentifier(categories.get(position).getPictureRes(), DRAWABLE, PACKAGE_NAME);
        updateDataRow(binding, position, resId);

        View categoryFilterRow = binding.getRoot();
        categoryFilterRow.setOnClickListener(view -> {
            categories.get(position).changeSelectionState();
            updateDataRow(binding, position, resId);
            clickOnCategoryMutable.setValue(position);
        });
        return categoryFilterRow;
    }

    private void updateDataRow(CategoryFilterRowBinding binding, int position, int resId) {
        binding.setFilterCategoriesViewModel(new FilterCategoriesViewModel(
                categories.get(position).getName(),
                resId,
                categories.get(position).isSelected()
        ));
    }
}