package com.yhw.netty.codec;

import com.yhw.netty.protocol.Packet;
import com.yhw.netty.protocol.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 编码
 * 将对象转换到二进制数据
 * PacketEncoder 继承自 MessageToByteEncoder
 * 泛型参数 Packet 表示这个类的作用是实现 Packet 类型对象到二进制的转换
 */
public class PacketEncoder extends MessageToByteEncoder<Packet> {

    /**
     * 我们注意到，在这个方法里面，第二个参数是 Java 对象，而第三个参数是 ByteBuf 对象，
     * 我们在这个方法里面要做的事情就是把 Java 对象里面的字段写到 ByteBuf，我们不再需要自行去分配 ByteBuf
     * @param channelHandlerContext
     * @param packet
     * @param out
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf out) throws Exception {
        PacketCodeC.getInstance().encode(out,packet);
    }
}
