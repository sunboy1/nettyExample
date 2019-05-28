package com.yhw.netty.demo12.client;

import com.yhw.netty.protocol.response.LogoutResponsePacket;
import com.yhw.netty.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LogoutResponseHandler extends SimpleChannelInboundHandler<LogoutResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogoutResponsePacket response) throws Exception {
        SessionUtil.unBindSession(ctx.channel());
    }
}
