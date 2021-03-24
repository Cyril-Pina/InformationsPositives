package com.pinalopes.informationspositives.search.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pinalopes.informationspositives.storage.RecentSearch;

import java.util.List;

public class SearchResultsViewModel extends ViewModel {
    private MutableLiveData<List<RecentSearch>> recentSearchMutableLiveData;

    private boolean isSearchEditTextEmpty;
    private boolean isSearchResultOk;

    public MutableLiveData<List<RecentSearch>> getRecentSearchMutableLiveData() {
        if (recentSearchMutableLiveData == null) {
            recentSearchMutableLiveData = new MutableLiveData<>();
        }
        return recentSearchMutableLiveData;
    }

    public boolean isSearchEditTextEmpty() {
        return isSearchEditTextEmpty;
    }

    public void setSearchEditTextEmpty(boolean searchEditTextEmpty) {
        isSearchEditTextEmpty = searchEditTextEmpty;
    }

    public void setRecentSearchMutableLiveData(MutableLiveData<List<RecentSearch>> recentSearchMutableLiveData) {
        this.recentSearchMutableLiveData = recentSearchMutableLiveData;
    }

    public boolean isSearchResultOk() {
        return isSearchResultOk;
    }

    public void setSearchResultOk(boolean searchResultOk) {
        isSearchResultOk = searchResultOk;
    }
}
