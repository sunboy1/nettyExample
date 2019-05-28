package com.yhw.netty.protocol.response;

import com.yhw.netty.protocol.Packet;
import com.yhw.netty.protocol.command.Command;
import com.yhw.netty.session.Session;
import lombok.Data;

import java.util.List;

@Data
public class ListGroupMembersResponsePacket extends Packet {

    private String groupId;

    private List<Session> sessionList;

    @Override
    public Byte getCommand() {
        return Command.LIST_GROUP_MEMBERS_RESPONSE;
    }
}
