package com.pinalopes.informationspositives.articles.model;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.pinalopes.informationspositives.R;
import com.pinalopes.informationspositives.articles.viewmodel.ArticleViewModel;
import com.pinalopes.informationspositives.articles.viewmodel.RecommendationRowViewModel;
import com.pinalopes.informationspositives.categories.model.Category;
import com.pinalopes.informationspositives.databinding.ArticleBinding;
import com.pinalopes.informationspositives.storage.DataStorage;
import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;
import java.util.List;

import static com.pinalopes.informationspositives.Constants.DATA_ARTICLE;
import static com.pinalopes.informationspositives.Constants.END_DISLIKE_PROGRESS;
import static com.pinalopes.informationspositives.Constants.END_LIKE_PROGRESS;
import static com.pinalopes.informationspositives.Constants.START_DISLIKE_PROGRESS;
import static com.pinalopes.informationspositives.Constants.START_LIKE_PROGRESS;
import static com.pinalopes.informationspositives.Constants.TEXT_PLAIN;
import static com.pinalopes.informationspositives.Constants.URI_ARTICLE;

public class ArticleActivity extends AppCompatActivity {

    private ArticleBinding binding;
    private boolean isLiked;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(DataStorage.getUserSettings().getCurrentTheme());
        super.onCreate(savedInstanceState);
        binding = ArticleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Slidr.attach(this);

        initArticleView();
        fetchIntentFromUri();
        setNestedScrollViewListener();
        setOnClickHeaderButtons();
        setOnClickLikeIcon();
        initGridViewRecommendation();
    }

    private void setNestedScrollViewListener() {
        float heightArticleImage = getResources().getDimension(R.dimen.height_article_image);
        binding.articleScrollView.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            binding.getArticleViewModel().setHeaderVisible(scrollY >= heightArticleImage);
            binding.invalidateAll();
        });
    }

    private void setOnClickHeaderButtons() {
        binding.leftArrowButton.setOnClickListener(v -> finish());

        binding.shareArticleButton.setOnClickListener(v -> {
            Intent intentShare = new Intent();
            intentShare.setAction(Intent.ACTION_SEND);
            intentShare.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.shared_article_subject));
            intentShare.putExtra(Intent.EXTRA_TEXT, "TITRE DE L'ARTICLE\n\n" + "Petite description de l'article... " + URI_ARTICLE + "12");
            intentShare.setType(TEXT_PLAIN);
            Intent chooserIntentShare = Intent.createChooser(intentShare, getString(R.string.chooser_message));
            startActivity(chooserIntentShare);
        });
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
            binding.getArticleViewModel().setLiked(isLiked);
            binding.invalidateAll();
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
                category, 35026, 360, R.drawable.picture_fauna, false, getString(R.string.written_by_sample),isLiked));
    }

    private void fetchIntentFromUri() {
        Intent intent = getIntent();
        Uri uri = intent.getData();
        if (uri != null) {
            String idArticle = uri.getQueryParameter(DATA_ARTICLE);
            Log.d("ID Article", idArticle);
        }
    }

    private void initGridViewRecommendation() {
        List<RecommendationRowViewModel> test = new ArrayList<>();

        Category category = new Category("Nature", 0, R.drawable.ic_nature);
        Category categoryFauna = new Category("Faune", 0, R.drawable.ic_fauna);
        Category categoryFood = new Category("Alimentation", 0, R.drawable.ic_food);

        test.add(new RecommendationRowViewModel("Un chiot est sauver par Gaston du PMU", category, R.drawable.picture_fauna, true));
        test.add(new RecommendationRowViewModel("Sangoku a encore sauvé la terre top", category,R.drawable.picture_economy, true));
        test.add(new RecommendationRowViewModel("Macron donne 1million d'euros a un jeune sans abri", categoryFauna, R.drawable.picture_food, false));
        test.add(new RecommendationRowViewModel("Oui, la news plus haute est vraie été test", categoryFood, R.drawable.picture_sport, false));

        RecommendationAdapter adapter = new RecommendationAdapter(this, test);
        binding.recommendationGridView.setExpanded(true);
        binding.recommendationGridView.setAdapter(adapter);
    }
}
