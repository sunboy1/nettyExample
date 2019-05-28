package com.yhw.netty.demo10.console;

import com.yhw.netty.protocol.request.MessageRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

public class SendToUserConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.println("发送消息给某个用户[userId,message]：");

        String toUserId = scanner.next();
        String message = scanner.next();
        channel.writeAndFlush(new MessageRequestPacket(toUserId,message));
    }
}
