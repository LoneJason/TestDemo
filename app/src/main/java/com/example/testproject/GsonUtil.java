package com.example.testproject;

import android.app.Notification;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GsonUtil
{
    String[] key,value;
   public GsonUtil(String[] key,String[] value)
   {
       this.key=key;
       this.value=value;
   }
   public String changeGson()
   {
       JsonObject jsonObject=new JsonObject();
       //添加json对象
       for (int i=0;i<key.length;i++)
       {
           jsonObject.addProperty(key[i],value[i]);
       }
       //添加json数组
       JsonObject jsonElement=new JsonObject();
       jsonElement.addProperty("ElementOne","ElementOne");
       jsonElement.addProperty("ElementTwo","ElementTwo");
       jsonObject.add("JsonElement",jsonElement);
       return jsonObject.toString();
   }
   public void fromGson(String text)
   {
       Gson gson=new Gson();
     JsonMode mode=gson.fromJson(text,JsonMode.class);
       Log.d("mytest",mode.oneJson+mode.twoJson+ mode.JsonElement.ElementOne+mode.JsonElement.ElementTwo);
   }
   public void toListJson(List<String> list)
   {
       Gson gson=new Gson();
      String text=gson.toJson(list,new TypeToken<List<String>>(){}.getType());
        Log.d("mytest",text);
   }
   public void toMapJson(Map map)
   {
       Gson gson=new Gson();
       String text=gson.toJson(map,new TypeToken<HashMap>(){}.getType());
       Log.d("mytest",text);
   }
   public class JsonMode
   {
      public String oneJson;
      public String twoJson;
      public Elem JsonElement;
   }
   public class Elem
   {
       public String ElementOne;
       public String ElementTwo;
   }
}
