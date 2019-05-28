package com.yhw.netty.util;

import com.yhw.netty.attribute.Attributes;
import com.yhw.netty.session.Session;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionUtil {

    private static final Map<String, Channel> userIdChannelMap = new ConcurrentHashMap<>();

    private static final Map<String, ChannelGroup> groupIdChannelGroupMap = new ConcurrentHashMap<>();

    /**
     * 绑定Session
     * @param session
     * @param channel
     */
    public static void bindSession(Session session,Channel channel){
        userIdChannelMap.put(session.getUserId(),channel);
        channel.attr(Attributes.SESSION).set(session);
    }

    /**
     * 解绑
     * @param channel
     */
    public static void unBindSession(Channel channel){
        if(hasLogin(channel)){
            userIdChannelMap.remove(getSession(channel).getUserId());
            channel.attr(Attributes.SESSION).set(null);
        }
    }

    /**
     * 通道是否存在Session
     * @param channel
     * @return
     */
    public static boolean hasLogin(Channel channel){
        return channel.hasAttr(Attributes.SESSION);
    }

    public static Session getSession(Channel channel){
        return channel.attr(Attributes.SESSION).get();
    }

    public static Channel getChannel(String userId){
        return userIdChannelMap.get(userId);
    }

    /**
     * 通过群组ID  绑定通道组
     * @param groupId
     * @param channelGroup
     */
    public static void  bindChannelGroup(String groupId,ChannelGroup channelGroup){
        groupIdChannelGroupMap.put(groupId,channelGroup);
    }

    /**
     * 根据群组ID 获取通道组
     * @param groupId
     * @return
     */
    public static ChannelGroup getChannelGroup(String groupId){
        return groupIdChannelGroupMap.get(groupId);
    }

}
