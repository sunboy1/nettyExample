package com.yhw.netty.protocol.response;

import com.yhw.netty.protocol.Packet;
import com.yhw.netty.protocol.command.Command;
import lombok.Data;

@Data
public class LoginResponsePacket extends Packet {

    /**
     * 响应状态
     */
    private boolean success;

    /**
     * 原因
     */
    private String reason;

    private String userName;

    private String userId;

    @Override
    public Byte getCommand() {
        return Command.LOGIN_RESPONSE;
    }
}
