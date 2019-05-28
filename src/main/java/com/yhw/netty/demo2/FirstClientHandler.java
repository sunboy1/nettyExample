package com.yhw.netty.demo2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.util.Date;

public class FirstClientHandler extends ChannelInboundHandlerAdapter {

    //此方法会在客户端建立成功之后被调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        super.channelActive(ctx);
        System.out.println(new Date() + ":客户端写出数据");

        //1.获取数据
        ByteBuf byteBuf = getByteBuf(ctx,"你好,sunday!!!!!!!");

        //2.写数据
        ctx.channel().writeAndFlush(byteBuf);
    }

    //服务端发来的数据之后 被回调
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        super.channelRead(ctx, msg);
        //============接收数据=================
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println(new Date() + ":接收服务端数据 -> "+byteBuf.toString(Charset.forName("utf-8")));


        //客户端回写数据
        /*System.out.println("客户端在此写出数据 -> ");
        byteBuf = getByteBuf(ctx,"收到，谢谢!");
        ctx.channel().writeAndFlush(byteBuf);*/
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx,String msg){
        //1.获取二级制抽象 byteBuf
        ByteBuf byteBuf = ctx.alloc().buffer();

        //2.准备数据，指定字符串的字符集为UTF-8
        byte[] bytes = msg.getBytes(Charset.forName("utf-8"));

        //3.填充数据到 ByteBuf
        byteBuf.writeBytes(bytes);

        return byteBuf;
    }
}
