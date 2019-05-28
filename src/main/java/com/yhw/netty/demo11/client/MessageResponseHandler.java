package com.yhw.netty.demo11.client;

import com.yhw.netty.protocol.response.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponsePacket response) throws Exception {
        String fromUserId = response.getFromUserId();
        String fromUserName = response.getFromUserName();
        System.out.println(fromUserId + ":" + fromUserName + " -> " + response
                .getMessage());
    }
}
