package com.tasklist.tasklist;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    public static NotificationManager notificationManager;
    public static String CHANNEL_1 = "channel1";
    private String title;
    @Override
    public void onReceive(Context context, Intent intent) {
        init(context);
        Intent intent2 = new Intent(context,MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context,0,intent2,0);
        title = intent.getStringExtra("title");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,CHANNEL_1);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("今日计划")
                .setContentText(title)
                .setContentIntent(pi)
                .setAutoCancel(true);
        notificationManager.notify(1,builder.build());
        Log.d("AlarmReceiver.class",title);
//        Intent intent1 = new Intent(context,RemindService.class);
//        context.startService(intent1);
    }
    public static void init(Context context){
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_1,
                    "通知渠道1", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("通知渠道的描述1");
            channel1.enableLights(true);
            channel1.setLightColor(Color.BLUE);
            notificationManager.createNotificationChannel(channel1);
        }
    }
}
