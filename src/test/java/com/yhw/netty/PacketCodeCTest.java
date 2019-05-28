package com.yhw.netty;

import com.yhw.netty.protocol.Packet;
import com.yhw.netty.protocol.PacketCodeC;
import com.yhw.netty.protocol.request.LoginRequestPacket;
import com.yhw.netty.serializer.Serializer;
import com.yhw.netty.serializer.impl.JSONSerializer;
import io.netty.buffer.ByteBuf;
import org.junit.Assert;
import org.junit.Test;

public class PacketCodeCTest {

    @Test
    public void encode() {

        Serializer serializer = new JSONSerializer();
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();

        loginRequestPacket.setVersion(((byte) 1));
        loginRequestPacket.setUserId("123");
        loginRequestPacket.setUserName("zhangsan");
        loginRequestPacket.setPassword("password");

        PacketCodeC packetCodeC = new PacketCodeC();
        //编码
        ByteBuf byteBuf = packetCodeC.encode(loginRequestPacket);
        //解码
        Packet decodedPacket = packetCodeC.decode(byteBuf);
//        LoginRequestPacket loginRequestPacket1 = (LoginRequestPacket)decodedPacket;
//        System.out.println(loginRequestPacket1.getPassword());

        Assert.assertArrayEquals(serializer.serialize(loginRequestPacket), serializer.serialize(decodedPacket));

    }
}
