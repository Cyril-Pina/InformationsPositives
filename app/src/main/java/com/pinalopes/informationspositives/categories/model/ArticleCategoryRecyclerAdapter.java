package com.pinalopes.informationspositives.categories.model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pinalopes.informationspositives.R;
import com.pinalopes.informationspositives.articles.model.ArticleActivity;
import com.pinalopes.informationspositives.categories.viewmodel.ArticleCategoryViewModel;
import com.pinalopes.informationspositives.databinding.ArticleCategoryRowBinding;

import java.util.List;

public class ArticleCategoryRecyclerAdapter extends RecyclerView.Adapter<ArticleCategoryRecyclerAdapter.ArticleCategoryViewHolder> {

    private final List<ArticleCategoryViewModel> articlesCategory;

    public static class ArticleCategoryViewHolder extends RecyclerView.ViewHolder {

        ArticleCategoryRowBinding binding;

        public ArticleCategoryViewHolder(ArticleCategoryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ArticleCategoryViewModel articleCategoryViewModel) {
            binding.setArticleCategoryViewModel(articleCategoryViewModel);
        }
    }

    public ArticleCategoryRecyclerAdapter(List<ArticleCategoryViewModel> articlesCategory) {
        this.articlesCategory = articlesCategory;
    }

    @NonNull
    @Override
    public ArticleCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ArticleCategoryRowBinding binding = ArticleCategoryRowBinding.inflate(layoutInflater, parent, false);
        binding.getRoot().setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.item_pressed_anim));
            Intent intentArticle = new Intent(context, ArticleActivity.class);
            context.startActivity(intentArticle);
        });
        return new ArticleCategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleCategoryViewHolder holder, int position) {
        holder.bind(articlesCategory.get(position));
    }

    @Override
    public int getItemCount() {
        return articlesCategory.size();
    }
}
