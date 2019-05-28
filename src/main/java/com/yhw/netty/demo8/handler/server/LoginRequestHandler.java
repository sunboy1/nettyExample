package com.yhw.netty.demo8.handler.server;

import com.yhw.netty.protocol.request.LoginRequestPacket;
import com.yhw.netty.protocol.response.LoginResponsePacket;
import com.yhw.netty.util.LoginUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket loginRequestPacket) throws Exception {
        System.out.println(new Date() + "收到客户端登录请求.....");

        LoginResponsePacket responsePacket = new LoginResponsePacket();
        loginRequestPacket.setVersion(loginRequestPacket.getVersion());
        if(valid(loginRequestPacket)){
            responsePacket.setSuccess(true);

            System.out.println(new Date() + "登录成功！");
            LoginUtil.markAsLogin(ctx.channel());
        }else{
            responsePacket.setReason("账号密码校验失败！");
            responsePacket.setSuccess(false);
            System.out.println(new Date() + "：登录失败");
        }

        //登录响应
        ctx.channel().writeAndFlush(responsePacket);
    }

    private boolean valid(LoginRequestPacket loginRequestPacket){
        if("sunday".equals(loginRequestPacket.getUserName()) && "sunday".equals(loginRequestPacket.getPassword())){
            return true;
        }
        return false;
    }
}
