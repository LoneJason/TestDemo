package com.example.testproject;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class GetDatagramSocketUtil extends Thread
{

    public GetDatagramSocketUtil()
    {
        Log.d("mytest", "开始获取");
    }
    @Override
    public void run() {
        super.run();
        getSocket();
    }

    public void  getSocket()
    {
        try {
            DatagramSocket socket=new DatagramSocket(10000);
            socket.setSoTimeout(1000 * 60); //设置超时时间， 超过时间没有收到停止接收
            byte[] b=new byte[1024];
            DatagramPacket dpReceive=new DatagramPacket(b, b.length);

            while (true)
            {
                    socket.receive(dpReceive);
                    byte[] Data=dpReceive.getData();
                    int len=Data.length;
                        Log.d("mytest","UDP客户端发送的内容是：" + new String(Data, 0, len).trim());
                        Log.d("mytest","UDP客户端IP：" + dpReceive.getAddress());
                        Log.d("mytest","UDP客户端端口：" + dpReceive.getPort());
                  
            }

        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
