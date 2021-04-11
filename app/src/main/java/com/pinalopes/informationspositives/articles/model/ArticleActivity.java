package com.pinalopes.informationspositives.articles.model;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pinalopes.informationspositives.R;
import com.pinalopes.informationspositives.articles.viewmodel.ArticleViewModel;
import com.pinalopes.informationspositives.databinding.ArticleBinding;
import com.pinalopes.informationspositives.feed.viewmodel.ArticleRowViewModel;
import com.pinalopes.informationspositives.storage.DataStorageHelper;
import com.r0adkll.slidr.Slidr;

import java.util.List;

import static com.pinalopes.informationspositives.Constants.ARTICLE_INFORMATION;
import static com.pinalopes.informationspositives.Constants.DATA_ARTICLE;
import static com.pinalopes.informationspositives.Constants.END_DISLIKE_PROGRESS;
import static com.pinalopes.informationspositives.Constants.END_LIKE_PROGRESS;
import static com.pinalopes.informationspositives.Constants.RECOMMENDED_ARTICLES;
import static com.pinalopes.informationspositives.Constants.START_DISLIKE_PROGRESS;
import static com.pinalopes.informationspositives.Constants.START_LIKE_PROGRESS;
import static com.pinalopes.informationspositives.Constants.TEXT_PLAIN;
import static com.pinalopes.informationspositives.Constants.URI_ARTICLE;

public class ArticleActivity extends AppCompatActivity {

    private ArticleBinding binding;
    private String articleAsString;
    private String recommendedArticlesAsString;
    private String linkToArticle;
    private boolean isLiked;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(DataStorageHelper.getUserSettings().getCurrentTheme());
        super.onCreate(savedInstanceState);
        binding = ArticleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Slidr.attach(this);

        getArticleInformation(savedInstanceState);
        fetchIntentFromUri();
        setNestedScrollViewListener();
        setOnClickHeaderButtons();
        setOnClickLikeIcon();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        outState.putString(ARTICLE_INFORMATION, articleAsString);
        outState.putString(RECOMMENDED_ARTICLES, recommendedArticlesAsString);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private void getArticleInformation(Bundle savedInstanceState) {
        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            articleAsString = extras.getString(ARTICLE_INFORMATION);
            recommendedArticlesAsString = extras.getString(RECOMMENDED_ARTICLES);
            setArticleViewModel();
        } else if (savedInstanceState != null) {
            articleAsString = savedInstanceState.getString(ARTICLE_INFORMATION);
            recommendedArticlesAsString = savedInstanceState.getString(RECOMMENDED_ARTICLES);
            setArticleViewModel();
        }
    }

    private void setArticleViewModel() {
        initGridViewRecommendation();
        ArticleViewModel articleViewModel = new ArticleViewModel(new Gson().fromJson(articleAsString, ArticleRowViewModel.class));
        linkToArticle = articleViewModel.getLinkToArticle();
        binding.setArticleViewModel(articleViewModel);
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

    private void fetchIntentFromUri() {
        Intent intent = getIntent();
        Uri uri = intent.getData();
        if (uri != null) {
            String idArticle = uri.getQueryParameter(DATA_ARTICLE);
            Log.d("ID Article", idArticle);
        }
    }

    private void initGridViewRecommendation() {
        List<ArticleRowViewModel> recommendations =
                new Gson().fromJson(recommendedArticlesAsString, new TypeToken<List<ArticleRowViewModel>>(){}.getType());

        RecommendationAdapter adapter = new RecommendationAdapter(
                this,
                recommendations,
                new Gson().fromJson(articleAsString, ArticleRowViewModel.class));
        binding.recommendationGridView.setExpanded(true);
        binding.recommendationGridView.setAdapter(adapter);
    }

    public void goToFullArticleOnClick(View view) {
        if (linkToArticle != null) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkToArticle));
            startActivity(browserIntent);
        }
    }
}
