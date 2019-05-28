package com.yhw.netty.protocol.request;

import com.yhw.netty.protocol.Packet;
import com.yhw.netty.protocol.command.Command;

public class LogoutRequestPacket extends Packet {
    @Override
    public Byte getCommand() {
        return Command.LOGOUT_REQUEST;
    }
}
