package com.yhw.netty.demo12.client;

import com.yhw.netty.protocol.response.LoginResponsePacket;
import com.yhw.netty.session.Session;
import com.yhw.netty.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket response) throws Exception {
        String userId = response.getUserId();
        String userName = response.getUserName();

        if(response.isSuccess()){
            System.out.println("["+userName+"]登录成功，userID为："+userId);
            SessionUtil.bindSession(new Session(userId,userName),ctx.channel());
        }else{
            System.out.println("["+userName+"]登录失败，原因："+response.getReason());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端连接关闭！");
    }
}
