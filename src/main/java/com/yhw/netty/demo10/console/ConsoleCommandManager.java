package com.yhw.netty.demo10.console;

import com.yhw.netty.util.SessionUtil;
import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConsoleCommandManager implements ConsoleCommand {

    private Map<String,ConsoleCommand> consoleCommandMap;

    public ConsoleCommandManager(){
        consoleCommandMap = new HashMap<>();
        consoleCommandMap.put("sendToUser",new SendToUserConsoleCommand());
        consoleCommandMap.put("logout",new LogoutConsoleCommand());
        consoleCommandMap.put("createGroup",new CreateGroupConsoleCommand());
    }

    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.println("请输入指令[sendToUser,logout,createGroup]:");
        //获取第一个指令
        String command = scanner.next();

        if(!SessionUtil.hasLogin(channel)){
            return;
        }

        ConsoleCommand consoleCommand = consoleCommandMap.get(command);
        if(consoleCommand != null){
            consoleCommand.exec(scanner,channel);
        }else {
            System.err.println("无法识别["+command+"]指令，请重新输入");
        }
    }
}
