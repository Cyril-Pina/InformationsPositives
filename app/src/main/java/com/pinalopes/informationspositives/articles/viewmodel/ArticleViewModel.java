package com.pinalopes.informationspositives.articles.viewmodel;

import com.pinalopes.informationspositives.categories.model.Category;
import com.pinalopes.informationspositives.feed.viewmodel.ArticleRowViewModel;

public class ArticleViewModel extends ArticleRowViewModel {

    private String writtenBy;
    private boolean isLiked;
    private boolean isHeaderVisible;

    public ArticleViewModel(String title, Category category, long nbViews, long nbLikes, int imageRes, boolean isVideo, String writtenBy, boolean isLiked) {
        super(title, "", "", category, nbViews, nbLikes, imageRes, isVideo);
        this.writtenBy = writtenBy;
        this.isLiked = isLiked;
        this.isHeaderVisible = false;
    }

    public String getWrittenBy() {
        return writtenBy;
    }

    public void setWrittenBy(String writtenBy) {
        this.writtenBy = writtenBy;
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
