package com.yhw.netty.demo12.server;

import com.yhw.netty.protocol.request.LogoutRequestPacket;
import com.yhw.netty.protocol.response.LogoutResponsePacket;
import com.yhw.netty.util.SessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
@ChannelHandler.Sharable
public class LogoutRequestHandler extends SimpleChannelInboundHandler<LogoutRequestPacket> {

    public static final LogoutRequestHandler INSTANCE = new LogoutRequestHandler();

    private LogoutRequestHandler(){}

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogoutRequestPacket logoutRequestPacket) throws Exception {
        SessionUtil.unBindSession(ctx.channel());

        LogoutResponsePacket response = new LogoutResponsePacket();
        response.setSuccess(true);
        ctx.channel().writeAndFlush(response);
    }
}
