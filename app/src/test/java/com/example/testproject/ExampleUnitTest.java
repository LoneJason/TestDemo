package com.example.testproject;

import android.util.Log;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        System.out.println("dasd");
        assertEquals(4, 2 + 2);
    }
    @Test
    public void TestList()
    {
       List<String> list=new ArrayList<>();
       String a="哈哈哈哈";
       list.add(a);
       a="你是沙雕吗";
     System.out.println(list.get(0));
        List<User> users=new ArrayList<>();
        User user=new User();
        user.setName("小明");
        user.setOld(18);
        users.add(user);
        user.setName("老王");
        user.setOld(25);
        System.out.println("list里面的值--"+users.get(0).getName()+users.get(0).getOld());
        System.out.println("User里面的值--"+user.getName()+user.getOld());
        System.out.println(  "list存储user的地址："+users.get(0).hashCode()+"，user的实际地址："+user.hashCode());
        user=null;
        if(users.get(0)==null)
        {

            System.out.println("User为空");
        }
        else
        {

            System.out.println("User不为空"+ users.get(0).getOld());
        }




    }
    class User
    {
        String name;
        int old;
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getOld() {
            return old;
        }

        public void setOld(int old) {
            this.old = old;
        }


    }
}