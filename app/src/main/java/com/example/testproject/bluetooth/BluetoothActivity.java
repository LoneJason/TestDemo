package com.example.testproject.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testproject.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BluetoothActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<String> deviceName = new ArrayList<>();
    List<BluetoothDevice> devices = new ArrayList<>();
    BluetoothDevice device;
    BluetoothAdapter bluetoothAdapter;
  MyAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        recyclerView = findViewById(R.id.bluetooth_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter=new MyAdapter();
        recyclerView.setAdapter(adapter);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();   //获取蓝牙适配器
        //开启权限检查
//        getBlePermissionFromSys();
        Permission();
        if(bluetoothAdapter==null)   //判断是否有蓝牙
        {
            Toast.makeText(this,"本机没有蓝牙设备",Toast.LENGTH_SHORT).show();
            finish();
        }
        if(!bluetoothAdapter.isEnabled())  //判断蓝牙功能有没有打开
        {
            bluetoothAdapter.enable();
        }
        serchBle();
//        serchBleTwo();
    }

    //蓝牙扫描方式1，经典蓝牙
    public void serchBle()
    {
        //创建一个IntentFilter对象，将其action指定为BluetoothDevice.ACTION_FOUND,查找蓝牙
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        //注册广播接收器
        registerReceiver(mReceiver, intentFilter);
        IntentFilter filterStart = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        registerReceiver(mReceiver, filterStart);

        IntentFilter filterFinish = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filterFinish);

        bluetoothAdapter.cancelDiscovery();    //暂停蓝牙扫描
        bluetoothAdapter.startDiscovery();    //开启蓝牙扫描
        Toast.makeText(this,"开始扫描",Toast.LENGTH_SHORT).show();
    }
    //蓝牙扫描方式2,低耗蓝牙
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void serchBleTwo()
    {
        BluetoothAdapter.LeScanCallback callback=new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                if(!devices.contains(device))
                {
                    deviceName.add(device.getName());   //获取设备名称
                    devices.add(device);   //获取设备
                    adapter.notifyDataSetChanged();   //刷新recyclerView数据
                    Log.d("mytest",device.getAddress());
                }
            }
        };
        bluetoothAdapter.startLeScan(callback);      //打开蓝牙，并且把搜索到的结果返回到回调中
    }
    //蓝牙扫描的返回结果
    BroadcastReceiver mReceiver=new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothDevice.ACTION_FOUND.equals(action))     //判断广播返回是否是蓝牙的返回
        {

            device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);   //获取当前的蓝牙设备
            if (device == null) return;
            if(!devices.contains(device))    //判断当前设备有没有被添加进去
            {
                deviceName.add(device.getName());   //获取设备名称
                devices.add(device);   //获取设备
                adapter.notifyDataSetChanged();   //刷新recyclerView数据
            }
        }
    }
};
    //蓝牙点击配对
    public void link(BluetoothDevice device)
    {
         Toast.makeText(this,"开始匹配",Toast.LENGTH_SHORT).show();
        try {
            Method method = BluetoothDevice.class.getMethod("createBond");
            method.invoke(device);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //检查配对对象绑定情况
    public Boolean checkBind(BluetoothDevice device)
    {
        if(bluetoothAdapter!=null)
        {
            Set<BluetoothDevice> linkDevice=bluetoothAdapter.getBondedDevices();  //获取已绑定的蓝牙设备
            if(linkDevice.contains(device))
            {
                Toast.makeText(this,"已经绑定过",Toast.LENGTH_SHORT).show();
                return false;
            }
            else
            {
                Toast.makeText(this,"暂无绑定",Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    //取消配对
    public void UnBindBlueTooth(BluetoothDevice device)
    {
        if(!checkBind(device))    //在取消配对之前要先判断它有没有配对过，要配对过的才能取消配对
        {
            try {
                Method method=device.getClass().getMethod("removeBond", (Class[]) null);
                method.invoke(device,(Object[]) null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //无弹窗配对
    public void NoToastLink()
    {

    }
    class MyAdapter extends RecyclerView.Adapter {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ble_recycler, parent, false);
            Myholder myholder = new Myholder(view);
            return myholder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Myholder my = (Myholder) holder;
            my.text.setText(deviceName.get(position));
            my.text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bluetoothAdapter.cancelDiscovery();    //暂停蓝牙扫描
                    BluetoothDevice CurrentDevice=devices.get(position);
                    if(checkBind(CurrentDevice))   //当前没有配对绑定的情况下就开始配对
                    {
                        link(CurrentDevice);
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return deviceName.size();
        }

        public class Myholder extends RecyclerView.ViewHolder {
            TextView text;

            public Myholder(@NonNull View itemView) {
                super(itemView);
                text = itemView.findViewById(R.id.ble_text);
            }
        }
    }

    //动态开启定位权限，因为6.0后的版本就需要这个了
    public void getBlePermissionFromSys() {
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 102;
            String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }
    }
    //踩坑Android10以后就要用ACCESS_FINE_LOCATION，如果用ACCESS_COARSE_LOCATION是扫描不出来任何东西的
    public void Permission()
    {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        for (String str : permissions) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    this.requestPermissions(permissions,103);
                    return;
                }

            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (bluetoothAdapter != null) {
                bluetoothAdapter.cancelDiscovery();
            }
            unregisterReceiver(mReceiver);    //关闭广播
        }
        catch (Exception e){

        }
    }
}
