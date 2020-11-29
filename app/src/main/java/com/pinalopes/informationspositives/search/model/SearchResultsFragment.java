package com.pinalopes.informationspositives.search.model;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.pinalopes.informationspositives.databinding.SearchResultsFragmentBinding;

import static com.pinalopes.informationspositives.Constants.FILTERS;

public class SearchResultsFragment extends Fragment {

    private SearchResultsFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SearchResultsFragmentBinding.inflate(inflater, container, false);
        runSearchRequestWithFilter();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static SearchResultsFragment newInstance(Filters filters) {
        SearchResultsFragment searchResultsFragment = new SearchResultsFragment();
        Bundle args = new Bundle();
        args.putString(FILTERS, new Gson().toJson(filters));
        searchResultsFragment.setArguments(args);
        return searchResultsFragment;
    }

    private void runSearchRequestWithFilter() {
        if (getArguments() != null) {
            Filters filters = new Gson().fromJson(getArguments().getString(FILTERS, ""), Filters.class);
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
    }
}
