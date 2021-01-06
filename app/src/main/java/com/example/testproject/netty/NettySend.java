package com.example.testproject.netty;

import android.util.Log;

import java.net.InetSocketAddress;
import java.util.List;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

public class NettySend
{
    /**
     * @author andychen https://blog.51cto.com/14815984
     * @description：通知的广播端
     */
    public class NoticeBroadcast {
        //广播线程组
        private final EventLoopGroup group;
        //广播启动器
        private final Bootstrap boot;

        /**
         * 默认构造
         * @param remotePort 接收端端口
         */
        public NoticeBroadcast(int remotePort) {
            this.group = new NioEventLoopGroup();
            this.boot = new Bootstrap();
            //绑定NioDatagramChannel数据报通道
            this.boot.group(group).channel(NioDatagramChannel.class)
                    //设置通道用于广播
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new NoticeEncoder(new InetSocketAddress(Constant.BROADCAST_IP, remotePort)));

        }

        /**
         * 运行广播
         */
        public void run() throws Exception {
            final int[] count = {0};
            //绑定广播通道
            Channel channel = this.boot.bind().sync().channel();
            System.out.println("开始运行广播，发送通知，目标所有主机端口("+Constant.ACCEPTER_PORT+")...");
            //循环广播通知
                    for (;;) {
                        channel.writeAndFlush(new Notice(++count[0], Constant.getNotice(), null));
                        Log.d("mytest",new Notice(++count[0], Constant.getNotice(), null).getContent());
                        //间隔3秒发送
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            Thread.interrupted();
                            e.printStackTrace();
                            break;
                        }
                    }



        }

        /**
         * 停止运行
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
     * @description：广播运行器
     */
    public  class BroadcastRunner {

        public  void main() {
            NoticeBroadcast broadcast = null;
            try {
                broadcast = new NoticeBroadcast(Constant.ACCEPTER_PORT);
                broadcast.run();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                broadcast.stop();
            }
        }
    }
    /**
     * @author andychen https://blog.51cto.com/14815984
     * @description：通知编码器
     */
    public class NoticeEncoder extends MessageToMessageEncoder<Notice> {
        //目的地
        private final InetSocketAddress target;

        public NoticeEncoder(InetSocketAddress target) {
            this.target = target;
        }

        /**
         * 编码方法实现
         * @param ctx 处理器上下文
         * @param notice 通知对象
         * @param list 集合
         * @throws Exception
         */
        protected void encode(ChannelHandlerContext ctx, Notice notice, List<Object> list) throws Exception {
            //内容数据
            byte[] bytes = notice.getContent().getBytes(CharsetUtil.UTF_8);
            //定义缓冲:一个int型+一个long型+内容长度+分隔符
            int capacity = 4+8+bytes.length+1;
            ByteBuf buf = ctx.alloc().buffer(capacity);
            //写通知id
            buf.writeInt(notice.getId());
            //发送时间
            buf.writeLong(notice.getTime());
            //分隔符
            buf.writeByte(Notice.SEPARATOR);
            //内容
            buf.writeBytes(bytes);
            //加入消息列表
            list.add(new DatagramPacket(buf, target));
        }
    }
}
