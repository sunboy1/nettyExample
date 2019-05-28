package com.yhw.netty.demo4;

import com.yhw.netty.protocol.Packet;
import com.yhw.netty.protocol.PacketCodeC;
import com.yhw.netty.protocol.request.LoginRequestPacket;
import com.yhw.netty.protocol.response.LoginResponsePacket;
import com.yhw.netty.protocol.response.MessageResponsePacket;
import com.yhw.netty.util.LoginUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;
import java.util.UUID;

public class ClientHandlerFour extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        super.channelActive(ctx);

        System.out.println(new Date() + ":客户端登录开始");

        //创建登录对象
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserId(UUID.randomUUID().toString());
        loginRequestPacket.setUserName("sunday");
        loginRequestPacket.setPassword("sunday");

        //编码
        ByteBuf byteBuf = PacketCodeC.getInstance().encode(ctx.alloc(),loginRequestPacket);

        //写数据
        ctx.channel().writeAndFlush(byteBuf);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        super.channelRead(ctx, msg);
        ByteBuf byteBuf = (ByteBuf)msg;

        Packet packet = PacketCodeC.getInstance().decode(byteBuf);

        if(packet instanceof LoginResponsePacket){
            LoginResponsePacket loginResponsePacket = (LoginResponsePacket)packet;

            if(loginResponsePacket.isSuccess()){
                System.out.println(new Date() + ":客户端登录成功！");
                LoginUtil.markAsLogin(ctx.channel());
            }else{
                System.out.println(new Date() + ":客户端登录失败，原因：" + loginResponsePacket.getReason());
            }

        }else if(packet instanceof MessageResponsePacket){
            MessageResponsePacket messagePesponsePacket = (MessageResponsePacket)packet;

            System.out.println(new Date() + "：收到服务端消息：" + messagePesponsePacket.getMessage());
        }
    }
}
