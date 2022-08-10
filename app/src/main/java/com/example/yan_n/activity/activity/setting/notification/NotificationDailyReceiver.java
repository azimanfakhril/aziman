package com.example.yan_n.activity.activity.setting.notification;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.yan_n.R;
import com.example.yan_n.activity.fragment.MainActivity;

import java.util.Calendar;

public class NotificationDailyReceiver extends BroadcastReceiver {

    private static final String NOTIFICATION_CHANNEL_ID = "channel_01";
    private static final int NOTIFICATION_ID = 100;

    public NotificationDailyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        sendNotification(context, context.getString(R.string.app_name), "Ada Berita Baru!, Daily News");
    }

    public void setDailyAlarm(Context context) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationDailyReceiver.class);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 1);
        calendar.set(Calendar.SECOND, 0);

        @SuppressLint("InlinedApi") PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent,  PendingIntent.FLAG_MUTABLE);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    private void sendNotification(Context context, String title, String desc) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, MainActivity.class);

        @SuppressLint("InlinedApi") PendingIntent pendingIntent = PendingIntent.getActivity(context, NotificationDailyReceiver.NOTIFICATION_ID, intent,
                PendingIntent.FLAG_MUTABLE);

        Uri uriTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.icon_news)
                .setContentTitle(title)
                .setContentText(desc)
                .setContentIntent(pendingIntent)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setSound(uriTone);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            builder.setChannelId(NOTIFICATION_CHANNEL_ID);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(NotificationDailyReceiver.NOTIFICATION_ID, builder.build());
    }

    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(getPendingIntent(context));
    }

    @SuppressLint("InlinedApi")
    private static PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, NotificationHeadlineReceiver.class);
        return PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent, PendingIntent.FLAG_MUTABLE);
    }
}
