package com.pinalopes.informationspositives.search.viewmodel;

import androidx.lifecycle.ViewModel;

public class RecentSearchViewModel extends ViewModel {

    private String articleSearched;

    public RecentSearchViewModel(String articleSearched) {
        this.articleSearched = articleSearched;
    }

    public String getArticleSearched() {
        return articleSearched;
    }

    public void setArticleSearched(String articleSearched) {
        this.articleSearched = articleSearched;
    }
}
