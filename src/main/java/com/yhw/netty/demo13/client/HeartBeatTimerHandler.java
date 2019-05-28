package com.yhw.netty.demo13.client;

import com.yhw.netty.protocol.request.HeartBeatRequestPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.TimeUnit;

public class HeartBeatTimerHandler extends ChannelInboundHandlerAdapter {
    private static final int HEARTBEAT_INTERVAL = 5;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        scheduleHeartBeat(ctx);

        super.channelActive(ctx);
    }

    private void scheduleHeartBeat(ChannelHandlerContext ctx){
        ctx.executor().schedule(()->{

            if(ctx.channel().isActive()){
                ctx.writeAndFlush(new HeartBeatRequestPacket());
                //递归,实现定时心跳
                scheduleHeartBeat(ctx);
            }

        },HEARTBEAT_INTERVAL, TimeUnit.SECONDS);
    }
}
