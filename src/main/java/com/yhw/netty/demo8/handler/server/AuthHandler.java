package com.yhw.netty.demo8.handler.server;

import com.yhw.netty.util.LoginUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 身份认证 Handler
 */
public class AuthHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(!LoginUtil.hasLogin(ctx.channel())){
            ctx.channel().close();
        }else{
            //如果认证通过移除当前Handler
            ctx.pipeline().remove(this);
            super.channelRead(ctx, msg);
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        if(LoginUtil.hasLogin(ctx.channel())){
            System.out.println("当前连接登录验证完毕，无需在此认证，AuthHandler被移除");
        }else{
            System.out.println("无登录验证，强制关闭连接！");
        }
    }
}
