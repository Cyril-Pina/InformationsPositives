package com.pinalopes.informationspositives.story.model;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pinalopes.informationspositives.R;
import com.pinalopes.informationspositives.articles.model.ArticleActivity;
import com.pinalopes.informationspositives.articles.viewmodel.ArticleViewModel;
import com.pinalopes.informationspositives.databinding.ArticleInStoryBinding;
import com.pinalopes.informationspositives.utils.AdapterUtils;

import java.util.List;

import static com.pinalopes.informationspositives.Constants.ARTICLES_IN_STORY;
import static com.pinalopes.informationspositives.Constants.ARTICLE_INFORMATION;
import static com.pinalopes.informationspositives.Constants.COUNT_DOWN_INTERVAL;
import static com.pinalopes.informationspositives.Constants.CURRENT_STORY_INDEX;
import static com.pinalopes.informationspositives.Constants.INDEX_FIRST_STORY;
import static com.pinalopes.informationspositives.Constants.LAST_INDEX_BOUND;
import static com.pinalopes.informationspositives.Constants.NEXT_STORY_VALUE;
import static com.pinalopes.informationspositives.Constants.NO_ANIM;
import static com.pinalopes.informationspositives.Constants.PREVIOUS_STORY_VALUE;
import static com.pinalopes.informationspositives.Constants.RECOMMENDED_ARTICLES;
import static com.pinalopes.informationspositives.Constants.SWIPE_MAX_PERCENT;
import static com.pinalopes.informationspositives.Constants.TIMER_INITIAL_VALUE;

public class ArticleInStory extends AppCompatActivity {

    private static final String TAG = "ArticleInStory";

    private ArticleInStoryBinding binding;
    private StoryCountDownTimer storyCountDownTimer;
    private Story story;
    private StoryHeaderService storyHeaderService;
    private List<ArticleViewModel> articlesInStoryViewModel;
    private int currentStoryIndex;

    private enum Story {
        NEXT_STORY,
        PREVIOUS_STORY
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ArticleInStoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initArticleInStoryView(savedInstanceState);
        startStoryCountDown();
        setOnClickStoryButtons();
        setOnStoryTouchListener();
        setOnMovementHeaderListener();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(ARTICLES_IN_STORY, new Gson().toJson(articlesInStoryViewModel));
        outState.putInt(CURRENT_STORY_INDEX, currentStoryIndex);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        closeStory();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (this.story != null && this.story.equals(Story.NEXT_STORY)) {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if (this.story != null && this.story.equals(Story.PREVIOUS_STORY)){
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            return storyHeaderService.setNewMovement(event);
        } catch (NullPointerException npe) {
            Log.e(TAG, npe.toString());
        }
        return super.onTouchEvent(event);
    }

    private void initArticleInStoryView(Bundle savedInstanceState) {
        Gson gson = new Gson();
        String articlesInStoryAsString;
        if (savedInstanceState != null) {
            articlesInStoryAsString = savedInstanceState.getString(ARTICLES_IN_STORY);
            articlesInStoryViewModel = gson.fromJson(articlesInStoryAsString, new TypeToken<List<ArticleViewModel>>(){}.getType());
            currentStoryIndex = savedInstanceState.getInt(CURRENT_STORY_INDEX);
            binding.setArticleViewModel(articlesInStoryViewModel.get(currentStoryIndex));
        } else if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            articlesInStoryAsString = extras.getString(ARTICLES_IN_STORY);
            articlesInStoryViewModel = gson.fromJson(articlesInStoryAsString, new TypeToken<List<ArticleViewModel>>(){}.getType());
            currentStoryIndex = extras.getInt(CURRENT_STORY_INDEX);
            binding.setArticleViewModel(articlesInStoryViewModel.get(currentStoryIndex));
        }
    }

