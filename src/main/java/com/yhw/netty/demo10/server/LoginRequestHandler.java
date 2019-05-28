package com.yhw.netty.demo10.server;

import com.yhw.netty.protocol.request.LoginRequestPacket;
import com.yhw.netty.protocol.response.LoginResponsePacket;
import com.yhw.netty.session.Session;
import com.yhw.netty.util.IDUtil;
import com.yhw.netty.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket loginRequestPacket) throws Exception {
        LoginResponsePacket responsePacket = new LoginResponsePacket();
        responsePacket.setVersion(loginRequestPacket.getVersion());
        responsePacket.setUserName(loginRequestPacket.getUserName());

        if(valid(loginRequestPacket)){
            String userId = IDUtil.randomId();
            responsePacket.setUserId(userId);
            responsePacket.setSuccess(true);
            System.out.println("["+loginRequestPacket.getUserName()+"]登录成功");
            SessionUtil.bindSession(new Session(userId,loginRequestPacket.getUserName()),ctx.channel());
        }else {
            responsePacket.setReason("账号密码校验失败！");
            responsePacket.setSuccess(false);
            System.out.println(new Date() + ":登录失败！");
        }

        ctx.channel().writeAndFlush(responsePacket);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        super.channelInactive(ctx);
        SessionUtil.unBindSession(ctx.channel());
    }

    private boolean valid(LoginRequestPacket loginRequestPacket){
        if(!"sunday".equals(loginRequestPacket.getPassword())){
            return false;
        }
        return true;
    }
}
