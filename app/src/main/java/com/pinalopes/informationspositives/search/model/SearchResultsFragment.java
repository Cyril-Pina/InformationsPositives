package com.pinalopes.informationspositives.search.model;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.pinalopes.informationspositives.R;
import com.pinalopes.informationspositives.databinding.SearchResultsFragmentBinding;
import com.pinalopes.informationspositives.feed.model.ArticlesFragment;
import com.pinalopes.informationspositives.feed.viewmodel.ArticleRowViewModel;
import com.pinalopes.informationspositives.feed.viewmodel.NewsViewModel;
import com.pinalopes.informationspositives.network.model.NetworkErrorFragment;
import com.pinalopes.informationspositives.network.model.NetworkService;
import com.pinalopes.informationspositives.newsapi.NewsRequestsApi;
import com.pinalopes.informationspositives.newsapi.responsebody.Article;
import com.pinalopes.informationspositives.newsapi.responsebody.News;
import com.pinalopes.informationspositives.search.viewmodel.SearchActivityViewModel;
import com.pinalopes.informationspositives.search.viewmodel.SearchResultsViewModel;
import com.pinalopes.informationspositives.storage.DataStorageHelper;
import com.pinalopes.informationspositives.storage.RecentSearch;
import com.pinalopes.informationspositives.utils.AdapterUtils;
import com.pinalopes.informationspositives.utils.DateUtils;
import com.pinalopes.informationspositives.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.pinalopes.informationspositives.Constants.ADD_CATEGORY;
import static com.pinalopes.informationspositives.Constants.ADD_NEW_ELEMENT;
import static com.pinalopes.informationspositives.Constants.AND_SUFFIX;
import static com.pinalopes.informationspositives.Constants.DEFAULT_PAGINATION_VALUE;
import static com.pinalopes.informationspositives.Constants.DEFAULT_VALUE_CATEGORIES_ADDED;
import static com.pinalopes.informationspositives.Constants.DOUBLE_QUOTE_REGEX;
import static com.pinalopes.informationspositives.Constants.FAILURE_ITERATION_INIT_VALUE;
import static com.pinalopes.informationspositives.Constants.FAILURE_VALUE;
import static com.pinalopes.informationspositives.Constants.FILTERS;
import static com.pinalopes.informationspositives.Constants.INITIAL_VALUE_NB_ELEMENTS_ADDED;
import static com.pinalopes.informationspositives.Constants.KEY_WORD_SEARCH;
import static com.pinalopes.informationspositives.Constants.LENGTH_EMPTY_KEYWORD_SEARCH;
import static com.pinalopes.informationspositives.Constants.MAX_FAILURE_ITERATION;
import static com.pinalopes.informationspositives.Constants.MIN_SIZE;
import static com.pinalopes.informationspositives.Constants.NEXT_PAGE;
import static com.pinalopes.informationspositives.Constants.NO_ARTICLE;
import static com.pinalopes.informationspositives.Constants.OR_SUFFIX;
import static com.pinalopes.informationspositives.Constants.PARENTHESIS_REGEX;
import static com.pinalopes.informationspositives.Constants.SIZE_EMPTY_LIST;

public class SearchResultsFragment extends Fragment {

    private static final String TAG = "SearchResultsFragment";

    private SearchResultsFragmentBinding binding;
    private NewsViewModel viewModel;
    private SearchActivityViewModel searchActivityViewModel;
    private ArticlesFragment articlesFragment;
    private List<ArticleRowViewModel> feedArticleDataList;

    private int page = DEFAULT_PAGINATION_VALUE;
    private int failureIteration = FAILURE_ITERATION_INIT_VALUE;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SearchResultsFragmentBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        binding.setSearchResultsViewModel(new ViewModelProvider(this).get(SearchResultsViewModel.class));

        viewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        rootView.setOnClickListener(v -> ViewUtils.hideSoftKeyboard((Activity) rootView.getContext()));
        initArticlesFragment();
        initNewsMutableLiveData(getContext());
        runSearchRequestWithFilter(rootView.getContext());
        initOnDeleteAllRecentSearchesClick();
        feedArticleDataList = new ArrayList<>();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchActivityViewModel = new ViewModelProvider(requireActivity()).get(SearchActivityViewModel.class);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initArticlesFragment() {
        FragmentManager fragmentManager = getChildFragmentManager();
        articlesFragment = new ArticlesFragment();
        fragmentManager.beginTransaction().add(R.id.articlesFragmentLayout, articlesFragment).commit();
        fragmentManager.executePendingTransactions();
    }

