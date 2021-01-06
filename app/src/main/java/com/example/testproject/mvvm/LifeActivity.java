package com.example.testproject.mvvm;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.testproject.R;
import com.example.testproject.databinding.ActivityLivedataBinding;

public class LifeActivity extends Activity implements LifecycleOwner {
    private LifecycleRegistry mLifecycleRegistry;    //通过LifecycleRegistry获取LifeCycle对象
    AMutableLiveData liveData;   //获取LiveData
  public   UserBean bean = new UserBean();
    //使用代码块来初始化这个对象
    {
        //创建Lifecycle对象
        mLifecycleRegistry = new LifecycleRegistry(this);
        //添加观察者
        getLifecycle().addObserver(new ALifeCycle());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_livedata);
        dataBind();
        //初始化livedata
        liveData = new AMutableLiveData();
        liveData.observeForever(observer);    //livedata被监听者，observer监听者
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UserBean bean = new UserBean();
                bean.setAge("18");
                bean.setName("小明");
                liveData.postValue(bean);

            }
        }, 3000);
    }

    private ActivityLivedataBinding binding;    //创建视图

    /**
     * 创建数据绑定
     */
    void dataBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_livedata);
        binding.name.setTextColor(Color.parseColor("#FFF002"));
        binding.setLifecycleOwner(this);
//        binding.setMyobject(new BindingObject());
//        binding.setUser(bean);
//        bean.setAge("21");
//        bean.setName("老张");

    }

    /**
     * 创建视图绑定
     */

    void viewBind() {
        binding = ActivityLivedataBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.name.setText("老张");
        binding.age.setText("36");
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }

    //获取变化,监听者
    Observer observer = new Observer<UserBean>() {
        @Override
        public void onChanged(UserBean b) {
            binding.setUser(b);
            Log.d("mytest", "姓名:" + bean.getName() + ",年龄:" + bean.getAge());
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        liveData.removeObserver(observer);
        
    }
}
