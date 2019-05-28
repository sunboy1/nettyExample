package com.yhw.netty.demo9;

import com.yhw.netty.codec.PacketDecoder;
import com.yhw.netty.codec.PacketEncoder;
import com.yhw.netty.codec.Spliter;
import com.yhw.netty.demo9.server.AuthHandler;
import com.yhw.netty.demo9.server.LoginRequestHandler;
import com.yhw.netty.demo9.server.MessageRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;

/**
 * 客户端互聊实现   一对一
 */
public class NettyServer {

    private ServerBootstrap bootstrap;

    {
        bootstrap = new ServerBootstrap();
    }

    public NettyServer(){
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup work = new NioEventLoopGroup();

        bootstrap.group(boss,work)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,1024)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childOption(ChannelOption.TCP_NODELAY,true)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new Spliter());
                        socketChannel.pipeline().addLast(new PacketDecoder());
                        socketChannel.pipeline().addLast(new LoginRequestHandler());

                        //添加用户认证Handler
                        socketChannel.pipeline().addLast(new AuthHandler());
                        socketChannel.pipeline().addLast(new MessageRequestHandler());
                        socketChannel.pipeline().addLast(new PacketEncoder());
                    }
                });

        bind(8800);
    }

    private void bind(final int port) {
        bootstrap.bind(port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println(new Date() + ": 端口[" + port + "]绑定成功!");
            } else {
                System.err.println("端口[" + port + "]绑定失败!");
            }
        });
    }

    public static void main(String[] args){
        NettyServer nettyServer = new NettyServer();
    }
}
