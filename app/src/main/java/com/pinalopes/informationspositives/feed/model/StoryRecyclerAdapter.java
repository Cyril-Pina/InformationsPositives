package com.pinalopes.informationspositives.feed.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pinalopes.informationspositives.R;
import com.pinalopes.informationspositives.story.model.ArticleInStory;
import com.pinalopes.informationspositives.databinding.StoryRowBinding;

import java.util.List;

public class StoryRecyclerAdapter extends RecyclerView.Adapter<StoryRecyclerAdapter.StoriesViewHolder> {

    private final List<String> storyArticleDataList;

    public static class StoriesViewHolder extends RecyclerView.ViewHolder {

        StoryRowBinding binding;

        public StoriesViewHolder(StoryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public StoryRecyclerAdapter(List<String> storyArticleDataList) {
        this.storyArticleDataList = storyArticleDataList;
    }

    @NonNull
    @Override
    public StoryRecyclerAdapter.StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        StoryRowBinding binding = StoryRowBinding.inflate(layoutInflater, parent, false);
        binding.getRoot().setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.item_pressed_anim));
            Intent intentArticleStory = new Intent(context, ArticleInStory.class);
            context.startActivity(intentArticleStory);
            ((Activity) context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        return new StoryRecyclerAdapter.StoriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {
        // onBindViewHolder ignored for now
    }

    @Override
    public int getItemCount() {
        return storyArticleDataList.size();
    }
}
