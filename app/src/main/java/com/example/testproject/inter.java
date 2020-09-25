package com.example.testproject;

public class inter
{
   public  @interface MyAnnotation
   {
       int id() default 0;
       String msg() default "默认值";
   }
}
