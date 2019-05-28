package com.yhw.netty.protocol.response;

import com.yhw.netty.protocol.Packet;
import com.yhw.netty.protocol.command.Command;
import com.yhw.netty.session.Session;
import lombok.Data;

@Data
public class GroupMessageResponsePacket extends Packet {

    private String fromGroupId;

    private Session fromUser;

    private String message;

    @Override
    public Byte getCommand() {
        return Command.GROUP_MESSAGE_RESPONSE;
    }
}
