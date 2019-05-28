package com.yhw.netty.demo6;

import com.yhw.netty.protocol.request.LoginRequestPacket;
import com.yhw.netty.protocol.response.LoginResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

/**
 * 服务端解析：客户端登录请求
 */
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket loginRequestPacket) throws Exception {
        System.out.println(new Date() + "：收到客户端请求");

        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginRequestPacket.setVersion(loginRequestPacket.getVersion());

        if (valid(loginRequestPacket)) {
            loginResponsePacket.setSuccess(true);
            System.out.println(new Date() + ": 登录成功!");
        } else {
            loginResponsePacket.setReason("账号密码校验失败");
            loginResponsePacket.setSuccess(false);
            System.out.println(new Date() + ": 登录失败!");
        }

        ctx.channel().writeAndFlush(loginResponsePacket);
    }

    private boolean valid(LoginRequestPacket loginRequestPacket){
        if("sunday".equals(loginRequestPacket.getPassword()) && "sunday".equals(loginRequestPacket.getUserName())){
            return true;
        }
        return false;
    }
}
