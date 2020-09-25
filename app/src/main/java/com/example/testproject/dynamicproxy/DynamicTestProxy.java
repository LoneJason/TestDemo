package com.example.testproject.dynamicproxy;


public class DynamicTestProxy
{
    /**
     * 开始调用
     */
    public void test() {
        DynamicRealSubject realSubject = new DynamicRealSubject();
        DynamicProxyInterface proxyInterface = new DynamicProxyHandler(realSubject).getProxy();
        proxyInterface.doAction("play");
    }
}
