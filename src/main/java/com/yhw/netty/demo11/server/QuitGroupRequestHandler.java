package com.yhw.netty.demo11.server;

import com.yhw.netty.protocol.request.QuitGroupRequestPacket;
import com.yhw.netty.protocol.response.QuitGroupResponsePacket;
import com.yhw.netty.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

public class QuitGroupRequestHandler extends SimpleChannelInboundHandler<QuitGroupRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, QuitGroupRequestPacket request) throws Exception {
        //1.获取群对应的channelGroup ,然后将当前用户的channel移除
        String groupId = request.getGroupId();
        ChannelGroup group = SessionUtil.getChannelGroup(groupId);
        group.remove(ctx.channel());

        //2.构造退群响应发送给客户端
        QuitGroupResponsePacket response = new QuitGroupResponsePacket();

        response.setGroupId(request.getGroupId());
        response.setSuccess(true);
        ctx.channel().writeAndFlush(response);
    }
}
