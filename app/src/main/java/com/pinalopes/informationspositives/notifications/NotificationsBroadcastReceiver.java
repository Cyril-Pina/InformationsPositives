package com.pinalopes.informationspositives.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pinalopes.informationspositives.R;

import java.util.Calendar;

public class NotificationsBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "NotificationsBroadcastR";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Notification set");
        Notification.getInstance().createNotification(context,
                context.getString(R.string.notification_title),
                context.getString(R.string.notification_desc));
    }

    public void setNotificationsAlarm(Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 9);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intentNotificationsService = new Intent(context, NotificationsBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentNotificationsService, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void cancelNotificationsAlarm(Context context) {
        Intent intent = new Intent(context, NotificationsBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}
