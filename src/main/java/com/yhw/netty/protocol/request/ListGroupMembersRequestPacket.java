package com.yhw.netty.protocol.request;

import com.yhw.netty.protocol.Packet;
import com.yhw.netty.protocol.command.Command;
import lombok.Data;

@Data
public class ListGroupMembersRequestPacket extends Packet {

    private String groupId;

    @Override
    public Byte getCommand() {
        return Command.LIST_GROUP_MEMBERS_REQUEST;
    }
}
