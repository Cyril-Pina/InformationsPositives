package com.pinalopes.informationspositives.story.viewmodel;

import com.pinalopes.informationspositives.articles.viewmodel.ArticleViewModel;
import com.pinalopes.informationspositives.categories.model.Category;

public class ArticleInStoryViewModel extends ArticleViewModel {

    public ArticleInStoryViewModel(String title, Category category, int imageRes, String writtenBy) {
        super(title, category, 0, 0, imageRes, false, writtenBy, false);
    }
}
