package com.finalproject.milestone_readbout.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;
import com.finalproject.milestone_readbout.MainActivity;
import com.finalproject.milestone_readbout.R;
import java.util.Random;

public class NotificationDecorator {
    private static final String TAG = "NotificationDecorator";
    private static final String CHANNEL_ID = "My Channel";
    private final Context context;
    private final NotificationManager notificationMgr;

    public NotificationDecorator(Context context, NotificationManager notificationManager) {
        this.context = context;
        this.notificationMgr = notificationManager;
    }

    public void displayExpandableNotification(String title, String body) {
        Log.d(TAG, "displayExpandableNotification Called..");

        if (true) {
            Log.d(TAG, "displayExpandableNotification Called again .");
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                    intent, PendingIntent.FLAG_IMMUTABLE);

            // notification message
            try {
                /** Add RemoteViews to show custom styles outside of the app */
                RemoteViews expandedView = new RemoteViews(context.getPackageName(), R.layout.custom_notification);
                expandedView.setTextViewText(R.id.notificationTitle, title);
                expandedView.setTextViewText(R.id.notificationBody, body);

                Notification noti;

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    noti = new Notification.Builder(context)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setStyle(new Notification.DecoratedMediaCustomViewStyle())
                            .setCustomContentView(expandedView)
                            .setContentIntent(contentIntent)
                            .setAutoCancel(true)
                            .setChannelId(CHANNEL_ID)
                            .build();
                    notificationMgr.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "New Channel", NotificationManager.IMPORTANCE_HIGH));
                } else {
                    noti = new Notification.Builder(context)
                            .setCustomContentView(expandedView)
                            .setContentIntent(contentIntent)
                            .setAutoCancel(true)
                            .setLights(Color.BLUE, 1000, 1000)
                            .build();

                }
                notificationMgr.notify(new Random().nextInt(), noti);
                noti.flags |= Notification.FLAG_AUTO_CANCEL;
                Log.d(TAG, "Inside try block notification");
            } catch (IllegalArgumentException e) {
                Log.d(TAG, "Inside catch block notification");
                Log.e(TAG, e.getMessage());
            }
        }
    }
}