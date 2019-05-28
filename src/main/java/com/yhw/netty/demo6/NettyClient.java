package com.yhw.netty.demo6;

import com.yhw.netty.codec.PacketDecoder;
import com.yhw.netty.codec.PacketEncoder;
import com.yhw.netty.protocol.request.MessageRequestPacket;
import com.yhw.netty.util.LoginUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;

/**
 * 客户端服务器双向通信 pipeline
 */
public class NettyClient {

    private Bootstrap bootstrap;

    {
        bootstrap = new Bootstrap();

        NioEventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true);
    }

    public void handler(){
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new PacketDecoder());
                socketChannel.pipeline().addLast(new LoginResponseHandler());
                socketChannel.pipeline().addLast(new MessageResponseHandler());
                socketChannel.pipeline().addLast(new PacketEncoder());
            }
        });
    }

    public void connect(){
        bootstrap.connect("localhost",8800).addListener(future -> {
           if(future.isSuccess()){
               System.out.println("连接成功！");

               //连接成功，启动控制台线程
               Channel channel = ((ChannelFuture)future).channel();
               startConsoleThread(channel);
           }else {
               System.out.println("连接失败！");
           }
        });
    }

    private void startConsoleThread(Channel channel){
        new Thread(() -> {
            while (!Thread.interrupted()){
                if(LoginUtil.hasLogin(channel)){
                    System.out.println("输入消息发送至服务端：");
                    Scanner scanner = new Scanner(System.in);
                    String line = scanner.nextLine();

                    /*MessageRequestPacket requestPacket = new MessageRequestPacket();
                    requestPacket.setMessage(line);
                    ByteBuf byteBuf = PacketCodeC.getInstance().encode(channel.alloc(),requestPacket);
                    channel.writeAndFlush(byteBuf);*/
                    //自动编码
                    channel.writeAndFlush(new MessageRequestPacket(line));
                }
            }

        }).start();
    }

    public static void main(String[] args){
         NettyClient nettyClient = new NettyClient();
         nettyClient.handler();
         nettyClient.connect();
    }

}
