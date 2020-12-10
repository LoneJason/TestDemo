package com.example.testproject.livedata;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.testproject.GetDatagramSocketUtil;
import com.example.testproject.R;
import com.example.testproject.UseCallBack;

public class LiveDataActivity extends AppCompatActivity
{
//    TextView tetx;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livedata);
//       tetx=findViewById(R.id.livedata_text);
       findViewById(R.id.livedata_btn).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //这里的this指的是LifecycleOwner，即LiveData会跟生命周期联系起来,第二个参数就是观察者
               //让观察者和被观察者绑定起来
               UseCallBack callBack=new UseCallBack();
               callBack.use();
               callBack.testExecute();
               LiveDataBus bus=new LiveDataBus();
             bus.getStatus().observe(LiveDataActivity.this,observer);
             //被观察者通知观察者数据变动
              bus.getStatus().setValue("这是传递的数据");
              new GetDatagramSocketUtil().start();
           }
       });

    }
    //创建一个观察者去更新UI
        Observer<String> observer=new Observer<String>() {
            @Override
            public void onChanged(String s) {
//                tetx.setText(s);
            }
        };



}
