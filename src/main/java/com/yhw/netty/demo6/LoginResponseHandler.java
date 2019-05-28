package com.yhw.netty.demo6;

import com.yhw.netty.protocol.request.LoginRequestPacket;
import com.yhw.netty.protocol.response.LoginResponsePacket;
import com.yhw.netty.util.LoginUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;
import java.util.UUID;

/**
 * 客户端解析： 服务端响应
 */
public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        super.channelActive(ctx);
        //创建登录请求
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setPassword("sunday");
        loginRequestPacket.setUserName("sunday");
        loginRequestPacket.setUserId(UUID.randomUUID().toString());



        //写数据
        ctx.channel().writeAndFlush(loginRequestPacket);
    }



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket loginResponsePacket) throws Exception {
        if(loginResponsePacket.isSuccess()){
            System.out.println(new Date() + ":客户端登录成功！");
            LoginUtil.markAsLogin(ctx.channel());
        }else{
            System.out.println(new Date() + ":客户端登录失败，原因：" + loginResponsePacket.getCommand());
        }
    }
}
