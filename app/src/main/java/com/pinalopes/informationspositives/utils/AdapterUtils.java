package com.pinalopes.informationspositives.utils;

import android.content.Context;

import com.pinalopes.informationspositives.R;
import com.pinalopes.informationspositives.categories.model.Category;
import com.pinalopes.informationspositives.feed.viewmodel.ArticleRowViewModel;
import com.pinalopes.informationspositives.feed.viewmodel.StoryViewModel;
import com.pinalopes.informationspositives.newsapi.responsebody.Article;
import com.pinalopes.informationspositives.search.viewmodel.FilterCategoriesViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.pinalopes.informationspositives.Constants.DRAWABLE;
import static com.pinalopes.informationspositives.Constants.PACKAGE_NAME;

public class AdapterUtils {

    private AdapterUtils() {
        throw new AssertionError();
    }

    public static List<FilterCategoriesViewModel> getFilterCategories(Context context) {
        List<FilterCategoriesViewModel> categories = new ArrayList<>();
        String[] categoriesName = context.getResources().getStringArray(R.array.categories_name);
        String[] categoriesRes = context.getResources().getStringArray(R.array.categories_res);
        String[] categoriesIcon = context.getResources().getStringArray(R.array.categories_icon);

        for (int i = 0; i != categoriesName.length ; i ++) {
            String categoryName = categoriesName[i];
            int categoryRes =  context.getResources().getIdentifier(categoriesRes[i], DRAWABLE, PACKAGE_NAME);
            int categoryIcon =  context.getResources().getIdentifier(categoriesIcon[i], DRAWABLE, PACKAGE_NAME);

            FilterCategoriesViewModel filterCategoriesViewModel = new FilterCategoriesViewModel(
                    new Category(categoryName,
                            categoryRes,
                            categoryIcon),
                    false
            );
            categories.add(filterCategoriesViewModel);
        }
        return categories;
    }

    public static Category getFeedGeneralCategory(Context context, int currentThemeId) {
        String generalCategory = context.getString(R.string.general_category);
        int generalCategoryIcon = ResourceUtils.getThemeCategoryIcon(context, currentThemeId, R.drawable.ic_general);
        return new Category(generalCategory, 0, generalCategoryIcon);
    }

    public static boolean isArticleValid(Article article) {
        return article.getTitle() != null && article.getContent() != null
                && article.getDescription() != null && article.getPublishedAt() != null
                && article.getUrl() != null && article.getUrlToImage() != null
                && (article.getAuthor() != null || (article.getSource() != null && article.getSource().getName() != null));
    }

    public static boolean isArticleNonDuplicate(List<?> list, String articleTitle) {
        for (Object object: list) {
            if ((object instanceof StoryViewModel
            && ((StoryViewModel) object).getTitle().equals(articleTitle))
            || (object instanceof ArticleRowViewModel
                    && ((ArticleRowViewModel) object).getTitle().equals(articleTitle))) {
                return false;
            }
        }
        return true;
    }

    public static String getArticleWriter(Article article) {
        return article.getAuthor() != null ? article.getAuthor() : article.getSource().getName();
    }
}