    private void initOnDeleteAllRecentSearchesClick() {
        binding.deleteAllRecentSearches.setOnClickListener(v -> {
            DataStorageHelper.deleteAllRecentSearchesInLocalDB();
            initRecentSearchesRecyclerView(v.getContext(), new ArrayList<>());
            updateHeaderSearchResultsLayout(false);
        });
    }

    private void initNewsMutableLiveData(Context context) {
        viewModel.getNewsMutableLiveData().observe((LifecycleOwner) context, news -> {
            if (news != null && news.getTotalResults() > NO_ARTICLE) {
                updateArticlesLayoutVisibility(true);
                int nbElementsAdded = updateFeedArticleDataList(news);
                updateArticlesFragment(this.feedArticleDataList, nbElementsAdded, new LinearLayoutManager(context));
                failureIteration = FAILURE_ITERATION_INIT_VALUE;
                page += NEXT_PAGE;
                return;
            } else if (failureIteration < MAX_FAILURE_ITERATION && getActivity() != null) {
                NewsRequestsApi.getInstance().getLatestNews(
                        viewModel.getNewsMutableLiveData(),
                        getActivity().getString(R.string.lang_prefix),
                        DateUtils.getPreviousDate(),
                        page);
            } else if (feedArticleDataList.size() <= MIN_SIZE) {
                    showNetworkErrorFragment(getNetworkErrorCause(context));
            }
            failureIteration += FAILURE_VALUE;
        });
    }

    private String getNetworkErrorCause(Context context) {
        if (getActivity() != null) {
            return NetworkService.isNetworkOn(context) ? getString(R.string.article_not_found) : getString(R.string.no_network_connection);
        }
        return "";
    }

