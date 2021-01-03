package com.pinalopes.informationspositives.articles.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;

import com.pinalopes.informationspositives.R;
import com.pinalopes.informationspositives.articles.viewmodel.RecommendationRowViewModel;
import com.pinalopes.informationspositives.databinding.RecommendationRowBinding;

import java.util.List;

public class RecommendationAdapter extends BaseAdapter {

    private final Context context;
    private final List<RecommendationRowViewModel> recommendations;

    public RecommendationAdapter(Context context, List<RecommendationRowViewModel> recommendations) {
        this.context = context;
        this.recommendations = recommendations;
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
            context.startActivity(new Intent(context, ArticleActivity.class));
        });
        return categoryFilterRow;
    }

    private void updateDataRow(RecommendationRowBinding binding, RecommendationRowViewModel recommendationRowViewModel) {
        binding.setRecommendationRowViewModel(recommendationRowViewModel);
    }
}
