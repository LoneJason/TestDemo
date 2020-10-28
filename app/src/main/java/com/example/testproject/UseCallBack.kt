package com.example.testproject

import android.util.Log
import okhttp3.internal.Util

class UseCallBack : TestKotlin.MyCallBack {
    fun use() {
        var TestKt = TestKotlin("Use")
        TestKt.setCallBack(this)
    }

    override fun back(name: String) {


    }

        fun execute(name: String = "老王", interest: (one: String, two: String) -> String) {

        Log.d("mytest", "赋值前")
        interest("抽烟", "喝酒")
        Log.d("mytest", "赋值后")
            Log.d("", interest.toString())
        //执行后的接结果：
        // 赋值前
        // 抽烟喝酒
        // 复制后


    }



    fun testExecute() {
        execute()
        { one, two ->
            Log.d("mytest", two + one)
            one
        }
        

        var hi={a:String,b:String->{
            Log.d("","")
            a+b

        }}
    }
}