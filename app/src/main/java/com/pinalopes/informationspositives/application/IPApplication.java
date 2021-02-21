package com.pinalopes.informationspositives.application;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.pinalopes.informationspositives.notifications.NotificationsBroadcastReceiver;

public class IPApplication extends Application {

    public static final NotificationsBroadcastReceiver
            notificationsBroadcastReceiver = new NotificationsBroadcastReceiver();

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
