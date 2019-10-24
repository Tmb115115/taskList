package com.tasklist.tasklist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

public class RemindService extends Service {
    private int sum;
    private int num1;
    private int num2;
    private int count1 = 0;
    private int flag;
    private String title;
    private String content;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        num1 = intent.getIntExtra("num1",0);//时
        num2 = intent.getIntExtra("num2",0);//分
        flag = intent.getIntExtra("flag",0x00000000);//今天还是明天
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Long seconds = Utils.getseconds(num1,num2,flag);
        long triggerAttime = SystemClock.elapsedRealtime()+seconds;
        Intent i = new Intent(this,AlarmReceiver.class);
        i.putExtra("title",title);
        i.putExtra("content",content);
        PendingIntent pi = PendingIntent.getBroadcast(this,count1++,i,0);//用count1的不同来实现任务不会被覆盖
        Log.d("RemindService.class",String.valueOf(count1));
        manager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAttime,pi);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
