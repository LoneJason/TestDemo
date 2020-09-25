package com.example.testproject.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.example.testproject.IMyAidlInterface;

public class MyServiceUtil
{
    /**
     * Bind用于进程内的通信
     */
    testService ts;
    //获取Bind服务
    public  void BinderService(Context context) {
        Intent intent = new Intent(context, testService.class);
        context.bindService(intent, BindConnection, Service.BIND_AUTO_CREATE);
    }

    ServiceConnection BindConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            testService.LocalBinder tl = (testService.LocalBinder) service;
            ts = tl.getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    /**
     * AIDL用于跨进程间的通信
     */
  public   IMyAidlInterface iMyAidlInterface;

    //获取AIDL服务
    public void aidlService(Context context)
    {
        Intent intent = new Intent(context, AIDLservice.class);
        context.bindService(intent, aidlConnection, Service.BIND_AUTO_CREATE);
    }

    ServiceConnection aidlConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };



}
