package com.yhw.netty.codec;

import com.yhw.netty.protocol.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 解码
 */
public class PacketDecoder extends ByteToMessageDecoder {


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        out.add(PacketCodeC.getInstance().decode(in));
    }
}
