package com.example.testproject

import android.util.Log

class TestKotlin(var text:String)
{

interface MyCallBack
{
    fun back(name:String)
}

  fun setCallBack(callback:MyCallBack)
 {
   callback.back("这个是回传："+text)
}

}