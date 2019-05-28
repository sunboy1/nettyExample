package com.yhw.netty.protocol.command;

import io.netty.buffer.ByteBuf;

/**
 * 协议命令
 */
public interface Command {

    /**
     * 登录请求占一字节
     */
    Byte LOGIN_REQUEST = 1;

    Byte LOGIN_RESPONSE = 2;


    //客户端收发消息----------------
    Byte MESSAGE_REQUEST = 3;

    Byte MESSAGE_RESPONSE = 4;

    //登出
    Byte LOGOUT_REQUEST = 5;

    Byte LOGOUT_RESPONSE = 6;


    //创建组
    Byte CREATE_GROUP_REQUEST = 7;

    Byte CREATE_GROUP_RESPONSE = 8;

    //获取群组list
    Byte LIST_GROUP_MEMBERS_REQUEST = 9;

    Byte LIST_GROUP_MEMBERS_RESPONSE = 10;

    //加入群组
    Byte JOIN_GROUP_REQUEST = 11;

    Byte JOIN_GROUP_RESPONSE = 12;

    //退出群组
    Byte QUIT_GROUP_REQUEST = 13;

    Byte QUIT_GROUP_RESPONSE = 14;

    //群组发送消息
    Byte GROUP_MESSAGE_REQUEST = 15;

    Byte GROUP_MESSAGE_RESPONSE = 16;

    //心跳检测
    Byte HEARTBEAT_REQUEST = 17;

    Byte HEARTBEAT_RESPONSE = 18;
}