    private void updateArticlesFragment(List<ArticleRowViewModel> feedArticleDataList, int nbElementsAdded, LinearLayoutManager layoutManager) {
        articlesFragment.initFeedRecyclerViewAdapter(feedArticleDataList, nbElementsAdded, layoutManager);
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
                if (binding != null) {
                    articleRowViewModel.setCategory(AdapterUtils.getFeedGeneralCategory(binding.getRoot().getContext(),
                            DataStorageHelper.getUserSettings().getCurrentTheme()));
                }
                feedArticleDataList.add(articleRowViewModel);
                nbElementsAdded += ADD_NEW_ELEMENT;
            }
        }
        return nbElementsAdded;
    }

    private void initRecentSearchesMutableLiveData(Context context) {
        binding.getSearchResultsViewModel().getRecentSearchMutableLiveData().observe((LifecycleOwner) context, recentSearches -> {
            if (recentSearches != null && recentSearches.size() > SIZE_EMPTY_LIST) {
                updateHeaderSearchResultsLayout(true);
                initRecentSearchesRecyclerView(context, recentSearches);
            } else {
                updateHeaderSearchResultsLayout(false);
            }
        });
        DataStorageHelper.getRecentSearchesFromLocalDB(binding.getSearchResultsViewModel().getRecentSearchMutableLiveData());
    }

    private void initRecentSearchesRecyclerView(Context context, List<RecentSearch> recentSearches) {
        RecyclerView searchResultsRecyclerView = binding.searchResultsRecyclerView;
        RecentSearchesAdapter adapter = new RecentSearchesAdapter(recentSearches, getOnRecentSearchClickListener(context));
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());

        Collections.reverse(recentSearches);

        searchResultsRecyclerView.setLayoutManager(layoutManager);
        searchResultsRecyclerView.addItemDecoration(dividerItemDecoration);
        searchResultsRecyclerView.setAdapter(adapter);
    }

    private void showNetworkErrorFragment(String cause) {
        if (this.isAdded()) {
            updateArticlesLayoutVisibility(false);
            FragmentManager fragmentManager = getChildFragmentManager();
            NetworkErrorFragment networkErrorFragment = NetworkErrorFragment.newInstance(cause, false);
            fragmentManager.beginTransaction().replace(R.id.networkErrorFragment, networkErrorFragment).commitAllowingStateLoss();
        }
    }

    private OnRecentSearchClickListener getOnRecentSearchClickListener(Context context) {
        return new OnRecentSearchClickListener() {
            @Override
            public void setOnRecentSearchClick(RecentSearch recentSearchSelected) {
                searchActivityViewModel.getClickOnRecentSearchMutable().postValue(recentSearchSelected.articleSearched);
            }

            @Override
            public void setOnDeleteRecentSearchClick(RecentSearch recentSearchToDelete, int position) {
                DataStorageHelper.deleteRecentSearchInLocalDB(recentSearchToDelete);
                try {
                    List<RecentSearch> recentSearches =
                            ((RecentSearchesAdapter) Objects.requireNonNull(
                                    binding.searchResultsRecyclerView.getAdapter())).getRecentSearches();
                    recentSearches.remove(position);
                    Collections.reverse(recentSearches);
                    if (recentSearches.size() <= SIZE_EMPTY_LIST) {
                        updateHeaderSearchResultsLayout(false);
                    }
                    initRecentSearchesRecyclerView(context, recentSearches);
                } catch (NullPointerException npe) {
                    Log.e(TAG, npe.toString());
                    initRecentSearchesMutableLiveData(context);
                }
            }
        };
    }

    private void updateHeaderSearchResultsLayout(boolean isHeaderVisible) {
        if (binding != null) {
            binding.getSearchResultsViewModel().setSearchEditTextEmpty(isHeaderVisible);
            binding.invalidateAll();
        }
    }

    private void updateArticlesLayoutVisibility(boolean isArticlesVisible) {
        if (binding != null) {
            binding.getSearchResultsViewModel().setSearchResultOk(isArticlesVisible);
            binding.invalidateAll();
        }
    }

    public static SearchResultsFragment newInstance(String keyWordSearch, Filters filters) {
        SearchResultsFragment searchResultsFragment = new SearchResultsFragment();
        Bundle args = new Bundle();
        args.putString(FILTERS, new Gson().toJson(filters));
        args.putString(KEY_WORD_SEARCH, new Gson().toJson(keyWordSearch));
        searchResultsFragment.setArguments(args);
        return searchResultsFragment;
    }

    private void runSearchRequestWithFilter(Context context) {
        if (getArguments() != null) {
            Filters filters = new Gson().fromJson(getArguments().getString(FILTERS, ""), Filters.class);
            String keyWordSearch = getArguments().getString(KEY_WORD_SEARCH, "");
            if (keyWordSearch != null) {
                if (keyWordSearch.length() > LENGTH_EMPTY_KEYWORD_SEARCH) {
                    updateHeaderSearchResultsLayout(false);
                    searchNewsFromDateFilters(filters, keyWordSearch.replace(DOUBLE_QUOTE_REGEX, ""));
                } else {
                    initRecentSearchesMutableLiveData(context);
                }
            }
        }
    }

    private void searchNewsFromDateFilters(Filters filters, String keyWordSearch) {
        Boolean[] isCategorySearchedList = filters.getCategories();
        if (Arrays.asList(isCategorySearchedList).contains(true)) {
            keyWordSearch = generateKeywordsField(keyWordSearch, isCategorySearchedList);
        }
        if (filters.getBeginningDate() != null && filters.getEndingDate() != null) {
            NewsRequestsApi.getInstance().getFilteredNews(
                    viewModel.getNewsMutableLiveData(),
                    keyWordSearch,
                    DateUtils.getDateToSearch(filters.getBeginningDate()),
                    DateUtils.getDateToSearch(filters.getEndingDate()),
                    getString(R.string.lang_prefix),
                    page);
        } else if (filters.getBeginningDate() != null) {
            NewsRequestsApi.getInstance().getFilteredNews(
                    viewModel.getNewsMutableLiveData(),
                    keyWordSearch,
                    DateUtils.getDateToSearch(filters.getBeginningDate()),
                    DateUtils.getActualDate(),
                    getString(R.string.lang_prefix),
                    page);
        } else if (filters.getEndingDate() != null) {
            NewsRequestsApi.getInstance().getFilteredNews(
                    viewModel.getNewsMutableLiveData(),
                    keyWordSearch,
                    DateUtils.getDateToSearch(filters.getEndingDate()),
                    getString(R.string.lang_prefix),
                    page);
        } else {
            NewsRequestsApi.getInstance().getFilteredNews(
                    viewModel.getNewsMutableLiveData(),
                    keyWordSearch,
                    getString(R.string.lang_prefix),
                    page);
        }
    }

    private String generateKeywordsField(String keyWordSearch, Boolean[] isCategorySearchedList) {
        StringBuilder builder = new StringBuilder(keyWordSearch);
        String[] categoriesName = getResources().getStringArray(R.array.categories_name);
        int nbCategoriesSelected = Arrays.stream(isCategorySearchedList).filter(c -> c).toArray().length;
        int nbCategoriesAdded = DEFAULT_VALUE_CATEGORIES_ADDED;

        builder.append(AND_SUFFIX);
        for (int i = 0 ; i != isCategorySearchedList.length ; i++) {
            boolean isCategorySelected = isCategorySearchedList[i];
            if (isCategorySelected) {
                builder.append(categoriesName[i]);
                nbCategoriesAdded += ADD_CATEGORY;
                builder.append(nbCategoriesAdded != nbCategoriesSelected ? OR_SUFFIX : PARENTHESIS_REGEX);
            }
        }
        return builder.toString();
    }
}
