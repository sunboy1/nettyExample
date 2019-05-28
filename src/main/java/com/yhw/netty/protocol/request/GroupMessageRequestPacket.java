package com.yhw.netty.protocol.request;

import com.yhw.netty.protocol.Packet;
import com.yhw.netty.protocol.command.Command;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class GroupMessageRequestPacket extends Packet {

    private String toGroupId;

    private String message;

    public GroupMessageRequestPacket(String toGroupId,String message){
        this.toGroupId = toGroupId;
        this.message = message;
    }

    @Override
    public Byte getCommand() {
        return Command.GROUP_MESSAGE_REQUEST;
    }
}
