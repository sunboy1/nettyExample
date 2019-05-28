package com.yhw.netty.demo10.client;

import com.yhw.netty.protocol.response.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageResponsePacket messageResponsePacket) throws Exception {
        String fromUserId = messageResponsePacket.getFromUserId();
        String fromUserName = messageResponsePacket.getFromUserName();
        System.out.println(fromUserId + ":" + fromUserName + " -> " + messageResponsePacket
                .getMessage());
    }
}
