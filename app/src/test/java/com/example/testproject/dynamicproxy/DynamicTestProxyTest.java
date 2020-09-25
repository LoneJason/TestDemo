package com.example.testproject.dynamicproxy;

import junit.framework.TestCase;

public class DynamicTestProxyTest extends TestCase {

    public void testTest1()
    {
        DynamicRealSubject realSubject = new DynamicRealSubject();
        DynamicProxyInterface proxyInterface = new DynamicProxyHandler(realSubject).getProxy();
        proxyInterface.doAction("play");
    }
}