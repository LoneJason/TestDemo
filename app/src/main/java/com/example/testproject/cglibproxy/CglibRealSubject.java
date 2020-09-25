package com.example.testproject.cglibproxy;

import com.example.testproject.staticproxy.proxyInterface;

/**
 * 实际类
 */
public class CglibRealSubject
{

    public void doAction(String action) {
        System.out.println("我是实际的类, do action "+ action);
    }
}
