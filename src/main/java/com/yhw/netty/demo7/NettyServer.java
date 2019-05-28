package com.yhw.netty.demo7;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyServer {

    private ServerBootstrap serverBootstrap;

    {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup work = new NioEventLoopGroup();

        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss,work)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new LifeCyCleTestHandler());
                    }
                }).bind(8800);
    }

    public static void main(String[] args){
        NettyServer nettyServer = new NettyServer();
    }

}
