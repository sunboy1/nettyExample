package com.yhw.netty.demo10.console;

import com.yhw.netty.protocol.request.LoginRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

public class LoginConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();

        System.out.println("输入用户名：");
        loginRequestPacket.setUserName(scanner.nextLine());
        loginRequestPacket.setPassword("sunday");

        //发送数据包
        channel.writeAndFlush(loginRequestPacket);
        waitForLoginResponse();
    }

    private static void waitForLoginResponse() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
    }
}
