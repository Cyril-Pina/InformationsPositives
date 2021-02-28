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
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.pinalopes.informationspositives.databinding.SearchResultsFragmentBinding;
import com.pinalopes.informationspositives.feed.model.FeedRecyclerAdapter;
import com.pinalopes.informationspositives.search.viewmodel.SearchActivityViewModel;
import com.pinalopes.informationspositives.search.viewmodel.SearchResultsViewModel;
import com.pinalopes.informationspositives.storage.DataStorage;
import com.pinalopes.informationspositives.storage.RecentSearch;
import com.pinalopes.informationspositives.utils.AdapterUtils;
import com.pinalopes.informationspositives.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.pinalopes.informationspositives.Constants.FILTERS;
import static com.pinalopes.informationspositives.Constants.KEY_WORD_SEARCH;
import static com.pinalopes.informationspositives.Constants.LENGTH_EMPTY_KEYWORD_SEARCH;
import static com.pinalopes.informationspositives.Constants.SIZE_EMPTY_LIST;

public class SearchResultsFragment extends Fragment {

    private static final String TAG = "SearchResultsFragment";

    private SearchResultsFragmentBinding binding;
    private SearchActivityViewModel searchActivityViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SearchResultsFragmentBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        binding.setSearchResultsViewModel(new ViewModelProvider(this).get(SearchResultsViewModel.class));

        rootView.setOnClickListener(v -> ViewUtils.hideSoftKeyboard((Activity) rootView.getContext()));
        runSearchRequestWithFilter(rootView.getContext());
        initOnDeleteAllRecentSearchesClick();
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

    private void initOnDeleteAllRecentSearchesClick() {
        binding.deleteAllRecentSearches.setOnClickListener(v -> {
            DataStorage.deleteAllRecentSearchesInLocalDB();
            initRecentSearchesRecyclerView(v.getContext(), new ArrayList<>());
            updateHeaderSearchResultsLayout(false);
        });
    }

    private void initSearchResultsRecyclerView(Context context) {
        RecyclerView searchResultsRecyclerView = binding.searchResultsRecyclerView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);

        searchResultsRecyclerView.setLayoutManager(layoutManager);
        FeedRecyclerAdapter adapter = new FeedRecyclerAdapter(AdapterUtils.getArticleRowViewModelList(context));
        searchResultsRecyclerView.setAdapter(adapter);
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
        DataStorage.getRecentSearchesFromLocalDB(binding.getSearchResultsViewModel().getRecentSearchMutableLiveData());
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
    
    private OnRecentSearchClickListener getOnRecentSearchClickListener(Context context) {
        return new OnRecentSearchClickListener() {
            @Override
            public void setOnRecentSearchClick(RecentSearch recentSearchSelected) {
                Log.wtf("Clicked", "yes");
                searchActivityViewModel.getClickOnRecentSearchMutable().postValue(recentSearchSelected.articleSearched);
            }

            @Override
            public void setOnDeleteRecentSearchClick(RecentSearch recentSearchToDelete, int position) {
                DataStorage.deleteRecentSearchInLocalDB(recentSearchToDelete);
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
            if (filters != null) {
                Log.wtf("Beginning date", filters.getBeginningDate());
                Log.wtf("Ending date", filters.getEndingDate());
                Log.wtf("0", filters.getCategories()[0] + "");
                Log.wtf("1", filters.getCategories()[1] + "");
                Log.wtf("2", filters.getCategories()[2] + "");
                Log.wtf("3", filters.getCategories()[3] + "");
                Log.wtf("4", filters.getCategories()[4] + "");
                Log.wtf("5", filters.getCategories()[5] + "");
                Log.wtf("6", filters.getCategories()[6] + "");
            }
            if (keyWordSearch != null) {
                Log.wtf("keyWordSearch", keyWordSearch + " " + keyWordSearch.length());
                if (keyWordSearch.length() > LENGTH_EMPTY_KEYWORD_SEARCH) {
                    updateHeaderSearchResultsLayout(false);
                    initSearchResultsRecyclerView(context);
                } else {
                    initRecentSearchesMutableLiveData(context);
                }
            }
        }
    }
}
