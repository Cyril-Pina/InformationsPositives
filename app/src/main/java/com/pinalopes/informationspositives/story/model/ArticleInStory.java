package com.pinalopes.informationspositives.story.model;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.pinalopes.informationspositives.R;
import com.pinalopes.informationspositives.articles.model.ArticleActivity;
import com.pinalopes.informationspositives.categories.model.Category;
import com.pinalopes.informationspositives.databinding.ArticleInStoryBinding;
import com.pinalopes.informationspositives.story.viewmodel.ArticleInStoryViewModel;

import static com.pinalopes.informationspositives.Constants.COUNT_DOWN_INTERVAL;
import static com.pinalopes.informationspositives.Constants.NO_ANIM;
import static com.pinalopes.informationspositives.Constants.SWIPE_MAX_PERCENT;
import static com.pinalopes.informationspositives.Constants.TIMER_INITIAL_VALUE;

public class ArticleInStory extends AppCompatActivity {

    private static final String TAG = "ArticleInStory";

    private ArticleInStoryBinding binding;
    private StoryCountDownTimer storyCountDownTimer;
    private Story story;
    private StoryHeaderService storyHeaderService;

    private enum Story {
        NEXT_STORY,
        PREVIOUS_STORY
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ArticleInStoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initArticleInStoryView();
        startStoryCountDown();
        setOnClickStoryButtons();
        setOnStoryTouchListener();
        setOnMovementHeaderListener();
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

    private void initArticleInStoryView() {
        Category category = new Category("Faune", 0, R.drawable.ic_fauna);
        binding.setArticleInStoryViewModel(new ArticleInStoryViewModel("Un chiot sauvé miraculeusement par un jeune homme dans le département de Tarn",
                getString(R.string.text), category, R.drawable.puppy,"Michaël Doe"));
    }

    private void setOnClickStoryButtons() {
        binding.closeStoryButton.setOnClickListener(v -> closeStory());

        binding.seeArticleButton.setOnClickListener(v -> {
            storyCountDownTimer.stop();
            Intent intentArticle = new Intent(ArticleInStory.this, ArticleActivity.class);
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
            Intent intent = new Intent(ArticleInStory.this, ArticleInStory.class);
            startActivity(intent);
            finish();
        }
        storyCountDownTimer.stop();
    }
}