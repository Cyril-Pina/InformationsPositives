package com.pinalopes.informationspositives.feed.model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.pinalopes.informationspositives.R;
import com.pinalopes.informationspositives.articles.model.ArticleActivity;
import com.pinalopes.informationspositives.databinding.ArticleRowBinding;
import com.pinalopes.informationspositives.databinding.HeaderFeedBinding;
import com.pinalopes.informationspositives.feed.viewmodel.ArticleRowViewModel;
import com.pinalopes.informationspositives.utils.AdapterUtils;

import java.util.List;

import static com.pinalopes.informationspositives.Constants.ARTICLE_INFORMATION;
import static com.pinalopes.informationspositives.Constants.FIRST_INDEX;
import static com.pinalopes.informationspositives.Constants.MIN_SIZE;
import static com.pinalopes.informationspositives.Constants.RECOMMENDED_ARTICLES;
import static com.pinalopes.informationspositives.Constants.TYPE_HEADER;
import static com.pinalopes.informationspositives.Constants.TYPE_ITEM;

public class FeedRecyclerAdapter extends RecyclerView.Adapter<FeedRecyclerAdapter.FeedViewHolder> {

    private final List<ArticleRowViewModel> feedArticleDataList;

    public static class FeedViewHolder extends RecyclerView.ViewHolder {

        ArticleRowBinding articleBinding;
        HeaderFeedBinding headerBinding;

        public FeedViewHolder(ArticleRowBinding binding) {
            super(binding.getRoot());
            this.articleBinding = binding;
        }

        public FeedViewHolder(HeaderFeedBinding binding) {
            super(binding.getRoot());
            this.headerBinding = binding;
        }

        public void bind(ArticleRowViewModel articleRowViewModel) {
            articleBinding.setArticleRowViewModel(articleRowViewModel);
        }
    }

    public FeedRecyclerAdapter(List<ArticleRowViewModel> feedArticleDataList) {
        this.feedArticleDataList = feedArticleDataList;
        if (feedArticleDataList != null && feedArticleDataList.size() > MIN_SIZE
            && feedArticleDataList.get(FIRST_INDEX) != null) {
            this.feedArticleDataList.add(FIRST_INDEX, null); // For header part
        }
    }

    @NonNull
    @Override
    public FeedRecyclerAdapter.FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        if (viewType == TYPE_HEADER) {
            HeaderFeedBinding binding = HeaderFeedBinding.inflate(layoutInflater, parent, false);
            return new FeedRecyclerAdapter.FeedViewHolder(binding);
        } else {
            ArticleRowBinding binding = ArticleRowBinding.inflate(layoutInflater, parent, false);
            FeedViewHolder holder = new FeedViewHolder(binding);
            binding.getRoot().setOnClickListener(v -> {
                v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.item_pressed_anim));
                Intent intentArticle = new Intent(context, ArticleActivity.class);
                ArticleRowViewModel articleRowViewModel = feedArticleDataList.get(holder.getAdapterPosition());
                articleRowViewModel.setCategory(AdapterUtils.getFeedGeneralCategory(binding.getRoot().getContext(),
                        R.style.AppTheme_Dark_NoActionBar));
                intentArticle.putExtra(ARTICLE_INFORMATION, new Gson().toJson(articleRowViewModel));
                intentArticle.putExtra(RECOMMENDED_ARTICLES, AdapterUtils.getRecommendedArticlesFromArticle(feedArticleDataList, holder.getAdapterPosition()));
                context.startActivity(intentArticle);
            });
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        if (position != FIRST_INDEX) {
            holder.bind(feedArticleDataList.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == FIRST_INDEX) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return feedArticleDataList.size();
    }
}
