package com.yhw.netty.demo13.server;

import com.yhw.netty.protocol.request.HeartBeatRequestPacket;
import com.yhw.netty.protocol.response.HeartBeatResponsePacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class HeartBeatRequestHandler extends SimpleChannelInboundHandler<HeartBeatRequestPacket> {

    public static final HeartBeatRequestHandler INSTANCE = new HeartBeatRequestHandler();

    private HeartBeatRequestHandler(){}

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartBeatRequestPacket heartBeatRequestPacket) throws Exception {
        //发送响应，客户端不需要接收处理
        ctx.writeAndFlush(new HeartBeatResponsePacket());
    }
}
