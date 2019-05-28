package com.yhw.netty.demo7;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * channelHandler的生命周期
 */
public class NettyClient {

    private Bootstrap bootstrap;

    {
        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {

                    }
                }).connect("localhost",8800)
        .addListener(future -> {
            if(future.isSuccess()){
                System.out.println("连接成功！");

                Channel channel = ((ChannelFuture)future).channel();
                Scanner scanner = new Scanner(System.in);
                System.out.println("请输入字符：");


                //---------------------------
                String request = scanner.nextLine();
                ByteBuf byteBuf = channel.alloc().buffer();

                byte[] bytes = request.getBytes(Charset.forName("utf-8"));
                byteBuf.writeBytes(bytes);
                //--------------------------

                channel.writeAndFlush(byteBuf);
            }
        });

    }


    public static void main(String[] args){
        NettyClient nettyClient = new NettyClient();
    }
}
