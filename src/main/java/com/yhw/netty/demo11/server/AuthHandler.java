package com.yhw.netty.demo11.server;

import com.yhw.netty.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class AuthHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(SessionUtil.hasLogin(ctx.channel())){
            System.out.println("登录认证成功，移除AuthHandler");
            ctx.pipeline().remove(this);
            super.channelRead(ctx, msg);
        }else {
            System.out.println("登录认证失败，关闭通道");
            ctx.channel().close();
        }

    }
}
