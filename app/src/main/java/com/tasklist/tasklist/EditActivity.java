package com.tasklist.tasklist;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.tasklist.tasklist.entity.Plan2;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class EditActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbartitle;
    private FloatingActionButton fab;
    private EditText editText1;
    private EditText editText2;
    private String title;
    private String text;
    private int flag2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        final Bundle bundle = getIntent().getExtras();
        if (bundle !=null){
            flag2 = bundle.getInt("flag");
        }
        ActivityCollector.addActivity(this);
        toolbar = findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);
        toolbartitle = toolbar.findViewById(R.id.title);
        toolbartitle.setText("编辑任务");
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        fab = findViewById(R.id.fab_save);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                Date newtime = new Date();
                Calendar calendar =Calendar.getInstance();
                calendar.add(Calendar.MONTH,1);//将月份+1，因为月份是0-11
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);//获取本地时间
                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));//初始化当前时区
                String[] split = sdf.format(newtime).split(" ");//将年月日 与 时间分隔
                title = editText1.getText().toString();//获取标题
                text = editText2.getText().toString();//获取内容
                int month = calendar.get(Calendar.MONTH);//获取月份
                Plan2 plan2 = new Plan2();
                plan2.setTitle(title);//存储标题
                plan2.setText(text);//存储内容
                plan2.setIsCompleted(false);
                if (flag2==0x00000000){//如果是今天
                    plan2.setYear(calendar.get(Calendar.YEAR));
                    plan2.setMonth(month);
                    plan2.setDay(calendar.get(Calendar.DAY_OF_MONTH));//calendar.get(Calendar.DAY_OF_MONTH)获取天数
                }

                if (flag2 ==0x00000001){//如果是明天
                    calendar.add(Calendar.DAY_OF_MONTH,1);
                    plan2.setYear(calendar.get(Calendar.YEAR));
                    plan2.setMonth(month);
                    plan2.setDay(calendar.get(Calendar.DAY_OF_MONTH));
                }
                if (flag2 ==0x00000005){//如果是指定日期
                    int year = bundle.getInt("year");
                    int Month = bundle.getInt("month")+1;
                    int dayOfmonth = bundle.getInt("dayOfmonth");
                    plan2.setYear(year);
                    plan2.setMonth(Month);
                    plan2.setDay(dayOfmonth);
                }
                plan2.setDate1(split[0]);
                plan2.setDate2(split[1]);
                plan2.save();
                if (flag2==0x00000000) {
                    intent = new Intent(EditActivity.this, MainActivity.class);
                    Bundle bundle2 = new Bundle();
                    bundle2.putInt("flag",flag2);
                    intent.putExtras(bundle2);
                    startActivity(intent);
                }
                if (flag2==0x00000001) {
                    intent = new Intent(EditActivity.this, MainActivity.class);
                    Bundle bundle2 = new Bundle();
                    bundle2.putInt("flag",flag2);
                    intent.putExtras(bundle2);
                    startActivity(intent);
                }
                if (flag2==0x00000005) {
                    intent = new Intent(EditActivity.this, MainActivity.class);
                    Bundle bundle2 = new Bundle();
                    bundle2.putInt("flag",flag2);
                    bundle2.putInt("year",bundle.getInt("year"));
                    bundle2.putInt("month",bundle.getInt("month"));
                    bundle2.putInt("dayOfmonth",bundle.getInt("dayOfmonth"));
                    Log.d("eeeeeeeeeeee",""+flag2);
                    intent.putExtras(bundle2);
                    startActivity(intent);
                }


            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(EditActivity.this,MainActivity.class);
                startActivity(intent);
                break;
        }return true;
    }

    @Override
    protected void onRestart() {//onRestart比MainActivity的onDestroy要先执行
        super.onRestart();
        ActivityCollector.finishAll();//销毁所有活动
    }
}
