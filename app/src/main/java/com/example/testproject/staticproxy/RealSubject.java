package com.example.testproject.staticproxy;

import android.util.Log;

/**
 * 实际类
 */
public class RealSubject implements proxyInterface
{
    @Override
    public void doAction(String action) {
        System.out.println("我是实际的类, do action "+ action);
    }
}
