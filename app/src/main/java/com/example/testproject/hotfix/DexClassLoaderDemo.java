package com.example.testproject.hotfix;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

import static android.content.Context.MODE_PRIVATE;

public class DexClassLoaderDemo
{


//    String file="file:///android_asset/app-debug.apk";
       String file=Environment.getExternalStorageDirectory()+"/"+"myapp.apk";
    public void Loader(Context context)  {
        //优化后的dex文件输出目录，应用必须具备读写权限
        String optimizedDirectory = context.getDir("dexOpt", MODE_PRIVATE).getAbsolutePath();
        Log.d("mytest",optimizedDirectory);
        //dexPath： 包含dex文件的压缩文件(jar,apk,zip)的绝对路径，不能为空。
        //optimizedDirectory： 解压后存储路径，建议放在程序私有路径/data/data/com.xxx.xxx下。
        //libraryPath：os库的存放路径，可以为空，若有os库，必须填写。
        //parent：父类加载器，一般为context.getClassLoader()。

        try {
            DexClassLoader dexClassLoader=new DexClassLoader(file,optimizedDirectory,null,ClassLoader.getSystemClassLoader());
            File f = new File(file);
            Log.d("mytest",f.exists()+"");
            Class  clazz = dexClassLoader.loadClass("com.example.pulgapp.MyPlug");
            Method method = clazz.getMethod("doLog",new Class[]{});
            method.invoke(clazz.newInstance());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }  catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }
}
