package com.pinalopes.informationspositives.articles.viewmodel;

import com.pinalopes.informationspositives.categories.model.Category;
import com.pinalopes.informationspositives.feed.viewmodel.ArticleRowViewModel;

public class RecommendationRowViewModel extends ArticleRowViewModel {

    public RecommendationRowViewModel(String title, Category category, int imageRes, boolean isVideo) {
        super(title, "", "", category, 0, 0, imageRes, isVideo);
    }
}
