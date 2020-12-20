package com.pinalopes.informationspositives.feed.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pinalopes.informationspositives.R;
import com.pinalopes.informationspositives.databinding.ArticleRowBinding;
import com.pinalopes.informationspositives.feed.viewmodel.ArticleRowViewModel;

import java.util.List;

public class FeedRecyclerAdapter extends RecyclerView.Adapter<FeedRecyclerAdapter.FeedViewHolder> {

    private final List<ArticleRowViewModel> feedArticleDataList;

    public static class FeedViewHolder extends RecyclerView.ViewHolder {

        ArticleRowBinding binding;

        public FeedViewHolder(ArticleRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ArticleRowViewModel articleRowViewModel) {
            binding.setArticleRowViewModel(articleRowViewModel);
        }
    }

    public FeedRecyclerAdapter(List<ArticleRowViewModel> feedArticleDataList) {
        this.feedArticleDataList = feedArticleDataList;
    }

    @NonNull
    @Override
    public FeedRecyclerAdapter.FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        ArticleRowBinding binding = ArticleRowBinding.inflate(layoutInflater, parent, false);
        binding.getRoot().setOnClickListener(v -> v.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.item_pressed_anim)));
        return new FeedRecyclerAdapter.FeedViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        holder.bind(feedArticleDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return feedArticleDataList.size();
    }
}
