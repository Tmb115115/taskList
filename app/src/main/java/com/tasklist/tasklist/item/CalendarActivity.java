package com.tasklist.tasklist.item;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import com.tasklist.tasklist.ActivityCollector;
import com.tasklist.tasklist.MainActivity;
import com.tasklist.tasklist.R;


public class CalendarActivity extends AppCompatActivity {
    private TextView title;
    private Toolbar toolbar;
    private CalendarView cv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ActivityCollector.addActivity(this);//将活动添加到活动管理器
        toolbar = findViewById(R.id.Calendar_toolbar);//实例化标题栏
        title = (TextView)findViewById(R.id.Ctitle);//实例化标题
        cv = (CalendarView)findViewById(R.id.calendarview);//实例化日历

        title.setText("日期选择");
        setSupportActionBar(toolbar);//建立标题栏
        ActionBar actionBar = getSupportActionBar();

        if (Build.VERSION.SDK_INT>=21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);//将返回按钮显示出来
        }
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {//日历点击事件
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Intent intent = new Intent(CalendarActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("year",year);
                bundle.putInt("month",month);
                bundle.putInt("dayOfmonth",dayOfMonth);
                bundle.putInt("flag",0x00000005);//存入数据
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
        }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//返回按钮
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(CalendarActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }return true;
    }
}
