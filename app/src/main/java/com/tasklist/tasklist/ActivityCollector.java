package com.tasklist.tasklist;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityCollector {//活动管理器
    public static List<Activity> activities = new ArrayList<>();
    public static void addActivity(Activity activity){//添加活动
        activities.add(activity);
    }
    public static void removeActivity(Activity activity){//移除活动
        activities.remove(activity);
    }
    public static void finishAll(){//将所有活动finish();
        for (Activity activity:activities){
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
        activities.clear();
    }
}
