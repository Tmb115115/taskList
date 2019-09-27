package com.tasklist.tasklist;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tasklist.tasklist.entity.Plan2;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class EditActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbartitle;
    private FloatingActionButton fab;
    private EditText editText1;
    private EditText editText2;
    private String title;
    private String text;
    private int flag2;
    private ImageView pic;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ActivityCollector.addActivity(this);
        final Bundle bundle = getIntent().getExtras();
        if (bundle !=null){
            flag2 = bundle.getInt("flag");//获取flag
        }
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        swipeRefreshLayout = findViewById(R.id.refresh_edit);
        pic = findViewById(R.id.image_edit);
        toolbar = findViewById(R.id.edit_toolbar);//实例化toolbar
        setSupportActionBar(toolbar);
        toolbartitle = toolbar.findViewById(R.id.title);
        toolbartitle.setText("编辑任务");//编辑标题
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);//实例化editText
        if (flag2 == 0x00000006){
            editText1.setText(bundle.getString("title"));
            editText2.setText(bundle.getString("text"));
        }
        fab = findViewById(R.id.fab_save);//实例化悬浮窗
        ActionBar actionBar = getSupportActionBar();//获取actionBar
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);//将返回按钮显示出来
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String backPic = prefs.getString("backPic",null);
        if (backPic  !=  null){
            Glide.with(this).load(backPic).into(pic);
        }else {
            loadBack();
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RefreshImage();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {//悬浮窗点击事件
            @Override
            public void onClick(View v) {
                Intent intent;
                if (flag2 != 0x00000006){
                    Date newtime = new Date();
                    Calendar calendar =Calendar.getInstance();//获取时间 年月日
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
                    plan2.setIsCompleted(false);//将所有checkbox初始化未false
                    if (flag2==0x00000000){//如果是今天
                        plan2.setYear(calendar.get(Calendar.YEAR));//将年月日信息储存到数据库
                        plan2.setMonth(month);
                        plan2.setDay(calendar.get(Calendar.DAY_OF_MONTH));//calendar.get(Calendar.DAY_OF_MONTH)获取天数
                        intent = new Intent(EditActivity.this, MainActivity.class);
                        Bundle bundle2 = new Bundle();
                        bundle2.putInt("flag",flag2);//将标记传递过去
                        intent.putExtras(bundle2);
                        startActivity(intent);
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
                if (flag2 == 0x00000006){
                    Plan2 plan2 = new Plan2();
                    plan2.setTitle(editText1.getText().toString());
                    plan2.setText(editText2.getText().toString());
                    plan2.updateAll("title = ? and text = ?",bundle.getString("title"),bundle.getString("text"));
                    intent = new Intent(EditActivity.this, MainActivity.class);
                    startActivity(intent);
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
    private void RefreshImage(){
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
                        loadBack();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
    private void loadBack(){
            String Bing ="http:guolin.tech/api/bing_pic";
            HttpUtil.sendOkHttpRequest(Bing, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    final String bing = response.body().string();
                    SharedPreferences.Editor editor =PreferenceManager.getDefaultSharedPreferences(EditActivity.this).edit();
                    editor.putString("backPic",bing);
                    editor.apply();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(EditActivity.this).load(bing).into(pic);
                            Log.d("rrrrrrrrrrrrrr","rrrrrrrrrrrrrrrr");
                        }
                    });
                }
            });
        }

}
