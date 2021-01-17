package com.pinalopes.informationspositives.story.model;

import android.animation.ObjectAnimator;
import android.os.CountDownTimer;
import android.view.animation.DecelerateInterpolator;

import com.pinalopes.informationspositives.databinding.ArticleInStoryBinding;

import static com.pinalopes.informationspositives.Constants.ANIMATOR_PROPERTY_NAME;
import static com.pinalopes.informationspositives.Constants.END_PROGRESS;
import static com.pinalopes.informationspositives.Constants.ONE;
import static com.pinalopes.informationspositives.Constants.ONE_SECOND_IN_MILLI;
import static com.pinalopes.informationspositives.Constants.ONE_THOUSAND;
import static com.pinalopes.informationspositives.Constants.TEN;

public class StoryCountDownTimer extends CountDownTimer {

    private final ArticleInStoryBinding binding;
    private final long countDownInterval;
    private long actualNbMillis;
    private final StoryTimerListener storyTimerListener;
    private boolean isStopped;

    public StoryCountDownTimer(long millisInFuture, long countDownInterval, ArticleInStoryBinding binding, StoryTimerListener storyTimerListener) {
        super(millisInFuture, countDownInterval);
        this.binding = binding;
        this.countDownInterval = countDownInterval;
        this.actualNbMillis = millisInFuture;
        this.storyTimerListener = storyTimerListener;
        this.isStopped = false;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        int nbSecond = (int) millisUntilFinished / ONE_THOUSAND;
        int previousProgress = nbSecondToProgress(nbSecond + ONE);
        int currentProgress = nbSecondToProgress(nbSecond);

        actualNbMillis = millisUntilFinished;
        binding.nbSecondsTimer.setText(String.valueOf(nbSecond));
        animateStoryProgressBar(previousProgress, currentProgress);
    }

    @Override
    public void onFinish() {
        if (!isStopped) {
            binding.storyProgressBar.setProgress(END_PROGRESS);
            storyTimerListener.onStoryFinished();
        }
    }

    public void stop() {
        this.cancel();
        this.isStopped = true;
    }
    public StoryCountDownTimer replay() {
        return (StoryCountDownTimer) new StoryCountDownTimer(actualNbMillis, countDownInterval, binding, storyTimerListener).start();
    }

    private void animateStoryProgressBar(int from, int to) {
        ObjectAnimator animation = ObjectAnimator.ofInt(binding.storyProgressBar, ANIMATOR_PROPERTY_NAME, from, to);
        animation.setDuration(ONE_SECOND_IN_MILLI);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

    private int nbSecondToProgress(int nbSecond) {
        return nbSecond * TEN;
    }

    public boolean isStopped() {
        return isStopped;
    }
}
