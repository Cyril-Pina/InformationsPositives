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
import com.pinalopes.informationspositives.articles.model.ArticleActivity;
import com.pinalopes.informationspositives.databinding.ArticleCategoryRowBinding;
import com.pinalopes.informationspositives.feed.viewmodel.ArticleRowViewModel;
import com.pinalopes.informationspositives.utils.AdapterUtils;

import java.util.List;

import static com.pinalopes.informationspositives.Constants.ARTICLE_INFORMATION;
import static com.pinalopes.informationspositives.Constants.RECOMMENDED_ARTICLES;

public class ArticleCategoryRecyclerAdapter extends RecyclerView.Adapter<ArticleCategoryRecyclerAdapter.ArticleCategoryViewHolder> {

    private final List<ArticleRowViewModel> articlesCategory;

    public static class ArticleCategoryViewHolder extends RecyclerView.ViewHolder {

        ArticleCategoryRowBinding binding;

        public ArticleCategoryViewHolder(ArticleCategoryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ArticleRowViewModel articleCategoryViewModel) {
            binding.setArticleCategoryViewModel(articleCategoryViewModel);
        }
    }

    public ArticleCategoryRecyclerAdapter(List<ArticleRowViewModel> articlesCategory) {
        this.articlesCategory = articlesCategory;
    }

    @NonNull
    @Override
    public ArticleCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ArticleCategoryRowBinding binding = ArticleCategoryRowBinding.inflate(layoutInflater, parent, false);
        ArticleCategoryViewHolder holder = new ArticleCategoryViewHolder(binding);
        binding.getRoot().setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.item_pressed_anim));
            Intent intentArticle = new Intent(context, ArticleActivity.class);
            ArticleRowViewModel articleRowViewModel = articlesCategory.get(holder.getAdapterPosition());
            intentArticle.putExtra(ARTICLE_INFORMATION, new Gson().toJson(articleRowViewModel));
            intentArticle.putExtra(RECOMMENDED_ARTICLES, AdapterUtils.getRecommendedArticlesFromArticle(articlesCategory, holder.getAdapterPosition()));
            context.startActivity(intentArticle);
        });
        return holder;
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
