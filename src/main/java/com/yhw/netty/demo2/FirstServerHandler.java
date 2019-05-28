package com.yhw.netty.demo2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.util.Date;

public class FirstServerHandler extends ChannelInboundHandlerAdapter {

    //此方法在接收到客户端发来的数据之后 被回调
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        super.channelRead(ctx, msg);
        //===========接收数据
        ByteBuf byteBuf = (ByteBuf) msg;

        System.out.println(new Date() + ":服务端读到数据 -> "+byteBuf.toString(Charset.forName("utf-8")));

        //============回复数据
        System.out.println(new Date() + "：服务端写出数据");
        byteBuf = getByteBuf(ctx,"你好,welcome sunday first netty server");
        ctx.channel().writeAndFlush(byteBuf);
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx,String msg){
        //将string转byte数组
        byte[] bytes = msg.getBytes(Charset.forName("utf-8"));

        //获取二级制抽象 byteBuf
        ByteBuf byteBuf = ctx.alloc().buffer();

        //填充数据到 ByteBuf
        byteBuf.writeBytes(bytes);

        return  byteBuf;
    }
}
