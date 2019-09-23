package com.tasklist.tasklist.entity;

import android.content.Intent;

import org.litepal.crud.LitePalSupport;

import java.util.Date;

public class Plan2 extends LitePalSupport {
    private int id;
    private String text;
    private String title;
    private Boolean isCompleted;
    private int year;
    private int month;
    private int day;
    private String date1;
    private String date2;
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
}
