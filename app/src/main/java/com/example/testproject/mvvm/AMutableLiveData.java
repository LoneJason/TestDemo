package com.example.testproject.mvvm;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

public class AMutableLiveData extends MutableLiveData<UserBean>
{
    @Override
    protected void onActive() {
        super.onActive();
        Log.d("mytest","当前LiveData可以传递或可以");
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        Log.d("mytest","当前LiveData你不可见");
    }

}
