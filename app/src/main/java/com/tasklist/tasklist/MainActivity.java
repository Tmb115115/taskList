package com.tasklist.tasklist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tasklist.tasklist.adapter.PlanAdapter2;
import com.tasklist.tasklist.entity.Plan2;
import com.tasklist.tasklist.item.CalendarActivity;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout; //侧滑栏
    private RecyclerView recyclerView;
    private List<Plan2> plan;
    private PlanAdapter2 adapter;
    private TextView title;
    private TextView sum;
    private ImageView pic;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int flag = 0x00000000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_main);
        ActivityCollector.addActivity(this);
        Log.d("eeeeeeeee",""+flag);
        final Bundle bundle =getIntent().getExtras();
        if (bundle !=null){
                flag = bundle.getInt("flag");//获取标记

        }
        title= (TextView)findViewById(R.id.title);//实例化标题
        sum = (TextView)findViewById(R.id.sum);
        pic = (ImageView)findViewById(R.id.image);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final FloatingActionButton fabAdd =findViewById(R.id.fab_add);//悬浮窗
        if (flag ==0x00000000 ){    //如果是今天
            title.setText("今日计划");//标题
            Calendar calendar =Calendar.getInstance();//获取今天的日期
            calendar.add(Calendar.MONTH,1);//month+1
            String str = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));//将int转化为String
            String str2 = String.valueOf(calendar.get(Calendar.MONTH));//将int转化为String
            String str3 = String.valueOf(calendar.get(Calendar.YEAR));//将int转化为String
            plan = LitePal.where("year = ? and month = ? and Day <= ? and isCompleted = ?",str3,str2,str,"0").find(Plan2.class);//从数据库查找数据
            String str4 = String.valueOf(plan.size());
            sum.setText(str4);//总任务数
            adapter = new PlanAdapter2(plan);
        }
        if (flag ==0x00000001){ //如果是明天
            title.setText("明日计划");//标题
            Calendar calendar =Calendar.getInstance();//获取今天的日期
            calendar.add(Calendar.DAY_OF_MONTH,1);//+1
            String str = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));//将int转化为String
            plan = LitePal.where("Day=?",str).find(Plan2.class);//在数据库查找
            String str4 = String.valueOf(plan.size());
            sum.setText(str4);//总任务数
            adapter = new PlanAdapter2(plan);
        }
        if (flag ==0x00000002){ //如果是本月
            title.setText("本月计划");//标题
            Calendar calendar =Calendar.getInstance();//获取今天的日期
            calendar.add(Calendar.MONTH,1);//+1
            String str = String.valueOf(calendar.get(Calendar.MONTH));//将int转化为String
            String str2 = String.valueOf(calendar.get(Calendar.YEAR));//将int转化为String
            plan = LitePal.where("year = ? and month=?",str2,str).find(Plan2.class);//在数据库查找
            String str4 = String.valueOf(plan.size());
            sum.setText(str4);//总任务数
            adapter = new PlanAdapter2(plan);
        }
        if (flag == 0x00000003){   //如果是已完成
            title.setText("已完成计划");//标题
            fabAdd.setVisibility(View.GONE);//隐藏悬浮窗
            plan = LitePal.where("isCompleted = ?","1").find(Plan2.class);//查询数据
            String str4 = String.valueOf(plan.size());
            sum.setText(str4);//总任务数
            adapter = new PlanAdapter2(plan);
        }
        if (flag == 0x00000004){ //如果是未完成
            title.setText("未完成计划");//标题
            fabAdd.setVisibility(View.GONE);//隐藏悬浮窗
            plan = LitePal.where("isCompleted = ?","0").find(Plan2.class);//查询数据 true = 1 ，false = 0
            String str4 = String.valueOf(plan.size());
            sum.setText(str4);//总任务数
            adapter = new PlanAdapter2(plan);
        }
        if (flag ==0x00000005){ //如果是自定义日期
            int year = bundle.getInt("year");
            int month = bundle.getInt("month")+1;
            int day = bundle.getInt("dayOfmonth");//获取年月日信息
            title.setText(year+"年"+month+"月"+day+"日");//标题
            String str = String.valueOf(year);
            String str2 = String.valueOf(month);
            String str3 = String.valueOf(day);
            plan = LitePal.where("day = ? and month = ? and year = ?",str3,str2,str).find(Plan2.class);//按年月日查询数据 都要满足 用and
            String str4 = String.valueOf(plan.size());
            sum.setText(str4);//总任务数
            adapter = new PlanAdapter2(plan);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        String backPic = prefs.getString("backPic",null);
        if (backPic  !=  null){
            Glide.with(this).load(backPic).into(pic);
        }else {
            loadBack();

        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);//建立Toolbar
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);//实例化recyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);//RecyclerView格式
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);//实例化侧滑栏
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_image);//侧滑栏具体内容
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(new MyItemTouchHelperCallback(adapter));
        mItemTouchHelper.attachToRecyclerView(recyclerView);//实现侧滑删除RecyclerView
        recyclerView.setAdapter(adapter);
        fabAdd.setOnClickListener(new View.OnClickListener() {//悬浮窗点击事件
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),EditActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putInt("flag",flag);//传递flag
                if (flag == 0x00000005){
                    bundle2.putInt("year",bundle.getInt("year"));
                    bundle2.putInt("month",bundle.getInt("month"));
                    bundle2.putInt("dayOfmonth",bundle.getInt("dayOfmonth"));//传递年月日信息
                }
                intent.putExtras(bundle2);
                startActivity(intent);
                finish();
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {//侧滑栏设置点击事件
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {//item点击事件
                switch (menuItem.getItemId()){
                    case R.id.date://日期选择
                        Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawers();
                        finish();
                        break;
                    case R.id.today://今日计划
                        intent = new Intent(getApplicationContext(),MainActivity.class);
                        Bundle bundle  = new Bundle();
                        bundle.putInt("flag",0x00000000);//将标记传过去
                        intent.putExtras(bundle);
                        startActivity(intent);
                        mDrawerLayout.closeDrawers();
                        finish();
                        break;
                    case R.id.tomorrow://明日计划
                        intent  = new Intent(getApplicationContext(),MainActivity.class);
                        Bundle bundle1 = new Bundle();
                        bundle1.putInt("flag",0x00000001);//将标记传过去
                        intent.putExtras(bundle1);
                        startActivity(intent);
                        mDrawerLayout.closeDrawers();
                        finish();
                        break;
                    case R.id.month://本月计划
                        intent = new Intent(getApplicationContext(),MainActivity.class);
                        Bundle bundle2 = new Bundle();
                        bundle2.putInt("flag",0x00000002);//将标记传过去
                        intent.putExtras(bundle2);
                        startActivity(intent);
                        mDrawerLayout.closeDrawers();
                        finish();
                        break;
                    case R.id.done://已完成计划
                        intent = new Intent(getApplicationContext(),MainActivity.class);
                        Bundle bundle3 = new Bundle();
                        bundle3.putInt("flag",0x00000003);//将标记传过去
                        intent.putExtras(bundle3);
                        startActivity(intent);
                        mDrawerLayout.closeDrawers();
                        finish();
                        break;
                    case R.id.to_do://未完成计划
                        intent = new Intent(getApplicationContext(),MainActivity.class);
                        Bundle bundle4 = new Bundle();
                        bundle4.putInt("flag",0x00000004);//将标记传过去
                        intent.putExtras(bundle4);
                        startActivity(intent);
                        mDrawerLayout.closeDrawers();
                        finish();
                        break;
                }
                return true;
            }
        });
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
                SharedPreferences.Editor editor =PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                editor.putString("backPic",bing);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(MainActivity.this).load(bing).into(pic);
                        Log.d("rrrrrrrrrrrrrr","rrrrrrrrrrrrrrrr");
                    }
                });
            }
        });
    }
    private void refreshData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Calendar calendar =Calendar.getInstance();//获取今天的日期
                        calendar.add(Calendar.MONTH,1);//month+1
                        String str = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));//将int转化为String
                        String str2 = String.valueOf(calendar.get(Calendar.MONTH));//将int转化为String
                        String str3 = String.valueOf(calendar.get(Calendar.YEAR));//将int转化为String
                        plan = LitePal.where("year = ? and month = ? and Day <= ? and isCompleted = ?",str3,str2,str,"0").find(Plan2.class);//从数据库查找数据
                        String str4 = String.valueOf(plan.size());
                        sum.setText(str4);//总任务数
                        adapter = new PlanAdapter2(plan);
                        recyclerView.setAdapter(adapter);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

}

