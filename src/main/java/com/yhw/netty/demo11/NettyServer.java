package com.yhw.netty.demo11;

import com.yhw.netty.codec.PacketDecoder;
import com.yhw.netty.codec.PacketEncoder;
import com.yhw.netty.codec.Spliter;
import com.yhw.netty.demo11.server.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;

public class NettyServer {
    private ServerBootstrap serverBootstrap;

    {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup work = new NioEventLoopGroup();

        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss,work)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,1024)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childOption(ChannelOption.TCP_NODELAY,true);
    }

    public NettyServer(){
        serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                //拆包
                ch.pipeline().addLast(new Spliter());
                //解码
                ch.pipeline().addLast(new PacketDecoder());
                //登录
                ch.pipeline().addLast(new LoginRequestHandler());
                //认证
                ch.pipeline().addLast(new AuthHandler());
                //单聊消息
                ch.pipeline().addLast(new MessageRequestHandler());


                //创建群消息
                ch.pipeline().addLast(new CreatGroupRequestHandler());
                //加群请求处理器
                ch.pipeline().addLast(new JoinGroupRequestHandler());
                //退群请求处理器
                ch.pipeline().addLast(new QuitGroupRequestHandler());
                //获取群成员请求处理器
                ch.pipeline().addLast(new ListGroupMembersRequestHandler());
                //登出请求处理器
                ch.pipeline().addLast(new LogoutRequestHandler());

                //编码
                ch.pipeline().addLast(new PacketEncoder());
            }
        });

        bind(8800);
    }

    private  void bind(final int port) {
        serverBootstrap.bind(port).addListener(future -> {
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
