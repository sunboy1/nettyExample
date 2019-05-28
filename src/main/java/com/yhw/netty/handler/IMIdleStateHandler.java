package com.yhw.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;
//空闲检测
//通常空闲检测时间要比发送心跳的时间的两倍要长一些，这也是为了排除偶发的公网抖动，防止误判
public class IMIdleStateHandler extends IdleStateHandler {

    private static final int READER_IDLE_TIME = 15;


    public IMIdleStateHandler(){
        //其中第一个表示读空闲时间，指的是在这段时间内如果没有数据读到，就表示连接假死；
        // 第二个是写空闲时间，指的是 在这段时间如果没有写数据，就表示连接假死；
        // 第三个参数是读写空闲时间，表示在这段时间内如果没有产生数据读或者写，就表示连接假死。
        // 写空闲和读写空闲为0，表示我们不关心者两类条件；最后一个参数表示时间单位
        super(READER_IDLE_TIME,0,0, TimeUnit.SECONDS);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        System.out.println(READER_IDLE_TIME + "秒内未读到数据，关闭连接");
        ctx.channel().close();
    }
}
