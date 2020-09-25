package com.example.testproject.annotation;

import android.util.Log;

/**
 * 注解模型
 */
public class Apple
{
    @FruitName(name = "苹果")
    private String name;
    @FruitMoney(money = 100)
    private int money;
    private void company()
    {
        Log.d("mytest","世界自由贸易苹果公司");
    }
}
