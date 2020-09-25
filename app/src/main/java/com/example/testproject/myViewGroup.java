package com.example.testproject;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class myViewGroup extends LinearLayout {

    public myViewGroup(Context context) {
        super(context);
    }

    public myViewGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d("test","Group拦截事件");
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("test","Group消费事件");
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d("test","Group分发事件");
        return super.dispatchTouchEvent(ev);
    }


}
