package com.example.testproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if ("mytest".equals(action))     //判断广播返回是否是蓝牙的返回
        {
            Log.d("myetst","test");
        }
    }
}
