package com.dailypit.dp.Utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.dailypit.dp.Activity.Splash;
import com.dailypit.dp.MainActivity;
import com.dailypit.dp.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Uri sound = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.funny);
        final long[] VIBRATE_PATTERN = {0, 500};

        Map<String, String> data = remoteMessage.getData();
        String o_id = data.get("o_id").toString();
        String title = data.get("title").toString();
        String body = data.get("body").toString();
        String type = data.get("type").toString();

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("order_id",o_id);
        intent.putExtra("notificationType",type);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        String channelId = "videocall";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.applogo)
                .setSound(sound)
                .setVibrate(VIBRATE_PATTERN)
                .setContentTitle(title)
                .setContentText(body).setAutoCancel(true).setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();
            NotificationChannel channel = new NotificationChannel(channelId, "videocall", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setSound(sound, att);
            channel.setVibrationPattern(VIBRATE_PATTERN);
            channel.enableVibration(true);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());
    }

}