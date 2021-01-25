package com.pinalopes.informationspositives.categories.model;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pinalopes.informationspositives.R;
import com.pinalopes.informationspositives.categories.viewmodel.CategoriesViewModel;
import com.pinalopes.informationspositives.databinding.CategoriesFragmentBinding;

import java.util.ArrayList;
import java.util.List;

import static com.pinalopes.informationspositives.Constants.BIG_PIC;
import static com.pinalopes.informationspositives.Constants.DRAWABLE;
import static com.pinalopes.informationspositives.Constants.PACKAGE_NAME;

public class CategoriesFragment extends Fragment {

    private CategoriesFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = CategoriesFragmentBinding.inflate(inflater, container, false);
        initCategoriesRecyclerView();
        return binding.getRoot();
    }

    private void initCategoriesRecyclerView() {
        RecyclerView categoriesRecyclerView = binding.categoriesRecyclerView;
        LinearLayoutManager layout = new LinearLayoutManager(getContext());

        categoriesRecyclerView.setLayoutManager(layout);
        CategoriesRecyclerAdapter adapter = new CategoriesRecyclerAdapter(getCategoryDataList());
        categoriesRecyclerView.setAdapter(adapter);
    }
    
    private List<CategoriesViewModel> getCategoryDataList() {
        List<CategoriesViewModel> categoryDataList = new ArrayList<>();
        String[] categoriesName = getResources().getStringArray(R.array.categories_name);
        String[] categoriesRes = getResources().getStringArray(R.array.categories_res);
        String[] categoriesIcon = getResources().getStringArray(R.array.categories_icon);
        int[] categoriesColors = getResources().getIntArray(R.array.categories_colors);
        int colorIndex = 0;

        for (int i = 0; i != categoriesName.length ; i ++) {
            String categoryName = categoriesName[i];
            String categoryRes = categoriesRes[i] + BIG_PIC;
            String categoryIcon = categoriesIcon[i];
            int categoryStartColor = categoriesColors[colorIndex];
            int categoryEndColor = categoriesColors[colorIndex + 1];

            CategoriesViewModel category = new CategoriesViewModel(
                    new Category(categoryName,
                            getResIdFromString(categoryRes),
                            getResIdFromString(categoryIcon)),
                    categoryStartColor,
                    categoryEndColor);
            categoryDataList.add(category);
            colorIndex += 2;
        }
        return categoryDataList;
    }

    private int getResIdFromString(String image) {
        return getResources().getIdentifier(image, DRAWABLE, PACKAGE_NAME);
    }
}