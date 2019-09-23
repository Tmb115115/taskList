package com.tasklist.tasklist.item;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
        ActivityCollector.addActivity(this);
        toolbar = findViewById(R.id.Calendar_toolbar);
        setSupportActionBar(toolbar);
        title = (TextView)findViewById(R.id.Ctitle);
        title.setText("日期选择");
        cv = (CalendarView)findViewById(R.id.calendarview);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);//将返回按钮显示出来
        }
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Intent intent = new Intent(CalendarActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("year",year);
                bundle.putInt("month",month);
                bundle.putInt("dayOfmonth",dayOfMonth);
                bundle.putInt("flag",0x00000005);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
        }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(CalendarActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }return true;
    }
}
