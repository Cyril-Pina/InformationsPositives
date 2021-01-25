package com.pinalopes.informationspositives.categories.model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.pinalopes.informationspositives.R;
import com.pinalopes.informationspositives.categories.viewmodel.CategoriesViewModel;
import com.pinalopes.informationspositives.databinding.CategoryRowBinding;

import java.util.List;

import static com.pinalopes.informationspositives.Constants.CURRENT_CATEGORY;

public class CategoriesRecyclerAdapter extends RecyclerView.Adapter<CategoriesRecyclerAdapter.CategoriesViewHolder> {

    private final List<CategoriesViewModel> categoryDataList;

    public static class CategoriesViewHolder extends RecyclerView.ViewHolder {

        CategoryRowBinding binding;

        public CategoriesViewHolder(CategoryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CategoriesViewModel categoriesViewModel) {
            binding.setCategoriesViewModel(categoriesViewModel);
            /*
                Uncomment for categories icons color

            setCategoryBackground(categoriesViewModel.getCategoryStartColor(), categoriesViewModel.getCategoryEndColor());
             */
        }

        /*private void setCategoryBackground(int startColor, int endColor) {
            GradientDrawable gradientDrawable = new GradientDrawable(
                    GradientDrawable.Orientation.LEFT_RIGHT,
                    new int[] {startColor, endColor});
            gradientDrawable.setShape(GradientDrawable.OVAL);
            binding.categoryIcon.setBackground(gradientDrawable);
        }*/
    }

    public CategoriesRecyclerAdapter(List<CategoriesViewModel> categoryDataList) {
        this.categoryDataList = categoryDataList;
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        CategoryRowBinding binding = CategoryRowBinding.inflate(layoutInflater, parent, false);
        CategoriesViewHolder holder = new CategoriesViewHolder(binding);
        binding.getRoot().setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.item_pressed_alpha_anim));
            Intent intentCategory = new Intent(context, CategoryActivity.class);
            String jsonCategoryObj = new Gson().toJson(categoryDataList.get(holder.getAdapterPosition()).getCategory());
            intentCategory.putExtra(CURRENT_CATEGORY, jsonCategoryObj);
            context.startActivity(intentCategory);
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {
        holder.bind(categoryDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return categoryDataList.size();
    }
}
