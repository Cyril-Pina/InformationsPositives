package com.pinalopes.informationspositives;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.transition.ChangeBounds;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

public class TransitionService {

    private TransitionService() {
        throw new AssertionError();
    }

    public static void setLayoutTransition(ViewGroup sceneRoot, View targetView, View targetView2, long startDelay, long duration) {
        TransitionManager.beginDelayedTransition(sceneRoot, new ChangeBounds()
                .addTarget(targetView)
                .addTarget(targetView2)
                .setStartDelay(startDelay)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(duration)
                .addListener(new Transition.TransitionListener() {
                    @Override
                    public void onTransitionStart(@NonNull Transition transition) {
                        // onTransitionStart() ignored
                    }
                    @Override
                    public void onTransitionEnd(@NonNull Transition transition) {
                        // onTransitionEnd() ignored
                    }
                    @Override
                    public void onTransitionCancel(@NonNull Transition transition) {
                        // onTransitionCancel() ignored
                    }
                    @Override
                    public void onTransitionPause(@NonNull Transition transition) {
                        // onTransitionPause() ignored
                    }
                    @Override
                    public void onTransitionResume(@NonNull Transition transition) {
                        // onTransitionResume() ignored
                    }
        }));
    }

    public static void setLayoutTransition(Transition.TransitionListener transitionListener, ViewGroup sceneRoot, View targetView, View targetView2, long startDelay, long duration) {
        TransitionManager.beginDelayedTransition(sceneRoot, new ChangeBounds()
                .addTarget(targetView)
                .addTarget(targetView2)
                .setStartDelay(startDelay)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(duration)
                .addListener(transitionListener));
    }
}
