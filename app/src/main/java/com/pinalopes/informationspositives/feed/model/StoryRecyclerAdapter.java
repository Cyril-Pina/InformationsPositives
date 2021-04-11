package com.pinalopes.informationspositives.feed.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.pinalopes.informationspositives.R;
import com.pinalopes.informationspositives.articles.viewmodel.ArticleViewModel;
import com.pinalopes.informationspositives.feed.viewmodel.StoryViewModel;
import com.pinalopes.informationspositives.story.model.ArticleInStory;
import com.pinalopes.informationspositives.databinding.StoryRowBinding;
import com.pinalopes.informationspositives.utils.AdapterUtils;

import java.util.List;

import static com.pinalopes.informationspositives.Constants.ARTICLES_IN_STORY;
import static com.pinalopes.informationspositives.Constants.CURRENT_STORY_INDEX;
import static com.pinalopes.informationspositives.Constants.MAX_STORY_TITLE_SIZE;

public class StoryRecyclerAdapter extends RecyclerView.Adapter<StoryRecyclerAdapter.StoriesViewHolder> {

    private final List<ArticleViewModel> articlesInStoryViewModel;

    public static class StoriesViewHolder extends RecyclerView.ViewHolder {

        StoryRowBinding binding;

        public StoriesViewHolder(StoryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(StoryViewModel storyViewModel) {
            binding.setStoryViewModel(storyViewModel);
        }
    }

    public StoryRecyclerAdapter(List<ArticleViewModel> articlesInStoryViewModel) {
        this.articlesInStoryViewModel = articlesInStoryViewModel;
    }

    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        StoryRowBinding binding = StoryRowBinding.inflate(layoutInflater, parent, false);
        StoriesViewHolder holder = new StoriesViewHolder(binding);
        binding.getRoot().setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.item_pressed_anim));
            Intent intentArticleStory = new Intent(context, ArticleInStory.class);
            intentArticleStory.putExtra(ARTICLES_IN_STORY, new Gson().toJson(articlesInStoryViewModel));
            intentArticleStory.putExtra(CURRENT_STORY_INDEX, holder.getAdapterPosition());
            context.startActivity(intentArticleStory);
            ((Activity) context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {
        ArticleViewModel articleInStory = articlesInStoryViewModel.get(position);
        StoryViewModel storyViewModel = new StoryViewModel();
        storyViewModel.setTitle(
                AdapterUtils.getSubstringStringFromMaxLength(articleInStory.getTitle(), MAX_STORY_TITLE_SIZE));
        storyViewModel.setImageUrl(articleInStory.getImageUrl());
        holder.bind(storyViewModel);
    }

    @Override
    public int getItemCount() {
        return articlesInStoryViewModel.size();
    }
}