    private void setOnClickStoryButtons() {
        binding.closeStoryButton.setOnClickListener(v -> closeStory());

        binding.seeArticleButton.setOnClickListener(v -> {
            storyCountDownTimer.stop();
            Intent intentArticle = new Intent(this, ArticleActivity.class);
            intentArticle.putExtra(ARTICLE_INFORMATION, new Gson().toJson(articlesInStoryViewModel.get(currentStoryIndex)));
            intentArticle.putExtra(RECOMMENDED_ARTICLES, AdapterUtils.getRecommendedArticlesFromArticle(articlesInStoryViewModel, currentStoryIndex));
            startActivity(intentArticle);
            finish();
        });
    }

    private void closeStory() {
        storyCountDownTimer.stop();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void setOnStoryTouchListener() {
        binding.storyView.setOnTouchListener((v, event) -> {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    setStoryElementsVisibility(View.INVISIBLE);
                    pauseStoryCountDown();
                    return true;
                case MotionEvent.ACTION_UP:
                    setStoryElementsVisibility(View.VISIBLE);
                    replayStoryCountDown();
                    v.performClick();
                    return true;
                default:
                    return false;
            }
        });

        binding.nextViewClickable.setOnClickListener(v -> changeStory(Story.NEXT_STORY));
        binding.previousViewClickable.setOnClickListener(v -> changeStory(Story.PREVIOUS_STORY));
    }

    private void setOnMovementHeaderListener() {
        storyHeaderService = new StoryHeaderService(binding.storyMainLayout, SWIPE_MAX_PERCENT, () -> {
            storyCountDownTimer.stop();
            binding.storyMainLayout.animate()
                    .setDuration(200)
                    .translationY(binding.storyMainLayout.getHeight())
                    .withEndAction(() -> {
                        finish();
                        overridePendingTransition(NO_ANIM, NO_ANIM);
                    });
        });

        binding.storyHeaderView.setOnTouchListener((v, event) -> {
            v.performClick();
            return storyHeaderService.setInitialY(event);
        });
    }

    private void setStoryElementsVisibility(int visibility) {
        binding.seeArticleButton.setVisibility(visibility);
        binding.closeStoryButton.setVisibility(visibility);
        binding.storyCountDownLayout.setVisibility(visibility);
    }

    private void startStoryCountDown() {
        storyCountDownTimer = (StoryCountDownTimer)
                new StoryCountDownTimer(TIMER_INITIAL_VALUE, COUNT_DOWN_INTERVAL, binding, () -> changeStory(Story.NEXT_STORY)).start();
    }

    private void pauseStoryCountDown() {
        storyCountDownTimer.stop();
    }

    private void replayStoryCountDown() {
        storyCountDownTimer = storyCountDownTimer.replay();
    }

    private void changeStory(Story story) {
        this.story = story;
        if (!storyCountDownTimer.isStopped()) {
            storyCountDownTimer.stop();
            Intent intentArticleStory = new Intent(ArticleInStory.this, ArticleInStory.class);
            intentArticleStory.putExtra(ARTICLES_IN_STORY, new Gson().toJson(articlesInStoryViewModel));
            intentArticleStory.putExtra(CURRENT_STORY_INDEX,
                    getStoryDirection(currentStoryIndex, articlesInStoryViewModel.size() - LAST_INDEX_BOUND, story));
            startActivity(intentArticleStory);
            finish();
        }
        storyCountDownTimer.stop();
    }

    private int getStoryDirection(int currentStoryIndex, int lastStoryIndex, Story story) {
        if (currentStoryIndex == 0 && story == Story.PREVIOUS_STORY) {
            return lastStoryIndex;
        } else if (currentStoryIndex == lastStoryIndex && story == Story.NEXT_STORY) {
            return INDEX_FIRST_STORY;
        } else {
            return story == Story.NEXT_STORY ? currentStoryIndex + NEXT_STORY_VALUE : currentStoryIndex + PREVIOUS_STORY_VALUE;
        }
    }
}