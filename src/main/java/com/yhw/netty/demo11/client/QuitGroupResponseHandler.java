package com.yhw.netty.demo11.client;

import com.yhw.netty.protocol.response.QuitGroupResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class QuitGroupResponseHandler extends SimpleChannelInboundHandler<QuitGroupResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, QuitGroupResponsePacket response) throws Exception {
        if (response.isSuccess()) {
            System.out.println("退出群聊[" + response.getGroupId() + "]成功！");
        } else {
            System.out.println("退出群聊[" + response.getGroupId() + "]失败！");
        }
    }
}
