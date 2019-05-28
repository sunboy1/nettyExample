package com.yhw.netty.clientconsole;

import com.yhw.netty.protocol.request.LoginRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

public class LoginConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.println("输入用户名登录：");
        String userName = scanner.next();

        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserName(userName);
        loginRequestPacket.setPassword("sunday");

        //发送登录数据包
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
