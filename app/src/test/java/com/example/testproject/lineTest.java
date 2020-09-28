package com.example.testproject;

import org.junit.Test;

public class lineTest {

    /**
     *  需要看  https://blog.csdn.net/wangkang19890121/article/details/106086141/
     */
    /**
     * 处理器，这个处理器数据处理并传递相关参数至下一个调用者
     */
    public abstract class AbstracHandler {
        abstract void doHandle(HandlerChainContext handlerChainContext, Object arg0);
    }

    /**
     * 处理链路的上下文节点，在此节点上持有下一个节点以及当前节点的进行处理的handler
     */
    class HandlerChainContext {
        HandlerChainContext next;  //下一个链路节点
        AbstracHandler handler;  //当前处理handler

        public HandlerChainContext(AbstracHandler handler) {
            this.handler = handler;
        }

        void hand(Object arg0) {
            this.handler.doHandle(this, arg0);
        }

        void findNextContext(Object arg0) {
            if (this.next != null) {
                this.next.hand(arg0);
            }
        }
    }

    class Handler1 extends AbstracHandler {

        @Override
        void doHandle(HandlerChainContext handlerChainContext, Object arg0) {
            handlerChainContext.findNextContext("我是流水线工人一号");
            System.out.println(arg0);
        }
    }
    class Handler2 extends AbstracHandler {
        @Override
        void doHandle(HandlerChainContext handlerChainContext, Object arg0) {
            handlerChainContext.findNextContext("我是流水线工人二号");
            System.out.println(arg0);
        }
    }
    class Handler3 extends AbstracHandler {
        @Override
        void doHandle(HandlerChainContext handlerChainContext, Object arg0) {

            System.out.println(arg0+"开始工作啦");
        }
    }

    //链表形式调用
    class SimulateNettyPipelineTest {
        public HandlerChainContext handlerChainContext = null;

        //这个handlerChainContext就相当于一个head
        public SimulateNettyPipelineTest() {
            this.handlerChainContext = new HandlerChainContext(new AbstracHandler() {
                @Override
                void doHandle(HandlerChainContext handlerChainContext, Object arg0) {
                    handlerChainContext.findNextContext(arg0);
                }
            });
        }

        public void requestProcess(Object agr0) {
            this.handlerChainContext.findNextContext(agr0);
        }

        //将handler加入链路的最后，也就是队尾
        public void addLast(AbstracHandler handler) {
            HandlerChainContext context = handlerChainContext;
            while (context.next != null)   //判断有没有下一个链路点（也就是有没有下一个对象）
            {
                context = context.next;   //用下一个链路点覆盖上一个链路点
            }
            context.next = new HandlerChainContext(handler);  //添加一个进入队列   就相当于HandlerChainContext next=new HandlerChainContext(handler)

        }
    }

    //开始调用
    @Test
    public void test() {
        SimulateNettyPipelineTest test = new SimulateNettyPipelineTest();
        test.addLast(new Handler1());
        test.addLast(new Handler2());
        test.addLast(new Handler3());
        //发起请求
        test.requestProcess("工作结束啦");
    }

    @Test
    public void TwoTest() {
        Handler handler = new Handler();
        Handler handler1 = handler;

    }

    class Handler {


    }





}
