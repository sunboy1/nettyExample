package com.yhw.netty.demo11.server;

import com.yhw.netty.protocol.request.MessageRequestPacket;
import com.yhw.netty.protocol.response.MessageResponsePacket;
import com.yhw.netty.session.Session;
import com.yhw.netty.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket request) throws Exception {
        //1.拿到消息发送方的 会话信息
        Session session = SessionUtil.getSession(ctx.channel());

        //2.通过消息发送方的会话信息  构造要发送的信息
        MessageResponsePacket response = new MessageResponsePacket();
        response.setFromUserId(session.getUserId());
        response.setFromUserName(session.getUserName());
        response.setMessage(request.getMessage());


        //3.拿到接收消息的channel
        String toUserId = request.getToUserId();
        Channel toUserChannel = SessionUtil.getChannel(toUserId);

        //4.将消息发送给消息接收方
        if(toUserChannel != null && SessionUtil.hasLogin(toUserChannel)){
            toUserChannel.writeAndFlush(response);
        }else{
            System.err.println("[" + request.getToUserId() + "]不在线，发送失败");
        }
    }
}
