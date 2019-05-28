package com.yhw.netty.demo11.client;

import com.yhw.netty.protocol.response.ListGroupMembersResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ListGroupMembersResponseHandler extends SimpleChannelInboundHandler<ListGroupMembersResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ListGroupMembersResponsePacket response) throws Exception {
        System.out.println("群[" + response.getGroupId() + "]中的人包括：" + response.getSessionList());
    }
}
