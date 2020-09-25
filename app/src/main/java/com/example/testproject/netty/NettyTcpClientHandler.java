//package com.example.testproject.netty;
//
//import android.util.Log;
//
//
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.SimpleChannelInboundHandler;
//import io.netty.channel.socket.DatagramPacket;
//import io.netty.handler.timeout.IdleState;
//import io.netty.handler.timeout.IdleStateEvent;
//
//
//
///**
// * @ author : lgh_ai
// * @ e-mail : lgh_developer@163.com
// * @ date   : 19-6-13 下午3:27
// * @ desc   : 数据回调处理，连接状态等
// */
//public class NettyTcpClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {
//
//    public static final int PACKAGE_HEARD_LENGTH = 2;                 //包头长度
//    public static final int PACKAGE_DEVICE_ID_LENGTH = 2;             //设备ID长度
//    public static final int PACKAGE_TOTAL_LENGTH_LENGTH = 1;          //包总长度
//    public static final int PACKAGE_CMD_TYPE_LENGTH = 1;              //指令类型长度
//    public static final int PACKAGE_CMD_PARA_LENGTH = 1;              //指令参数第一个字节长度
//    public static final int PACKAGE_CMD_LAST_TWO_LENGTH = 2;          //协议数据倒数三位字节长度(去包尾)
//
//    public static Map<String, List<byte[]>> mBuffMap = new HashMap<>();           //指令数据存储
//    public static Map<String, String> mLiftingMicIdMap = new HashMap<>();         //已升起话筒存储
//
//    /**
//     * tcp数据回调
//     * <p>
//     * 完整包 aaee08ffe1820108000000eefc >> aaee08ffe1820108000000
//     * 由于分隔符解决粘包，故包尾eefc在返回的每一个包中都过滤掉
//     *
//     * @param ctx
//     * @param msg
//     */
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        super.channelRead(ctx, msg);
//
//        //含有包头，去除包尾数据 （DelimiterBasedFrameDecoder类去除包尾分隔符）
//        byte[] allData = (byte[]) msg;
//
//        Log.e(NettyTcpClient.TAG, "接收服务器数据包(去包尾eefc) >>> " + bytesToHexString(allData));
//
//        //获取主要数据（长度一字节 + 主要数据八字节，即去除包头）
//        allData = subBytes(allData, PACKAGE_HEARD_LENGTH, allData.length - PACKAGE_HEARD_LENGTH);
//
//        int index = PACKAGE_TOTAL_LENGTH_LENGTH;
//        //获取设备ID
//        String deviceIDStr = bytesToHexString(subBytes(allData, index, PACKAGE_DEVICE_ID_LENGTH));
//        index += PACKAGE_DEVICE_ID_LENGTH;
//
//        //获取指令类型（f0 特殊处理）
//        String cmdTypeStr = bytesToHexString(subBytes(allData, index, PACKAGE_CMD_TYPE_LENGTH));
//        index += PACKAGE_CMD_TYPE_LENGTH;
//
//        //获取指令参数第一个字节
//        String cmdParaFirstStr = bytesToHexString(subBytes(allData, index, PACKAGE_CMD_PARA_LENGTH));
//        index += PACKAGE_CMD_TYPE_LENGTH;
//
//        //获取指令参数第二个字节
//        String cmdParaSecondStr = bytesToHexString(subBytes(allData, index, PACKAGE_CMD_PARA_LENGTH));
//        index += PACKAGE_CMD_TYPE_LENGTH;
//
//        //获取指令参数后第一个字节，作为编ID区分
//        String cmdParaEditID = bytesToHexString(subBytes(allData, index, PACKAGE_CMD_PARA_LENGTH));
//        index += PACKAGE_CMD_PARA_LENGTH;
//
//        //获取倒数两位字节，作为判断升降话筒状态依据
//        String cmdParaLastTwo = bytesToHexString(subBytes(allData, index, PACKAGE_CMD_LAST_TWO_LENGTH));
//
//        //话筒开关标志位
//        String micSwitchTag = cmdParaFirstStr + cmdParaSecondStr;
//
//        /** aaee08 fff0 86 33 01 03 0000eefc  主机正在编ID */
//        /** aaee08fff086 1801 000000eefc  主机ID重复 */
//        /** aaee08fff086 1800 000000eefc  主机ID回复正常 */
//        /** aaee08fff0800001000000eefc  拔插话筒 */
//
//        //单包/多包存储键值对
//        String cmdKey = "";
//        //有线、无线话筒配置参数key
//        if (deviceIDStr.equals("ffe1") || deviceIDStr.equals("ffea"))
//            cmdKey = deviceIDStr + cmdTypeStr;
//            //话筒列表回复包key
//        else if ((cmdTypeStr + cmdParaFirstStr).equals("8631") || (cmdTypeStr + cmdParaFirstStr).equals("8630"))
//            cmdKey = cmdTypeStr + cmdParaFirstStr;
//            //话筒打开或关闭,ID重复回复包，获取话筒灵敏度包key
//        else if (micSwitchTag.equals("0006") || micSwitchTag.equals("0008") || micSwitchTag.equals("0005") || micSwitchTag.equals("0007")
//                || micSwitchTag.equals("0444") || micSwitchTag.equals("000d") || micSwitchTag.equals("000e") || micSwitchTag.equals("1801")
//                || micSwitchTag.equals("1800"))
//            cmdKey = cmdParaFirstStr + cmdParaSecondStr;
//            //主机正在编ID过程
//        else if ((cmdParaSecondStr + cmdParaEditID).equals("0103"))
//            cmdKey = cmdParaSecondStr + cmdParaEditID;
//            //升降话筒最顶端通知
//        else if ((cmdParaFirstStr + cmdParaSecondStr + cmdParaEditID + cmdParaLastTwo).equals("0458000001"))
//            cmdKey = cmdParaFirstStr + cmdParaSecondStr + cmdParaEditID + cmdParaLastTwo;
//        else //fff0类型包key
//            cmdKey = deviceIDStr + cmdTypeStr + cmdParaFirstStr;
//
//        //单包多包接收处理
//        List<byte[]> byteList = new ArrayList<>();
//        if (mBuffMap.get(cmdKey) != null && mBuffMap.get(cmdKey).size() > 0)
//            byteList.addAll(mBuffMap.get(cmdKey));
//        byteList.add(allData);
//        mBuffMap.put(cmdKey, byteList);
//
//        //指令分类并以eventbus方式发送数据
//        switch (cmdKey) {
//            case "ffe182": //设置模块：有线话筒模式 单包
//                EventBus.getDefault().post(new MicModeParaEvent(mBuffMap.get("ffe182")));
//                mBuffMap.remove("ffe182");
//                break;
//            case "ffea82":  //设置模块：无线话筒模式 单包
//                EventBus.getDefault().post(new MicModeParaEvent(mBuffMap.get("ffea82")));
//                mBuffMap.remove("ffea82");
//                break;
//            case "8631":  //话筒列表（有线/无线）多包
//                EventBus.getDefault().post(new MicListEvent(mBuffMap.get("8631")));
//                mBuffMap.remove("8631");
//                /** aaee 08 fff0 86 1800000000
//                 aaee 08 0002 86 3100000000
//                 aaee 08 000b 86 3100000000
//                 aaee 08 000f 86 3100000000
//                 aaee 08 0010 86 3101000000
//                 aaee 08 fff0 86 3001000004
//
//                 aaee 08 3001 86 3101000000
//                 aaee 08 3003 86 3100000000
//                 aaee 08 2ff0 86 3001010002 */
//
//                break;
//            case "8630":   //有线、无线扫描完成后回复的话筒数量总数
//                EventBus.getDefault().post(new MicListEvent(mBuffMap.get("8630")));
//                mBuffMap.remove("8630");
//                break;
//            case "0006": //代表机关闭话筒
//                EventBus.getDefault().post(new MicSwitchEvent(mBuffMap.get("0006")));
//                mBuffMap.remove("0006");
//                break;
//            case "0008": //主席机关闭话筒
//                EventBus.getDefault().post(new MicSwitchEvent(mBuffMap.get("0008")));
//                mBuffMap.remove("0008");
//                break;
//            case "0005":  //代表机开话筒
//                EventBus.getDefault().post(new MicSwitchEvent(mBuffMap.get("0005")));
//                mBuffMap.remove("0005");
//                break;
//            case "0007":  //主席机开话筒
//                EventBus.getDefault().post(new MicSwitchEvent(mBuffMap.get("0007")));
//                mBuffMap.remove("0007");
//                break;
//            case "0444":  //获取话筒灵敏度
//                EventBus.getDefault().post(new MicSensitivityEvent(mBuffMap.get("0444")));
//                mBuffMap.remove("0444");
//                break;
//            case "000d":  //话筒等待中状态
//                EventBus.getDefault().post(new MicSwitchEvent(mBuffMap.get("000d")));
//                mBuffMap.remove("000d");
//                break;
//            case "000e": //话筒等待直到关闭状态
//                EventBus.getDefault().post(new MicSwitchEvent(mBuffMap.get("000e")));
//                mBuffMap.remove("000e");
//                break;
//            case "0103":  //主机正在编ID过程中
//                EventBus.getDefault().post(new EditIDEvent(0));
//                Config.IS_EDITING_ID = true;
//                mBuffMap.remove("0103");
//
//                //aaee08fff0800380000000eefc 正在编ID
//                //aaee08fff0861800000000eefc
//                //aaee08fff0861800000000eefc
//                //aaee08fff0861800000000eefc
//                //aaee080005862101000000eefc
//
//                // aaee08fff0800001000000 编ID结束
//                // aaee08fff0861800000000
//                break;
//            case "1801":  //主机编ID重复
//                EventBus.getDefault().post(new EditIDEvent(1));
//                Config.IS_EDIT_ID_REPEAT = true;
//                mBuffMap.remove("1801");
//                break;
//            case "1800": //主机编ID重复恢复正常
//                Config.IS_EDIT_ID_REPEAT = false;
//                Config.IS_EDITING_ID = false;
//                mBuffMap.remove("1800");
//                break;
//            case "0458000001": //升降话筒升到最顶端通知 aaee08000a800458000001
//                Config.IS_LIFTING_HIGHEST = true;
//                EventBus.getDefault().post(new LiftingHighestEvent(mBuffMap.get("0458000001")));
//                //存储已升起的话筒
//                List<byte[]> bytes = mBuffMap.get("0458000001");
//                String hexString = bytesToHexString(bytes.get(bytes.size() - 1));
//                final String id = hexString.substring(2, 6);
//                mLiftingMicIdMap.put(hexString, id);
//                mBuffMap.remove("0458000001");
//                break;
//            default:
//                break;
//
//            // TODO: 19-12-17 id重复有线话筒全部关闭，无线话筒不影响
//            // TODO: 19-12-17 主机正在编ID，全部话筒都关闭
//
//        }
//
//    }
//
//
//    /**
//     * TCP通道是否开启
//     *
//     * @param ctx 通道对象
//     * @throws
//     */
//    @Override
//    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
//        Log.d(NettyTcpClient.TAG, "TCP通道已经开启 ");
//    }
//
//
//    /**
//     * TCP通道连接成功
//     *
//     * @param ctx
//     * @throws
//     */
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        super.channelActive(ctx);
//        Log.d(NettyTcpClient.TAG, "TCP通道连接成功 ");
//
//        //发送通道连接成功广播
//        EventBus.getDefault().post(new TcpConnectStatus(1));
//    }
//
//
//    /**
//     * 断开连接触发channelInactive
//     *
//     * @param ctx
//     * @throws
//     */
//    @Override
//    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        NettyTcpClient.getInstance().onStop();  //断开连接
//        if (!Config.IS_LOGOUT)
//            EventBus.getDefault().post(new TcpConnectStatus(3));  //发送通道连接断开广播
//        Log.d(NettyTcpClient.TAG, "断开连接");
//    }
//
//
//    /**
//     * 在指定时间内未进行读写操作回调
//     *
//     * @param ctx
//     * @param evt
//     * @throws
//     */
//    @Override
//    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        super.userEventTriggered(ctx, evt);
//
//        if (evt instanceof IdleStateEvent) {
//
//            IdleStateEvent event = (IdleStateEvent) evt;
//
//            if (event.state().equals(IdleState.READER_IDLE)) {
//                //未进行读操作
//                Log.d(NettyTcpClient.TAG, "READER_IDLE");
//                ctx.close();
//
//            } else if (event.state().equals(IdleState.WRITER_IDLE)) {
//                //未进行写操作
//                Log.d(NettyTcpClient.TAG, "WRITE_IDLE");
//                ctx.close();
//
//            } else if (event.state().equals(IdleState.ALL_IDLE)) {
//                //未进行读写操作
//                Log.d(NettyTcpClient.TAG, "ALL_IDLE");
//                ctx.close();
//            }
//
//        }
//    }
//
//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
//        Log.d(NettyTcpClient.TAG, "channelRead0接收到了数据 ");
//    }
//
//
//}
