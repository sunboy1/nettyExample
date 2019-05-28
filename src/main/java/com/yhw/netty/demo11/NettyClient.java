package com.yhw.netty.demo11;

import com.yhw.netty.clientconsole.ConsoleCommandManager;
import com.yhw.netty.clientconsole.LoginConsoleCommand;
import com.yhw.netty.codec.PacketDecoder;
import com.yhw.netty.codec.PacketEncoder;
import com.yhw.netty.codec.Spliter;
import com.yhw.netty.demo11.client.*;
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

/**
 * 加入与退出 获取成员列表
 */
public class NettyClient {

    private static final int MAX_RETRY = 5;
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8800;

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
                //粘包拆包
                ch.pipeline().addLast(new Spliter());
                //解码
                ch.pipeline().addLast(new PacketDecoder());
                //登录响应
                ch.pipeline().addLast(new LoginResponseHandler());
                //接收消息处理器
                ch.pipeline().addLast(new MessageResponseHandler());
                //创建群响应处理器
                ch.pipeline().addLast(new CreateGroupResponseHandler());
                //加群响应处理器
                ch.pipeline().addLast(new JoinGroupResponseHandler());
                //退群响应处理器
                ch.pipeline().addLast(new QuitGroupResponseHandler());
                //获取群成员处理器
                ch.pipeline().addLast(new ListGroupMembersResponseHandler());
                //登出处理器
                ch.pipeline().addLast(new LogoutResponseHandler());
                //编码
                ch.pipeline().addLast(new PacketEncoder());
            }
        });

        connect(HOST,PORT,MAX_RETRY);
    }

    private  void connect(String host, int port, int retry) {
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println(new Date() + ": 连接成功，启动控制台线程……");
                Channel channel = ((ChannelFuture) future).channel();
                startConsoleThread(channel);
            } else if (retry == 0) {
                System.err.println("重试次数已用完，放弃连接！");
            } else {
                // 第几次重连
                int order = (MAX_RETRY - retry) + 1;
                // 本次重连的间隔
                int delay = 1 << order;
                System.err.println(new Date() + ": 连接失败，第" + order + "次重连……");
                bootstrap.config().group().schedule(() -> connect(host, port, retry - 1), delay, TimeUnit
                        .SECONDS);
            }
        });
    }

    private  void startConsoleThread(Channel channel) {
        ConsoleCommandManager consoleCommandManager = new ConsoleCommandManager();
        LoginConsoleCommand loginConsoleCommand = new LoginConsoleCommand();
        Scanner scanner = new Scanner(System.in);

        new Thread(() -> {
            while (!Thread.interrupted()) {
                if (!SessionUtil.hasLogin(channel)) {
                    loginConsoleCommand.exec(scanner, channel);
                } else {
                    consoleCommandManager.exec(scanner, channel);
                }
            }
        }).start();
    }

    public static void main(String[] args){
         NettyClient nettyClient = new NettyClient();
    }
}
