package com.example.testproject.netty;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.ReferenceCountUtil;

public class AirKiss {
    EventLoopGroup group = new NioEventLoopGroup();  //创建EventLoopGroup

    Channel channel;

    public void startBoot() {
        Bootstrap bootstrap = new Bootstrap();  //创建引导
        bootstrap.group(group)
                .channel(NioDatagramChannel.class)    //指定Nio的channel
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new ChannelInitializer<DatagramChannel>() {
                    @Override
                    protected void initChannel(DatagramChannel ch) throws Exception {
                        ch.pipeline().addLast(new three());
                        ch.pipeline().addLast(new two());
                        ch.pipeline().addLast(new myOutBoundHandler());
                        ch.pipeline().addLast(new NormalUDPClientHandler());
                    }
                })
        ;
        try {
            channel = bootstrap.bind(0).sync().channel();   //因为广播是无连接的，只有绑定功能

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage() throws UnknownHostException {
        byte text[] = "这是一段测试的文本".getBytes();
        DatagramPacket pkg = new DatagramPacket(text, text.length, InetAddress.getByName("255.255.255.255"), 0);
        channel.writeAndFlush(pkg);
    }

    public void closeNetty() throws InterruptedException {
        group.shutdownGracefully().sync();  //关闭线程池并且释放所有资源
    }

    public class NormalUDPClientHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            Log.d("mytest", ctx.channel().remoteAddress() + "");
            byte DUMMY_DATA[] = new byte[1500];
//              DatagramPacket pkg = new DatagramPacket(DUMMY_DATA, anEncoded_data, InetAddress.getByName("255.255.255.255"), 10000);
//               ctx.writeAndFlush(pkg);

        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            Log.d("mytest", ctx.channel().remoteAddress() + "");
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            Log.d("mytest", msg.toString());
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
        }

        //channel已经被注册到了eventLoop
        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            super.channelRegistered(ctx);
        }

        //channel已经被创建，但还未注册到eventLoop
        @Override
        public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
            super.channelUnregistered(ctx);
        }
    }


    public class myOutBoundHandler extends ChannelOutboundHandlerAdapter {
        //当请求将channel绑定到本地调用
        @Override
        public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
            super.bind(ctx, localAddress, promise);
            Log.d("mytest", "bind");
//            byte text[] = "这是一段测试的文本".getBytes();
//            DatagramPacket pkg = new DatagramPacket(text, text.length, InetAddress.getByName("255.255.255.255"), 8888);
//            ctx.writeAndFlush(pkg);
        }

        //当channel连接到远程节点时候被调用
        @Override
        public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
            super.connect(ctx, remoteAddress, localAddress, promise);
            Log.d("mytest", "connect");
        }

        //当请求将channel从远程节点断开时被调用
        @Override
        public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
            super.disconnect(ctx, promise);
        }

        //请求关闭channel的时候调用
        @Override
        public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
            super.close(ctx, promise);
        }

        //请求将channel从它的eventLoop注销时调用
        @Override
        public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
            super.deregister(ctx, promise);
        }

        //从channel中读取更多的数据
        @Override
        public void read(ChannelHandlerContext ctx) throws Exception {
            super.read(ctx);
            Log.d("mytest", "read");
        }

        //当请求通过channel将入队数据冲刷到远程节点时候被调用
        @Override
        public void flush(ChannelHandlerContext ctx) throws Exception {
            super.flush(ctx);
            Log.d("mytest", "flush");
        }

        //当请求通过channel将数据写到远程节点被调用
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            super.write(ctx, msg, promise);
            Log.d("mytest", "发送数据是" + msg.toString());
        }
    }

    public class two extends ChannelOutboundHandlerAdapter {

        //从channel中读取更多的数据
        @Override
        public void read(ChannelHandlerContext ctx) throws Exception {
            super.read(ctx);
            Log.d("mytest", "two_read");
        }

        //当请求通过channel将入队数据冲刷到远程节点时候被调用
        @Override
        public void flush(ChannelHandlerContext ctx) throws Exception {
            super.flush(ctx);
            Log.d("mytest", "two_flush");
        }

        //当请求通过channel将数据写到远程节点被调用
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            super.write(ctx, msg, promise);
            Log.d("mytest", "two_发送数据是" + msg.toString());

        }
    }
    public class three extends ChannelOutboundHandlerAdapter {

        //从channel中读取更多的数据
        @Override
        public void read(ChannelHandlerContext ctx) throws Exception {
            super.read(ctx);
            Log.d("mytest", "three_read");
        }

        //当请求通过channel将入队数据冲刷到远程节点时候被调用
        @Override
        public void flush(ChannelHandlerContext ctx) throws Exception {
            super.flush(ctx);
            Log.d("mytest", "three_flush");
        }

        //当请求通过channel将数据写到远程节点被调用
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            super.write(ctx, msg, promise);
            Log.d("mytest", "three_发送数据是" + msg.toString());
        }
    }
}

