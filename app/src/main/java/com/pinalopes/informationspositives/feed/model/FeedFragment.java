package com.pinalopes.informationspositives.feed.model;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.pinalopes.informationspositives.LoadingService;
import com.pinalopes.informationspositives.R;
import com.pinalopes.informationspositives.databinding.FeedFragmentBinding;
import com.pinalopes.informationspositives.feed.dagger.DaggerFeedComponent;
import com.pinalopes.informationspositives.feed.dagger.LoadingModule;
import com.pinalopes.informationspositives.feed.viewmodel.ArticleRowViewModel;
import com.pinalopes.informationspositives.feed.viewmodel.DataLoadingViewModel;
import com.pinalopes.informationspositives.feed.viewmodel.NewsViewModel;
import com.pinalopes.informationspositives.network.model.NetworkErrorFragment;
import com.pinalopes.informationspositives.network.model.NetworkService;
import com.pinalopes.informationspositives.newsapi.NewsRequestsApi;
import com.pinalopes.informationspositives.newsapi.responsebody.Article;
import com.pinalopes.informationspositives.newsapi.responsebody.News;
import com.pinalopes.informationspositives.storage.DataStorageHelper;
import com.pinalopes.informationspositives.utils.AdapterUtils;
import com.pinalopes.informationspositives.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import static com.pinalopes.informationspositives.Constants.ADD_NEW_ELEMENT;
import static com.pinalopes.informationspositives.Constants.DEFAULT_PAGINATION_VALUE;
import static com.pinalopes.informationspositives.Constants.DELAY_BEFORE_RELOAD_FEED_ARTICLES;
import static com.pinalopes.informationspositives.Constants.FAILURE_ITERATION_INIT_VALUE;
import static com.pinalopes.informationspositives.Constants.FAILURE_VALUE;
import static com.pinalopes.informationspositives.Constants.INITIAL_VALUE_NB_ELEMENTS_ADDED;
import static com.pinalopes.informationspositives.Constants.MAX_FAILURE_ITERATION;
import static com.pinalopes.informationspositives.Constants.MIN_SIZE;
import static com.pinalopes.informationspositives.Constants.NEXT_PAGE;
import static com.pinalopes.informationspositives.Constants.NO_ARTICLE;
import static com.pinalopes.informationspositives.Constants.NO_ELEMENT_ADDED;

public class FeedFragment extends Fragment implements NetworkErrorFragment.OnNetworkErrorEventListener, ArticlesFragment.OnRefreshEventListener {

    @Inject LoadingService loadingService;
    private FeedFragmentBinding binding;
    private NewsViewModel viewModel;
    private OnFeedFragmentEventListener listener;
    private List<ArticleRowViewModel> feedArticleDataList;
    private ArticlesFragment articlesFragment;

    private int page = DEFAULT_PAGINATION_VALUE;
    private int failureIteration = FAILURE_ITERATION_INIT_VALUE;

    public interface OnFeedFragmentEventListener {
        void onFeedArticleUpdated(List<ArticleRowViewModel> feedArticleDataList, int page);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FeedFragmentBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        DaggerFeedComponent
                .builder()
                .loadingModule(new LoadingModule(rootView.getContext(), binding.loadingMainLayout, binding.loadingImageView))
                .build()
                .inject(this);

        viewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        initStoriesFragment();
        initArticlesFragment();
        if (feedArticleDataList != null && feedArticleDataList.size() > MIN_SIZE) {
            showLoadedFeedArticles(rootView.getContext());
        } else {
            feedArticleDataList = new ArrayList<>();
            initFeedRecyclerView(rootView.getContext());
        }
        return rootView;
    }

    @Override
    public void onRefreshButtonPressed() {
        onRefresh();
    }

    @Override
    public void onRefresh() {
        failureIteration = FAILURE_ITERATION_INIT_VALUE;
        page = DEFAULT_PAGINATION_VALUE;
        this.feedArticleDataList = new ArrayList<>();
        updateDataLoadingViewModel(false, true);
        if (viewModel.getNewsMutableLiveData().hasObservers()) {
            NewsRequestsApi.getInstance().getLatestNews(
                    viewModel.getNewsMutableLiveData(),
                    getString(R.string.lang_prefix),
                    DateUtils.getActualDate(),
                    page);
        } else {
            initFeedRecyclerView(binding.getRoot().getContext());
        }
    }

    @Override
    public void onFeedListReachesBottomListener() {
        initFeedRecyclerView(binding.getRoot().getContext());
    }

    private void initStoriesFragment() {
        FragmentManager fragmentManager = getChildFragmentManager();
        Fragment storiesFragment = new StoriesFragment();
        fragmentManager.beginTransaction().add(R.id.storiesFrameLayout, storiesFragment).commit();
    }

