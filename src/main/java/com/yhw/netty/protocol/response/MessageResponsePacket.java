package com.yhw.netty.protocol.response;

import com.yhw.netty.protocol.Packet;
import com.yhw.netty.protocol.command.Command;
import lombok.Data;

@Data
public class MessageResponsePacket extends Packet {

    private String message;

    private String fromUserId;

    private String fromUserName;

    @Override
    public Byte getCommand() {
        return Command.MESSAGE_RESPONSE;
    }
}
