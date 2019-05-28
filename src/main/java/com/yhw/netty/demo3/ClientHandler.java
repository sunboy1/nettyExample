package com.yhw.netty.demo3;

import com.yhw.netty.protocol.Packet;
import com.yhw.netty.protocol.PacketCodeC;
import com.yhw.netty.protocol.request.LoginRequestPacket;
import com.yhw.netty.protocol.response.LoginResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.UUID;

/**
 * 客户端登录请求handler
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    //建立连接事件
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        super.channelActive(ctx);

        System.out.println("客户端登录开始>>>>>>>>>>>>>>>>>>>");

        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setPassword("123456");
        loginRequestPacket.setUserId(UUID.randomUUID().toString());
        loginRequestPacket.setUserName("Thursday");

        //编码
        ByteBuf byteBuf = PacketCodeC.getInstance().encode(loginRequestPacket);
        //写数据
        ctx.channel().writeAndFlush(byteBuf);

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        super.channelRead(ctx, msg);
        ByteBuf byteBuf = (ByteBuf) msg;

        Packet packet = PacketCodeC.getInstance().decode(byteBuf);
        if(packet instanceof LoginResponsePacket){
           LoginResponsePacket responsePacket = (LoginResponsePacket)packet;
           if(responsePacket.isSuccess()){
               System.out.println("登录成功！");
           }else{
               System.out.println("登录失败>>>>>>>>>>>>");
               System.out.println("失败原因:"+responsePacket.getReason());
           }
        }
    }
}
