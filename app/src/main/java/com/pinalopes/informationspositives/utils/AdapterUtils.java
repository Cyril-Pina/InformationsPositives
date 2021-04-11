package com.pinalopes.informationspositives.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.pinalopes.informationspositives.R;
import com.pinalopes.informationspositives.categories.model.Category;
import com.pinalopes.informationspositives.feed.viewmodel.ArticleRowViewModel;
import com.pinalopes.informationspositives.feed.viewmodel.StoryViewModel;
import com.pinalopes.informationspositives.newsapi.responsebody.Article;
import com.pinalopes.informationspositives.search.viewmodel.FilterCategoriesViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.pinalopes.informationspositives.Constants.DRAWABLE;
import static com.pinalopes.informationspositives.Constants.ENDING_BRACKET;
import static com.pinalopes.informationspositives.Constants.FIRST_INDEX;
import static com.pinalopes.informationspositives.Constants.NB_RECOMMENDED_ARTICLES;
import static com.pinalopes.informationspositives.Constants.OPENING_BRACKET;
import static com.pinalopes.informationspositives.Constants.PACKAGE_NAME;
import static com.pinalopes.informationspositives.Constants.SUSPENSION_POINTS;
import static com.pinalopes.informationspositives.application.IPApplication.rand;

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

    public static String getSubstringStringFromMaxLength(String initialString, int maxTitleLength) {
        if (initialString == null) {
            return "";
        }
        return initialString.length() <= maxTitleLength ?
                initialString :
                initialString.substring(FIRST_INDEX, maxTitleLength) + SUSPENSION_POINTS;
    }

    public static String getTextContentWithoutBrackets(String content) {
        if (content != null && content.contains(OPENING_BRACKET) && content.contains(ENDING_BRACKET)) {
            int lastIdOfOpenBracket = content.lastIndexOf(OPENING_BRACKET);
            return content.substring(FIRST_INDEX, lastIdOfOpenBracket);
        }
        return content;
    }

    public static String getRecommendedArticlesFromArticle(List<? extends ArticleRowViewModel> articles, int currentArticleId) {
        List<ArticleRowViewModel> recommendedArticles = new ArrayList<>();
        List<Integer> selectedArticles = new ArrayList<>();

        if (articles.size() > NB_RECOMMENDED_ARTICLES) {
            articles.remove(currentArticleId);
            int totalArticle = articles.size();
            while (recommendedArticles.size() < NB_RECOMMENDED_ARTICLES) {
                int index = rand.nextInt(totalArticle);
                if (!selectedArticles.contains(index)) {
                    selectedArticles.add(index);
                    recommendedArticles.add(articles.get(index));
                }
            }
            return new Gson().toJson(recommendedArticles);
        }
        return new Gson().toJson(articles);
    }

}