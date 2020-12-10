package com.example.testproject;

import android.widget.Toast;

/**
 * 这是父类
 */
public class ClassB  implements InterfaceD
{
    @Override
    public void getName(String name) {
        System.out.println("这是父类:"+name);
//        getName("有名小泽");
    }
}
