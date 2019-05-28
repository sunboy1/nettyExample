package com.yhw.netty.protocol.response;

import com.yhw.netty.protocol.Packet;
import com.yhw.netty.protocol.command.Command;
import lombok.Data;

import java.util.List;

@Data
public class CreateGroupResponsePacket extends Packet {

    private boolean success;

    private String groupId;

    private List<String> userNameList;

    @Override
    public Byte getCommand() {
        return Command.CREATE_GROUP_RESPONSE;
    }
}
