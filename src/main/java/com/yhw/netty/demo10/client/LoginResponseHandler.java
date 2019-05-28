package com.yhw.netty.demo10.client;

import com.yhw.netty.protocol.response.LoginResponsePacket;
import com.yhw.netty.session.Session;
import com.yhw.netty.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket loginResponsePacket) throws Exception {
        String userId = loginResponsePacket.getUserId();
        String userName = loginResponsePacket.getUserName();

        if(loginResponsePacket.isSuccess()){
            System.out.println("[" + userName + "]登录成功，userId为："+userId);

            SessionUtil.bindSession(new Session(userId,userName),ctx.channel());
        }else {
            System.out.println("[" + userName + "]登录失败，原因："+loginResponsePacket.getReason());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        super.channelInactive(ctx);
        System.out.println("客户端被关闭！");
    }
}
