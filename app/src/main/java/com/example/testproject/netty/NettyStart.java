package com.example.testproject.netty;

import android.util.Log;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;


public class NettyStart {
    /**
     * 启动netty服务器
     * 流程如下：
     * 1.ServerHandler 实现了业务逻辑
     * 2.使用ServerBootstrap引导和绑定服务器
     * 3.分配一个NioEventLoopGroup实例进行事件的处理，如接受新连接以及读/写数据
     * 4.指定服务器绑定的本地端口号
     * 5.在每次有新的连接的时候就会创建新的channel，ChannelInitializer就会使用一个NettyServer实例化每一个channel
     * 6.调用ServerBootstrap的bind方法进行绑定服务器
     */
    void startServerNetty() throws InterruptedException {
        final ServerHandler server = new ServerHandler();
        EventLoopGroup group = new NioEventLoopGroup();  //创建EventLoopGroup
        ServerBootstrap bootstrap = new ServerBootstrap();  //创建服务引导
        bootstrap.group(group)
                .channel(NioServerSocketChannel.class)  //指定NIO传输Channel
                .localAddress(12530)  //指定端口设置套接字地址
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(server);   //把NettyServer添加到子channel的channelpipelien中来
                    }
                });
        ChannelFuture future = bootstrap.bind().sync(); //异步绑定服务器，调用sync方法阻塞，等待直到绑定完成
        future.channel().closeFuture().sync();  //获取channel的closeFuture，并且阻塞当前线程直到它完成
        group.shutdownGracefully().sync(); //关闭EventLoopGroup，释放所有资源
    }

    /**
     * Netty服务的业务流程，这个最终也是继承ChannelHandler，所以这个就是一个处理channel业务逻辑的
     */
    public class ServerHandler extends ChannelInboundHandlerAdapter {
        //当有数据到来时，eventLoop被唤醒而调用channelRead方法处理数据
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            super.channelRead(ctx, msg);
            ByteBuf in = (ByteBuf) msg;
            Log.d("mytest", in.toString(CharsetUtil.UTF_8));
            ctx.write(in);     //将接收到的消息写给发送者，而不冲刷出站消息
        }

        //通知ChannelInboundHandler最后一次channelRead的调用是当前批量读取中的最后一条消息，也就是说数据已经读完了就回调这个方法
        //也就是msg的自己到0个或者小于buffer的容量，这都代表数据读完了。
        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            super.channelReadComplete(ctx);
            ctx.writeAndFlush(Unpooled.EMPTY_BUFFER);
        }

        //发生异常时调用
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            super.exceptionCaught(ctx, cause);
        }
    }

    /**
     * 启动netty客户端
     * 流程如下：
     * 1.ClientHandler，创建一个handler，业务处理类
     * 2.使用Bootstrap去引导
     * 3.为事件处理分配一个NioEventLoopGroup，其中事件处理包括创建新的连接，以及处理进站出站的数据
     * 4.设置一个连接服务器的地址和端口号
     * 5.当连接被创建的时候，把ClientHandler安装到channel的pipeline
     * 6.配置完成调用bootstrap的connect进行连接
     */
    void startClient(String host, int port) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();  //创建EventLoopGroup
        Bootstrap bootstrap = new Bootstrap();  //创建引导
        bootstrap.group(group)
                .channel(NioSocketChannel.class)    //指定Nio的channel
                .remoteAddress(new InetSocketAddress(host, port))   //设置服务器的地址和端口号
                .handler(new ChannelInitializer<SocketChannel>()   //在创建channel的时候，向ChannelPipeline中添加一个ClientHandler的实例
                {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ClientHandler());   //把Handler添加进channel的pipeline中
                    }
                });
        ChannelFuture future = bootstrap.connect().sync();     //连接远程节点，阻塞等待并且等待直到连接完成，返回一个ChannelFuture
        future.channel().closeFuture().sync();  //阻塞直到channel关闭
        group.shutdownGracefully().sync();  //关闭线程池并且释放所有资源
    }

    /**
     * 客户端的业务流程,最终也是ChannelHandler，所以这个就是一个处理channel业务逻辑的
     */
    public class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

        //从服务器中接收到一条消息时被调用
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
            Log.d("mytest", msg.toString(CharsetUtil.UTF_8));   //接收到服务器的消息

        }

        //服务器的连接已经建立后将被调用
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            ctx.writeAndFlush(Unpooled.copiedBuffer("这是一条消息", CharsetUtil.UTF_8));  //给服务器发送一条消息
        }

        //发生异常的时候调用
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            super.exceptionCaught(ctx, cause);
            cause.printStackTrace();   //记录异常错误
            ctx.close();   //关闭channel
        }
    }
}
