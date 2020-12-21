package com.pinalopes.informationspositives.feed.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pinalopes.informationspositives.R;
import com.pinalopes.informationspositives.databinding.ArticleRowBinding;
import com.pinalopes.informationspositives.databinding.HeaderFeedBinding;
import com.pinalopes.informationspositives.feed.viewmodel.ArticleRowViewModel;

import java.util.List;

import static com.pinalopes.informationspositives.Constants.FIRST_INDEX;
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
        this.feedArticleDataList.add(FIRST_INDEX, null);
    }

    @NonNull
    @Override
    public FeedRecyclerAdapter.FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        if (viewType == TYPE_HEADER) {
            HeaderFeedBinding binding = HeaderFeedBinding.inflate(layoutInflater, parent, false);
            return new FeedRecyclerAdapter.FeedViewHolder(binding);
        } else {
            ArticleRowBinding binding = ArticleRowBinding.inflate(layoutInflater, parent, false);
            binding.getRoot().setOnClickListener(v -> v.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.item_pressed_anim)));
            return new FeedRecyclerAdapter.FeedViewHolder(binding);
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
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return feedArticleDataList.size();
    }
}
