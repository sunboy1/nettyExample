package com.yhw.netty.demo9.server;

import com.yhw.netty.protocol.request.LoginRequestPacket;
import com.yhw.netty.protocol.response.LoginResponsePacket;
import com.yhw.netty.session.Session;
import com.yhw.netty.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;
import java.util.UUID;

public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket loginRequestPacket) throws Exception {
        LoginResponsePacket responsePacket = new LoginResponsePacket();
        responsePacket.setVersion(loginRequestPacket.getVersion());
        responsePacket.setUserName(loginRequestPacket.getUserName());

        if(valid(loginRequestPacket)){
            responsePacket.setSuccess(true);
            String userId = randomUserId();
            responsePacket.setUserId(userId);
            System.out.println("["+loginRequestPacket.getUserName()+"]登录成功！");
            SessionUtil.bindSession(new Session(userId,loginRequestPacket.getUserName()),ctx.channel());
        }else {

            responsePacket.setReason("账号密码校验失败！");
            responsePacket.setSuccess(false);
            System.out.println(new Date() + "：登录失败！");
        }

        //登录响应
        ctx.channel().writeAndFlush(responsePacket);
    }

    //端口连接
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //解绑Session
        SessionUtil.unBindSession(ctx.channel());
    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {
        if("sunday".equals(loginRequestPacket.getPassword())){
            //"sunday".equals(loginRequestPacket.getUserName()) &&
            return true;
        }
        return false;
    }

    private static String randomUserId() {
        return UUID.randomUUID().toString().split("-")[0];
    }
}
