package com.yhw.netty.demo12.server;

import com.yhw.netty.util.SessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 *  显示地告诉 Netty，这个 handler 是支持多个 channel 共享的，否则会报错
 */
@ChannelHandler.Sharable
public class AuthHandler extends ChannelInboundHandlerAdapter {

    public static final AuthHandler INSTANCE = new AuthHandler();

    private AuthHandler(){

    }

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
