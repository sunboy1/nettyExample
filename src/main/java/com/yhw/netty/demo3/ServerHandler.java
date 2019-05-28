package com.yhw.netty.demo3;

import com.yhw.netty.protocol.Packet;
import com.yhw.netty.protocol.PacketCodeC;
import com.yhw.netty.protocol.request.LoginRequestPacket;
import com.yhw.netty.protocol.response.LoginResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("服务端登录校验开始>>>>>>>>>>>>>>>>>");
//        super.channelRead(ctx, msg);
        ByteBuf byteBuf = (ByteBuf) msg;

        Packet packet = PacketCodeC.getInstance().decode(byteBuf);


        //判断是否有登录请求包
        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        if(packet instanceof LoginRequestPacket){
            loginResponsePacket.setVersion(packet.getVersion());
            if(valid(packet)){
                loginResponsePacket.setSuccess(true);
            }else {
                loginResponsePacket.setSuccess(false);
                loginResponsePacket.setReason("账号密码校验失败");
            }
        }else{
            loginResponsePacket.setSuccess(false);
            loginResponsePacket.setReason("未传递登录包");
        }
        byteBuf = PacketCodeC.getInstance().encode(loginResponsePacket);

        //回写登录结果
        ctx.channel().writeAndFlush(byteBuf);

    }

    public boolean valid(Packet packet){
        LoginRequestPacket loginRequestPacket = (LoginRequestPacket)packet;

        if("123456".equals(loginRequestPacket.getPassword()) && "Thursday".equals(loginRequestPacket.getUserName())){
            return true;
        }
        return false;
    }
}
