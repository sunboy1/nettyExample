package com.yhw.netty.protocol.request;

import com.yhw.netty.protocol.Packet;
import com.yhw.netty.protocol.command.Command;
import lombok.Data;

/**
 * 登录请求包
 */
@Data
public class LoginRequestPacket extends Packet {

    private String userId;

    private String userName;

    private String password;

    @Override
    public Byte getCommand() {
        return Command.LOGIN_REQUEST;
    }
}
