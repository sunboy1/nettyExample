package com.yhw.netty.clientconsole;

import com.yhw.netty.protocol.request.GroupMessageRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

public class SendToGroupConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.println("发送消息给某个群组[groupId message]：");

        String groupId = scanner.next();
        String message = scanner.next();

        channel.writeAndFlush(new GroupMessageRequestPacket(groupId,message));
    }
}
