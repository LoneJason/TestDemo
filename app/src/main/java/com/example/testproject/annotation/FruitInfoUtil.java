package com.example.testproject.annotation;

import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 *  注释处理器
 */
public class FruitInfoUtil
{
    public static void useUtil() throws Exception {
        //通过反射获取对象
        Class clazz=Class.forName("com.example.testproject.annotation.Apple");
        Object o=clazz.newInstance();     //获取一个对象实例
        Field[] fields=clazz.getDeclaredFields();
        for (Field field:fields)     //遍历Apple里面的变量
        {
            if(field.isAnnotationPresent(FruitName.class))  //判断当前的变量是否是FruitName注解
            {
                //获取注解
                FruitName fruitName = field.getAnnotation(FruitName.class);
                Log.d("mytest",fruitName.name());    //获取注解参数的值
            }
            if(field.isAnnotationPresent(FruitMoney.class))
            {
                FruitMoney fruitMoney=field.getAnnotation(FruitMoney.class);
                Log.d("mytest",fruitMoney.money()+"");    //获取注解参数的值
            }
        }
        Method company=clazz.getDeclaredMethod("company");
        company.setAccessible(true);   //允许访问私有变量
        company.invoke(o);
    }

}
