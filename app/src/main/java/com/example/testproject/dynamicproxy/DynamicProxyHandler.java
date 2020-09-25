package com.example.testproject.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 动态代理类
 */
public class DynamicProxyHandler implements InvocationHandler {
    private Object target;

    //把接口对象传入
    public DynamicProxyHandler(Object target) {
        this.target = target;
        String a;

    }

    // 通过 Proxy.newProxyInstance 直接创建动态代理类实例
    //Proxy.newProxyInstance三个参数，参数1：用哪个类加载器去加载代理对象，参数2：动态代理类需要实现的接口，如果有多个就是数组形式传入，参数3：代理类实例
    //返回结果就是代理类
    public <T> T getProxy() {
        return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(), this);
    }

    //proxy：就是代理对象，newProxyInstance方法的返回对象
   //method：调用的方法
   // args: 方法中的参数
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("办事之前先收取点费用");
        System.out.println("开始办事");
        Object result = null;
        try {
            result = method.invoke(target, args); // 调用方法,通过反射的形式来调用 target 的 method
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        System.out.println("办完了");
        return result;
    }
}
