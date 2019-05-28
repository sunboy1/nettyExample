package com.yhw.netty.demo10.console;

import io.netty.channel.Channel;

import java.util.Scanner;

public interface ConsoleCommand {

    void exec(Scanner scanner, Channel channel);
}
