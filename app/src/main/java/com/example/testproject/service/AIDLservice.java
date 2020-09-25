package com.example.testproject.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import com.example.testproject.IMyAidlInterface;

public class AIDLservice extends Service {
    private IMyAidlInterface.Stub bind= new IMyAidlInterface.Stub() {
        @Override
        public String mytest(String text) throws RemoteException {
            text="我是来自服务器的："+text;
            return text;
        }
    };
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return bind;
    }
}
