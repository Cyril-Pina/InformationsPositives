package com.pinalopes.informationspositives.feed.model;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pinalopes.informationspositives.R;
import com.pinalopes.informationspositives.databinding.StoriesFragmentBinding;
import com.pinalopes.informationspositives.feed.viewmodel.DataLoadingViewModel;
import com.pinalopes.informationspositives.feed.viewmodel.NewsViewModel;
import com.pinalopes.informationspositives.feed.viewmodel.StoryViewModel;
import com.pinalopes.informationspositives.newsapi.NewsRequestsApi;
import com.pinalopes.informationspositives.newsapi.responsebody.Article;
import com.pinalopes.informationspositives.newsapi.responsebody.News;
import com.pinalopes.informationspositives.utils.AdapterUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StoriesFragment extends Fragment {

    private StoriesFragmentBinding binding;
    private NewsViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = StoriesFragmentBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        initStoriesRecyclerView(rootView.getContext());
        return rootView;
    }

    private void initStoriesRecyclerView(Context context) {
        RecyclerView storiesRecyclerView = binding.storiesRecyclerView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        viewModel.getNewsMutableLiveData().observe((LifecycleOwner) context, news -> {
            if (news != null) {
                storiesRecyclerView.setLayoutManager(layoutManager);
                StoryRecyclerAdapter adapter = new StoryRecyclerAdapter(newsToStoryRows(news));
                storiesRecyclerView.setAdapter(adapter);
                binding.setDataLogoViewModel(new DataLoadingViewModel(true, true));
            }
        });
        NewsRequestsApi.getInstance().getTopHeadlineNews(
                viewModel.getNewsMutableLiveData(),
                getString(R.string.country_prefix));
    }

    private List<StoryViewModel> newsToStoryRows(News news) {
        List<StoryViewModel> storyViewModels = new ArrayList<>();
        for (Article article : news.getArticles()) {
            if (AdapterUtils.isArticleValid(article)
                    && AdapterUtils.isArticleNonDuplicate(Collections.singletonList(storyViewModels), article.getTitle())) {
                StoryViewModel storyViewModel = new StoryViewModel();
                storyViewModel.setTitle(article.getTitle());
                storyViewModel.setImageUrl(article.getUrlToImage());
                storyViewModels.add(storyViewModel);
            }
        }
        return storyViewModels;
    }
}
