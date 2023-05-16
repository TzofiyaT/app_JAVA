package com.example.messangerusers.BroadcastReceiver;

import android.app.Notification;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.messangerusers.Entities.Parcel;
import com.example.messangerusers.R;
import com.example.messangerusers.data.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class MyService extends LifecycleService {

    private static String username ="";
    private Repository repository;
    private LiveData<List<Parcel>> parcels;
    final String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
    static private NotificationManagerCompat notificationManager;

    public MyService() { }

    @Override
    public void onCreate() {
        super.onCreate();
        repository = Repository.getRepositoryInstance();
        parcels = repository.getNoDeliveryPersonParcels();
    }

    void sendBroadcastToInformer(){
        Intent intent = new Intent();
        ComponentName cn = new ComponentName("com.example.messangerusers.BroadcastReceiver","com.example.messangerusers.BroadcastReceiver.Receiver");
        intent.setComponent(cn);
        sendBroadcast(intent);
    }
    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (intent != null) {
            username = intent.getStringExtra("username");
        }

        parcels.observe((LifecycleOwner) this, new Observer<List<Parcel>>() {
            @Override
            public void onChanged(@Nullable List<Parcel> newParcels) {
                for (Parcel obj : newParcels) {

                    if (!username.equals(obj.getRecipientName()) && isNew(obj.getUploadDate()))
                    //sendBroadcastToInformer();
                    {
                        notificationManager = NotificationManagerCompat.from(getApplicationContext());

                        Notification notification = new NotificationCompat.Builder(getApplicationContext(), App.CHANNEL_1_ID)
                                .setSmallIcon(R.drawable.parcel_image)
                                .setContentTitle("New Parcel")
                                .setContentText("A new parcel has just been added!")
                                .build();
                        notificationManager.notify(1, notification);
                    }
                }
            }
        });

        return START_STICKY;
    }

    private boolean isNew(String uploadDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date upload = sdf.parse(uploadDate);
            Date lastFiveMinutes = new Date(System.currentTimeMillis() - 300000);
            if(upload.after(lastFiveMinutes))
                return true;
        }
        catch(Exception ex) { }
        return false;
    }

    @Override
    public void onDestroy() { super.onDestroy(); }

    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return null;
    }
}