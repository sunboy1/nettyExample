package com.yhw.netty.demo11.server;

import com.yhw.netty.protocol.request.ListGroupMembersRequestPacket;
import com.yhw.netty.protocol.response.ListGroupMembersResponsePacket;
import com.yhw.netty.session.Session;
import com.yhw.netty.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

import java.util.ArrayList;
import java.util.List;

public class ListGroupMembersRequestHandler extends SimpleChannelInboundHandler<ListGroupMembersRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ListGroupMembersRequestPacket request) throws Exception {
        //1.获取群的ChannelGroup
        String groupId = request.getGroupId();
        ChannelGroup group = SessionUtil.getChannelGroup(groupId);

        //2.遍历群 成员的channel,对应的session,构造群成员的信息
        List<Session> sessionList = new ArrayList<>();
        Session session = null;
        for(Channel channel : group){
            session = SessionUtil.getSession(channel);
            sessionList.add(session);
        }


        //3.构建获取成员列表响应写回到客户端
        ListGroupMembersResponsePacket response = new ListGroupMembersResponsePacket();

        response.setGroupId(groupId);
        response.setSessionList(sessionList);
        ctx.channel().writeAndFlush(response);
    }
}
