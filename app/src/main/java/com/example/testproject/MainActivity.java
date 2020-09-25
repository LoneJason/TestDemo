package com.example.testproject;


import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testproject.base.BaseActivity;
import com.example.testproject.bluetooth.BluetoothActivity;
import com.example.testproject.changeview.ChangeActivity;
import com.example.testproject.service.MyServiceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        serviceUtil = new MyServiceUtil();    //测试服务demo
        findViewById(R.id.serch).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, BluetoothActivity.class));
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
                GsonUtil util = new GsonUtil(new String[]{"oneJson", "twoJson"}, new String[]{"JUSToNE", "JUSTtWO"});
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
                Map<String, String> params = new HashMap<>();
                params.put("oneMap", "hi");
                params.put("twoMap", "hello");
                params.put("threeMap", "hai");
                util.toMapJson(params);
            }
        });
        openSeriport();
        serviceUtil.aidlService(this);
        imageList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

    }


    //测试手机的dpi
    public void phoneMessage() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Log.d("mytest", "dp转px比例：" + dm.density + "，dpi值" + dm.densityDpi + "，宽度的尺寸" + dm.widthPixels + "--" + dm.ydpi);
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