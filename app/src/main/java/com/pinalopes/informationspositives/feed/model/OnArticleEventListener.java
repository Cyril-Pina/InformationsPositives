package com.pinalopes.informationspositives.feed.model;

import com.pinalopes.informationspositives.feed.viewmodel.ArticleRowViewModel;

import java.util.List;

public interface OnArticleEventListener {
    void onArticleUpdated(List<ArticleRowViewModel> feedArticleDataList, int page);
}