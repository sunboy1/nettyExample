package com.yhw.netty.codec;

import com.yhw.netty.protocol.Packet;
import com.yhw.netty.protocol.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

/**
 * 编码解码合并操作
 */
@ChannelHandler.Sharable
public class PacketCodeHandler extends MessageToMessageCodec<ByteBuf, Packet> {

    public static final PacketCodeHandler INSTANCE = new PacketCodeHandler();

    private PacketCodeHandler(){}

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> out) throws Exception {
        out.add(PacketCodeC.getInstance().decode(byteBuf));
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, List<Object> out) throws Exception {
        ByteBuf byteBuf = ctx.channel().alloc().ioBuffer();
        PacketCodeC.getInstance().encode(byteBuf,packet);
        out.add(byteBuf);
    }
}
