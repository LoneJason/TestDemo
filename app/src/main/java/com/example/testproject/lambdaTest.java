package com.example.testproject;

import android.view.View;

import java.io.Serializable;

public class lambdaTest
{
    public void test()
    {
        View view=null;
        view.setOnClickListener(v -> {});
    }

  public static class Test implements myInterface
    {
        public static String hh()
        {
             return  "Asdfas";
        };
        public String appId;
        public void setAppId(String appId)
        {
            this.appId=appId;
        }
        public String getAppId()
        {
            return appId;
        }

        @Override
        public String name() {
            return "dasasd";
        }
    }
    interface myInterface
    {
      String name();

    }
}
