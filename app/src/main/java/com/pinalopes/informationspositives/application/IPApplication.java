package com.pinalopes.informationspositives.application;

import android.app.Application;

import androidx.room.Room;

import com.facebook.stetho.Stetho;
import com.pinalopes.informationspositives.R;
import com.pinalopes.informationspositives.notifications.NotificationsBroadcastReceiver;
import com.pinalopes.informationspositives.storage.LocalDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IPApplication extends Application {

    public static final NotificationsBroadcastReceiver
            notificationsBroadcastReceiver = new NotificationsBroadcastReceiver();

    public static final ExecutorService executorService = Executors.newFixedThreadPool(4);
    public static LocalDatabase localDB;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        localDB = Room.databaseBuilder(this, LocalDatabase.class, getString(R.string.local_db_name))
                .fallbackToDestructiveMigration()
                .build();
    }
}
