package com.yhw.netty.protocol.response;

import com.yhw.netty.protocol.Packet;
import com.yhw.netty.protocol.command.Command;

public class HeartBeatResponsePacket extends Packet {
    @Override
    public Byte getCommand() {
        return Command.HEARTBEAT_RESPONSE;
    }
}
