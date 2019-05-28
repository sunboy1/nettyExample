package com.yhw.netty.demo8;

import com.yhw.netty.codec.PacketDecoder;
import com.yhw.netty.codec.PacketEncoder;
import com.yhw.netty.codec.Spliter;
import com.yhw.netty.demo8.handler.client.LoginResponseHandler;
import com.yhw.netty.demo8.handler.client.MessageResponseHandler;
import com.yhw.netty.protocol.request.MessageRequestPacket;
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
 * 使用channelHandler 热拔插 实现客户端身份校验
 */
public class NettyClient {

    private Bootstrap bootstrap;
    private static final int MAX_RETRY = 5;


    static {

    }

    {
        bootstrap = new Bootstrap();
    }



    public NettyClient(){
        NioEventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,5000)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new Spliter());
                        socketChannel.pipeline().addLast(new PacketDecoder());
                        socketChannel.pipeline().addLast(new LoginResponseHandler());
                        socketChannel.pipeline().addLast(new MessageResponseHandler());
                        socketChannel.pipeline().addLast(new PacketEncoder());
                    }
                });

        connect("localhost",8800,MAX_RETRY);
    }

    private  void connect( String host, int port, int retry) {
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println(new Date() + ": 连接成功，启动控制台线程……");
                Channel channel = ((ChannelFuture) future).channel();

                //开始建立消息通信
                startConsoleThread(channel);
            } else if (retry == 0) {
                System.err.println("重试次数已用完，放弃连接！");
            } else {
                // 第几次重连
                int order = (MAX_RETRY - retry) + 1;
                // 本次重连的间隔
                int delay = 1 << order;
                System.err.println(new Date() + ": 连接失败，第" + order + "次重连……");
                bootstrap.config().group().schedule(() -> connect( host, port, retry - 1), delay, TimeUnit
                        .SECONDS);
            }
        });
    }

    private  void startConsoleThread(Channel channel) {
        new Thread(() -> {
            while (!Thread.interrupted()) {
//                if (LoginUtil.hasLogin(channel)) {
                System.out.println("输入消息发送至服务端: ");
                Scanner sc = new Scanner(System.in);
                String line = sc.nextLine();

                channel.writeAndFlush(new MessageRequestPacket(line));
//                }
            }
        }).start();
    }




    public static void main(String[] args){
         NettyClient nettyClient = new NettyClient();
    }


}
