package com.pinalopes.informationspositives.application;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class IPApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
