package com.yhw.netty.demo1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * 启动服务端，必须指定三类属性
 * 1.线程模型 2.IO模型 3.连接读写处理逻辑
 */
public class NettyServer {

    public static void main(String[] args){
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        //对应IOServer中接收 新连接线程，主要负责创建新连接
        NioEventLoopGroup boss = new NioEventLoopGroup();
        //对应IOServer中负责读取数据的线程，主要用于读取数据以及业务逻辑处理
        NioEventLoopGroup worker = new NioEventLoopGroup();

        serverBootstrap
                //绑定线程组
                .group(boss,worker)
                //指定服务的IO模型
                .channel(NioServerSocketChannel.class)
                //处理每条连接的数据读写，业务处理逻辑
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    //NioSocketChannel是对NioServerSocketChannel连接的抽象
                    //可以BIO中的ServerSocket和Socket的概念对应上
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                                System.out.println(msg);
                            }
                        });
                    }
                }).bind(8000);
    }
}
