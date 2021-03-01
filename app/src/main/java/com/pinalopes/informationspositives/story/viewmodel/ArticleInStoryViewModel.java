package com.pinalopes.informationspositives.story.viewmodel;

import com.pinalopes.informationspositives.articles.viewmodel.ArticleViewModel;
import com.pinalopes.informationspositives.categories.model.Category;

public class ArticleInStoryViewModel extends ArticleViewModel {

    public ArticleInStoryViewModel(String title, String text, Category category, int imageRes, String writtenBy) {
        super(title, text, category, 0, 0, imageRes, false, writtenBy, false);
    }
}
