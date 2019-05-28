package com.yhw.netty.demo7;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * channelHandler的生命周期 执行顺序 如下面重写方法
 */
public class LifeCyCleTestHandler extends ChannelInboundHandlerAdapter {

    //接收到消息 ----------------执行顺序如下
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("逻辑处理器被添加");
        super.handlerAdded(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel 绑定线程(NioEventLoop): channelRegistered()");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //可以使用此方法做黑名单过滤
        System.out.println("channel 准备就绪：channelActive()");
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channel 有数据可读：channelRead()");
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel 某次数据读完：channelReadComplete()");
        super.channelReadComplete(ctx);
    }

    //===================连接关闭 按序执行下面方法===============

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel 被关闭：channelInactive()");
        super.channelInactive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel 取消线程(NioEventLoop) 的绑定: channelUnregistered()");
        super.channelUnregistered(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("逻辑处理器被移除：handlerRemoved()");
        super.handlerRemoved(ctx);
    }
}
