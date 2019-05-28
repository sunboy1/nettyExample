package com.yhw.netty.clientconsole;

import com.yhw.netty.util.SessionUtil;
import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConsoleCommandManager1 implements ConsoleCommand {

    private Map<String,ConsoleCommand>  consoleCommandMap;

    public ConsoleCommandManager1(){
        consoleCommandMap = new HashMap<>();

        consoleCommandMap.put("sendToUser",new SendToUserConsoleCommand());
        consoleCommandMap.put("logout",new LogoutConsoleCommand());
        consoleCommandMap.put("createGroup",new CreateGroupConsoleCommand());
        consoleCommandMap.put("joinGroup",new JoinGroupConsoleCommand());
        consoleCommandMap.put("quitGroup",new QuitGroupConsoleCommand());
        consoleCommandMap.put("listGroupMembers",new ListGroupMembersConsoleCommand());
        consoleCommandMap.put("sendToGroup",new SendToGroupConsoleCommand());
    }

    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.println("请输入指令[sendToUser,logout,createGroup,joinGroup,quitGroup,listGroupMembers,sendToGroup]:");
        //获取第一个指令
        String command = scanner.next();

        if(!SessionUtil.hasLogin(channel)){
            return;
        }

        ConsoleCommand consoleCommand = consoleCommandMap.get(command);
        if(consoleCommand != null){
            consoleCommand.exec(scanner, channel);
        }else {
            System.out.println("无法识别[" + command + "]指令，请重新输入");
        }
    }
}
