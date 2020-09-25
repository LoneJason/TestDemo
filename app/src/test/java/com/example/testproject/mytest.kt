package com.example.testproject

import org.junit.Test

class mytest
{
   @Test
   fun mytest()
   {
       var a:String?=null;
       System.out.println(a?.length?:"hello");
   }
}