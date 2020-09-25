package com.example.testproject.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class testService extends Service {
    LocalBinder binder=new LocalBinder();
    public class LocalBinder extends Binder
    {
      public   testService getService()
        {
            return testService.this;
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    public String text()
    {
      return "jakinl";
    };
}
