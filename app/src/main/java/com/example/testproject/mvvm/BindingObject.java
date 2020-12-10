package com.example.testproject.mvvm;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.testproject.R;

import static com.example.testproject.base.BaseActivity.TAG;

public class BindingObject
{
    public void click(View view) {
        int viewId = view.getId();
        if (viewId == R.id.livedata_btn) {
            Log.d(TAG,"通过id区分事件1");
        }
    }

    public void clickMethod1(View view) {
        Log.d(TAG,"普通的点击事件");
    }


    public void paramsMethod(View view, Context context, String params) {
        Log.d(TAG,params);
    }

}
