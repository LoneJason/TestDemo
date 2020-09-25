package com.example.testproject.dynamicproxy;

/**
 * 实际类
 */
public class DynamicRealSubject implements DynamicProxyInterface {
    @Override
    public void doAction(String action) {
        System.out.println("我是实际的类, do action " + action);
    }
}
