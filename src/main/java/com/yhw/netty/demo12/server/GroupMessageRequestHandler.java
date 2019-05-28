package com.yhw.netty.demo12.server;

import com.yhw.netty.protocol.request.GroupMessageRequestPacket;
import com.yhw.netty.protocol.response.GroupMessageResponsePacket;
import com.yhw.netty.util.SessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

@ChannelHandler.Sharable
public class GroupMessageRequestHandler extends SimpleChannelInboundHandler<GroupMessageRequestPacket> {

    public static final GroupMessageRequestHandler INSTANCE = new GroupMessageRequestHandler();

    private GroupMessageRequestHandler(){}

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageRequestPacket request) throws Exception {
        //1.拿到groupId 构造群聊消息响应
        String groupId = request.getToGroupId();
        GroupMessageResponsePacket response = new GroupMessageResponsePacket();
        response.setFromGroupId(groupId);
        response.setMessage(request.getMessage());
        //发送用户session
        response.setFromUser(SessionUtil.getSession(ctx.channel()));

        //2.拿到群聊对应的channelGroup,写到每个客户端
        ChannelGroup group = SessionUtil.getChannelGroup(groupId);
        group.writeAndFlush(response);
    }
}
