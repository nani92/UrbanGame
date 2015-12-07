package com.example.nataliajastrzebska.urbangame;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.nataliajastrzebska.urbangame.GameInformation;
import com.example.nataliajastrzebska.urbangame.XMLPullParser;

import java.io.InputStream;

/**
 * Created by Przemysław on 2015-12-03.
 */

public class NotificationSender {
    private static NotificationSender notificationSender = null;
    private NotificationSender() {
    }
    //Instance getter
    public static NotificationSender getInstance() {
        if (notificationSender == null) {
            notificationSender = new NotificationSender();
        }
        return notificationSender;
    }
    public void sendNotification(String text, PendingIntent pendingIntent, Context context) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.notification_icon_small)
                        .setContentTitle("Urban Game")
                        .setContentText(text);
        if (!(pendingIntent == null)) mBuilder.setContentIntent(pendingIntent);
        Notification notification = mBuilder.build();
        if (Settings.getInstance().getVibrationEnabled())
            notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, notification);
    }
}
