package com.example.testproject

import org.hamcrest.core.StringContains
import org.junit.Test

class mytest
{

   @Test
   fun mytest()
   {
       var a:String?=null;
       System.out.println(a?.length?:"hello");
   }

    /**
     * Kotlin扩展测试
     */
    //扩展函数,主要就是作用就是在不修改原有对象外加一个函数，然后同时还能在函数中调用对象的方法和属性
    fun TestKotlin.funTest(name:String):String
    {

        setCallBack(object:TestKotlin.MyCallBack{         //调用扩展函数对象的内部方法
            override fun back(name: String) {
                println(name)
            }
        })
        println(name);
        return "这是测试函数的返回"

    }
    //扩展属性,只能以val修饰，因为扩展属性是不允许修改的
    private val TestKotlin.funShuxing:String
    get() {
        val text=funTest("我是扩展函数，我传递了内容给扩展属性")
        return "$text,扩展属性说：好的我收到了"
    }


    @Test
    fun ktFund()
    {

       var testKt=TestKotlin("hi")
//       println(testKt.funTest("这是一个扩展函数"))
       println(testKt.funShuxing)
    }


        var me:String
        get(){
            return "这是获取"
        }
        set(value) {
            println(value)
        }
    @Test
    fun myTest()
    {
        me="这是设置"
        println(me)

    }

    interface Base {
        fun print()
    }

    class BaseImpl(val x: Int) : Base {
        override fun print() { print(x) }
    }

    class Derived(b: Base) : Base by b

    @Test
    fun main()
    {
        var name= listOf("老张","王教授","小李","校长","马冬梅")
        var exitName= listOf("小李","效力","炸NG","马冬梅")
        val tmp=name.filterNot { exitName.contains(it) }
        println(tmp)
        println(listOf(tmp,exitName).flatten())   //合并在一起
        println(exitName+tmp)  //和上面那个是一样的
    }

}