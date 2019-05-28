package com.yhw.netty.clientconsole;

import com.yhw.netty.protocol.request.CreateGroupRequestPacket;
import io.netty.channel.Channel;

import java.util.Arrays;
import java.util.Scanner;

public class CreateGroupConsoleCommand implements ConsoleCommand {
    private static final String USER_ID_SPLIER = ",";

    @Override
    public void exec(Scanner scanner, Channel channel) {
        CreateGroupRequestPacket createGroupRequestPacket = new CreateGroupRequestPacket();

        System.out.println("【拉人群聊】输入userID 之间英文逗号隔开：");
        String userIds = scanner.next();

        createGroupRequestPacket.setUserIdList(Arrays.asList(userIds.split(USER_ID_SPLIER)));

        channel.writeAndFlush(createGroupRequestPacket);
    }
}
