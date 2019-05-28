package com.yhw.netty.codec;

import com.yhw.netty.protocol.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 基于长度域拆包器
 */
public class Spliter extends LengthFieldBasedFrameDecoder {

    //1.固定长度的拆包器 FixedLengthFrameDecoder
    //2.行拆包器 LineBasedFrameDecoder
    //3.分隔符拆包器 DelimiterBasedFrameDecoder
    //4.基于长度域拆包器 LengthFieldBasedFrameDecoder

    private static final int LENGTH_FIELD_OFFSET = 7;
    private static final int LENGTH_FIELD_LENGTH = 4;

    public Spliter(){
        //第一个参数指的是数据包的最大长度，第二个参数指的是长度域的偏移量，第三个参数指的是长度域的长度
        super(Integer.MAX_VALUE,LENGTH_FIELD_OFFSET,LENGTH_FIELD_LENGTH);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        //屏蔽非本协议的客户端(返回当前读指针位置，没有被读过为0,因为设置的我们第一个为int类型的魔数)
        if(in.getInt(in.readerIndex()) != PacketCodeC.MAGIC_NUMBER){
            ctx.channel().close();
            return null;
        }

        return super.decode(ctx, in);
    }
}
