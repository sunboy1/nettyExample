package com.yhw.netty.clientconsole;

import com.yhw.netty.protocol.request.MessageRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

public class SendToUserConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        MessageRequestPacket messageRequestPacket = new MessageRequestPacket();

        System.out.println("发送消息给某个用户[toUserId message]:");
        String toUserId = scanner.next();
        String message = scanner.next();

        messageRequestPacket.setToUserId(toUserId);
        messageRequestPacket.setMessage(message);
        channel.writeAndFlush(messageRequestPacket);
    }
}
