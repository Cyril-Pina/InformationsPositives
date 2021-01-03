package com.pinalopes.informationspositives.feed.dagger;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.pinalopes.informationspositives.LoadingService;

import dagger.Module;
import dagger.Provides;

@Module
public class LoadingModule {

    private final Context context;
    private final RelativeLayout loadingMainLayout;
    private final ImageView loadingImageView;

    public LoadingModule(Context context, RelativeLayout loadingMainLayout, ImageView loadingImageView) {
        this.context = context;
        this.loadingMainLayout = loadingMainLayout;
        this.loadingImageView = loadingImageView;
    }

    @Provides
    LoadingService providesLoadingService() {
        LoadingService loadingService = new LoadingService(context, loadingMainLayout, loadingImageView);
        loadingService.start();
        return loadingService;
    }
}
