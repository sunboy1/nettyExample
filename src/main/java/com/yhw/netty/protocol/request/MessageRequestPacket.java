package com.yhw.netty.protocol.request;

import com.yhw.netty.protocol.Packet;
import com.yhw.netty.protocol.command.Command;
import lombok.Data;

@Data
public class MessageRequestPacket extends Packet {

    private String message;

    /**
     * 消息发送者 用户ID
     */
    private String toUserId;

    public MessageRequestPacket(){

    }

    public MessageRequestPacket(String toUserId,String message){
            this.toUserId = toUserId;
            this.message = message;
    }

    public MessageRequestPacket(String message){
        this.message = message;
    }

    @Override
    public Byte getCommand() {
        return Command.MESSAGE_REQUEST;
    }
}
