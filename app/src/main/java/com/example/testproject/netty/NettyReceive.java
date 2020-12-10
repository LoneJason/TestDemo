package com.example.testproject.netty;

import java.util.List;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

public class NettyReceive
{
    public class NoticeAccepter {
        //通知线程组
        private final EventLoopGroup group;
        //启动器
        private final Bootstrap boot;
        public NoticeAccepter() {
            this.group = new NioEventLoopGroup();
            this.boot = new Bootstrap();
            this.boot.group(this.group)
                    .channel(NioDatagramChannel.class)
                    //开启通道底层广播
                    .option(ChannelOption.SO_BROADCAST, true)
                    //端口重用
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast(new NoticeDecoder());
                            pipeline.addLast(new NoticeChannelHanler());
                        }
                    })
                    .localAddress(Constant.ACCEPTER_PORT);
        }

        /**
         * 运行接收器
         */
        public void run(){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //设置不间断接收消息，并绑定通道
                        Channel channel = boot.bind().syncUninterruptibly().channel();
                        System.out.println("接收器启动，端口("+ Constant.ACCEPTER_PORT+")，等待接收通知...");
                        //通道阻塞，直到关闭
                        channel.closeFuture().sync();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                       stop();
                    }
                }
            }).start();

        }

        /**
         * 停止接收消息
         */
        public void stop(){
            try {
                this.group.shutdownGracefully();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * @author andychen https://blog.51cto.com/14815984
     * @description：通知通道处理器
     */
    public class NoticeChannelHanler extends SimpleChannelInboundHandler<Notice> {
        /**
         * 接收广播传递过来的报文
         * @param channelHandlerContext
         * @param notice
         * @throws Exception
         */
        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, Notice notice) throws Exception {
            StringBuffer buffer = new StringBuffer();
            buffer.append("时间[");
            buffer.append(notice.getTime());
            buffer.append("]，广播源[");
            buffer.append(notice.getSource().toString());
            buffer.append("]=====[");
            buffer.append(notice.getId());
            buffer.append("]=====通知内容：");
            buffer.append(notice.getContent());
            //打印接收到的数据
            System.out.println(buffer.toString());
        }

        /**
         * 异常捕获
         * @param ctx 上下文
         * @param cause
         * @throws Exception 异常信息
         */
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }


    /**
     * @author andychen https://blog.51cto.com/14815984
     * @description：通知解码器
     */
    public class NoticeDecoder extends MessageToMessageDecoder<DatagramPacket> {

        /**
         * 解码器核心实现
         * @param channelHandlerContext 处理器上下文
         * @param datagramPacket 数据报
         * @param list 消息列表
         * @throws Exception
         */
        @Override
        protected void decode(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket, List<Object> list) throws Exception {
            //数据报内容
            ByteBuf data = datagramPacket.content();
            //通知id
            int id = data.readInt();
            //发送时间
            long time = data.readLong();
            //分隔符
            data.readByte();
            //当前索引
            int idx = data.readerIndex();
            //通知内容
            String content = data.slice(idx, data.readableBytes()).toString(CharsetUtil.UTF_8);
            //加入消息列表
            list.add(new Notice(id,content, datagramPacket.sender()));
        }
    }
    /**
     * @author andychen https://blog.51cto.com/14815984
     * @description：消息接收器启动器
     */
    public class AccepterRunner {
        /**
         * 运行通知接收任务
         *
         */
        public  void main() {
            NoticeAccepter accepter = null;
            try {
                accepter = new NoticeAccepter();
                accepter.run();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                accepter.stop();
            }
        }
    }
}
