package com.pinalopes.informationspositives.utils;

import android.content.Context;

import com.pinalopes.informationspositives.R;
import com.pinalopes.informationspositives.categories.model.Category;
import com.pinalopes.informationspositives.search.viewmodel.FilterCategoriesViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.pinalopes.informationspositives.Constants.DRAWABLE;
import static com.pinalopes.informationspositives.Constants.PACKAGE_NAME;

public class AdapterUtils {

    private AdapterUtils() {
        throw new AssertionError();
    }

    public static List<FilterCategoriesViewModel> getFilterCategories(Context context) {
        List<FilterCategoriesViewModel> categories = new ArrayList<>();
        String[] categoriesName = context.getResources().getStringArray(R.array.categories_name);
        String[] categoriesRes = context.getResources().getStringArray(R.array.categories_res);
        String[] categoriesIcon = context.getResources().getStringArray(R.array.categories_icon);

        for (int i = 0; i != categoriesName.length ; i ++) {
            String categoryName = categoriesName[i];
            int categoryRes =  context.getResources().getIdentifier(categoriesRes[i], DRAWABLE, PACKAGE_NAME);
            int categoryIcon =  context.getResources().getIdentifier(categoriesIcon[i], DRAWABLE, PACKAGE_NAME);

            FilterCategoriesViewModel filterCategoriesViewModel = new FilterCategoriesViewModel(
                    new Category(categoryName,
                            categoryRes,
                            categoryIcon),
                    false
            );
            categories.add(filterCategoriesViewModel);
        }
        return categories;
    }
}
