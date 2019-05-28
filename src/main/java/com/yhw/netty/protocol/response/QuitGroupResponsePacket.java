package com.yhw.netty.protocol.response;

import com.yhw.netty.protocol.Packet;
import com.yhw.netty.protocol.command.Command;
import lombok.Data;

@Data
public class QuitGroupResponsePacket extends Packet {

    private String groupId;

    private boolean success;

    private String reason;

    @Override
    public Byte getCommand() {
        return Command.QUIT_GROUP_RESPONSE;
    }
}
