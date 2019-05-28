package com.yhw.netty.demo11.server;

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

public class CreatGroupRequestHandler extends SimpleChannelInboundHandler<CreateGroupRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupRequestPacket createGroupRequestPacket) throws Exception {
        List<String> userIdList = createGroupRequestPacket.getUserIdList();


        List<String> userNameList = new ArrayList<>();
        //1.创建一个channel 分组
        ChannelGroup channelGroup = new DefaultChannelGroup(ctx.executor());

        //2.筛选出待加入群聊用户的 channel和userId
        Channel channel = null;
        for(String userId : userIdList){
            channel = SessionUtil.getChannel(userId);
            if(channel != null){
                channelGroup.add(channel);
                userNameList.add(SessionUtil.getSession(channel).getUserName());
            }
        }

        //3.创建群聊结果的响应
        String groupId = IDUtil.randomId();
        CreateGroupResponsePacket responsePacket = new CreateGroupResponsePacket();
        responsePacket.setGroupId(groupId);
        responsePacket.setUserNameList(userNameList);
        responsePacket.setSuccess(true);


        //4.给每个客户端发送拉群的通知
        channelGroup.writeAndFlush(responsePacket);

        System.out.print("群创建成功,Id为：" + groupId + ",");
        System.out.print("群员有：" + userNameList);

        //5.保存群组相关信息
        SessionUtil.bindChannelGroup(groupId,channelGroup);
    }


}
