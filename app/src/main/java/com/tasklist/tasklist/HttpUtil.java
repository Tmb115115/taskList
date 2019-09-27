package com.tasklist.tasklist;

import okhttp3.OkHttpClient;
import okhttp3.Request;


//访问网络
public class HttpUtil {
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient实例
        Request request = new Request.Builder().url(address).build();//发起HTTP请求，创建Request对象
        client.newCall(request).enqueue(callback);//获取数据
    }
}
