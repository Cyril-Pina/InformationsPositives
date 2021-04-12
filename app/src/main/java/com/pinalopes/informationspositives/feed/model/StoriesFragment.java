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
import com.pinalopes.informationspositives.articles.viewmodel.ArticleViewModel;
import com.pinalopes.informationspositives.databinding.StoriesFragmentBinding;
import com.pinalopes.informationspositives.feed.viewmodel.DataLoadingViewModel;
import com.pinalopes.informationspositives.feed.viewmodel.NewsViewModel;
import com.pinalopes.informationspositives.newsapi.NewsRequestsApi;
import com.pinalopes.informationspositives.newsapi.responsebody.Article;
import com.pinalopes.informationspositives.newsapi.responsebody.News;
import com.pinalopes.informationspositives.utils.AdapterUtils;
import com.pinalopes.informationspositives.utils.DateUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.pinalopes.informationspositives.Constants.MAX_RAND_LIKES;
import static com.pinalopes.informationspositives.Constants.MAX_RAND_VIEWS;
import static com.pinalopes.informationspositives.Constants.MIN_RAND_LIKES;
import static com.pinalopes.informationspositives.Constants.MIN_RAND_VIEWS;

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
                StoryRecyclerAdapter adapter = new StoryRecyclerAdapter(newsToArticlesInStoryViewModel(news));
                storiesRecyclerView.setAdapter(adapter);
                binding.setDataLogoViewModel(new DataLoadingViewModel(true, true));
            }
        });
        NewsRequestsApi.getInstance().getTopHeadlineNews(
                viewModel.getNewsMutableLiveData(),
                getString(R.string.country_prefix));
    }

    private List<ArticleViewModel> newsToArticlesInStoryViewModel(News news) {
        List<ArticleViewModel> articlesInStoryViewModel = new ArrayList<>();
        for (Article article : news.getArticles()) {
            if (AdapterUtils.isArticleValid(article)
                    && AdapterUtils.isArticleNonDuplicate(Collections.singletonList(articlesInStoryViewModel), article.getTitle())) {
                ArticleViewModel articleViewModel = new ArticleViewModel();
                articleViewModel.setTitle(article.getTitle());
                articleViewModel.setDescription(article.getDescription());
                articleViewModel.setText(article.getContent());
                articleViewModel.setLinkToArticle(article.getUrl());
                articleViewModel.setImageUrl(article.getUrlToImage());
                articleViewModel.setWriter(AdapterUtils.getArticleWriter(article));
                articleViewModel.setDate(DateUtils.formatArticlePublishedDate(article.getPublishedAt()));
                articleViewModel.setCategory(AdapterUtils.getFeedGeneralCategory(binding.getRoot().getContext(),
                       R.style.AppTheme_Dark_NoActionBar));
                articleViewModel.setNbLikes(AdapterUtils.getRandomDecimalNumber(MIN_RAND_LIKES, MAX_RAND_LIKES));
                articleViewModel.setNbViews(AdapterUtils.getRandomDecimalNumber(MIN_RAND_VIEWS, MAX_RAND_VIEWS));
                articlesInStoryViewModel.add(articleViewModel);
            }
        }
        return articlesInStoryViewModel;
    }
}
