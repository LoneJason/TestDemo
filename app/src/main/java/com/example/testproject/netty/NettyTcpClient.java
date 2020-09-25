//package com.example.testproject.netty;
//
//import android.content.Context;
//import android.text.TextUtils;
//import android.util.Log;
//
//import java.net.InetSocketAddress;
//import java.util.List;
//import java.util.Timer;
//import java.util.TimerTask;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//import io.netty.bootstrap.Bootstrap;
//import io.netty.buffer.ByteBuf;
//import io.netty.buffer.Unpooled;
//import io.netty.channel.Channel;
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.ChannelOption;
//import io.netty.channel.ChannelPipeline;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioSocketChannel;
//import io.netty.handler.codec.DelimiterBasedFrameDecoder;
//import io.netty.handler.codec.bytes.ByteArrayDecoder;
//import io.netty.handler.timeout.IdleStateHandler;
//
//
///**
// * @ author : lgh_ai
// * @ e-mail : lgh_developer@163.com
// * @ date   : 19-6-13 下午3:17
// * @ desc   : 初始化netty，连接，断开，发送数据，心跳等
// */
//
//
//public class NettyTcpClient {
//
//    public static final String TAG = "NettyTcpClient";
//    public static NettyTcpClient nettyClient;
//
//    private ScheduledExecutorService executorService;    //开启一个服务
//    private EventLoopGroup eventLoopGroup;               //EventLoop线程组
//    private Bootstrap b;                                 //引导程序
//    private static Channel channel;                      //tcp连接通道对象
//
//    private String hostIP;                               //登录ip地址
//    private int port;                                    //登录端口号
//
//    private TimerTask task;                              //心跳任务
//    private Timer timer;                                 //心跳计时
//
//    public static boolean isConnSucc = false;            //是否连接成功
//
//    /**
//     * NettyTcpClient
//     *
//     * @return 单例对象
//     */
//    public synchronized static NettyTcpClient getInstance() {
//        if (nettyClient == null) {
//            synchronized (NettyTcpClient.class) {
//                if (nettyClient == null) {
//                    nettyClient = new NettyTcpClient();
//                }
//            }
//        }
//        return nettyClient;
//    }
//
//
//    public NettyTcpClient() {
//        initServer();
//
//    }
//
//
//    public void initServer() {
//        eventLoopGroup = new NioEventLoopGroup();
//        b = new Bootstrap();
//        b.group(eventLoopGroup);   //1 设置reactor 线程
//        b.channel(NioSocketChannel.class); //2.设置nio类型的channel
//        b.localAddress(new InetSocketAddress(port));   //设置监听端口号
//        b.option(ChannelOption.SO_KEEPALIVE, true); //维持链接的活跃，清除死链接(SocketChannel的设置)
//        b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000);  //超时时长
//        b.option(ChannelOption.TCP_NODELAY, true);  //关闭延迟发送
//        b.handler(new ChannelInitializer<SocketChannel>() //5 装配流水线
//        {
//            //有连接到达时会创建一个channel
//            @Override
//            protected void initChannel(SocketChannel socketChannel) throws Exception {
//                ChannelPipeline pipeline = socketChannel.pipeline();
//                //自定义分隔符解码类(解决粘包,去包尾)
//                pipeline.addLast(new DelimiterBasedFrameDecoder(1572, Unpooled.copiedBuffer(hexStringtoBytes("eefc"))));
//                //byte解码器
//                pipeline.addLast(new ByteArrayDecoder());
//                //读写超时监听
//                pipeline.addLast("ping", new IdleStateHandler(15, 15, 15, TimeUnit.SECONDS));
//                //数据接收监听
//                pipeline.addLast("handler", new NettyTcpClientHandler());
//            }
//        });
//    }
//
//
//    //连接通道
//    public void connServer(String mhost, int mport) {
//        this.hostIP = mhost;
//        this.port = mport;
//        if (executorService != null && executorService.isShutdown()) {
//            executorService.shutdown();
//            executorService = null;
//        }
//        if (executorService == null) {
//            executorService = Executors.newScheduledThreadPool(1);
//        }
//        executorService.scheduleWithFixedDelay(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    //连接服务器
//                    if (channel != null && channel.isOpen()) {
//                        channel.close();
//                        channel = null;
//                    }
//                    channel = b.connect(hostIP, port).sync().channel();
//                    if (channel.isOpen()) {
//                        /** tcp连接成功后发送心跳 */
//                        heartTask();
//                        isConnSucc = true;
//                        //赋值退出登录标志位
//                        Config.IS_LOGOUT = false;
//                        //连接成功后发送主机锁屏命令
//                        sendCmd("FFF0", "86", "0F00000000", false);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    EventBus.getDefault().post(new TcpConnectStatus(2));  //发送通道连接失败广播
//                    onStop();
//                    Log.d(NettyTcpClient.TAG, "连接失败");
//                } finally {
//                    if (isConnSucc) {
//                        if (executorService != null) {
//                            executorService.shutdown();
//                            executorService = null;
//                        }
//                    }
//                }
//            }
//        }, 1, 3, TimeUnit.SECONDS);
//    }
//
//
//    /**
//     * 心跳
//     */
//    private void heartTask() {
//        if (task == null && timer == null) {
//            timer = new Timer();
//            task = new TimerTask() {
//                @Override
//                public void run() {
//                    if (channel != null) {
//                        sendCmd("fff0", "86", "0000000000", true);
//                    }
//                }
//            };
//            timer.schedule(task, 1000, 5000);
//        }
//    }
//
//
//    /**
//     * 单包发送
//     * 发送tcp指令数据包到指定的ip与端口
//     *
//     * @param tcpCommand 指令字节
//     * @param isHeart    是否为心跳指令
//     */
//    public void sendPackage(final byte[] tcpCommand, boolean isHeart) {
//        Context context = MettingControlApplication.getContext();
//
//        //发送无网络广播
//        if (!isHeart)
//            if (!isNetworkAvailable(context)) {
//                EventBus.getDefault().post(new NoNetWorkEvent());
//                return;
//            }
//
//        //发送通道连接断开广播
//        if (!isConnSucc) {
//            if (!isHeart)
//                EventBus.getDefault().post(new TcpConnectStatus(3));
//            return;
//        }
//
//        // 向网段类所有机器广播发TCP，这是想客户端发送内容
//        String hexString = HexUtils.bytesToHexString(tcpCommand);
//        Log.d(TAG, "发送数据包 " + hexString + " >>> ");
//        ByteBuf byteBuf = Unpooled.copiedBuffer(tcpCommand);
//        try {
//            channel.writeAndFlush(byteBuf).sync();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    /**
//     * 命令拼接封装
//     * model层最终调用此方法发送具体指令
//     *
//     * @param deviceID     设备ID
//     * @param cmdType      指令类型
//     * @param cmdParameter 指令参数
//     * @param isHeart      是否为心跳
//     */
//    public void sendCmd(String deviceID, String cmdType, String cmdParameter, boolean isHeart) {
//        StringBuffer cmdStr = new StringBuffer();
//        //协议头
//        cmdStr.append("AAEE");
//        //长度
//        cmdStr.append("08");
//        //设备ID
//        cmdStr.append(deviceID);
//        //指令类型
//        cmdStr.append(cmdType);
//        //指令参数
//        cmdStr.append(cmdParameter);
//        //协议尾
//        cmdStr.append("EEFC");
//        if (!TextUtils.isEmpty(cmdStr)) {
//            byte[] bytes = hexStringtoBytes(cmdStr.toString());
//            sendPackage(bytes, isHeart);
//        }
//    }
//
//
//    /**
//     * 多包发送
//     * List<byte[]> 容器打包byte[]
//     */
//    public void sendPackages(List<byte[]> list, Context context) {
//        for (byte[] udpcommand : list) {
//            sendPackage(udpcommand, false);
//            try {
//                Thread.sleep(30);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//
//    /**
//     * 关闭通道 、线程池
//     */
//    public synchronized void onStop() {
//        isConnSucc = false;
//
//        if (channel != null && channel.isOpen()) {
//            channel.close();
//            channel = null;
//        }
//        if (executorService != null) {
//            executorService.shutdown();
//            executorService = null;
//        }
//
//        if (task != null) {
//            task.cancel();
//            task = null;
//        }
//
//        if (timer != null) {
//            timer.cancel();
//            timer = null;
//        }
//    }
//
//    /**
//     * 重新连接通道
//     */
//    public void reconnectChannel() {
//        //已登录才能重连, 退出登录不需要重连
//        if (!Config.IS_LOGOUT)
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    String ip = AppDataCache.getInstance().getString(IP);
//                    String port = AppDataCache.getInstance().getString(PORT);
//                    NettyTcpClient.getInstance().connServer(ip, Integer.parseInt(port));
//                }
//            }).start();
//    }
//
//
//}
