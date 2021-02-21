package com.pinalopes.informationspositives.categories.model;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.pinalopes.informationspositives.R;
import com.pinalopes.informationspositives.categories.viewmodel.ArticleCategoryViewModel;
import com.pinalopes.informationspositives.categories.viewmodel.CategoryViewModel;
import com.pinalopes.informationspositives.databinding.CategoryBinding;
import com.pinalopes.informationspositives.storage.DataStorage;
import com.pinalopes.informationspositives.utils.ResourceUtils;
import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;
import java.util.List;

import static com.pinalopes.informationspositives.Constants.CURRENT_CATEGORY;
import static com.pinalopes.informationspositives.Constants.ELEVATION_TEN;
import static com.pinalopes.informationspositives.Constants.NO_BACKGROUND_RESOURCE;
import static com.pinalopes.informationspositives.Constants.NO_ELEVATION;

public class CategoryActivity extends AppCompatActivity {

    private CategoryBinding binding;
    private Category category;

    private int currentThemeId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        currentThemeId = DataStorage.getUserSettings().getCurrentTheme();
        setTheme(currentThemeId);
        super.onCreate(savedInstanceState);
        binding = CategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Slidr.attach(this);

        initCategoryView(savedInstanceState);
        setNestedScrollViewListener();
        setOnClickHeaderButtons();
        initArticleCategoryRecyclerView();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(CURRENT_CATEGORY, new Gson().toJson(category));
        super.onSaveInstanceState(outState);
    }

    private void initCategoryView(Bundle savedInstanceState) {
        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            category = new Gson().fromJson(extras.getString(CURRENT_CATEGORY), Category.class);
        } else if (savedInstanceState != null) {
            category = new Gson().fromJson(savedInstanceState.getString(CURRENT_CATEGORY), Category.class);
        }
        binding.setCategoryViewModel(new CategoryViewModel(category, category.getDescFromName(this)));
    }

    private void setNestedScrollViewListener() {
        final double boundaryChangeColor = getResources().getDimension(R.dimen.height_category_header) * 0.7;
        final int colorBlack = ContextCompat.getColor(this, android.R.color.black);
        final int colorWhite = ContextCompat.getColor(this, android.R.color.white);
        final Animation fadeInAnimation  = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        final Bitmap iconCategoryWhite = ResourceUtils.imgResToBitmap(this, category.getCategoryIcon());
        final Bitmap iconCategoryBlack = ResourceUtils.imgResToBitmap(this,
                ResourceUtils.getCategoryBlackIcon(this, category.getCategoryIcon()));
        final int leftArrowWhiteRes = R.drawable.left_arrow_white_selector;
        final int leftArrowBlackRes = R.drawable.left_arrow_selector;

        binding.articleCategoryScrollView.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY <= boundaryChangeColor && oldScrollY > boundaryChangeColor) {
                if (currentThemeId == R.style.AppTheme_NoActionBar) {
                    animateTextColorChanges(colorWhite, fadeInAnimation);
                    animateImageSrcChanges(iconCategoryWhite, fadeInAnimation);
                    animateImageButtonChanges(leftArrowWhiteRes, fadeInAnimation);
                }
                updateHeaderBackground(NO_ELEVATION, NO_BACKGROUND_RESOURCE);
            } else if (scrollY > boundaryChangeColor && oldScrollY <= boundaryChangeColor) {
                if (currentThemeId == R.style.AppTheme_NoActionBar) {
                    animateTextColorChanges(colorBlack, fadeInAnimation);
                    updateHeaderBackground(ELEVATION_TEN, Color.WHITE);
                    animateImageSrcChanges(iconCategoryBlack, fadeInAnimation);
                    animateImageButtonChanges(leftArrowBlackRes, fadeInAnimation);
                } else {
                    updateHeaderBackground(ELEVATION_TEN, Color.BLACK);
                }
            }
        });
    }

    private void setOnClickHeaderButtons() {
        binding.leftArrowButton.setOnClickListener(v -> finish());
    }

    private void initArticleCategoryRecyclerView() {
        RecyclerView articleCategoryRecyclerView = binding.articleCategoryRecyclerView;
        LinearLayoutManager layout = new LinearLayoutManager(this);

        List<ArticleCategoryViewModel> test = new ArrayList<>();
        test.add(new ArticleCategoryViewModel("Un chiot est sauvers par Gaston du PMU", "19:25-12/12/2020", "Michael Jaqueson", 1802, 235, R.drawable.picture_economy, true));
        test.add(new ArticleCategoryViewModel("Sangoku a encore sauvé laa terre top", "17:10-12/12/2020", "Miki Mike", 36820, 13, R.drawable.picture_economy, true));
        test.add(new ArticleCategoryViewModel("Macron donne 1million d'eusros e un jeune sans abri", "18:02-12/12/2020", "Michel Jaqueson", 36974, 13, R.drawable.picture_economy, false));
        test.add(new ArticleCategoryViewModel("Oui, la news plus haute esta vraie été test", "06:01-12/12/2020", "Brigitte Bardot", 1859265, 483, R.drawable.picture_economy,  false));
        test.add(new ArticleCategoryViewModel("Un chiot est sauver par Gastodn du PMU", "20:25-12/12/2020", "Mickael Jaqueson", 1802, 235, R.drawable.picture_economy, true));
        test.add(new ArticleCategoryViewModel("Sangoku a encore sauvé la terrde top", "09:10-12/12/2020", "Mikky Mike", 36820, 13, R.drawable.picture_economy, true));
        test.add(new ArticleCategoryViewModel("Macron donne 1million d'euros ag un jeune sans abri", "02:02-12/12/2020", "Micael Jaqueson", 36974, 13, R.drawable.picture_economy, false));
        test.add(new ArticleCategoryViewModel("Oui, la news plus haute est vraire été test", "08:01-12/12/2020", "Brigittes Bardot", 1859265, 483, R.drawable.picture_economy,  false));
        test.add(new ArticleCategoryViewModel("Un chiot est sauver par Gaston du PMU", "18:25-12/12/2020", "Michelle Jaqueson", 1802, 235, R.drawable.picture_economy, true));
        test.add(new ArticleCategoryViewModel("Sangoku a encore sauvé la terre top", "16:10-12/12/2020", "Mikitty Mike", 36820, 13, R.drawable.picture_economy, true));
        test.add(new ArticleCategoryViewModel("Macron donne 1million d'euros z un jeune sans abri", "08:30-12/12/2020", "Mickel Jaqueson", 36974, 13, R.drawable.picture_economy, false));
        test.add(new ArticleCategoryViewModel("Oui, la news plus haute est vraie été test", "08:59-12/12/2020", "Brigittef Bardot", 1859265, 483, R.drawable.picture_economy,  false));

        articleCategoryRecyclerView.setLayoutManager(layout);
        ArticleCategoryRecyclerAdapter adapter = new ArticleCategoryRecyclerAdapter(test);
        articleCategoryRecyclerView.setAdapter(adapter);
    }

    private void animateTextColorChanges(int newColor, Animation textColorInAnim) {
        binding.categoryTitle.setTextColor(newColor);
        binding.categoryTitle.startAnimation(textColorInAnim);
    }

    private void animateImageSrcChanges(Bitmap newImage, Animation imageBitmapInAnim) {
        binding.categoryIcon.setImageBitmap(newImage);
        binding.categoryIcon.startAnimation(imageBitmapInAnim);
    }

    private void animateImageButtonChanges(int newImageRes,  Animation imageResInAnim) {
        binding.leftArrowButton.setImageResource(newImageRes);
        binding.leftArrowButton.startAnimation(imageResInAnim);
    }

    private void updateHeaderBackground(float elevation, int color) {
        binding.categoryTitleLayout.setElevation(elevation);
        if (color == NO_BACKGROUND_RESOURCE) {
            binding.categoryTitleLayout.setBackgroundResource(NO_BACKGROUND_RESOURCE);
        } else {
            binding.categoryTitleLayout.setBackgroundColor(color);
        }
    }
}
