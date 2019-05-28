package com.yhw.netty.demo10.server;

import com.yhw.netty.protocol.request.LogoutRequestPacket;
import com.yhw.netty.protocol.response.LogoutResponsePacket;
import com.yhw.netty.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 登出请求
 */
public class LogoutRequestHandler  extends SimpleChannelInboundHandler<LogoutRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogoutRequestPacket logoutRequestPacket) throws Exception {
        SessionUtil.unBindSession(ctx.channel());

        LogoutResponsePacket responsePacket = new LogoutResponsePacket();
        responsePacket.setSuccess(true);
        ctx.channel().writeAndFlush(responsePacket);
    }
}
