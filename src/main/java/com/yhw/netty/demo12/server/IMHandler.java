package com.yhw.netty.demo12.server;

import com.yhw.netty.protocol.Packet;
import com.yhw.netty.protocol.command.Command;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.HashMap;
import java.util.Map;

@ChannelHandler.Sharable
public class IMHandler extends SimpleChannelInboundHandler<Packet> {

    public static final IMHandler INSTANCE = new IMHandler();

    private Map<Byte,SimpleChannelInboundHandler<? extends Packet>> handlerMap;

    private IMHandler(){
        handlerMap = new HashMap<>();

        handlerMap.put(Command.MESSAGE_REQUEST, MessageRequestHandler.INSTANCE);
        handlerMap.put(Command.CREATE_GROUP_REQUEST, CreatGroupRequestHandler.INSTANCE);
        handlerMap.put(Command.JOIN_GROUP_REQUEST, JoinGroupRequestHandler.INSTANCE);
        handlerMap.put(Command.QUIT_GROUP_REQUEST, QuitGroupRequestHandler.INSTANCE);
        handlerMap.put(Command.LIST_GROUP_MEMBERS_REQUEST, ListGroupMembersRequestHandler.INSTANCE);
        handlerMap.put(Command.GROUP_MESSAGE_REQUEST, GroupMessageRequestHandler.INSTANCE);
        handlerMap.put(Command.LOGOUT_REQUEST, LogoutRequestHandler.INSTANCE);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet packet) throws Exception {
        //使用工厂模式动态调用
        handlerMap.get(packet.getCommand()).channelRead(ctx,packet);
    }
}
