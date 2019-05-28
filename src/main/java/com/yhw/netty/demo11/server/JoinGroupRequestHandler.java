package com.yhw.netty.demo11.server;

import com.yhw.netty.protocol.request.JoinGroupRequestPacket;
import com.yhw.netty.protocol.response.JoinGroupResponsePacket;
import com.yhw.netty.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

public class JoinGroupRequestHandler extends SimpleChannelInboundHandler<JoinGroupRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinGroupRequestPacket joinGroupRequestPacket) throws Exception {
        //1.获取群对应的 channelGroup,然后将当前用户的Channel添加进入
        String groupId = joinGroupRequestPacket.getGroupId();
        ChannelGroup group = SessionUtil.getChannelGroup(groupId);
        group.add(ctx.channel());

        //2.构造加群响应发送给客户端
        JoinGroupResponsePacket responsePacket = new JoinGroupResponsePacket();

        responsePacket.setSuccess(true);
        responsePacket.setGroupId(groupId);
        ctx.channel().writeAndFlush(responsePacket);
    }
}
