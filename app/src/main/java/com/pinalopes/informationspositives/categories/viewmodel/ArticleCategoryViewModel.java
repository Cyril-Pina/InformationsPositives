package com.pinalopes.informationspositives.categories.viewmodel;

import com.pinalopes.informationspositives.feed.viewmodel.ArticleRowViewModel;

public class ArticleCategoryViewModel extends ArticleRowViewModel {

    public ArticleCategoryViewModel(String title, String date, String writer, long nbViews, long nbLikes, int imageRes, boolean isVideo) {
        super(title, date, writer, null, nbViews, nbLikes, imageRes, "", isVideo);
    }
}
