package com.example.testproject.livedata;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class LiveDataBus {
    public MutableLiveData<String> status;

    //用来创建数据,类似被观察者这样
    public MutableLiveData<String> getStatus() {
        if (status == null) {
            status = new MutableLiveData<>();
        }
        return status;
    }
}
