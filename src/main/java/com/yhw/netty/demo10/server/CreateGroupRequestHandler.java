package com.yhw.netty.demo10.server;

import com.yhw.netty.protocol.request.CreateGroupRequestPacket;
import com.yhw.netty.protocol.response.CreateGroupResponsePacket;
import com.yhw.netty.util.IDUtil;
import com.yhw.netty.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;

import java.util.ArrayList;
import java.util.List;

public class CreateGroupRequestHandler extends SimpleChannelInboundHandler<CreateGroupRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupRequestPacket createGroupRequestPacket) throws Exception {
        List<String> userIdList = createGroupRequestPacket.getUserIdList();

        List<String> userNameList = new ArrayList<>();

        //1.创建一个channel 分组
        ChannelGroup channelGroup = new DefaultChannelGroup(ctx.executor());

        //2.筛选出待加入群聊的用户的channel 和 userName
        Channel channel = null;
        for(String userId : userIdList){
            channel = SessionUtil.getChannel(userId);
            if(channel != null){
                channelGroup.add(channel);
                userNameList.add(SessionUtil.getSession(channel).getUserName());
            }
        }

        //3.创建群聊 创建结果响应
        CreateGroupResponsePacket createGroupResponsePacket = new CreateGroupResponsePacket();
        createGroupResponsePacket.setSuccess(true);
        createGroupResponsePacket.setGroupId(IDUtil.randomId());
        createGroupResponsePacket.setUserNameList(userNameList);

        //4.给每个客户端发送拉群通知
        channelGroup.writeAndFlush(createGroupResponsePacket);

        System.out.println("群创建成功，id为["+createGroupResponsePacket.getGroupId()+"],");
        System.out.println("群里面有：" + createGroupResponsePacket.getUserNameList());
    }
}
