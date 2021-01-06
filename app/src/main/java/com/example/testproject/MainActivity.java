package com.example.testproject;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.transition.Explode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testproject.base.NotifyService;
import com.example.testproject.hotfix.DexClassLoaderDemo;
import com.example.testproject.hotfix.PathClassLoaderDemo;
import com.example.testproject.mvvm.AViewModel;
import com.example.testproject.mvvm.LifeActivity;
import com.example.testproject.base.BaseActivity;
import com.example.testproject.netty.AirKiss;
import com.example.testproject.netty.NettySend;
import com.example.testproject.service.MyServiceUtil;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.DexClassLoader;

public class MainActivity extends BaseActivity {

    List<Bitmap> list = new ArrayList<>();
    RecyclerView imageList;
    SurfaceView surfaceView;
    MyServiceUtil serviceUtil;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        super.onCreate(savedInstanceState);
        getWindow().setEnterTransition(new Explode().setDuration(1000));
        getWindow().setExitTransition(new Explode().setDuration(1000));
        imageList = findViewById(R.id.recycler);
        surfaceView = findViewById(R.id.mysurface);
//        hideBottomUIMenu();
        phoneMessage();
        serviceUtil = new MyServiceUtil();    //测试服务demo
        ViewModelProviders.of(this).get(AViewModel.class).getLiveData().observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                Log.d("mytest",o.toString());
            }
        });
        AirKiss airKiss=new AirKiss();
        airKiss.startBoot();
        findViewById(R.id.receive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                NettyReceive receive=new NettyReceive();
//                NettyReceive.AccepterRunner runner=receive.new AccepterRunner();
//                runner.main();
                ViewModelProviders.of(MainActivity.this).get(AViewModel.class).updataUser();
                startActivity(new Intent(MainActivity.this, LifeActivity.class));

            }
        });
        findViewById(R.id.serch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DexClassLoaderDemo demo=new DexClassLoaderDemo();
                demo.Loader(MainActivity.this);



//                NettySend send=new NettySend();
//                NettySend.BroadcastRunner runner=send.new BroadcastRunner();
//                runner.main();
//                MainActivity.this.startActivity(new Intent(MainActivity.this, BluetoothActivity.class));
//                MainActivity.this.startActivity(new Intent(MainActivity.this, ChangeActivity.class), ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
//            imageList.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayout.VERTICAL,false));
//            MyRecyclerViewAdapter adapter=new MyRecyclerViewAdapter(list);
//            imageList.setAdapter(adapter);
                //注解反射
//                try {
//                    FruitInfoUtil.useUtil();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                try {
//                 Toast.makeText(MainActivity.this, serviceUtil.iMyAidlInterface.mytest("hello"), Toast.LENGTH_SHORT).show();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                GsonUtil util = new GsonUtil(new String[]{"oneJson", "twoJson"}, new String[]{"JUSToNE", "JUSTtWO"});
//             String text=util.changeGson();
//            Log.d("mytest", text);
//            util.fromGson(text);

                //list转换json
//            List<String> list=new ArrayList<>();
//            list.add("hello");
//            list.add("hi");
//            list.add("fuck");
//            util.toListJson(list);
                //map转换json
//                Map<String, String> params = new HashMap<>();
//                params.put("oneMap", "hi");
//                params.put("twoMap", "hello");
//                params.put("threeMap", "hai");
//                util.toMapJson(params);

            }
        });
//        openSeriport();
//        serviceUtil.aidlService(this);
//        imageList.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return false;
//            }
//        });

    }


    //测试手机的dpi
    public void phoneMessage() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Log.d("mytest", "dp转px比例：" + dm.density + "，dpi值" + dm.densityDpi + "，宽度的尺寸" + dm.widthPixels );
    }

    //给父类传值
    @Override
    protected int provideContenViewId() {
        Log.d(BaseActivity.TAG, "设置布局");
        return R.layout.activity_main;
    }

    //获取父类的参数
    @Override
    protected void ParentSenText(String text) {
        Log.d(BaseActivity.TAG, text);
    }

    //用父类的虚方法向上转型，获取其它地方的传参，有点类似接口回调
    @Override
    public void ParentGetText(String text) {
        Log.d(BaseActivity.TAG, text);
    }

    //调用Seriport类
    void openSeriport() {
        new seriport().main();
    }


    //调用枚举单例模式
    public void getEnum() {
        danli.he.doSomething();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("test", "Activity消费事件");
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d("test", "Activity分发事件");
        return super.dispatchTouchEvent(ev);
    }

    //隐藏标题栏
    protected void hideBottomUIMenu() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);//API19
        } else {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
            );
        }

    }

    @inter.MyAnnotation(id = 103, msg = "调用者")
    public void testAnnotation() {
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAfterTransition();
    }



}
