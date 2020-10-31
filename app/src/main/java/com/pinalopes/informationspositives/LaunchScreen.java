package com.pinalopes.informationspositives;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.pinalopes.informationspositives.Home.Model.MainActivity;
import com.pinalopes.informationspositives.databinding.LaunchScreenBinding;

public class LaunchScreen extends AppCompatActivity {

    private static final String NAME_IDENTIFIER = "navigation_bar_height";
    private static final String DIMEN_IDENTIFIER = "dimen";
    private static final String PACKAGE_IDENTIFIER = "android";
    private static final float CIRCLE_MARGIN = 300;
    private static final long DELAY_BEFORE_ANIMATION = 1500;
    private static final long DURATION_ANIMATION = 2000;
    private static final int DURATION_TRANSITION = (int) DURATION_ANIMATION;
    private LaunchScreenBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LaunchScreenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ViewTreeObserver viewTreeObserver = binding.circleView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.circleView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                startAnimation();
            }
        });
    }

    private void startAnimation() {
        float scaleXY = getScaleXY();
        binding.circleView.animate()
                .scaleY(scaleXY)
                .scaleX(scaleXY)
                .setListener(getAnimatorListener())
                .setStartDelay(DELAY_BEFORE_ANIMATION)
                .setDuration(DURATION_ANIMATION);
    }

    private Animator.AnimatorListener getAnimatorListener() {
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                TransitionDrawable transition = (TransitionDrawable) binding.circleView.getBackground();
                transition.startTransition(DURATION_TRANSITION);
            }
            @Override
            public void onAnimationEnd(Animator animator) {
                startActivity(new Intent(LaunchScreen.this, MainActivity.class));
                overridePendingTransition(0, 0);
                finish();
            }
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        };
    }

    private float getScaleXY() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float bottomNavBarHeight = getResources().getDimensionPixelSize(
                getResources().getIdentifier(NAME_IDENTIFIER, DIMEN_IDENTIFIER, PACKAGE_IDENTIFIER));

        return (displayMetrics.heightPixels + bottomNavBarHeight + CIRCLE_MARGIN)
                / (float) binding.circleView.getMeasuredHeight();
    }
}
