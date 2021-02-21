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

public class FilterCategoriesAdapter extends BaseAdapter {

    protected final Context context;
    protected final List<FilterCategoriesViewModel> categories;
    private final MutableLiveData<Integer> clickOnCategoryMutable;

    public FilterCategoriesAdapter(Context context, List<FilterCategoriesViewModel> categories, MutableLiveData<Integer> clickOnCategoryMutable) {
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
        return position;
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder")
        CategoryFilterRowBinding binding = CategoryFilterRowBinding.inflate(((Activity) context).getLayoutInflater());

        updateDataRow(binding, categories.get(position));

        View categoryFilterRow = binding.getRoot();
        categoryFilterRow.setOnClickListener(view -> {
            categories.get(position).changeSelection();
            updateDataRow(binding, categories.get(position));
            clickOnCategoryMutable.setValue(position);
        });
        return categoryFilterRow;
    }

    protected void updateDataRow(CategoryFilterRowBinding binding, FilterCategoriesViewModel filterCategoriesViewModel) {
        binding.setFilterCategoriesViewModel(filterCategoriesViewModel);
    }
}