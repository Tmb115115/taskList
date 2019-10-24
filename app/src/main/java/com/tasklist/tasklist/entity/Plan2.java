package com.tasklist.tasklist.entity;

import android.content.Intent;

import org.litepal.crud.LitePalSupport;

import java.util.Date;

public class Plan2 extends LitePalSupport {
    private int id;//数据id
    private int hour;//任务开始的时间
    private int minute;
    private String text;//文本信息
    private String title;//标题
    private Boolean isCompleted;//是否完成
    private int year;//年
    private int month;//月
    private int day;//日
    private int count;//用来删除闹钟
    private String date1;//yyyy-MM-dd
    private String date2;//HH:mm:ss
    public void setDate1(String  date1){
        this.date1 = date1;
    }
    public String getDate2(){
        return this.date2;
    }
    public void setDate2(String  date2){
        this.date2 = date2;
    }
    public String getDate1(){
        return this.date1;
    }
    public void setYear(int year){
        this.year = year;
    }
    public int getYear(){
        return this.year;
    }
    public void setMonth(int month){
        this.month = month;
    }
    public int getMonth(){
        return this.month;
    }
    public void setDay(int day){
        this.day = day;
    }
    public int getDay(){
        return  this.day;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public void setIsCompleted(Boolean isCompleted){
        this.isCompleted = isCompleted;
    }
    public Boolean getIsCompleted(){
        return this.isCompleted;
    }
    public void setText(String text){
        this.text=text;
    }
    public String getText(){
        return text;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return title;
    }
    public void setHour(int hour){this.hour = hour;}
    public int getHour(){return hour;}
    public void setMinute(int minute){this.minute = minute ;}
    public int getMinute(){return  minute ;}
    public void setCount(int count){this.count = count;}
    public int getCount(){return count;}
}
