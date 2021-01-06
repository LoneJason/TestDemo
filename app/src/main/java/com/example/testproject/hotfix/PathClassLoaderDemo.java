package com.example.testproject.hotfix;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.PathClassLoader;

public class PathClassLoaderDemo
{
   public void load(Context context)
   {
       Log.d("myetst","地址"+context.getPackageResourcePath());
       PathClassLoader pathClassLoader = new PathClassLoader(context.getPackageResourcePath(),ClassLoader.getSystemClassLoader());
       try {
           Class  clazz = pathClassLoader.loadClass("com.example.testproject.hotfix.Test");
           Method method = clazz.getMethod("method");
           method.invoke(clazz.newInstance());
       } catch (ClassNotFoundException e) {
           e.printStackTrace();
       } catch (NoSuchMethodException e) {
           e.printStackTrace();
       } catch (IllegalAccessException e) {
           e.printStackTrace();
       } catch (InstantiationException e) {
           e.printStackTrace();
       } catch (InvocationTargetException e) {
           e.printStackTrace();
       }
   }
}
