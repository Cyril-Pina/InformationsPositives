package com.pinalopes.informationspositives;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Timer;
import java.util.TimerTask;

import static com.pinalopes.informationspositives.Constants.DRAWABLE;
import static com.pinalopes.informationspositives.Constants.PACKAGE_NAME;

public class LoadingService {

    private static final String LOADING_RES = "loading_";
    private static final int TOTAL_LOADING_IMAGE = 15;
    private static final int INDEX_RES_DEFAULT_VALUE = 1;
    private static final long DELAY_LOADING = 1000;
    private static final long PERIOD_LOADING = 1500;

    private Context context;
    private RelativeLayout loadingMainLayout;
    private ImageView loadingImageView;
    private Animation fadeOutAnimation;
    private Animation fadeInAnimation;
    private Timer timerLoading;
    private int indexRes;

    public LoadingService(Context context, RelativeLayout loadingMainLayout, ImageView loadingImageView) {
        this.context = context;
        this.loadingMainLayout = loadingMainLayout;
        this.loadingImageView = loadingImageView;

        indexRes = INDEX_RES_DEFAULT_VALUE;
        timerLoading = new Timer();
        fadeOutAnimation = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        fadeInAnimation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
    }

    public void start() {
        timerLoading.schedule(new TimerTask() {
            @Override
            public void run() {
                ((Activity) context).runOnUiThread(() -> {
                    indexRes++;
                    if (indexRes > TOTAL_LOADING_IMAGE)
                        indexRes = INDEX_RES_DEFAULT_VALUE;
                    int resId = context
                            .getResources()
                            .getIdentifier(getResourceName(indexRes), DRAWABLE, PACKAGE_NAME);
                    imageViewAnimatedChange(resId);
                });
            }
        }, DELAY_LOADING, PERIOD_LOADING);
    }

    public void stop() {
        loadingMainLayout.setVisibility(View.GONE);
    }

    private String getResourceName(int indexRes) {
        return LOADING_RES + indexRes;
    }

    private void imageViewAnimatedChange(final int resource) {
        fadeOutAnimation.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) {
                // onAnimationStart() ignored
            }

            @Override public void onAnimationRepeat(Animation animation) {
                // onAnimationRepeat() ignored
            }
            @Override public void onAnimationEnd(Animation animation)
            {
                loadingImageView.setImageResource(resource);
                fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {
                        // onAnimationStart() ignored
                    }

                    @Override public void onAnimationRepeat(Animation animation) {
                        // onAnimationRepeat() ignored
                    }
                    @Override public void onAnimationEnd(Animation animation) {
                        // onAnimationEnd() ignored
                    }
                });
                loadingImageView.startAnimation(fadeInAnimation);
            }
        });
        loadingImageView.startAnimation(fadeOutAnimation);
    }
}
