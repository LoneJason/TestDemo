package com.example.testproject;

public class ClassA extends ClassB
{
    @Override
    public void getName(String name) {
        super.getName(name);
        System.out.println("这是子类:"+name);
    }
}
