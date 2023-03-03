package com.example.tudaapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;

public class FBase_Messaging extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String Title = remoteMessage.getNotification().getTitle();
        String Message = remoteMessage.getNotification().getBody();

        Log.i("Mess",Message);

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        sendNotification(Title, Message);

    }

    private void sendNotification(String Title, String Message) {

        Intent intent = new Intent();
        //intent.putExtras(b);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri Sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(getResources().getString(R.string.default_notification_channel_id),
                    "Tuda", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(Title);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), getResources().getString(R.string.default_notification_channel_id))

                .setContentTitle(Title)
                .setContentText(Message)
                .setAutoCancel(true)
                .setSound(Sound)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setLights(Color.YELLOW, 1000, 300);
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        assert notificationManager != null;
        notificationManager.notify(m, builder.build());
    }


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

                Log.i("tk",s);
    }
}
