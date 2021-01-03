package com.pinalopes.informationspositives.articles.model;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.pinalopes.informationspositives.R;
import com.pinalopes.informationspositives.articles.viewmodel.ArticleViewModel;
import com.pinalopes.informationspositives.articles.viewmodel.RecommendationRowViewModel;
import com.pinalopes.informationspositives.categories.model.Category;
import com.pinalopes.informationspositives.databinding.ArticleBinding;
import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;
import java.util.List;

import static com.pinalopes.informationspositives.Constants.END_DISLIKE_PROGRESS;
import static com.pinalopes.informationspositives.Constants.END_LIKE_PROGRESS;
import static com.pinalopes.informationspositives.Constants.START_DISLIKE_PROGRESS;
import static com.pinalopes.informationspositives.Constants.START_LIKE_PROGRESS;

public class ArticleActivity extends AppCompatActivity {

    private ArticleBinding binding;

    private boolean isLiked;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ArticleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Slidr.attach(this);

        setNestedScrollViewListener();
        setOnClickPreviousArrow();
        setOnClickLikeIcon();
        initArticleView();
        initGridViewRecommendation();
    }

    private void setNestedScrollViewListener() {
        float heightArticleImage = getResources().getDimension(R.dimen.height_article_image);
        binding.articleScrollView.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY >= heightArticleImage) {
                binding.headerArticle.setVisibility(View.VISIBLE);
            } else {
                binding.headerArticle.setVisibility(View.GONE);
            }
        });
    }

    private void setOnClickPreviousArrow() {
        binding.leftArrowButton.setOnClickListener(v -> finish());
    }

    private void setOnClickLikeIcon() {
        isLiked = false;
        initLikeStatus();
        binding.likeAnimationView.setOnClickListener(v -> {
            if (isLiked) {
                binding.likeAnimationView.setMinAndMaxProgress(START_DISLIKE_PROGRESS, END_DISLIKE_PROGRESS);
            } else {
                binding.likeAnimationView.setMinAndMaxProgress(START_LIKE_PROGRESS, END_LIKE_PROGRESS);
            }
            binding.likeAnimationView.playAnimation();
            isLiked = !isLiked;
        });
    }

    private void initLikeStatus() {
        if (isLiked) {
            binding.likeAnimationView.setProgress(START_DISLIKE_PROGRESS);
        } else {
            binding.likeAnimationView.setProgress(START_LIKE_PROGRESS);
        }
    }

    private void initArticleView() {
        Category category = new Category("Nature", 0, R.drawable.ic_nature);
        binding.setArticleViewModel(new ArticleViewModel("Un chiot est sauver par Gaston du PMU",
                category, 35026, 360, R.drawable.picture_fauna, false, "Redigé par X le 25 décembre 2020 à 12h18",isLiked));
    }

    private void initGridViewRecommendation() {
        List<RecommendationRowViewModel> test = new ArrayList<>();

        Category category = new Category("Nature", 0, R.drawable.ic_nature);
        Category categoryFauna = new Category("Faune", 0, R.drawable.ic_fauna);
        Category categoryFood = new Category("Alimentation", 0, R.drawable.ic_food);

        test.add(new RecommendationRowViewModel("Un chiot est sauver par Gaston du PMU", category, R.drawable.picture_fauna, true));
        test.add(new RecommendationRowViewModel("Sangoku a encore sauvé la terre top", category,R.drawable.picture_economy, true));
        test.add(new RecommendationRowViewModel("Macron donne 1million d'euros à un jeune sans abri", categoryFauna, R.drawable.picture_food, false));
        test.add(new RecommendationRowViewModel("Oui, la news plus haute est vraie été test", categoryFood, R.drawable.picture_sport, false));

        RecommendationAdapter adapter = new RecommendationAdapter(this, test);
        binding.recommendationGridView.setExpanded(true);
        binding.recommendationGridView.setAdapter(adapter);
    }
}
