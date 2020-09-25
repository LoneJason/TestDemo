package com.example.testproject.staticproxy;

import junit.framework.TestCase;

public class TestProxyTest extends TestCase {

    public void testTest1()
    {
        RealSubject real=new RealSubject();   //创建实际类的对象
        ProxySubject proxy=new ProxySubject(real);  //创建代理类，并把实际类对象传进去
        proxy.doAction("this is action");  //用代理类里面的方法去调用实际类
    }
}