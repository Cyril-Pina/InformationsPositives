package com.pinalopes.informationspositives.articles.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;

import com.google.gson.Gson;
import com.pinalopes.informationspositives.R;
import com.pinalopes.informationspositives.databinding.RecommendationRowBinding;
import com.pinalopes.informationspositives.feed.viewmodel.ArticleRowViewModel;
import com.pinalopes.informationspositives.utils.AdapterUtils;

import java.util.List;

import static com.pinalopes.informationspositives.Constants.ARTICLE_INFORMATION;
import static com.pinalopes.informationspositives.Constants.RECOMMENDED_ARTICLES;

public class RecommendationAdapter extends BaseAdapter {

    private final Context context;
    private final List<ArticleRowViewModel> recommendations;
    private final ArticleRowViewModel currentArticle;

    public RecommendationAdapter(Context context, List<ArticleRowViewModel> recommendations, ArticleRowViewModel currentArticle) {
        this.context = context;
        this.recommendations = recommendations;
        this.currentArticle = currentArticle;
    }

    @Override
    public int getCount() {
        return recommendations.size();
    }

    @Override
    public Object getItem(int position) {
        return recommendations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder")
        RecommendationRowBinding binding = RecommendationRowBinding.inflate(((Activity) context).getLayoutInflater());
        updateDataRow(binding, recommendations.get(position));

        View categoryFilterRow = binding.getRoot();
        categoryFilterRow.setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.item_pressed_anim));
            Intent intentArticle = new Intent(context, ArticleActivity.class);
            ArticleRowViewModel articleRowViewModel = recommendations.remove(position);
            recommendations.add(currentArticle);
            intentArticle.putExtra(ARTICLE_INFORMATION, new Gson().toJson(articleRowViewModel));
            intentArticle.putExtra(RECOMMENDED_ARTICLES,
                    AdapterUtils.getRecommendedArticlesFromArticle(recommendations, position));
            context.startActivity(intentArticle);
        });
        return categoryFilterRow;
    }

    private void updateDataRow(RecommendationRowBinding binding, ArticleRowViewModel recommendationRowViewModel) {
        if (recommendationRowViewModel != null) {
            recommendationRowViewModel.setCategory(AdapterUtils.getFeedGeneralCategory(binding.getRoot().getContext(),
                    R.style.AppTheme_Dark_NoActionBar));
        }
        binding.setRecommendationRowViewModel(recommendationRowViewModel);
    }
}
