package com.pinalopes.informationspositives.utils;

import android.content.Context;

import com.pinalopes.informationspositives.R;
import com.pinalopes.informationspositives.categories.model.Category;
import com.pinalopes.informationspositives.feed.viewmodel.ArticleRowViewModel;
import com.pinalopes.informationspositives.search.viewmodel.FilterCategoriesViewModel;
import com.pinalopes.informationspositives.storage.DataStorage;

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

    public static List<ArticleRowViewModel> getArticleRowViewModelList(Context context) {
        List<ArticleRowViewModel> test = new ArrayList<>();
        int currentThemeId = DataStorage.getUserSettings().getCurrentTheme();

        Category categoryFauna = new Category("Faune", 0,
                ResourceUtils.getThemeCategoryIcon(context, currentThemeId, R.drawable.ic_fauna));
        Category categoryFood = new Category("Alimentation", 0,
                ResourceUtils.getThemeCategoryIcon(context, currentThemeId, R.drawable.ic_food));
        Category categoryHuma = new Category("Humanitaire", 0,
                ResourceUtils.getThemeCategoryIcon(context, currentThemeId, R.drawable.ic_food));

        String writer = "Michaël Doe";

        test.add(new ArticleRowViewModel("Un chiot sauvé miraculeusement par un jeune homme dans le département de Tarn", "18:25-06/01/2020", writer, categoryFauna, 1802, 235, R.drawable.puppy, false));
        test.add(new ArticleRowViewModel("La mairie de Nice fait signé un contrat de travail à un sans-abri bienfaiteur", "10:12-01/12/2020",  writer, categoryHuma, 36820, 13, R.drawable.homeless, false));
        test.add(new ArticleRowViewModel("Macron donne 1million d'euros à un jeune sans abri", "08:02-12/12/2020", writer, categoryFauna, 36974, 13, R.drawable.picture_economy, false));
        test.add(new ArticleRowViewModel("Oui, la news plus haute est vraie été test", "08:01-12/12/2020", writer, categoryFood, 1859265, 483, R.drawable.picture_economy,  false));
        return test;
    }
}
