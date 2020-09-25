package com.example.testproject;

import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class NIOSocket extends Thread {
    private Selector selector;
    private SocketChannel socketChannel;
    final private String LogName="MyTest";
    public void mySocket() throws IOException {
        socketChannel =SocketChannel.open();  //打开通道
        socketChannel.configureBlocking(false);  //设置为非阻塞模式
         selector=Selector.open();   //创建选择器
    }
    @Override
    public void run() {
        super.run();

        try {
            //channel发起连接
            if( socketChannel.connect(new InetSocketAddress("",8080)))
            {
                //如果连接成功后就注册到选择器上面来
                socketChannel.register(selector, SelectionKey.OP_READ);
                //发起请求
                byte[] req = "QUERY TIME ORDER".getBytes();
                ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);  //初始化长度
                writeBuffer.put(req);   //写入数据
                writeBuffer.flip();     //转换成写出模式
                socketChannel.write(writeBuffer);  //通过通道写数据
                if (!writeBuffer.hasRemaining())
                {
                    Log.d(LogName,"写出数据成功");
                }
            }
            else
            {
                Log.d(LogName,"连接失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
