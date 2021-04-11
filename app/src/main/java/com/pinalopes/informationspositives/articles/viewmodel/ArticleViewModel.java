package com.pinalopes.informationspositives.articles.viewmodel;

import com.pinalopes.informationspositives.feed.viewmodel.ArticleRowViewModel;

public class ArticleViewModel extends ArticleRowViewModel {

    private boolean isLiked;
    private boolean isHeaderVisible;

    public ArticleViewModel() {}

    public ArticleViewModel(ArticleRowViewModel articleRowViewModel) {
        super(articleRowViewModel);
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public boolean isHeaderVisible() {
        return isHeaderVisible;
    }

    public void setHeaderVisible(boolean headerVisible) {
        isHeaderVisible = headerVisible;
    }
}
