package com.tasklist.tasklist;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tasklist.tasklist.entity.Plan2;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;




public class EditActivity extends AppCompatActivity {
    private Button btn_time;
    private TextView time_tv;
    private Toolbar toolbar;
    private TextView toolbartitle;//标题
    private FloatingActionButton fab;
    private EditText editText1;
    private EditText editText2;
    private String title;
    private String text;
    private static int count;
    private int flag2;
    public static int hour = -1;
    public static int min = -1;
    DateFormat format = DateFormat.getDateTimeInstance();
    Calendar calendar= Calendar.getInstance(Locale.CHINA);
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ActivityCollector.addActivity(this);//将活动添加到活动管理器
        final Bundle bundle = getIntent().getExtras();//获取数据
        if (bundle != null){
            flag2 = bundle.getInt("flag");//获取flag
        }
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        swipeRefreshLayout = findViewById(R.id.refresh_edit);
//        pic = findViewById(R.id.image_edit);
        toolbar = findViewById(R.id.edit_toolbar);//实例化toolbar
        toolbartitle = toolbar.findViewById(R.id.title);
        toolbartitle.setText("编辑任务");//编辑标题
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);//实例化editText
        fab = findViewById(R.id.fab_save);//实例化悬浮窗
        btn_time = findViewById(R.id.btn_time);
        time_tv = findViewById(R.id.time_tv);

        setSupportActionBar(toolbar);
        if (flag2 == 0x00000006){
            editText1.setText(bundle.getString("title"));
            editText2.setText(bundle.getString("text"));
        }
        ActionBar actionBar = getSupportActionBar();//获取actionBar
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);//将返回按钮显示出来
        }
        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            showTimePickerDialog(EditActivity.this,0,time_tv,calendar);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {//刷新点击事件
            @Override
            public void onRefresh() {
                RefreshImage();//刷新图片
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {//悬浮窗点击事件
            @Override
            public void onClick(View v) {
                Intent intent;
                if (flag2 != 0x00000006 && flag2 != 0x00000007){
                    Date newtime = new Date();
                    Calendar calendar =Calendar.getInstance();//获取时间 年月日
                    calendar.add(Calendar.MONTH,1);//将月份+1，因为月份是0-11
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);//获取本地时间
                    sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));//初始化当前时区
                    String[] split = sdf.format(newtime).split(" ");//将年月日 与 时间分隔

                    title = editText1.getText().toString();//获取标题
                    text = editText2.getText().toString();//获取内容
                    int month = calendar.get(Calendar.MONTH);//获取月份
                    Plan2 plan2 = new Plan2();
                    plan2.setTitle(title);//存储标题
                    plan2.setText(text);//存储内容
                    plan2.setHour(hour);//任务开始时间
                    plan2.setMinute(min);
                    plan2.setCount(count);//用来删除闹钟
                    count = count+1;
                    Log.d("EditActivity.class",String.valueOf(count));
                    plan2.setIsCompleted(false);//将所有checkbox初始化为false
                    if (flag2==0x00000000){//如果是今天
                        plan2.setYear(calendar.get(Calendar.YEAR));//将年月日信息储存到数据库
                        plan2.setMonth(month);
                        plan2.setDay(calendar.get(Calendar.DAY_OF_MONTH));//calendar.get(Calendar.DAY_OF_MONTH)获取天数
                        intent = new Intent(EditActivity.this, MainActivity.class);
                        Bundle bundle2 = new Bundle();
                        bundle2.putInt("flag",flag2);//将标记传递过去
                        intent.putExtras(bundle2);
                        startActivity(intent);
                        Intent intent3 = new Intent(EditActivity.this,RemindService.class);//启动定时服务
                        intent3.putExtra("num1",hour);
                        intent3.putExtra("num2",min);
                        intent3.putExtra("flag",flag2);//传递标记 用来判断通知开始时间 是今天还是明天
                        intent3.putExtra("title",title);//传递标题
                        intent3.putExtra("content",text);//传递内容
                        startService(intent3);
                        finish();
                    }

                    if (flag2 ==0x00000001){//如果是明天
                        calendar.add(Calendar.DAY_OF_MONTH,1);//日期加一
                        plan2.setYear(calendar.get(Calendar.YEAR));//将年月日信息存储到数据库
                        plan2.setMonth(month);
                        plan2.setDay(calendar.get(Calendar.DAY_OF_MONTH));
                        intent = new Intent(EditActivity.this, MainActivity.class);
                        Bundle bundle2 = new Bundle();
                        bundle2.putInt("flag",flag2);//传递标记
                        intent.putExtras(bundle2);
                        startActivity(intent);
                        Intent intent5 = new Intent(EditActivity.this,RemindService.class);//启动定时服务
                        intent5.putExtra("num1",hour);
                        intent5.putExtra("num2",min);
                        intent5.putExtra("flag",flag2);//传递标记 用来判断通知开始时间是今天还是明天
                        startService(intent5);
                        finish();
                    }
                    if (flag2 ==0x00000005){//如果是指定日期
                        int year = bundle.getInt("year");
                        int Month = bundle.getInt("month")+1;
                        int dayOfmonth = bundle.getInt("dayOfmonth");//获取年月日信息
                        plan2.setYear(year);
                        plan2.setMonth(Month);
                        plan2.setDay(dayOfmonth);//存储年月日信息
                        intent = new Intent(EditActivity.this, MainActivity.class);
                        Bundle bundle2 = new Bundle();
                        bundle2.putInt("flag",flag2);
                        bundle2.putInt("year",bundle.getInt("year"));
                        bundle2.putInt("month",bundle.getInt("month"));
                        bundle2.putInt("dayOfmonth",bundle.getInt("dayOfmonth"));//将年月日信息以及标记传递过去
                        intent.putExtras(bundle2);
                        startActivity(intent);
                        finish();
                    }
                    plan2.setDate1(split[0]);
                    plan2.setDate2(split[1]);//将yyyy-MM-dd HH:mm:ss 分块存储
                    plan2.save();
                }
                if (flag2 == 0x00000006){//如果是修改数据
                    Plan2 plan2 = new Plan2();
                    plan2.setTitle(editText1.getText().toString());
                    plan2.setText(editText2.getText().toString());
                    plan2.setHour(hour);
                    plan2.setMinute(min);
                    plan2.updateAll("title = ? and text = ?",bundle.getString("title"),bundle.getString("text"));//更新数据
                    intent = new Intent(EditActivity.this, MainActivity.class);
                    startActivity(intent);
                    Intent intent4 = new Intent(EditActivity.this,RemindService.class);//启动定时服务
                    intent4.putExtra("num1",hour);
                    intent4.putExtra("num2",min);
                    intent4.putExtra("title",editText1.getText().toString());//将修改过的标题传递过去
                    intent4.putExtra("flag",0x00000000);
                    startService(intent4);
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//返回按钮点击事件
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(EditActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }return true;
    }
    private void RefreshImage(){//刷新图片
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(500);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
    public static void showTimePickerDialog(final Activity activity, int themeResId, final TextView tv, Calendar calendar) {
        // Calendar c = Calendar.getInstance();
        // 创建一个TimePickerDialog实例，并把它显示出来
        // 解释一哈，Activity是context的子类
        new TimePickerDialog( activity,themeResId,
                // 绑定监听器
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {//确定的逻辑
                        hour = hourOfDay;
                        min  = minute;
                    }

                }
                // 设置初始时间
                , calendar.get(Calendar.HOUR_OF_DAY)
                , calendar.get(Calendar.MINUTE)
                // true表示采用24小时制
                ,true).show();
    }

}
