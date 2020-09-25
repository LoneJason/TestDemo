package com.example.testproject.staticproxy;

public class TestProxy
{
    /**
     * 开始调用
     */
    public void test()
    {
        RealSubject real=new RealSubject();   //创建实际类的对象
        ProxySubject proxy=new ProxySubject(real);  //创建代理类，并把实际类对象传进去
        proxy.doAction("this is action");  //用代理类里面的方法去调用实际类
        /**
         * 实际上是可以直接real.doAction("this is action");,这样就可以直接访问了，
         * 这里加了一个代理类的作用就是多了一个中间层，让中间层去处理一些过滤、安全之类的东西
         */
    }
}
