package com.yhw.netty.protocol.request;

import com.yhw.netty.protocol.Packet;
import com.yhw.netty.protocol.command.Command;
import lombok.Data;

@Data
public class JoinGroupRequestPacket extends Packet {

    private String groupId;


    @Override
    public Byte getCommand() {
        return Command.JOIN_GROUP_REQUEST;
    }
}
