package com.pinalopes.informationspositives.search.model;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.pinalopes.informationspositives.databinding.SearchResultsFragmentBinding;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.pinalopes.informationspositives.Constants.FILTERS;
import static com.pinalopes.informationspositives.Constants.KEY_WORD_SEARCH;
import static com.pinalopes.informationspositives.Constants.NO_FLAG;

public class SearchResultsFragment extends Fragment {

    private SearchResultsFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SearchResultsFragmentBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        runSearchRequestWithFilter();
        rootView.setOnClickListener(v -> hideSoftKeyboard((Activity) rootView.getContext()));
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static SearchResultsFragment newInstance(String keyWordSearch, Filters filters) {
        SearchResultsFragment searchResultsFragment = new SearchResultsFragment();
        Bundle args = new Bundle();
        args.putString(FILTERS, new Gson().toJson(filters));
        args.putString(KEY_WORD_SEARCH, new Gson().toJson(keyWordSearch));
        searchResultsFragment.setArguments(args);
        return searchResultsFragment;
    }

    private void runSearchRequestWithFilter() {
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
                Log.wtf("keyword", keyWordSearch);
            }
        }
    }

    private void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), NO_FLAG);
    }
}
