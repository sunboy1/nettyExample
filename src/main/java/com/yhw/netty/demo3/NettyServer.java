package com.yhw.netty.demo3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;

/**
 * 接收客户端登录，并返回响应
 */
public class NettyServer {

    private ServerBootstrap bootstrapServer;

    {
        bootstrapServer = new ServerBootstrap();

        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup work = new NioEventLoopGroup();

        bootstrapServer.group(boss,work)
                //指定IO模型
                .channel(NioServerSocketChannel.class);
    }

    public void childHandler(){
        bootstrapServer.childHandler(new ChannelInitializer<NioSocketChannel>(){
            @Override
            protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                nioSocketChannel.pipeline().addLast(new ServerHandler());
            }
        });
    }

    public void bind(int port){
        bootstrapServer.bind(port).addListener(future -> {
            if(future.isSuccess()){
                System.out.println("端口绑定成功");
            }else {
                System.out.println("端口绑定失败");
            }
        });
    }

    public static void main(String[] args){
        NettyServer nettyServer = new NettyServer();
        nettyServer.childHandler();
        nettyServer.bind(8800);
    }



}
