package com.yhw.netty.demo4;

import com.yhw.netty.protocol.Packet;
import com.yhw.netty.protocol.PacketCodeC;
import com.yhw.netty.protocol.request.LoginRequestPacket;
import com.yhw.netty.protocol.request.MessageRequestPacket;
import com.yhw.netty.protocol.response.LoginResponsePacket;
import com.yhw.netty.protocol.response.MessageResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

public class ServerHandlerFour extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        super.channelRead(ctx, msg);

        ByteBuf byteBuf = (ByteBuf) msg;

        Packet packet = PacketCodeC.getInstance().decode(byteBuf);

        if(packet instanceof LoginRequestPacket){
            LoginRequestPacket loginRequestPacket = (LoginRequestPacket)packet;
            LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
            loginResponsePacket.setVersion(packet.getVersion());

            if(vaild(loginRequestPacket)){
                loginResponsePacket.setSuccess(true);
                System.out.println(new Date() + ":登陆成功！");
            }else{
                loginResponsePacket.setSuccess(false);
                loginResponsePacket.setReason("账号密码错误！");
                System.out.println(new Date() + ":登陆失败！");
            }
            byteBuf = PacketCodeC.getInstance().encode(ctx.alloc(),loginResponsePacket);
            ctx.channel().writeAndFlush(byteBuf);

        }else if(packet instanceof MessageRequestPacket){
            //客户端发来消息
            MessageRequestPacket messageRequestPacket = (MessageRequestPacket)packet;
            System.out.println(new Date() + ":收到客户端消息" + messageRequestPacket.getMessage());

            MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
            messageResponsePacket.setMessage("服务端回复【"+messageRequestPacket.getMessage()+"】");

            byteBuf = PacketCodeC.getInstance().encode(ctx.alloc(),messageResponsePacket);
            ctx.channel().writeAndFlush(byteBuf);
        }else{
            System.out.println(111111);
        }
    }

    private boolean vaild(LoginRequestPacket loginRequestPacket){
        if("sunday".equals(loginRequestPacket.getPassword()) && "sunday".equals(loginRequestPacket.getUserName())){
            return true;
        }
        return false;
    }
}