    private void initArticlesFragment() {
        FragmentManager fragmentManager = getChildFragmentManager();
        articlesFragment = new ArticlesFragment();
        articlesFragment.setOnRefreshEventListener(this);
        fragmentManager.beginTransaction().add(R.id.articlesFragmentLayout, articlesFragment).commit();
        fragmentManager.executePendingTransactions();
    }

    private void showLoadedFeedArticles(Context context) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                updateDataLoadingViewModel(true, true);
                updateArticlesFragment(feedArticleDataList, NO_ELEMENT_ADDED, new LinearLayoutManager(context));
            }
        }, DELAY_BEFORE_RELOAD_FEED_ARTICLES);
    }

    private void showNetworkErrorFragment(String cause) {
        updateDataLoadingViewModel(false, false);
        articlesFragment.stopSwipeRefreshing();
        FragmentManager fragmentManager = getChildFragmentManager();
        NetworkErrorFragment networkErrorFragment = NetworkErrorFragment.newInstance(cause, true);
        networkErrorFragment.setOnNetworkErrorEventListener(this);
        fragmentManager.beginTransaction().replace(R.id.networkErrorFragment, networkErrorFragment).commitAllowingStateLoss();
    }

    private void initFeedRecyclerView(Context context) {
        updateDataLoadingViewModel(false, true);
        viewModel.getNewsMutableLiveData().observe((LifecycleOwner) context, news -> {
            if (news != null && news.getTotalResults() > NO_ARTICLE) {
                int nbElementsAdded = updateFeedArticleDataList(news);
                updateDataLoadingViewModel(true, true);
                updateArticlesFragment(this.feedArticleDataList, nbElementsAdded, new LinearLayoutManager(context));
                failureIteration = FAILURE_ITERATION_INIT_VALUE;
                page += NEXT_PAGE;
                listener.onFeedArticleUpdated(this.feedArticleDataList, page);
                return;
            } else if (failureIteration < MAX_FAILURE_ITERATION) {
                NewsRequestsApi.getInstance().getLatestNews(
                        viewModel.getNewsMutableLiveData(),
                        getString(R.string.lang_prefix),
                        DateUtils.getPreviousDate(),
                        page);
            } else if (feedArticleDataList.size() <= MIN_SIZE) {
                showNetworkErrorFragment(NetworkService.isNetworkOn(context) ?
                        getString(R.string.article_not_found) : getString(R.string.no_network_connection));
            } else {
                articlesFragment.stopSwipeRefreshing();
                updateDataLoadingViewModel(true, true);
            }
            failureIteration += FAILURE_VALUE;
        });
        NewsRequestsApi.getInstance().getLatestNews(
                viewModel.getNewsMutableLiveData(),
                getString(R.string.lang_prefix),
                DateUtils.getActualDate(),
                page);
    }

    private void updateArticlesFragment(List<ArticleRowViewModel> feedArticleDataList, int nbElementsAdded, LinearLayoutManager layoutManager) {
        articlesFragment.initFeedRecyclerViewAdapter(feedArticleDataList, nbElementsAdded, layoutManager);
    }

    public void scrollUpToTopOfList() {
        articlesFragment.scrollUpToTopOfList();
    }

    private int updateFeedArticleDataList(News news) {
        int nbElementsAdded = INITIAL_VALUE_NB_ELEMENTS_ADDED;
        for (Article article : news.getArticles()) {
            if (AdapterUtils.isArticleValid(article)
                    && AdapterUtils.isArticleNonDuplicate(feedArticleDataList, article.getTitle())) {
                ArticleRowViewModel articleRowViewModel = new ArticleRowViewModel();
                articleRowViewModel.setTitle(article.getTitle());
                articleRowViewModel.setImageUrl(article.getUrlToImage());
                articleRowViewModel.setWriter(AdapterUtils.getArticleWriter(article));
                articleRowViewModel.setDate(DateUtils.formatArticlePublishedDate(article.getPublishedAt()));
                articleRowViewModel.setCategory(AdapterUtils.getFeedGeneralCategory(binding.getRoot().getContext(),
                        DataStorageHelper.getUserSettings().getCurrentTheme()));
                feedArticleDataList.add(articleRowViewModel);
                nbElementsAdded += ADD_NEW_ELEMENT;
            }
        }
        return nbElementsAdded;
    }

    private void updateDataLoadingViewModel(boolean isDataLoaded, boolean isNetworkOn) {
        if (binding.getDataLoadingViewModel() != null) {
            binding.getDataLoadingViewModel().setDataLoaded(isDataLoaded);
            binding.getDataLoadingViewModel().setNetworkOn(isNetworkOn);
        } else {
            binding.setDataLoadingViewModel(new DataLoadingViewModel(isDataLoaded, isNetworkOn));
        }
        binding.invalidateAll();
    }

    public void setOnFeedFragmentEventListener(OnFeedFragmentEventListener listener, List<ArticleRowViewModel> feedArticleDataList, int feedPage) {
        this.listener = listener;
        this.feedArticleDataList = feedArticleDataList;
        this.page = feedPage;
    }
}