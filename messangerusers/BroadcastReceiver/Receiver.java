package com.example.messangerusers.BroadcastReceiver;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.messangerusers.R;

public class Receiver extends BroadcastReceiver {
    static private NotificationManagerCompat notificationManager;

    public Receiver() { }

    @Override
    public void onReceive(Context context, Intent intent) { notificate(context); }

    static public void notificate(Context context) {
        notificationManager = NotificationManagerCompat.from(context);

        Notification notification = new NotificationCompat.Builder(context, App.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.parcel_image)
                .setContentTitle("New Parcel")
                .setContentText("A new parcel has just been added!")
                .build();
        notificationManager.notify(1, notification);
    }
}
