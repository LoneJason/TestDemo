package com.example.testproject.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity
{
    static final public String TAG="TestProject";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(provideContenViewId());
        ParentSenText("这是父类的传参");
    }
    protected abstract int provideContenViewId();
   //父类传参给子类
    protected abstract void ParentSenText(String text);
    //其它类用向上转型给子类传的参数
    public abstract void ParentGetText(String text);

}
