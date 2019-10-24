package com.tasklist.tasklist;

import android.util.Log;

import java.util.Calendar;

public class Utils {
    private static Long seconds;
    public static Long getseconds(int num1,int num2,int flag){
        Calendar cal = Calendar.getInstance();
        if(flag ==0x00000000){
            cal.set(Calendar.HOUR_OF_DAY,num1);
            cal.set(Calendar.MINUTE,num2);
            cal.set(Calendar.SECOND,0);
            cal.set(Calendar.MILLISECOND,0);
            seconds = (cal.getTimeInMillis() - System.currentTimeMillis());
            Log.d("Utils.class",String.valueOf(seconds));
        }
        if (flag ==0x00000001){
            if (cal.getTimeInMillis() - System.currentTimeMillis() > 0){
                cal.set(Calendar.HOUR_OF_DAY,num1);
                cal.set(Calendar.MINUTE,num2);
                cal.set(Calendar.SECOND,0);
                cal.set(Calendar.MILLISECOND,0);
                seconds = (cal.getTimeInMillis() - System.currentTimeMillis())+86400000;
                Log.d("Utils.class",String.valueOf(seconds));
            }else{
                cal.set(Calendar.HOUR_OF_DAY,24);
                cal.set(Calendar.MINUTE,0);
                cal.set(Calendar.SECOND,0);
                cal.set(Calendar.MILLISECOND,0);
                seconds = (cal.getTimeInMillis() - System.currentTimeMillis())+num1*3600000+num2*60000;
                Log.d("Utils.class",String.valueOf(seconds));
            }
        }


        return seconds;
    }
}
