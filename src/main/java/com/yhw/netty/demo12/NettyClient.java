package com.yhw.netty.demo12;

import com.yhw.netty.clientconsole.ConsoleCommand;
import com.yhw.netty.clientconsole.ConsoleCommandManager1;
import com.yhw.netty.clientconsole.LoginConsoleCommand;
import com.yhw.netty.codec.PacketDecoder;
import com.yhw.netty.codec.PacketEncoder;
import com.yhw.netty.codec.Spliter;
import com.yhw.netty.demo12.client.*;
import com.yhw.netty.util.SessionUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class NettyClient {
    private Bootstrap bootstrap;

    {
        bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,5000)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .option(ChannelOption.TCP_NODELAY,true);
    }

    public NettyClient(){
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new Spliter());
                ch.pipeline().addLast(new PacketDecoder());
                // 登录响应处理器
                ch.pipeline().addLast(new LoginResponseHandler());
                // 收消息处理器
                ch.pipeline().addLast(new MessageResponseHandler());
                // 创建群响应处理器
                ch.pipeline().addLast(new CreateGroupResponseHandler());
                // 加群响应处理器
                ch.pipeline().addLast(new JoinGroupResponseHandler());
                // 退群响应处理器
                ch.pipeline().addLast(new QuitGroupResponseHandler());
                // 获取群成员响应处理器
                ch.pipeline().addLast(new ListGroupMembersResponseHandler());
                // 群消息响应
                ch.pipeline().addLast(new GroupMessageResponseHandler());
                // 登出响应处理器
                ch.pipeline().addLast(new LogoutResponseHandler());
                ch.pipeline().addLast(new PacketEncoder());
            }
        });
        connection(MAX_RETRY);
    }

    private static final int MAX_RETRY = 5;

    private void connection(int retry){
        bootstrap.connect("localhost",8800)
                .addListener(future -> {
                   if(future.isSuccess()){
                       System.out.println("连接成功！");

                       Channel channel = ((ChannelFuture)future).channel();
                       consoleStart(channel);
                   }else if(retry == 0){
                        System.out.println("重连次数已用完，放弃重新连接");
                   }else{
                       int order = (MAX_RETRY - retry) + 1;
                       // 本次重连的间隔
                       int delay = 1 << order;
                       System.err.println(new Date() + ": 连接失败，第" + order + "次重连……");
                       bootstrap.config().group().schedule(()->connection(retry-1),delay, TimeUnit.SECONDS);
                   }
                });
    }

    private void consoleStart(final Channel channel){
        Scanner scanner = new Scanner(System.in);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    ConsoleCommand consoleCommand;
                    if(!SessionUtil.hasLogin(channel)){
                        consoleCommand = new LoginConsoleCommand();
                        consoleCommand.exec(scanner, channel);
                        sleepSeconds();
                    }else{
                        consoleCommand = new ConsoleCommandManager1();
                        consoleCommand.exec(scanner, channel);
                    }
                }
            }
        }).start();
    }

    private void sleepSeconds(){
        try{
            TimeUnit.SECONDS.sleep(1);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
         NettyClient client = new NettyClient();
    }

}
