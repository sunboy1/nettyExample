package com.yhw.netty.demo9.server;

import com.yhw.netty.protocol.request.MessageRequestPacket;
import com.yhw.netty.protocol.response.MessageResponsePacket;
import com.yhw.netty.session.Session;
import com.yhw.netty.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket messageRequestPacket) throws Exception {
        //1.拿到消息发送方 的回话信息
        Session session = SessionUtil.getSession(ctx.channel());

        //2.通过消息发送方 的会话信息构造要发送的消息
        MessageResponsePacket responsePacket = new MessageResponsePacket();
        responsePacket.setFromUserId(session.getUserId());
        responsePacket.setFromUserName(session.getUserName());
        responsePacket.setMessage(messageRequestPacket.getMessage());

        //3.拿到消息 接收方 的channel
        Channel toUserChannel = SessionUtil.getChannel(messageRequestPacket.getToUserId());

        //4.将消息发送给消息接收方
        if(toUserChannel != null && SessionUtil.hasLogin(toUserChannel)){
            toUserChannel.writeAndFlush(responsePacket);
        }else{
            System.out.println("[" + messageRequestPacket.getToUserId() + "]不在线，发送失败！");
        }
    }
}
