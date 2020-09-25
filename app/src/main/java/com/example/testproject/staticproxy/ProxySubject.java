package com.example.testproject.staticproxy;

/**
 * 创建代理对象
 */
public class ProxySubject implements proxyInterface
{
    proxyInterface proxy;
   public ProxySubject(proxyInterface proxy)
   {
       this.proxy=proxy;
   }

    @Override
    public void doAction(String action)
    {
        proxyClass();
        proxy.doAction(action);
    }
    private void proxyClass()
    {
        System.out.println("我是代理类");
    }
}
